(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.GetAll = GetAll;
        service.GetByUsername = GetByUsername;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get('/hotchat/rest/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function GetByUsername(userName) {
            return $http.get('/hotchat/rest/users/' + userName).then(handleSuccess, handleError('Error getting user by userName'));
        }

        function Create(user) {
            return $http.post('/hotchat/rest/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function Update(user) {
            return $http.put('/hotchat/rest/users/' + userName, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete('/hotchat/rest/users/' + userName).then(handleSuccess, handleError('Error deleting user'));
        }

        function handleSuccess(res) {
            var data = res.data;
        	return { success: true, data };
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
