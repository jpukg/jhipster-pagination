'use strict';

angular.module('jhipsterApp')
    .controller('EngineDetailController', function ($scope, $rootScope, $stateParams, entity, Engine) {
        $scope.engine = entity;
        $scope.load = function (id) {
            Engine.get({id: id}, function(result) {
                $scope.engine = result;
            });
        };
        $rootScope.$on('jhipsterApp:engineUpdate', function(event, result) {
            $scope.engine = result;
        });
    });
