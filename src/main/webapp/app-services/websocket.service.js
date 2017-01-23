(function() {
	'use strict';
	angular.module('app').factory('WebSocketService', WebSocketService);

	WebSocketService.$inject = [ '$q', '$rootScope' ];
	function WebSocketService($q, $rootScope) {
		var receiver = $rootScope.globals.receiver;
		var userName = $rootScope.globals.currentUser.userName;
		var service = {};
		service.callbacks = {};
		service.sendMessage = sendMessage;
		service.subscribe = subscribe;
		service.unsubscribe = unsubscribe;
		service.createEndpoint = createEndpoint;
		service.logout = logout;
		var serviceLocation = "ws://0.0.0.0:8080/hotchat/chat/" + userName;
		var wsocket;
		
		
		function createEndpoint() {
			wsocket = new WebSocket(serviceLocation);
			wsocket.onmessage = onMessageReceived;
		}
		
		function onMessageReceived(evt) {
			var msg = JSON.parse(evt.data);
			angular.forEach(service.callbacks, function(callback){
                callback(msg);
            });
		}
		
		function sendMessage(hotMessage) {
			wsocket.send(hotMessage);
		}
		
		function subscribe(concernedScopeId, callback) {
	          service.callbacks[concernedScopeId] = callback;
	    }

		function unsubscribe(concernedScopeId) {
          delete service.callbacks[concernedScopeId];
        }
		
		function logout() {
			wsocket.close();
		}

		return service;
	}

})();