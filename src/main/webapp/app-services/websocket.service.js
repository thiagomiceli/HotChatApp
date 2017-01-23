//Websocket client to send/receive chat messages
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
		//url endpoint of the server websocket
		var serviceLocation = "ws://0.0.0.0:8080/hotchat/chat/" + userName;
		var wsocket;
		
		//Connect to a new server endpoint
		function createEndpoint() {
			wsocket = new WebSocket(serviceLocation);
			//listener of new messages
			wsocket.onmessage = onMessageReceived;
		}
		
		//Listener of new messages
		function onMessageReceived(evt) {
			var msg = JSON.parse(evt.data);
			angular.forEach(service.callbacks, function(callback){
                callback(msg);
            });
		}
		
		//Send a new message		
		function sendMessage(hotMessage) {
			wsocket.send(hotMessage);
		}
		
		//Subscribe a new controller callback
		function subscribe(concernedScopeId, callback) {
	          service.callbacks[concernedScopeId] = callback;
	    }

		//Unsubscribe a controller callback
		function unsubscribe(concernedScopeId) {
          delete service.callbacks[concernedScopeId];
        }
		
		//Close connection with the server
		function logout() {
			wsocket.close();
		}

		return service;
	}

})();