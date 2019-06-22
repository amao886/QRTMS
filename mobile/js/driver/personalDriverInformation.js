const vm = new Vue({
	el: '#driverInformation',
	data: {
		driver: [],
		end: '',
		start: ''
	},
	mounted: function() {
		var _this = this;
		EasyAjax.ajax_Post_Json({
			url: 'driver/personal/info',
		}, function(res) {
			console.log(res);
			_this.driver.push(res.driver);
			if(res.driver.routes[0].ends!=null){
				_this.end = res.driver.routes[0].ends.join('-');
			}
			if(res.driver.routes[0].starts!=null){
			_this.start = res.driver.routes[0].starts.join('-');
			}

		})
	},
	methods: {
		editInformation: function() {
			var _this = this;
			window.location.href = '../../view/driver/applyOrEditDriverInformation.html'
		}
	},

})