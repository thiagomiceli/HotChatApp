﻿(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'AuthenticationService', 'FlashService'];
    function LoginController($location, AuthenticationService, FlashService) {
        var vm = this;

        vm.login = login;

        (function initController() {
            // reset login status
            AuthenticationService.ClearCredentials();
        })();

        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.userName, vm.password, function (response) {
                if (response.status == 200) {
                    AuthenticationService.SetCredentials(vm.userName, vm.password);
                    $location.path('/');
                } else {
                    FlashService.Error(response.data);
                    vm.dataLoading = false;
                }
            });
        };
    }

})();
