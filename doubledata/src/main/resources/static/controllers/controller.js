angular.module('demo', ['ngMaterial', 'ngCookies'])
    .controller('Hello', ['$cookies', '$scope', '$http', function ($cookies, $scope, $http) {
        $scope.addTask = function (task) {
            console.log("addTask " + JSON.stringify(task))
            $http({
                method: 'PUT',
                url: 'http://localhost:8080/digesttask/add?src=' + task.src + "&algo=" + task.algo
            }).then(function (response) {
                console.log(response)
                $scope.status = response.data.statusR;
                // $scope.data = response.data;
                // $scope.data = response.data || 'Request failed';
            }, function (response) {
                $scope.status = response.data.statusR;
                console.log(response)
            });
        };

        $scope.userid = $cookies.get('userid');
        $scope.newTask = {algo: 'md5'};
    }]);