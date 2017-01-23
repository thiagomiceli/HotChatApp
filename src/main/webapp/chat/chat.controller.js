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
		vm.receiver;
		var receiverUserName = $rootScope.globals.receiver;
		vm.hotMessages = $rootScope.hotMessages;
		var userName = $rootScope.globals.currentUser.userName;
		init();
		
		WebSocketService.subscribe($scope.$id, function (hotMessage) {   
		      $scope.$apply(function () {
		    	//if not self message
		         if(userName !== hotMessage.sender) {
		        	// if in chat with user
		        	 if(hotMessage.sender === receiverUserName) {
		        		//add hotMessage on the chat message list
		        		 $rootScope.hotMessages.push(hotMessage);
		        	 } else {
		        		 //pop a toast
		        		 var msg = '[' + hotMessage.senderFirstLastName + '] ' + hotMessage.message  
		        		 ngToast.create({
		        			 content: '<p>'+msg+'</p>',
		        			 dismissOnTimeout: true,
		        			 timeout: 15000,
		        			 dismissOnClick: true,
		        		 });
		        	 }
		         } else {
		        	// if in chat with user
		        	 if(hotMessage.receiver === receiverUserName) {
		        		//add hotMessage on the chat message list
		        		 $rootScope.hotMessages.push(hotMessage);
		        	 } else {
		        		 //pop a toast
		        		 var msg = '[' + hotMessage.senderFirstLastName + '] ' + hotMessage.message  
		        		 ngToast.create({
		        			 content: '<p>'+msg+'</p>',
		        			 dismissOnTimeout: true,
		        			 timeout: 15000,
		        			 dismissOnClick: true,
		        		 });
		        	 }
		         }
		      });
		});
		
		function init() {
			UserService.getByUsername(receiverUserName)
			.then(function(response) {
				vm.receiver = response.data;
				UserService.getChatHistory(userName, vm.receiver.userName)
				.then(function(response) {	
					angular.forEach(response.data, function(hotMessage) {
						var strDate = hotMessage.timeStamp.toString();
						hotMessage.received = strDate;
						$rootScope.hotMessages.push(hotMessage);
					});
				});
			});
		}
		
		function sendMessage() {
			var msg = '{"sender":"' + $rootScope.globals.currentUser.userName +
			'", "message":"' + vm.message +
			'", "receiver":"' + receiverUserName +
			'", "senderFirstLastName":"' +  $rootScope.globals.currentUser.firstName +
			' ' +  $rootScope.globals.currentUser.lastName +
			'", "received":""}';
			WebSocketService.sendMessage(msg);
		}
		
		function leaveRoom() {
			$location.path('/');
		}
		
		$scope.$on("$destroy",function() {
			WebSocketService.unsubscribe($scope.$id);      
		});
		
	}
})();
