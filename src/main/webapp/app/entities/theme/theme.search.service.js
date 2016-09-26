(function() {
    'use strict';

    angular
        .module('jadminApp')
        .factory('ThemeSearch', ThemeSearch);

    ThemeSearch.$inject = ['$resource'];

    function ThemeSearch($resource) {
        var resourceUrl =  'api/_search/themes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
