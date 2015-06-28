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
        	var start = new Date().getTime();
            $http.get('image').then(function(result) {
            	$scope.image = result.data;
            	$scope.duration = new Date().getTime() - start;
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

appModule.directive('commands', function($interval, $http) {
    return {
        restrict: 'E',
        templateUrl: 'commands.html',

        scope: {
        },
        controller: function($scope) {
            $interval(function() {
        	    $http.get('status/commands').then(function(result) {
        	    	$scope.commands = result.data;
        	    });
            }, 1000);
        }
    };
});

appModule.directive('threadpool', function($interval, $http) {
    return {
        restrict: 'E',
        templateUrl: 'threadpool.html',

        scope: {
        },
        controller: function($scope) {
            $interval(function() {
        	    $http.get('status/threads').then(function(result) {
        	    	$scope.threads = result.data;
        	    });
            }, 1000);
        }
    };
});