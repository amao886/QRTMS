$(document).ready(function () {
    if (!$.fn.serializeObject) {
        $.fn.serializeObject = function () {
            var o = {};
            $.each(this.serializeArray(), function () {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        }
    }
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

    if (!$.validate) {
        $.validate = {
            // 验证手机号
            mobile: function (phone) {
                return phone && /^1\d{10}$/.test(phone);
            },
            //身份证号
            idCard: function (cardNo) {
                return cardNo && /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(cardNo);
            }
        }
    }


    if (!$.util) {
        $.util = {
            loading: {
                open: function (message) {
                    if (this.el == null) {
                        var _message = message || "请求正在进行中...请耐心等待!";
                        var _context = "<div style='text-align: center;'>" + _message + "</div>";
                        this.el = $.dialog({
                            closeIcon: false,
                            content: _context,
                            title: '',
                            animation: 'none',
                            closeAnimation: 'none',
                            draggable: false,
                            lazyOpen: true
                        });
                    }
                    this.el.open();
                },
                close: function () {
                    if (this.el != null) {
                        if (!this.el.isOpen()) {
                            this.el.close();
                        } else {
                            this.el.toggle();
                        }
                    }
                }
            },
            tips: function (message) {
                return $.dialog({closeIcon: false, content: message, title: ''});
            },
            pictures: function (title, message, wide) {
                var _context = "<div style='margin:20px auto;text-align: center;'>" + message + "</div>";
                return $.dialog({
                    closeIcon: false, content: _context, title: title, closeAnimation: 'right', boxWidth: 400
                });
            },
            success: function (message, callback, time) {
                var _autoClose = 'success';
                if (time) {
                    _autoClose = 'success|' + Number(time);
                }
                var _buttons = null;
                if (callback) {
                    _buttons = {success: {text: '确定', btnClass: 'btn-blue', action: callback}};
                } else {
                    _buttons = {success: {text: '确定', btnClass: 'btn-blue'}};
                    if (!time) {
                        _autoClose = 'success|3000';
                    }
                }
                return $.confirm({
                    title: '操作提示', content: message, animation: 'left', closeAnimation: 'right', type: 'blue',
                    buttons: _buttons,
                    autoClose: _autoClose
                });
            },
            successfuc: function (message, ok_fuc) {
                return $.confirm({
                    title: '操作提示', content: message, animation: 'left', closeAnimation: 'right', type: 'blue',
                    buttons: {success: {text: '确定', action: ok_fuc}},
                });
            },
            warning: function (message) {
                return $.confirm({
                    title: '操作警告', content: message, animation: 'left', closeAnimation: 'right', type: 'orange',
                    buttons: {warning: {text: '确定', btnClass: 'btn-orange'}}
                });
            },
            error: function (message) {
                $.confirm({
                    title: '操作异常', content: message, animation: 'left', closeAnimation: 'right', type: 'red',
                    buttons: {error: {text: '确定', btnClass: 'btn-red'}}
                });
                return false;
            },
            danger: function (title, context, action) {
                return $.confirm({
                    title: title, content: context, animation: 'left', closeAnimation: 'right', type: 'red',
                    buttons: {
                        danger: {text: '确定', btnClass: 'btn-red', action: action}
                    }
                });
            },
            confirm: function (title, context, ok_obj, can_obj) {
                var buttons = {confirm: {text: '确定'}, cancel: {text: '取消'}}
                if (typeof ok_obj == 'function') {
                    buttons.confirm['action'] = ok_obj;
                } else if (ok_obj) {
                    buttons.confirm = ok_obj;
                }
                if (typeof can_obj == 'function') {
                    buttons.cancel['action'] = can_obj;
                } else if (can_obj) {
                    buttons.cancel = can_obj;
                }
                return $.confirm({
                    title: title,
                    content: context,
                    animation: 'left',
                    closeAnimation: 'right',
                    type: 'blue',
                    closeIcon: true,
                    buttons: buttons
                });
            },
            confirmWarning: function (title, context, okAction, canAction) {
                return $.confirm({
                    title: title, content: context, animation: 'left', closeAnimation: 'right', type: 'orange',
                    buttons: {
                        confirm: {text: '确定', action: okAction},
                        cancel: {text: '取消', action: canAction}
                    }
                });
            },
            alert: function (title, context, ok_fuc) {
                return $.confirm({
                    title: title, content: context, animation: 'left', closeAnimation: 'right',
                    buttons: {
                        alert: {text: '确定', action: ok_fuc}
                    },
                    autoClose: 'alert|6000'
                });
            },
            iframe: function (url, options) {
                var settings = $.extend({
                    closeIcon: false,
                    offsetTop: 10,
                    offsetBottom: 10,
                    useBootstrap: false,
                    draggable: false,
                    buttons: {confirm: {text: '确定'}, cancel: {text: '取消'}}
                }, options);
                if (settings.ok_obj) {
                    if (typeof settings.ok_obj == 'function') {
                        settings.buttons.confirm['action'] = settings.ok_obj;
                    } else {
                        settings.buttons.confirm = settings.ok_obj;
                    }
                }
                if (settings.cancel_obj) {
                    if (typeof settings.cancel_obj == 'function') {
                        settings.buttons.cancel['action'] = settings.cancel_obj;
                    } else {
                        settings.buttons.cancel = settings.cancel_obj;
                    }
                }
                settings.content = $("<iframe frameborder='0' marginheight='0' marginwidth='0' scrolling='auto'></iframe>");
                settings.content.attr('src', url);
                settings.onOpenBefore = function () {
                    var target = this;
                    target.$iframe = target.$content.find('iframe');
                    target.$iframe.css('width', target.boxWidth - 35);

                    var dwidth = ($(document).width()), dheight = $(document).height();
                    if (settings.cheight && settings.cheight > 0) {
                        target.$iframe.height(Math.min(settings.cwidth, dheight - 120));
                    } else {
                        target.$iframe.height(dheight - 140);
                        target.$scrollPane.height(dheight - 15);
                    }
                    var iframe = target.$iframe[0];
                    if (iframe.attachEvent) {
                        iframe.attachEvent('onload', function () {
                            if (settings.initfun) {
                                target.$ibody = target.$iframe.contents().find('body');
                                settings.initfun.call(target, target.$iframe, target.$ibody);
                            }
                        });
                    } else {
                        iframe.onload = function () {
                            if (settings.initfun) {
                                target.$ibody = target.$iframe.contents().find('body');
                                settings.initfun.call(target, target.$iframe, target.$ibody);
                            }
                        };
                    }
                };
                return $.confirm(settings);
            },
            model: function (_title, _context, _buttons, _initialize) {
                var options = {};
                options['title'] = _title;
                options['content'] = _context;
                options['useBootstrap'] = false;
                options['type'] = 'blue';
                options['closeIcon'] = true;
                options['boxWidth'] = 0;
                if (_context) {
                    options['boxWidth'] = Math.max(300, ($(_context).outerWidth(true) | 0) + 35);
                }
                options['onOpenBefore'] = function () {
                    var self = this;
                    if (!options.boxWidth || options.boxWidth <= 0) {
                        var width = Math.max(300, $(self.content).outerWidth(true));
                        self.setBoxWidth(width + 35 + 'px');
                    }
                    if (_initialize) {
                        _initialize.apply(self);
                    }
                };
                options['buttons'] = _buttons;
                return $.confirm(options);
            },
            form: function (_title, _context, _initialize, _submit) {
                var panel = $.util.panel(_title, _context);
                panel.initialize = _initialize;
                if (_submit) {
                    var submit_btn = {text: '确定', btnClass: 'btn-blue'};
                    if (typeof _submit == 'function') {
                        submit_btn['action'] = _submit;
                    } else if (typeof _submit == 'object') {
                        submit_btn = _submit;
                    } else if (typeof _submit == 'string') {
                        submit_btn['text'] = _submit;
                    }
                    panel.addButton(submit_btn);
                }
                return panel.open();
            },
            forms: function (_title, _context, _initialize, _buttons) {
                var panel = $.util.panel(_title, _context);
                panel.setButtons(_buttons);
                panel.initialize = _initialize;
                return panel.open();
            },
            panel: function (_title, _content, boxWidth) {
                var object = {title: _title, content: _content, bcount: 0, boxWidth: boxWidth};
                object.putButton = function (key, btnObj) {
                    if (!object.buttons) {
                        object.buttons = {};
                    }
                    if (!btnObj.text) {
                        btnObj.text = 'BTN_' + key;
                    }
                    if (!btnObj.btnClass) {
                        btnObj.btnClass = 'btn-blue';
                    }
                    object.buttons[key] = btnObj;
                    object.bcount++;
                }
                object.addButton = function (button) {
                    var btnObj = button, key = object.bcount;
                    if (typeof btnObj == 'function') {
                        btnObj = {text: '确定', btnClass: 'btn-blue', action: button};
                    }
                    if (typeof button == 'string') {
                        btnObj = {text: button, btnClass: 'btn-blue'};
                    }
                    if (typeof button == 'object' && button.key) {
                        key = button.key;
                    }
                    object.putButton(key, btnObj);
                }
                object.setButtons = function (_buttons) {
                    if (_buttons instanceof Array) {
                        if (_buttons != null && _buttons.length > 0) {
                            for (j = 0; j < _buttons.length; j++) {
                                object.addButton(_buttons[j]);
                            }
                        }
                    } else {
                        $.each(_buttons, function (key, button) {
                            object.putButton(key, button);
                        });
                    }
                }
                object.open = function () {
                    var options = {};
                    options['title'] = object.title;
                    options['content'] = object.content;
                    options['useBootstrap'] = false;
                    options['type'] = 'blue';
                    options['closeIcon'] = true;
                    if (object.boxWidth && object.boxWidth > 0) {
                        options['boxWidth'] = Math.max(300, object.boxWidth) + 35;
                    }
                    /*
                    if(object.context){
                        options['boxWidth'] = Math.max(300, ($(object.context).outerWidth(true) | 0) + 35);
                    }
                    */
                    if (object.initialize == null) {
                        object.initialize = function () {
                        }
                    }
                    options['onOpenBefore'] = function () {
                        var self = this;
                        if (!options.boxWidth || options.boxWidth <= 300) {
                            var width = Math.max(300, $(self.content).outerWidth(true));
                            self.setBoxWidth(width + 35 + 'px');
                        }
                        object.initialize.apply(self);
                    };
                    if (object.buttons) {
                        options['buttons'] = object.buttons;
                        object['target'] = $.confirm(options);
                    } else {
                        object['target'] = $.dialog(options);
                    }
                    return object.target;
                }
                object.close = function () {
                    if (object.target) {
                        object.target.close();
                    }
                }
                return object;
            },
            json: function (url, data, callback) {
                return jQuery.ajax({
                    'type': 'POST',
                    'url': url,
                    'contentType': 'application/json',
                    'data': JSON.stringify(data),
                    'dataType': 'json',
                    'success': callback
                });
            },
            get: function (url, callback) {
                return jQuery.ajax({
                    'type': 'GET',
                    'url': url,
                    'success': callback
                });
            },
            random: function getRandom(n) {
                return Math.floor(Math.random() * n + 1);
            },
            getvalue: function (obj) {
                var value = "" || 'undefined' || 'null' || null;
                if (obj === value || typeof(obj) === value) {
                    return '';
                } else {
                    return obj;
                }
            },
            getparam: function (url, name) {
                var num = url.indexOf('?');
                var str = url.substring(num + 1);
                var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
                var result = str.match(reg);
                return result ? decodeURIComponent(result[2]) : null;
            },
            download: function (url) {
                $('<form action="' + url + '" method="post"></form>').appendTo('body').submit().remove();
            }
        }
    }
    $.ajaxSetup({
        beforeSend: function () {
            $.util.loading.open();
        },
        complete: function () {
            $.util.loading.close();
        },
        error: function (request) {
            $.util.loading.close();
            if (request.status !== 200) {
                switch (request.status) {
                    case(500):
                        $.util.error("服务器系统异常");
                        break;
                    case(401):
                        $.util.danger('登陆提示', '登陆超时,请重新登陆后操作', function () {
                            if (window != window.parent) {
                                window.parent.location.reload(true);
                            } else {
                                window.location.reload();
                            }
                        })
                        break;
                    case(403):
                        $.util.error("权限不足");
                        break;
                    case(408):
                        $.util.error("请求超时");
                        break;
                    default:
                        $.util.error("未知错误[" + request.status + "]");
                }
            }
            var q = {
                'url': this.url,
                'type': this.type,
                'contentType': this.contentType,
                'data': this.data,
                'dataType': this.dataType
            };
            console.log(q);
            var p = {
                'readyState': request.readyState,
                'status': request.status,
                'statusText': request.statusText,
            }
            console.log(p);
        }
    });
    $("form").submit(function (event) {
        if ("submit" === event.type) {
            setTimeout(function () {//0.2秒后出现loading
                $.util.loading();
            }, 400);
        }
    });
});