'use strict';

angular.module('jhipsterApp').controller('EngineDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Engine',
        function($scope, $stateParams, $modalInstance, entity, Engine) {

        $scope.engine = entity;
        $scope.load = function(id) {
            Engine.get({id : id}, function(result) {
                $scope.engine = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:engineUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.engine.id != null) {
                Engine.update($scope.engine, onSaveFinished);
            } else {
                Engine.save($scope.engine, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
