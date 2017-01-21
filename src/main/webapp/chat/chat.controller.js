(function() {
	'use strict';

	angular.module('app').controller('ChatController', ChatController);

	ChatController.$inject = [ 'WebSocketService', '$rootScope', '$scope', '$location', 'ngToast'];
	function ChatController(WebSocketService, $rootScope, $scope, $location, ngToast) {
		var vm = this;
		$rootScope.messages = [];
		vm.sendMessage = sendMessage;
		vm.leaveRoom = leaveRoom;
		vm.receiver = $rootScope.globals.receiver;
		vm.messages = $rootScope.messages;
		
		WebSocketService.subscribe($scope.$id, function (message) {   
		      $scope.$apply(function () {
		         $rootScope.messages.push(message);
		         ngToast.create('a toast message...');
		         
		      });
		});
		
		function sendMessage() {
			WebSocketService.sendMessage(vm.message);
		}
		
		function leaveRoom() {
			WebSocketService.leaveRoom();
			$location.path('/');
		}
	}
})();
