angular.module('myapp', []).controller("myctrl", function ($scope, $http) {
    function appendTransform(defaults, transform) {
        // We can't guarantee that the default transformation is an array
        defaults = angular.isArray(defaults) ? defaults : [defaults];
        // Append the new transformation to the defaults
        return defaults.concat(transform);
    }

    function transformTasks(value) {
        map = {};

        angular.forEach(value, function (v, k) {
            v.elapsedSeconds = Number.parseInt(Date.now() / 1000 - v.received.epochSecond);
            map[v.id] = v;
        });

        return map
    }

    $scope.tasksToArr = function (tasks) {
        var arr = [];

        angular.forEach(tasks, function (taks, id) {
            arr.push(taks)
        });

        return arr
    };

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

    function refresh() {
        $http({
            method: "GET",
            url: "/task",
            transformResponse: appendTransform($http.defaults.transformResponse, transformTasks)
        }).then(function successCallback(response) {
            $scope.tasks = response.data
        }, function errorCallback(response) {
            console.error(response);
            clearInterval(intervalID)
        });
    }

    intervalID = setInterval(refresh, 200)


});
