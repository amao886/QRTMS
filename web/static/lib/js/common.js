$(function(){
	 Vue.filter('dateFormat', function (value) {
        if (!value) return '';
        return moment(value).format('YYYY-MM-DD');
    })
    Vue.filter('date', function (value) {
        if (!value) return '';
        return moment(value).format('YYYY-MM-DD');
    })
    Vue.filter('time', function (value) {
        if (!value) return '';
        return moment(value).format('HH:mm:ss');
    })
    Vue.filter('datetime', function (value) {
        if (!value) return '';
        return moment(value).format('YYYY-MM-DD HH:mm:ss');
    })
    Vue.filter('datehour', function (value) {
        if (!value) return '';
        return moment(value).format('YYYY-MM-DD HH:mm');
    })
    Vue.filter('numberFixed', function (value) {
        if (!value) return 0;
        return value.toFixed(2);
    })
})