(function() {
	'use strict';

	angular.module('app').controller('ChatController', ChatController);

	ChatController.$inject = [ 'UserService', 'WebSocketService', '$rootScope', '$scope', '$location', 'ngToast'];
	function ChatController(UserService, WebSocketService, $rootScope, $scope, $location, ngToast) {
		var vm = this;
		$rootScope.hotMessages = [];
		var chatHistory;
		vm.sendMessage = sendMessage;
		vm.leaveRoom = leaveRoom;
		vm.receiver = $rootScope.globals.receiver;
		vm.hotMessages = $rootScope.hotMessages;
		var userName = $rootScope.globals.currentUser.userName;
		init();
		
		WebSocketService.subscribe($scope.$id, function (hotMessage) {   
		      $scope.$apply(function () {
		         $rootScope.hotMessages.push(hotMessage);
		         var msg = '[' + hotMessage.sender + '] ' + hotMessage.message  
		         ngToast.create({
		        	 	content: '<a href="#!/" class="">'+msg+'</a>',
		        	    dismissOnTimeout: true,
		        	    timeout: 15000,
		        	    dismissOnClick: true,
		        	  });
		         
		      });
		});
		
		function init() {
//			var userName = $rootScope.globals.currentUser.userName;
			chatHistory = UserService.getChatHistory(userName, vm.receiver);
		}
		
		function sendMessage() {
			WebSocketService.sendMessage(vm.message);
		}
		
		function leaveRoom() {
			$location.path('/');
		}
	}
})();
