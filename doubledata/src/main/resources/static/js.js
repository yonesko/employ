angular.module('myapp', []).controller("myctrl", function ($scope, $http) {
    function appendTransform(defaults, transform) {
        // We can't guarantee that the default transformation is an array
        defaults = angular.isArray(defaults) ? defaults : [defaults];
        // Append the new transformation to the defaults
        return defaults.concat(transform);
    }

    function transformToTaskMap(value) {
        map = {};

        angular.forEach(value, function (v, k) {
            map[v.id] = v;
        });

        return map
    }

    $scope.add = function (task) {
        $http({
            method: "PUT",
            url: "/task",
            params: {
                src: task.src,
                algo: task.algo
            }
        });
    };

    $scope.remove = function (id) {
        $http({
            method: "DELETE",
            url: "/task",
            params: {
                id: id
            }
        });
    };

    $http({
        method: "GET",
        url: "/task",
        transformResponse: appendTransform($http.defaults.transformResponse, transformToTaskMap)
    }).then(function successCallback(response) {
        $scope.tasks = response.data
    }, function errorCallback(response) {
        console.error(response)
    });

    var eventSource = new EventSource('/task/event');

    eventSource.onopen = function (e) {
        console.log("SSE Open");
    };

    eventSource.onerror = function (e) {
        if (this.readyState === EventSource.CONNECTING) {
            console.log("SSE Connection end, retry");
        } else {
            console.log("SSE Error: " + this.readyState);
        }
    };

    eventSource.addEventListener("ADD", function (e) {
        var task = angular.fromJson(e.data);
        $scope.tasks[task.id] = task;
        $scope.$apply()
    });
    eventSource.addEventListener("DELETE", function (e) {
        delete $scope.tasks[angular.fromJson(e.data)];
        $scope.$apply()
    });

});
