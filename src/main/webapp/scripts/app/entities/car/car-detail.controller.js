'use strict';

angular.module('jhipsterApp')
    .controller('CarDetailController', function ($scope, $rootScope, $stateParams, entity, Car) {
        $scope.car = entity;
        $scope.load = function (id) {
            Car.get({id: id}, function(result) {
                $scope.car = result;
            });
        };
        $rootScope.$on('jhipsterApp:carUpdate', function(event, result) {
            $scope.car = result;
        });
    });
