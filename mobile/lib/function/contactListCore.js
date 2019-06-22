
;(function (d, b, a, e) { //jQuery,window,document,undefined
    var c = function (g,msg,f) {
        this.$ele = g,
        this.defaults = {},
        this.listBox = msg,
        this.options = d.extend(true, this.defaults, f || {})
    };
    c.prototype = {
        constructor: c,
        init: function () {
            this.boxHeight = a.documentElement.clientHeight;
            this.initList();
            this.initNavBar();
            this.initPrompt();
            this.bindBarEvent();
        },
        initList: function () {
            var g = "";
            for (var l in this.listBox) { //l就是字母
                var f = this.listBox[l];  //f是数组
                g += "<dl><dt id=" + l + ">" + l + "</dt>";
                for(var i=0;i<f.length;i++){
                	var telphone = f[i].fullName;
                	var pid = f[i].pid;
                	if(pid){
                		//注册
                		g += '<dd class="dd-item" uid="'+f[i].id+'">'+                                      
			                    '<div class="weui-flex">'+ 
							      	'<div class="weui-flex__item">'+ 
							      		'<span class="admin-icon"></span>'+ 
							      		'<span>'+telphone+'</span>'+ 
							      	'</div>'+
							    '</div>'+ 
							 '</dd>';
                	}else{
                		//未注册
                		g += '<dd class="dd-item" uid="'+f[i].id+'">'+                                      
			                    '<div class="weui-flex">'+ 
							      	'<div class="weui-flex__item">'+ 
							      		'<span class="admin-icon"></span>'+ 
							      		'<span>'+telphone+'</span>'+ 
							      	'</div>'+ 
							      	'<div class="reg-stutas">'+ 
							      		'<span>未注册</span>'+ 
							      	'</div>'+ 
							    '</div>'+ 
							 '</dd>';
                	}	
                }
                g += "</dl>"
            }
            this.$ele.html("<section>" + g + "</section>")
        },
        initNavBar: function () {
            var g = this.boxHeight / 40;
            var f = '<nav id="navBar">',
                j = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (var i = 0; i < 26; i++) {
                    f += '<a href="#' + j[i] + '" style="height:' + g + "px;line-height:" + g + 'px">' + j[i] + "</a>"
                }
            f += "</nav>";
            this.$ele.append(f)
        },
        initPrompt: function () {
            this.$ele.append('<span id="prompt"></span>');
            this.p = a.getElementById("prompt")
        },
        bindBarEvent: function () {
            var f = this,
                j = a.querySelector("#navBar"),
                h = null;
                
            var k = function (o) {
                    if (h) {
                        h.classList.remove("active");
                        h = null
                    }
                    j.classList.add("active");
                    var n = o.changedTouches ? o.changedTouches[0] : o;
                    console.log('clientY:'+n.clientY);
                    h = a.elementFromPoint(n.clientX, n.clientY);
                    if (h) {
                        var m = h.innerText;
                        if (m && m.length == 1) {
                            h.classList.add("active");
                            f.p.innerText = m;
                            f.p.classList.add("active");
                            f.scrollTo(m, h)
                        }
                    }
                    o.preventDefault()
                };
            var g = function (m) { //p元素跟navBar都去掉active class类
                    f.p.classList.remove("active");
                    j.classList.remove("active");
                    if (h) {
                        h.classList.remove("active");
                        h = null
                    }
                };
            j.addEventListener("touchstart", function (m) {
                    k(m)
                }, false);
            j.addEventListener("touchmove", function () {
                    k(event)
                }, false);
            a.body.addEventListener("touchend", function (m) {
                    g(m)
                }, false);
            a.body.addEventListener("touchcancel", function (m) {
                    g(m);
                }, false)
        },
        scrollTo: function (f, h) {
            var g = this;
            if (h && h.href) {
                location.href = h.href;
                return false;
            }
        }
    };
    d.fn.myList = function (msg,g) {
        var f = new c(this,msg,g);
        return f.init();
    }
})(jQuery, window, document);