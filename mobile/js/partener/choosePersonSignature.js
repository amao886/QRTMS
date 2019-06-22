var fontStyle = '';

choosePersonSignature(0);

function choosePersonSignature(clickPosition) {
	if(clickPosition == 0) {
		$("#personSignatureFontPic0").attr('src', "../../images/regular_red.png");
		$("#personSignatureFontPic1").attr('src', "../../images/songtypeface_gray.png");
		$("#personSignatureFontPic2").attr('src', "../../images/microsoftyahei_gray.png");
		$($(".personSignatureFontName")[0]).addClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[1]).removeClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[2]).removeClass("personSignatureFontNameSelected");
		fontStyle = 'F1';
	} else if(clickPosition == 1) {
		$("#personSignatureFontPic0").attr('src', "../../images/regular_gray.png");
		$("#personSignatureFontPic1").attr('src', "../../images/songtypeface_red.png");
		$("#personSignatureFontPic2").attr('src', "../../images/microsoftyahei_gray.png");
		$($(".personSignatureFontName")[0]).removeClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[1]).addClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[2]).removeClass("personSignatureFontNameSelected");
		fontStyle = 'F2';
	} else {
		$("#personSignatureFontPic0").attr('src', "../../images/regular_gray.png");
		$("#personSignatureFontPic1").attr('src', "../../images/songtypeface_gray.png");
		$("#personSignatureFontPic2").attr('src', "../../images/microsoftyahei_red.png");
		$($(".personSignatureFontName")[0]).removeClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[1]).removeClass("personSignatureFontNameSelected");
		$($(".personSignatureFontName")[2]).addClass("personSignatureFontNameSelected");
		fontStyle = 'F3';
	}
	
//	getSignaturePic();
}

function getSignaturePic(){
	
}

$("#showTooltips").click(function() {
//	var img = $('#picShow')[0].src;
//	if(!img) {
//		$.toptip('请先选择签章样式');
//		return;
//	}

	if (isNull(fontStyle)) {
		$.toptip('请先选择签章样式');
		return;
	}

	var baseValue = {
		sealStyle: fontStyle,
	}
	EasyAjax.ajax_Post_Json({
		url: '/enterprise/company/userLegalize/sealType',
		data: JSON.stringify(baseValue)
	}, function(res) {
		window.location.href = _common.version("../../index.html");
	});
});

function isNull(text) {
	return !text && text !== 0 && typeof text !== "boolean" ? true : false;
}