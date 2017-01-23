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
			WebSocketService.createEndpoint();
			WebSocketService.subscribe($scope.$id, function(hotMessage) {
				if(hotMessage.sender !== userName) {
					createToast(hotMessage);
				}
			});
		}

		function showOfflineMessages() {
			UserService.getOfflineMessages(userName).then(function(response) {
				vm.offlineMessages = response.data;
				angular.forEach(vm.offlineMessages, function(hotMessage) {
					var msg = 'While you were offline... <br> [' + hotMessage.senderFirstLastName + '] ' + hotMessage.message  
			         ngToast.create({
			        	 	content: '<p>'+msg+'</p>',
			        	    dismissOnTimeout: true,
			        	    timeout: 15000,
			        	    className:	'warning',
			        	    dismissOnClick: true,
			        	  });
				});
			});
		}

		function createToast(hotMessage) {
			var msg = '[' + hotMessage.senderFirstLastName + '] ' + hotMessage.message; 
			$scope.$apply(function() {
				ngToast.create({
	        	 	content: '<p>'+msg+'</p>',
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
						$rootScope.globals.currentUser = response.data;
						vm.user = $rootScope.globals.currentUser
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
		
		$scope.$on("$destroy",function() {
			WebSocketService.unsubscribe($scope.$id);      
		});
		
	}

})();