(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.getAll = getAll;
        service.getByUsername = getByUsername;
        service.getChatHistory = getChatHistory;
        service.getOfflineMessages = getOfflineMessages;
        service.setUserOnlineStatus = setUserOnlineStatus;
        service.create = create;
        service.update = update;
        service.Delete = Delete;

        return service;

        function getAll() {
            return $http.get('/hotchat/rest/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function getByUsername(userName) {
            return $http.get('/hotchat/rest/users/' + userName).then(handleSuccess, handleError('Error getting user by userName'));
        }
        
        function getChatHistory(sender, receiver) {
            return $http.get('/hotchat/rest/users/' + sender + "/" + receiver).then(handleSuccess, handleError('Error retrieving chat history'));
        }
        
        function getOfflineMessages(userName) {
            return $http.get('/hotchat/rest/users/offline/' + userName).then(handleSuccess, handleError('Error retrieving offline messages'));
        }
        
        function setUserOnlineStatus(userName, status) {
            return $http.post('/hotchat/rest/users/' + userName + "/"+ status).then(handleSuccess, handleError('Error setting user status'));
        }

        function create(user) {
            return $http.post('/hotchat/rest/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function update(user) {
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
