//时间过滤
Vue.filter('date', function (value) {
    if (!value) return '';
    return moment(value).format('YYYY-MM-DD');
})
Vue.filter('time', function (value) {
    if (!value) return '';
    return moment(value).format('HH:mm:ss');
})
Vue.filter('ShorTime', function (value) {
    if (!value) return '';
    return moment(value).format('YYYY-MM-DD HH:mm');
})
Vue.filter('datetime', function (value) {
    if (!value) return '';
    return moment(value).format('YYYY-MM-DD HH:mm:ss');
})
Vue.filter('numberFixed',function(value){
	var val = value + '';
	if(val.indexOf('.')> 0){
		return value.toFixed(2);
	}else{
		return value;
	}
})

//地址去掉横杠
Vue.filter('address', function (str) {
    if (!str) return '--';
    var strArr =  str.split("-"),
    	_str="";
	for(var i=0 ; i < strArr.length ; i++){
		_str += strArr[i]
	}
	return _str;
})