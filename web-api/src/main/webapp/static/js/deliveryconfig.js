require.config({
	paths: {
		"vue": '../vue/vue.min2',
		"sortablejs": '../vue/Sortable',
		"vuedraggable": '../vue/vuedraggable'
	},
	shim: {
		'vue': {
			exports: 'vue'
		}
	}
});

//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8083
var localhostPaht = curWwwPath.substring(0, pos);

require(['vue', 'vuedraggable'], function(Vue, draggable) {
	Vue.component('draggable', draggable);
	new Vue({
		el: "#main",
		data: {
            category: $('body').data('category'),
			bases: [],
			list: [],
			list1: [],
			list2: [],
			list3: [{
				fieldName: " ",
				id: 0,
				fieldText: " ",
				type: 3,
				required: false
			}],
			list4: [],
			dragging: false,
			dragObj: null,
			item: [{
				state: false
			}, {
				state: true
			}],
			textStep3: '',
			mergeType: 0,
			count: 0
		},
		created: function() {
			this.getModelData();
		},
		methods: {
			getModelData: function() {
				var _this = this;
				$.util.json(localhostPaht + "/enterprise/template/detail", {category: _this.category}, function(res) {
					if(res.success) {
						var count = 0;
						_this.bases = res.bases;
						for(var i = 0; i < _this.bases.length; i++) {
							if(_this.bases[i].type === 1) {
								_this.list.push({
									fieldName: _this.bases[i].name,
									id: count,
									fieldText: _this.bases[i].description,
									keyValue: _this.bases[i].key,
									type: _this.bases[i].type,
									required: false
								});
								_this.list1.push({
									fieldName: _this.bases[i].name,
									id: count,
									fieldText: _this.bases[i].description,
									keyValue: _this.bases[i].key,
									type: _this.bases[i].type,
									required: false
								});
								count++;
							}
							if(_this.bases[i].type === 2) {
								_this.list2.push({
									fieldName: _this.bases[i].name,
									id: 0,
									fieldText: _this.bases[i].description,
									keyValue: _this.bases[i].key,
									type: _this.bases[i].type,
									required: false
								});
							}
						}
						console.log("0:" + JSON.stringify(_this.list));
						console.log("1:" + JSON.stringify(_this.list1));
						console.log("2:" + JSON.stringify(_this.list2));
					} else {
						$.util.error(res.message);
					}
				});
			},
			getdata: function(evt) {
				console.log(evt.draggedContext.element.id);
				this.dragObj = evt;
			},
			datadragEnd: function(evt) {
				console.log('拖动前的索引：' + evt.oldIndex);
				console.log('拖动后的索引：' + evt.newIndex);
				this.dragObj = evt;
			},
			saveData: function(type) {
				if(this.isNull($("#tempName").val())) {
					$.util.error("模板名称不能为空");
					return;
				}
				var parmas = {},
					ary = new Array();
				for(var i = 0; i < this.list.length; i++) {
					if(this.list[i].type == 3) {
						ary.push({
							customName: this.list[i].fieldName,
							description: this.list[i].fieldText,
							required: this.list[i].required
						});
					} else if(this.list[i].type == 1 || this.list[i].type == 2) {
						ary.push({
							propertyKey: this.list[i].keyValue,
							required: this.list[i].required
						});
					} else {
						ary.push({
							type: this.list[i].type
						});
					}
				}
                parmas.category = this.category;
				//模板名称
				parmas.templateName = $("#tempName").val();
				//导入字段合并设置
				parmas.mergeType = this.mergeType;
				//模板字段配置
				parmas.templateDetails = JSON.stringify(ary);
				//是否导出操作
				if(type == 1) {
					parmas.export = true;
				} else if(type == 0) {
					parmas.export = false;
				}
				$.util.json(localhostPaht + "/enterprise/template/add", parmas, function(res) {
					console.log(res)
					if(res.success) {
						if(type == 1) {
							if(res.url) {
								$.util.download(res.url);
							}
						}
                        $.util.success(res.message, function(){
                            if(parent.layer){
                                parent.layer.close(parent.layer.getFrameIndex(window.name));
                            }
                        }, 3000);
					} else {
						$.util.error(res.message);
					}
				});
			},
			chooseRequiredFiledMust: function(index) {
				console.log("Required必填选择前" + JSON.stringify(this.list));
				var names = document.getElementsByName("requiredFiledMust");
				if(names[index].checked) {
					this.list1[index].required = true;
				} else {
					this.list1[index].required = false;
				}
				for(var i = 0; i < this.list.length; i++) {
					if(this.list[i].fieldName == this.list1[index].fieldName) {
						this.list[i].required = this.list1[index].required;
						break;
					}
				}
				console.log("Required必填选择后" + JSON.stringify(this.list));
			},
			chooseNormalFiled: function(index) {
				console.log("Normal选择前" + JSON.stringify(this.list));
				var nowId = this.list.length - 1;

				var names = document.getElementsByName("normalFiled");
				if(names[index].checked) {
					nowId += 1;
					this.list.push({
						fieldName: this.list2[index].fieldName,
						id: nowId,
						fieldText: this.list2[index].fieldText,
						keyValue: this.list2[index].keyValue,
						type: this.list2[index].type,
						required: this.list2[index].required
					});
					$($("input[name='normalFiledMust']")[index]).prop("disabled", false);
				} else {
					$($("input[name='normalFiledMust']")[index]).prop("disabled", true);
					$($("input[name='normalFiledMust']")[index]).prop("checked", false);
					nowId -= 1;
					for(var i = 0; i < this.list.length; i++) {
						if(this.list[i].fieldName == this.list2[index].fieldName) {
							this.list.splice(i, 1);
							break;
						}
					}
					for(var i = 0; i < this.list.length; i++) {
						this.list[i].id = i;
					}
				}
				console.log("Normal选择后" + JSON.stringify(this.list));
			},
			chooseNormalFiledMust: function(index) {
				var names = document.getElementsByName("normalFiledMust");
				if(names[index].checked) {
					this.list2[index].required = true;
				} else {
					this.list2[index].required = false;
				}
				for(var i = 0; i < this.list.length; i++) {
					if(this.list[i].fieldName == this.list2[index].fieldName) {
						this.list[i].required = this.list2[index].required;
						break;
					}
				}
				console.log("Normal必填选择后" + JSON.stringify(this.list));
			},
			addCustomFiled: function() {
				this.list3.push({
					fieldName: " ",
					id: 0,
					fieldText: " ",
					type: 3,
					required: false
				});
			},
			deleteCustomFiled: function(index) {
				var temp = this.list3;
				for(var i = 0; i < this.list.length; i++) {
					if(this.list3[index].fieldName.indexOf(this.list[i].fieldName) > -1) {
						this.list.splice(i, 1);
						break;
					}
				}
				for(var i = 0; i < this.list3.length; i++) {
					if(this.list3[i].fieldName == " ") {
						this.list3[i].fieldName = $($("input[name='customFiledName']")[i]).val();
					}
					if(this.list3[i].fieldText == " ") {
						this.list3[i].fieldText = $($("input[name='customFiledDesc']")[i]).val();
					}
					if(!this.list3[i].required) {
						this.list3[i].required = $($("input[name='customFiledMust']")[i]).is(":checked");
					}
				}
				this.list3.splice(index, 1);
				var mustBoxes = document.getElementsByName("customFiledMust");
				for(var i = 0; i < this.list3.length; i++) {
					$($("input[name='customFiledName']")[i]).val(this.list3[i].fieldName);
					$($("input[name='customFiledDesc']")[i]).val(this.list3[i].fieldText);
					mustBoxes[i].checked = this.list3[i].required;
				}
				for(var i = 0; i < this.list.length; i++) {
					this.list[i].id = i;
				}
				var checkedBox = [];
				for(var i = 0; i < this.list3.length; i++) {
					checkedBox[i] = false;
				}
				for(var i = 0; i < this.list3.length; i++) {
					for(var j = 0; j < this.list.length; j++) {
						if(this.list3[i].fieldName.indexOf(this.list[j].fieldName) > -1) {
							checkedBox[i] = true;
							break;
						}
					}
				}
				var boxes = document.getElementsByName("customFiled");
				for(var i = 0; i < checkedBox.length; i++) {
					boxes[i].checked = checkedBox[i];
				}
				if (boxes[index].checked == true) {
					$($("input[name='customFiledMust']")[index]).prop("disabled", false);
				} else {
					$($("input[name='customFiledMust']")[index]).prop("disabled", true);
					$($("input[name='customFiledMust']")[index]).prop("checked", false);
				}
			},
			chooseCustomFiled: function(index) {
				var boxes = document.getElementsByName("customFiled");
				if(this.isNull($($("input[name='customFiledName']")[index]).val())) {
					boxes[index].checked = false;
					$.util.error("请输入字段名称后再选择");
					return;
				}
				if(boxes[index].checked == true) {
					for(var i = 0; i < this.list.length; i++) {
						if(this.list[i].fieldName == $($("input[name='customFiledName']")[index]).val()) {
							boxes[index].checked = false;
							$.util.error("选择的字段名称不能重复");
							return;
						}
					}
				}

				if (boxes[index].checked == true) {
					$($("input[name='customFiledMust']")[index]).prop("disabled", false);
				} else {
					$($("input[name='customFiledMust']")[index]).prop("disabled", true);
					$($("input[name='customFiledMust']")[index]).prop("checked", false);
				}
				console.log("自定义选择前" + JSON.stringify(this.list));
				var nowId = this.list.length - 1;

				for(var i = 0; i < this.list3.length; i++) {
					this.list3[i].id = nowId + 1 + i;
					this.list3[i].fieldName = $($("input[name='customFiledName']")[i]).val();
					this.list3[i].fieldText = $($("input[name='customFiledDesc']")[i]).val();
					this.list3[i].required = $($("input[name='customFiledMust']")[i]).is(":checked");
					this.list3[i].type = 3;
				}

				var names = document.getElementsByName("customFiled");
				if(names[index].checked) {
					nowId += 1;
					this.list.push({
						fieldName: this.list3[index].fieldName,
						id: nowId,
						fieldText: this.list3[index].fieldText,
						type: this.list3[index].type,
						required: this.list3[index].required
					});
				} else {
					nowId -= 1;
					for(var i = 0; i < this.list.length; i++) {
						if(this.list[i].fieldName == this.list3[index].fieldName) {
							this.list.splice(i, 1);
							break;
						}
					}
					for(var i = 0; i < this.list.length; i++) {
						this.list[i].id = i;
					}
				}
				console.log("自定义选择后" + JSON.stringify(this.list));
			},
			chooseCustomFiledMust: function(index) {
				var names = document.getElementsByName("customFiledMust");
				if(names[index].checked) {
					this.list3[index].required = true;
				} else {
					this.list3[index].required = false;
				}
				for(var i = 0; i < this.list.length; i++) {
					if(this.list[i].fieldName == this.list3[index].fieldName) {
						this.list[i].required = this.list3[index].required;
						break;
					}
				}
				console.log("自定义必填选择后" + JSON.stringify(this.list));
			},
			changeNameText: function(index) {
				for(var i = 0; i < this.list.length; i++) {
					if(this.list3[index].fieldName.indexOf(this.list[i].fieldName) > -1) {
						this.list[i].fieldName = $($("input[name='customFiledName']")[index]).val();
						this.list3[index].fieldName = $($("input[name='customFiledName']")[index]).val();
						break;
					}
				}
				var boxes = document.getElementsByName("customFiled");
				for(var i = 0; i < this.list.length; i++) {
					if(this.isNull(this.list[i].fieldName)) {
						this.list.splice(i, 1);
						boxes[index].checked = false;
						break;
					}
				}
				for(var i = 0; i < this.list.length; i++) {
					this.list[i].id = i;
				}
			},
			changeDescText: function(index) {
				for(var i = 0; i < this.list.length; i++) {
					if(this.list3[index].fieldText.indexOf(this.list[i].fieldText) > -1) {
						this.list[i].fieldText = $($("input[name='customFiledDesc']")[index]).val();
						this.list3[index].fieldText = $($("input[name='customFiledDesc']")[index]).val();
						break;
					}
				}
			},
			checkInput: function() {
				var _this = this;

				var blankCount = $("input[name='blankFiled']").val();
				if(this.isNull(blankCount) || blankCount == 0) {
					$("input[name='blankFiled']").val(0);
					blankCount = $("input[name='blankFiled']").val();
				}
				if(blankCount > 20) {
					$("input[name='blankFiled']").val(20);
					blankCount = $("input[name='blankFiled']").val();
				}

				if(this.list4.length - blankCount > 0) {
					for(var i = 0; i < this.list4.length - blankCount; i++) {
						for(var j = this.list.length - 1; j >= 0; j--) {
							if(this.list[j].fieldName == '空白列') {
								this.list.splice(j, 1);
								break;
							}
						}
					}

					var deleteCount = this.list4.length - blankCount;
					this.list4.splice(this.list4.length - deleteCount, this.list4.length - blankCount);
					this.count = this.list4.length;
				} else {
					var addCount = blankCount - this.list4.length;
					for(var i = 0; i < addCount; i++) {
						this.list4.push({
							fieldName: "空白列",
							id: this.list.length + i,
							fieldText: " ",
							type: 4,
							required: true
						});
					}

					if(this.count < 20) {
						for(var i = this.count; i < this.list4.length; i++) {
							this.list.push({
								fieldName: this.list4[i].fieldName,
								id: this.list4[i].id,
								fieldText: this.list4[i].fieldText,
								type: this.list4[i].type,
								required: this.list4[i].required
							});
							this.count++
						}
					}
				}
				for(var i = 0; i < this.list.length; i++) {
					this.list[i].id = i;
				}
			},
			isNull: function(text) {
				return !text && text !== 0 && typeof text !== "boolean" ? true : false;
			}
		}
	})
})