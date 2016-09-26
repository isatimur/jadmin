(function() {
    'use strict';

    angular
        .module('jadminApp')
        .controller('ThemeDetailController', ThemeDetailController);

    ThemeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Theme', 'Faq'];

    function ThemeDetailController($scope, $rootScope, $stateParams, previousState, entity, Theme, Faq) {
        var vm = this;

        vm.theme = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jadminApp:themeUpdate', function(event, result) {
            vm.theme = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
