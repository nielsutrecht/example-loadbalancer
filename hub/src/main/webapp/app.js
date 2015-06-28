var appModule = angular.module('myApp', []); 

appModule.controller('MainCtrl', ['$scope', '$http', '$interval', function($scope, $http, $interval) {
    $scope.nodes = [];
    
    $interval(function() {
	    $http.get('status/nodes').then(function(result) {
	    	$scope.nodes = result.data;
	    });
    }, 1000);

    $scope.setSpeed = function(speed) {
        if (angular.isDefined($scope.updateImage)) {
            $interval.cancel($scope.updateImage);
            $scope.updateImage = undefined;
        }
    	if(speed <= 0) {
    		return;
    	}
        $scope.updateImage = $interval(function() {
            $http.get('image').then(function(result) {
            	$scope.image = result.data;
            });
        }, speed);
    }
    
    $scope.speed = 1000;
    
    $scope.$watch('speed', $scope.setSpeed);

}]);

appModule.directive('nodes', function() {
    return {
        restrict: 'E',
        templateUrl: 'nodes.html',

        scope: {
            nodes: '='
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

appModule.directive('hub', function() {
    return {
        restrict: 'E',
        templateUrl: 'hub.html',

        scope: {
            data: '='
        },
        controller: function($scope) {
        }
    };
});