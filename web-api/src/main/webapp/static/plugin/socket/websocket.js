function CustomSocket(host, wss_path, sockjs_path, swf_path){
    var socket = {};
    // Let the library know where WebSocketMain.swf is:
    WEB_SOCKET_SWF_LOCATION = swf_path + "plugin/socket/WebSocketMain.swf";
    console.log(WEB_SOCKET_SWF_LOCATION);
    socket.init = function(host, wss_path, sockjs_path){
        var self = socket;
        self.host = host;
        self.wss_path = wss_path;
        self.sockjs_path = sockjs_path;
        return self;
    }
    socket.contact = function(refresh){
        var self = socket;
        if ('WebSocket' in window) {
            self.instance = new WebSocket("wss://" + socket.host + "/" + socket.wss_path);
        } else if ('MozWebSocket' in window) {
            self.instance = new MozWebSocket("wss://" + socket.host + "/" + socket.wss_path);
        } else {
            self.instance = new SockJS("https://" + socket.host + "/" + socket.sockjs_path);
        }
        if(!refresh){
            self.bind();
            self.heartbeat();
        }
        return self;
    }
    socket.refresh = function(){
        console.log("websocket -> refresh");
        var self = socket;
        if(self.available()){
            self.close();
        }
        self.contact();
        return self;
    }
    socket.bind = function(){
        var self = socket, instance = self.instance;
        if(!instance){ return; }
        instance.onopen = function(evnt) {
            console.log("websocket -> onopen");
            if(self.monitors){
                var listener = self.monitors['open'];
                if(listener && (typeof listener == 'function')){
                    listener.call(self, evnt);
                }
            }
        };
        // 收到消息时
        instance.onmessage = function(evnt) {
            console.log("websocket -> onmessage");
            if(evnt.data && self.monitors){
                var data = JSON.parse(evnt.data);
                var listener = self.monitors[data.eventType];
                if(!listener && self.monitors.receive){
                    listener = self.monitors.receive[data.eventType];
                }
                if(listener && (typeof listener == 'function')){
                    listener.call(self, data.context, data.eventType, evnt);
                }
            }
        };
        // 出现异常时
        instance.onerror = function(evnt) {
            console.log("websocket -> onerror");
            if(self.monitors){
                var listener = self.monitors['error'];
                if(listener && (typeof listener == 'function')){
                    listener.call(self, evnt);
                }
            }
        };
        // 连接关闭时
        instance.onclose = function(evnt) {
            console.log("websocket -> onclose");
            if(self.monitors){
                var listener = self.monitors['close'];
                if(listener && (typeof listener == 'function')){
                    listener.call(self, evnt);
                }
                if(self.monitors.close){
                    for (var type in self.monitors.close) {
                        var listener = self.monitors.close[type];
                        if(listener && (typeof listener == 'function')){
                            listener.call(self, type);
                        }
                    }
                }
            }
            self.close();
        };
        return self;
    }
    socket.send = function(data){
        var self = socket, instance = socket.instance;
        if(instance){
            instance.send(JSON.stringify(data));
        }
        return self;
    }
    socket.close = function(){
        var self = socket, instance = self.instance;
        try {
            self.clean();
            if(instance){
                instance.close();
            }
        } catch (e) { }
        return self;
    }
    socket.available = function(){
        var self = socket, instance = self.instance;
        if(instance && instance.readyState === 1){
            return true;
        }else{
            return false;
        }
    }
    socket.listener = function(event, eventType, callback){
        var self = socket;
        if(!self.monitors){
            self.monitors = {};
        }
        if(!self.monitors[event]){
            self.monitors[event] = {};
        }
        self.monitors[event][eventType]= callback;
        return self;
    }
    socket.on = function(event, callback){
        var self = socket;
        if(!self.monitors){
            self.monitors = {};
        }
        self.monitors[event] = callback;
        return self;
    }
    socket.heartbeat = function() {
        var self = socket, instance = self.instance;
        self.intervalKey = window.setInterval(function() {
            var state = instance.readyState;
            if (state === 1) {//连接状态
                instance.send(JSON.stringify({
                    eventType : 'heartbeat',
                    context: new Date().getTime()
                }));//发送心跳数据
            } else if(state === 0 || state === 3){
                self.refresh();
            } else {
                self.clean();//清除
            }
        }, 60000);//周期1分钟
        return self;
    }
    socket.clean = function(){
        var self = socket;
        if(self.intervalKey){
            window.clearInterval(self.intervalKey);//清除
        }
    }
    socket.init(host, wss_path, sockjs_path);
    return socket;
}