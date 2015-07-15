'use strict';

angular.module('jhipsterApp')
    .controller('CarController', function ($scope, Car, CarSearch, ParseLinks) {
        $scope.cars = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Car.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cars = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Car.get({id: id}, function(result) {
                $scope.car = result;
                $('#deleteCarConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Car.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCarConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CarSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.cars = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.car = {name: null, fuelType: null, id: null};
        };
    });
