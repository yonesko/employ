angular.module('demo', ['ngMaterial', 'ngCookies'])
    .controller('Hello', ['$cookies', '$scope', '$http', function ($cookies, $scope, $http) {
        $scope.userid = $cookies.get('userid')
    }]);