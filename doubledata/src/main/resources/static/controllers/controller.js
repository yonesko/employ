angular.module('demo', ['ngCookies'])
    .controller('Hello', ['$cookies', '$scope', '$http', function ($cookies, $scope, $http) {
        // $http.get('http://rest-service.guides.spring.io/greeting').then(function (response) {
        //     $scope.userid = response.data;
        // });
        $scope.userid = $cookies.get('userid')
    }]);