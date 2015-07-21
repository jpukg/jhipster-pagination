'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('engine', {
                parent: 'entity',
                url: '/engines',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Engines'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/engine/engines.html',
                        controller: 'EngineController'
                    }
                },
                resolve: {
                }
            })
            .state('engine.detail', {
                parent: 'entity',
                url: '/engine/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Engine'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/engine/engine-detail.html',
                        controller: 'EngineDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Engine', function($stateParams, Engine) {
                        return Engine.get({id : $stateParams.id});
                    }]
                }
            })
            .state('engine.new', {
                parent: 'engine',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/engine/engine-dialog.html',
                        controller: 'EngineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {capacity: null, name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('engine', null, { reload: true });
                    }, function() {
                        $state.go('engine');
                    })
                }]
            })
            .state('engine.edit', {
                parent: 'engine',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/engine/engine-dialog.html',
                        controller: 'EngineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Engine', function(Engine) {
                                return Engine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('engine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
