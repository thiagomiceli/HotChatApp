(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$location', 'UserService', '$rootScope'];
    function HomeController($location, UserService, $rootScope) {
        var vm = this;

        vm.user = null;
        vm.allUsers = [];
        vm.deleteUser = deleteUser;
        vm.chatWith = chatWith;

        initController();

        function initController() {
            loadCurrentUser();
            loadAllUsers();
        }

        function loadCurrentUser() {
            UserService.GetByUsername($rootScope.globals.currentUser.userName)
                .then(function (response) {
                    vm.user = response.data;
                });
        }

        function loadAllUsers() {
            UserService.GetAll()
                .then(function (response) {
                    vm.allUsers = response.data;
                });
        }

        function deleteUser(id) {
            UserService.Delete(id)
            .then(function () {
                loadAllUsers();
            });
        }
        
        function chatWith(userName) {
        	$rootScope.globals.receiver = userName;
        	$location.path('/chat');
        }
    }

})();