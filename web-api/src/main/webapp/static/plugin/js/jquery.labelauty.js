(function ($) {
    $.fn.extend({
        labelautychecked:function(){
            var target = $(this), label = target.next('label');
            if(target.prop("checked") == false){
                label.css({'background': '#ECF0F1', 'color': '#666', 'transition-property': 'background', 'transition-delay': '0s', 'transition-duration': '.1s', 'transition-timing-function': 'linear'});
            }else{
                label.css({'background': '#3498DB', 'color': '#fff'});
            }

        },
        labelauty:function(){
            var $this = $(this);
            if(!$this.is('input')){
                return;
            }
            var type = $this.attr('type');
            if(!type || ('radio' != type && 'checkbox' != type)){
                return;
            }
            var $label = $this.next('label');
            $this.css('display', 'none');
            $label.css({'display': 'inline-block','padding': '3px','height': '20px','line-height': '14px','background': '#eee', 'color': '#666', 'margin-right': '10px', 'text-align':'center', 'cursor': 'pointer'});
            //判断是否选中
            $this.each(function(){
                $(this).labelautychecked();
            });
            $label.on('click', function(){
                var label = $(this), target = label.prev('input'), name = target.attr('name');
                /*
                if('radio' == target.attr('type')){
                    target.prop("checked", "checked");
                }
                if('checkbox' == target.attr('type')){
                    if (target.prop("checked") == true) {
                        target.removeAttr("checked");
                    } else {
                        target.prop("checked", "checked");
                    }
                }
                */
                target.click();
                $("input[name='"+ name +"']").each(function(){
                    $(this).labelautychecked();
                });
            });
        }
    });
}(jQuery));