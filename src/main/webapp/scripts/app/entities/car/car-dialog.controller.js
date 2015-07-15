'use strict';

angular.module('jhipsterApp').controller('CarDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Car',
        function($scope, $stateParams, $modalInstance, entity, Car) {

        $scope.car = entity;
        $scope.load = function(id) {
            Car.get({id : id}, function(result) {
                $scope.car = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:carUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.car.id != null) {
                Car.update($scope.car, onSaveFinished);
            } else {
                Car.save($scope.car, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
