(function() {
    'use strict';

    angular
        .module('jadminApp')
        .factory('FaqSearch', FaqSearch);

    FaqSearch.$inject = ['$resource'];

    function FaqSearch($resource) {
        var resourceUrl =  'api/_search/faqs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
