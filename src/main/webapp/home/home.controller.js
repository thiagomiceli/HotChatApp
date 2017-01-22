(function() {
	'use strict';

	angular.module('app').controller('HomeController', HomeController);

	HomeController.$inject = [ '$location', 'UserService', '$rootScope',
			'$scope', 'WebSocketService', 'ngToast' ];
	function HomeController($location, UserService, $rootScope, $scope,
			WebSocketService, ngToast) {
		var vm = this;
		vm.user = null;
		vm.allUsers = [];
		vm.offlineMessages = [];
		vm.chatWith = chatWith;
		vm.logout = logout;
		var userName = $rootScope.globals.currentUser.userName;
		
		initController();

		function initController() {
			loadCurrentUser();
			loadAllUsers();
			showOfflineMessages();
			WebSocketService.subscribe($scope.$id, function(hotMessage) {
				createToast(hotMessage);
			});
		}
		
		 

		function showOfflineMessages() {
			UserService.getOfflineMessages(userName).then(function(response) {
				vm.offlineMessages = response.data;
//				angular.forEach(vm.offlineMessages, function(hotMessage) {
//					createToast(hotMessage);
//				});
			});
		}

		function createToast(hotMessage) {
			var msg = '[' + hotMessage.sender + '] ' + hotMessage.message; 
			$scope.$apply(function() {
				ngToast.create({
	        	 	content: '<a href="#!/" class="">'+msg+'</a>',
	        	    dismissOnTimeout: true,
	        	    timeout: 15000,
	        	    dismissOnClick: true,
	        	  });
			});
		}

		function loadCurrentUser() {
			UserService.setUserOnlineStatus(
					userName, true);
			UserService.getByUsername(userName)
					.then(function(response) {
						vm.user = response.data;
					});
		}

		function loadAllUsers() {
			UserService.getAll().then(function(response) {
				vm.allUsers = response.data;
			});
		}

		function chatWith(userName) {
			$rootScope.globals.receiver = userName;
			$location.path('/chat');
		}

		function logout() {
			UserService.setUserOnlineStatus(
					userName, false);
			WebSocketService.logout();
			$location.path('#!/login');
		}
	}

})();