<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript" src="${baseStatic}js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/bootstrap.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/jquery-confirm.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/nav.js" ></script>
<script type="text/javascript" src="${baseStatic}layer/layer.js" ></script>
<script type="text/javascript" src="${baseStatic}vue/vue.min.js" ></script>
<script type="text/javascript" src="${baseStatic}js/moment.min.js" ></script>
<script type="text/javascript" src="${baseStatic}js/common.js?times=${times}"></script>


<script>
var base_url = '${basePath}', base_static = '${baseStatic}', base_host = '${path_host}',base_img ='${baseImg}';

document.onkeydown = function (event) {
    if (!event) event = window.event;
    if ((event.keyCode || event.which) === 13) {
        return false;
    }
    var target, code, tag;
    if (!event) {
        event = window.event; //针对ie浏览器
        target = event.srcElement;
        code = event.keyCode;
        if (code === 13) {
            tag = target.tagName;
            if (tag === "TEXTAREA") { return true; }
            else { return false; }
        }
    }
    else {
        target = event.target; //针对遵循w3c标准的浏览器，如Firefox
        code = event.keyCode;
        if (code === 13) {
            tag = target.tagName;
            if (tag === "INPUT") { return false; }
            else { return true; }
        }
    }


}
</script>