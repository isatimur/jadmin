(function() {
    'use strict';

    angular
        .module('jadminApp')
        .controller('FaqDetailController', FaqDetailController);

    FaqDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Faq', 'Theme'];

    function FaqDetailController($scope, $rootScope, $stateParams, previousState, entity, Faq, Theme) {
        var vm = this;

        vm.faq = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jadminApp:faqUpdate', function(event, result) {
            vm.faq = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
