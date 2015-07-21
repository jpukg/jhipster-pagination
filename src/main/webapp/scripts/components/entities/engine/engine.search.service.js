'use strict';

angular.module('jhipsterApp')
    .factory('EngineSearch', function ($resource) {
        return $resource('api/_search/engines/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
