document.addEventListener("plusready", function() {
	var _BARCODE = 'loginout',
		B = window.plus.bridge;
	var loginout = {
		loginOut: function(Argus, successCallback, errorCallback) {
			var success = typeof successCallback !== 'function' ? null : function(args) {
					successCallback(args);
				},
				fail = typeof errorCallback !== 'function' ? null : function(code) {
					errorCallback(code);
				};
			callbackID = B.callbackId(success, fail);
			return B.exec(_BARCODE, "loginOut", [callbackID, Argus]);
		}
	};
	window.plus.loginout = loginout;
}, true);