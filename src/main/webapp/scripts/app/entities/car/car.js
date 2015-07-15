'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('car', {
                parent: 'entity',
                url: '/cars',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Cars'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car/cars.html',
                        controller: 'CarController'
                    }
                },
                resolve: {
                }
            })
            .state('car.detail', {
                parent: 'entity',
                url: '/car/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Car'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car/car-detail.html',
                        controller: 'CarDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Car', function($stateParams, Car) {
                        return Car.get({id : $stateParams.id});
                    }]
                }
            })
            .state('car.new', {
                parent: 'car',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/car/car-dialog.html',
                        controller: 'CarDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, fuelType: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('car', null, { reload: true });
                    }, function() {
                        $state.go('car');
                    })
                }]
            })
            .state('car.edit', {
                parent: 'car',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/car/car-dialog.html',
                        controller: 'CarDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Car', function(Car) {
                                return Car.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('car', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
