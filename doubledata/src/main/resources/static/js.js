angular.module('myapp', ['ngFileUpload']).controller("myctrl", ['$scope', '$http', 'Upload', function ($scope, $http, Upload) {
    function appendTransform(defaults, transform) {
        // We can't guarantee that the default transformation is an array
        defaults = angular.isArray(defaults) ? defaults : [defaults];
        // Append the new transformation to the defaults
        return defaults.concat(transform);
    }

    function transformTasks(values) {
        angular.forEach(values, function (v, k) {
            v.elapsedSeconds = Number.parseInt(Date.now() / 1000 - v.received.epochSecond);
        });

        return values
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

    function refresh() {
        $http({
            method: "GET",
            url: "/task",
            transformResponse: appendTransform($http.defaults.transformResponse, transformTasks)
        }).then(function successCallback(response) {
            $scope.tasks = response.data;
        }, function errorCallback(response) {
            console.error(response);
            clearInterval(intervalID)
        });
    }

    intervalID = setInterval(refresh, 200);
    $scope.userid = document.cookie;

    $scope.uploadFiles = function (file, errFiles) {
        $scope.f = file;
        if (file) {
            file.upload = Upload.upload({
                url: '/task',
                data: {file: file}
            });

            file.upload.then(
                function (response) {
                },
                function (response) {
                    if (response.status > 0)
                        alert(response.status + ': ' + response.data)
                }, function (evt) {
                });
        }
    }

}]);
