var appModule = angular.module('myApp', []); 

appModule.controller('MainCtrl', ['$scope', '$http', '$interval', function($scope, $http, $interval) {
    $scope.nodes = [];
    
    $http.get('status/nodes').then(function(result) {
    	$scope.nodes = result.data;
    });

    $interval(function() {
        $http.get('/d/image').then(function(result) {
        	$scope.image = result.data;
        });
    }, 10);

}]);

appModule.directive('node', function() {
    return {
        restrict: 'E',
        templateUrl: 'node.html',

        scope: {
            node: '='
        },
        controller: function($scope) {
        }
    };
});

appModule.directive('data', function() {
    return {
        restrict: 'E',
        templateUrl: 'data.html',

        scope: {
            data: '='
        },
        controller: function($scope) {
        }
    };
});