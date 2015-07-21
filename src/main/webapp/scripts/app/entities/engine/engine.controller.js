'use strict';

angular.module('jhipsterApp')
    .controller('EngineController', function ($scope, Engine, EngineSearch, ParseLinks) {
        $scope.engines = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            Engine.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.engines = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Engine.get({id: id}, function(result) {
                $scope.engine = result;
                $('#deleteEngineConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Engine.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEngineConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EngineSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.engines = result;
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
            $scope.engine = {capacity: null, name: null, id: null};
        };
    });
