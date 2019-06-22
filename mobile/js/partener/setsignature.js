window.onload = function() {
  window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function() {  
    if (window.orientation === 180 || window.orientation === 0) {   
      window.location.reload() ;
    }   
    if (window.orientation === 90 || window.orientation === -90 ){   
      window.location.reload() ; 
    }    
}, false);   
};

const graphics = new Graphics({
    el: document.getElementById('canvas'), // 挂载节点
    linewidth: 2,                          // 线条宽度
    color: '#000',                         // 线条颜色
    background: '#fff'                     // 背景颜色
});

function clearGraphics(){
	graphics.clear();
}
function save(){
	graphics.save();
}