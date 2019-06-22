document.addEventListener("plusready", function() {
	var _BARCODE = 'scancode',
		B = window.plus.bridge;
	var scancode = {
		getScanCode: function(Argus) {
			return B.execSync(_BARCODE, "getScanCode", Argus);
		}
	};
	window.plus.scancode = scancode;
}, true);