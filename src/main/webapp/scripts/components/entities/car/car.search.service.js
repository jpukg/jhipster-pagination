'use strict';

angular.module('jhipsterApp')
    .factory('CarSearch', function ($resource) {
        return $resource('api/_search/cars/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
