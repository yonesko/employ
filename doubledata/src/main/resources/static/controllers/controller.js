angular.module('demo', ['ngMaterial', 'ngCookies'])
    .controller('Hello', ['$cookies', '$scope', '$http', function ($cookies, $scope, $http) {
        $scope.addTask = function (task) {
            console.log("addingTask " + JSON.stringify(task))
            $http({
                method: 'PUT',
                url: 'http://localhost:8080/digesttask/add?src=' + task.src + "&algo=" + task.algo
            }).then(function (response) {
                console.log("addTask OK response")
            }, function (response) {
                console.log("addTask BAD response")
            });
        };
        if (typeof(EventSource) !== "undefined") {
            var source = new EventSource('/digesttask/getall');

            source.onmessage = function (event) {
                // $scope.openListingsReport = event.data;
                // $scope.$apply();
                console.log("From EventSource:" + event);
            };
        }
        else {
            alert('SSE not supported by browser.');
        }

        $scope.userid = $cookies.get('userid');
        $scope.newTask = {algo: 'md5', src: 'yandex'};
    }]);