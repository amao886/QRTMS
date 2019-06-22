(function($){	
    $.fn.extend({
        uploadImage: function(options,defaults){
            var $target = $(this), imgSrcs = [], imgNames = [], imgFiles = []; //图片名字,图片名称,图片文件
            var tempHtml = "<div class='uploadimg-context'>" +
                "<div class='uploadimg-file-div'><input type='file' title='请选择图片' class='uploadimg-file-input' accept='image/png,image/jpg,image/gif,image/JPEG'/>点击选择图片</div>" +
                "<div class='uploadimg-file-box'></div>" +
                "</div>";
            $target.append(tempHtml);
            if(defaults){
            	$target.find(".uploadimg-file-input").removeAttr("multiple");
            }
            var $fileBox = $target.find('.uploadimg-file-box');
            //图片展示
            function reloadBoxContent() {
                $fileBox.html("");
                for(var a = 0; a < imgSrcs.length; a++) {
                    $fileBox.append("<div><img title='" + imgNames[a] + "' alt='" + imgNames[a] + "' src='" + imgSrcs[a] + "'><p data-index='"+ a +"' class='imgDelete'>删除</p></div>");
                }
                $fileBox.find('img').on('click', function(){
                    if(options.imgClick && typeof options.imgClick == 'function'){
                        options.imgClick.call(this);
                    }
                });
                $fileBox.find('p.imgDelete').on('click', function(){
                    var index = $(this).data('index');
                    if(Number(index) >= 0){
                        imgSrcs.splice(index, 1);
                        imgFiles.splice(index, 1);
                        imgNames.splice(index, 1);
                        reloadBoxContent();
                        if(options.imgDelete && typeof options.imgDelete == 'function'){
                            options.imgDelete.call(this, imgFiles, index);
                        }
                    }
                });
            }
            //图片预览路径
            function getObjectURL(file) {
                var url = null;
                if(window.createObjectURL != undefined) { // basic
                    url = window.createObjectURL(file);
                } else if(window.URL != undefined) { // mozilla(firefox)
                    url = window.URL.createObjectURL(file);
                } else if(window.webkitURL != undefined) { // webkit or chrome
                    url = window.webkitURL.createObjectURL(file);
                }
                return url;
            }
            $target.find('input').on("change", function() {
                var files = this.files;
                for(var i = 0; i < files.length; i++) {
                    //var imgSrcI = getObjectURL(files[i]);
                    imgNames.push(files[i].name);
                    imgSrcs.push(getObjectURL(files[i]));
                    imgFiles.push(files[i]);
                }
                reloadBoxContent();
                if(options.imgSelect && typeof options.imgSelect == 'function'){
                    options.imgSelect.call(this, imgFiles, this.files);
                }
            })
            /*
            $target.find('.uploadimg-submit').on('click', function() {
                if(!imgFiles || imgFiles.length <= 0){
                    console.log('请至少选择一个文件');
                }
                //用formDate对象上传
                var formData = new FormData($target.find('form')[0]);
                for(var i = 0; i < imgFiles.length; i++){
                    formData.append("files[]", imgFiles[i]);
                }
                if(options.params){
                    for(key in options.params){
                        console.log(key +":"+ options.params[key]);
                        formData.append(key, options.params[key]);
                    }
                }
                submitPicture(options.url, formData);
            })

            //上传(将文件流数组传到后台)
            function submitPicture(url, data) {
                console.log(data);
                $.ajax({
                    type: "post",
                    url: url,
                    async: true,
                    data: data,
                    processData: false,
                    contentType: false,
                    success: function(dat) {
                        console.log(dat);
                    }
                });
            }
            */
        }
    });
}(jQuery));