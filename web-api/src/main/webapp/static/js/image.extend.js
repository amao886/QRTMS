/**
 * 图片扩展
 */
var imageExt = {
    
	/**
	 * 将图片url转成base64 
	 */
    imageToBase64 : function(url, callback, outputFormat) {
		var canvas = document.createElement('CANVAS'), 
			ctx = canvas.getContext('2d'), 
			img = new Image;
		img.crossOrigin = 'Anonymous';
		img.onload = function() {
			canvas.height = img.height;
			canvas.width = img.width;
			ctx.drawImage(img, 0, 0);
			callback.call(this, canvas.toDataURL(outputFormat || 'image/png'));
			canvas = null;
		};
		img.src = url;
	},
		
	/**
	 * @param image_obj
	 *            图片对象（JPG 或者 PNG）
	 * @param quality
	 *            压缩质量（0-100）
	 * @param output_format
	 *            输出格式（jpg=image/jpeg,png=image/png）
	 * @returns
	 */
    compress: function(image_obj, quality, output_format){
         var mime_type = "image/jpeg";
         if(typeof output_format !== "undefined" && output_format=="png"){
            mime_type = "image/png";
         }
         var cvs = document.createElement('canvas');
         cvs.width = image_obj.naturalWidth;
         cvs.height = image_obj.naturalHeight;
         var ctx = cvs.getContext("2d").drawImage(image_obj, 0, 0);
         var newImageData = cvs.toDataURL(mime_type, quality / 100);
         var new_img_obj = new Image();
         new_img_obj.src = newImageData;
         return new_img_obj;
    },

    /**
     *  @param compressed_img_obj  
     *  @param upload_url  
     *  @param file_input_name 
     *  @param filename 
     *  @param successCallback 
     *  @param errorCallback  
     *  @param duringCallback
     *  @param customHeaders
     */
    upload: function(compressed_img_obj, upload_url, file_input_name, filename, successCallback, errorCallback, duringCallback, customHeaders){
        if (XMLHttpRequest.prototype.sendAsBinary === undefined) {
            XMLHttpRequest.prototype.sendAsBinary = function(string) {
                var bytes = Array.prototype.map.call(string, function(c) {
                    return c.charCodeAt(0) & 0xff;
                });
                this.send(new Uint8Array(bytes).buffer);
            };
        }
        var type = "image/jpeg";
        if(filename.substr(-4).toLowerCase() == ".png"){
            type = "image/png";
        }
        var data = compressed_img_obj.src;
        data = data.replace('data:' + type + ';base64,', '');
        var xhr = new XMLHttpRequest();
        xhr.open('POST', upload_url, true);
        var boundary = '===###===image===ext===upload===###===';
        xhr.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);
		if (customHeaders && typeof customHeaders === "object") {
			for (var headerKey in customHeaders){
				xhr.setRequestHeader(headerKey, customHeaders[headerKey]);
			}
		}
		if (duringCallback && duringCallback instanceof Function) {
			xhr.upload.onprogress = function (evt) {
				if (evt.lengthComputable) {  
					duringCallback ((evt.loaded / evt.total)*100);  
				}
			};
		}
        xhr.sendAsBinary(['--' + boundary, 'Content-Disposition: form-data; name="' + file_input_name + '"; filename="' + filename + '"', 'Content-Type: ' + type, '', atob(data), '--' + boundary + '--'].join('\r\n'));
        xhr.onreadystatechange = function() {
			if (this.readyState == 4){
				if (this.status == 200) {
					successCallback(this.responseText);
				}else if (this.status >= 400) {
					if (errorCallback &&  errorCallback instanceof Function) {
						errorCallback(this.responseText);
					}
				}
			}
        };
    },
    multiple: function(url, images, events){
    	var xhr = new XMLHttpRequest();
        for(var v in events){  
        	xhr.addEventListener(v, events[v], false);    
        }  
        var formData = new FormData();  
        if(images.length > 0){  
        	for(var i = 0; i < images.length; i++){
        		var dataurl = images[i];
        		var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	            while(n--){
	                u8arr[n] = bstr.charCodeAt(n);
	            }
        		formData.append("multiple_"+ i, new Blob([u8arr], {type:mime}), i+ ".jpg");  
        	}  
        }  
        xhr.open("POST", url, true);  
        xhr.send(formData);  
    }
};