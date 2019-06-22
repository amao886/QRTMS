/**
 * 画签章方法
 */
var THISPI = Math.PI / 180;

function DrawSeal(canvasObj, upStr, downStr, middleStr, radii) {
	var canvas = canvasObj;
	canvas.width = radii * 2;
	canvas.height = canvas.width;
	var context = canvas.getContext('2d');
	context.save();
	FillPentacle("#FF0000", context, radii, radii, radii * 0.3);
	var edge = radii * 0.04; // 外圈宽度
	var diametre = radii * 2 - edge * 2; // 外圈的直径
	// 开始绘外圆
	context.restore();
	context.save();
	context.beginPath();
	context.arc(radii, radii, radii - edge, 0, Math.PI * 2, false);
	context.closePath();
	context.lineWidth = edge;
	context.strokeStyle = 'red';
	context.stroke();
	// 开始绘上半部份文字
	var _startAngle = 135,
		_endAngle = 45;
	if(upStr != "") {
		if(downStr != "") {
			_startAngle = 155; // 如果有底部文字则缩紧点
			_endAngle = 25;
		}
		if(middleStr != "") {
			_startAngle = 170; // 如果有中间文字则缩紧点
			_endAngle = 10;
		}
		var fontSize = radii * 0.38;
		context.restore();
		context.save();
		var font = "bold " + fontSize + "px 宋体 ";
		var fillStyle = "#FF0000"
		DrawRotatedText(context, upStr, font, fillStyle, _startAngle, _endAngle, radii, radii, radii * 0.66, 90, true);
	}
	// 开始绘下半边文字
	if(downStr != "") {
		var font = "bold " + (radii * 0.08) + "px 宋体 ";
		var fillStyle = "#FF0000"
		// 计算出平均角度
		var upAvgAngle = 0;
		if(_startAngle <= _endAngle) // 如果开始角度大于结束角度则需要考虑超出 360度的问题
		{
			upAvgAngle = (_endAngle - _startAngle) / (upStr.length - 1);
		} else {
			upAvgAngle = (_endAngle + 360 - _startAngle) / (upStr.length - 1);
		}
		_endAngle += upAvgAngle; // 底部文字与顶部文字增加间距
		_startAngle -= upAvgAngle;
		if(middleStr != "") {
			_startAngle = 130; // 如果有中间文字 , 再次综紧一点文字间距
			_endAngle = 50;
		}
		DrawRotatedText(context, downStr, font, fillStyle, _endAngle, _startAngle, radii, radii, radii * 0.85, 270, false);
	}
	// 开始绘制中间文字
	if(middleStr != "") {
		context.restore();
		context.save();
		context.font = "bold " + (radii * 0.23) + "px 宋体 "
		context.fillStyle = "#FF0000"
		var tempX = 0,
			tempY = 0;
		var thisRadii = (radii - edge * 2);
		var downMaxWidth = (thisRadii - (thisRadii + thisRadii * Math.cos(135 * THISPI))) * 2; //
		//中间总宽度
		tempY = radii + radii * Math.sin(30 * THISPI); // 中间文件的 Y坐标
		var tempSizeF = context.measureText(middleStr);
		tempY = radii + radii * Math.sin(30 * THISPI);
		if(downMaxWidth >= tempSizeF.width) // 如果当前文字没超过总宽度 , 则文本居中
		{
			tempX = radii - tempSizeF.width / 2;
		} else {
			var fontWidth = downMaxWidth / middleStr.length;
			tempX = thisRadii + thisRadii * Math.cos(135 * THISPI) + edge * 2;
		}
		context.fillText(middleStr, tempX, tempY, downMaxWidth);
	}
}

// 绘制环绕中心点的文字 参数说明 :
// context : H5canvas
// Content : 文本内容
// font : 字体样式 (bold 20px 宋体 )
// fillStyle : 字体颜色
// _startAngle : 开始角度
// _endAngle : 结束角度
// X : 圆心 X坐标
// Y : 圆心 Y坐标
// r : 圆的半径
// fontDirection : 旋转角度
// strDirection : 文字排列方向 (true 顺时钟 ,false 逆时钟 )
function DrawRotatedText(context, Content, font, fillStyle, _startAngle, _endAngle, X, Y, r,
	fontDirection, strDirection) {
	if(Content == null || Content == "") return;
	// 计算出平均角度
	var avgAngle = 0;
	if(_startAngle <= _endAngle) // 如果开始角度大于结束角度则需要考虑超出 360度的问题
	{
		avgAngle = (_endAngle - _startAngle) / (Content.length - 1);
	} else {
		avgAngle = (_endAngle + 360 - _startAngle) / (Content.length - 1);
	}
	var tempX = _startAngle,
		tempY = _endAngle;
	var nowAngle = _startAngle;
	var strIdnex = 0,
		Step = 1;
	if(!strDirection) // 逆时钟旋转
	{
		strIdnex = Content.length - 1;
		Step = -1;
	}
	while(strIdnex >= 0 && strIdnex < Content.length) {
		var test = nowAngle * THISPI;
		var tempX = X + r * Math.cos(test);
		var tempY = Y + r * Math.sin(test);
		context.restore(); // 恢复到刚刚保存的状态 , 保存恢复只能使用一次
		context.save(); // 保存了当前 context 的状态
		context.font = font;
		context.fillStyle = fillStyle;
		context.translate(tempX, tempY);
		context.rotate((nowAngle + fontDirection) * THISPI);
		context.textAlign = "center";
		context.textBaseline = "middle";
		var avgWidth = 2 * r * Math.PI * 0.6 / Content.length;
		context.fillText(Content[strIdnex], 0, 0, avgWidth); //tempX,tempY);
		nowAngle += avgAngle;
		if(nowAngle > 360) nowAngle = nowAngle - 360;
		strIdnex += Step;
	}
}

// 画五角星 参数说明 :
// fillColor : 五角星颜色
// context : H5canvas
// X : 五角星中心 X 坐标
// Y : 五角星中心 Y 坐标
// radius : 五角星顶端到中心点的长度
function FillPentacle(fillColor, context, X, Y, radius) {
	var tempsin1 = Math.sin(72 * THISPI);
	var tempsin2 = Math.sin(36 * THISPI);
	var tempcos1 = Math.cos(72 * THISPI);
	var tempcos2 = Math.cos(36 * THISPI);
	var x1 = X,
		y1 = Y - radius;
	var x2 = X + radius * tempsin1,
		y2 = Y - radius * tempcos1;
	var x3 = X + radius * tempsin2,
		y3 = Y + radius * tempcos2;
	var x4 = X - radius * tempsin2,
		y4 = Y + radius * tempcos2;
	var x5 = X - radius * tempsin1,
		y5 = Y - radius * tempcos1;
	// 设置是个顶点的坐标，根据顶点制定路径
	context.beginPath();
	context.moveTo(x1, y1);
	context.lineTo(x3, y3);
	context.lineTo(x5, y5);
	context.lineTo(x2, y2);
	context.lineTo(x4, y4);
	context.lineTo(x1, y1);
	context.closePath();
	// 设置边框样式以及填充颜色
	context.lineWidth = "1";
	context.fillStyle = fillColor;
	context.strokeStyle = fillColor;
	context.fill();
	context.stroke();
}