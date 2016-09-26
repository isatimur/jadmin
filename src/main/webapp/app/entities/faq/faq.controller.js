(function() {
    'use strict';

    angular
        .module('jadminApp')
        .controller('FaqController', FaqController);

    FaqController.$inject = ['$scope', '$state', 'Faq', 'FaqSearch'];

    function FaqController ($scope, $state, Faq, FaqSearch) {
        var vm = this;
        
        vm.faqs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Faq.query(function(result) {
                vm.faqs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FaqSearch.query({query: vm.searchQuery}, function(result) {
                vm.faqs = result;
            });
        }    }
})();
