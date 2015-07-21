'use strict';

angular.module('jhipsterApp')
    .factory('Engine', function ($resource, DateUtils) {
        return $resource('api/engines/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
