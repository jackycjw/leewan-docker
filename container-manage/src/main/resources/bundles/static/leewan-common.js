// 对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}


window.pickerOptions = {
        shortcuts: [{
            text: '当月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setDate(1);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '今年',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setDate(1);
              start.setMonth(0);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '去年',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              
              start.setYear(start.getFullYear()-1);
              start.setDate(1);
              start.setMonth(0);
              
              end.setYear(end.getFullYear()-1);
              end.setMonth(11);
              end.setDate(31);
              picker.$emit('pick', [start, end]);
            }
          }]
  	}

window.getContextPath = function() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}

window.getHash= function(){
	var match = window.location.href.match(/#(.*)$/);
    return match ? match[1] : '';
}

window.getOldHash= function(oldUrl){
	var match = oldUrl.match(/#(.*)$/);
    return match ? match[1] : '';
}

window.getContainer= function(hash){
	return $("[container-name='"+hash+"']");
}

window.registerComponent = function(name, async){
	var comp = $("#components").find("#"+name);
	if(comp.length > 0){
		//该组件已存在，无需再次加载
		return;
	}
	$.ajax({
		url:"./getPage?pageName="+name,
		type:"get",
		async:async?false: true,
		success: function(data){
			$("#components").append(data);
		}
	});
};

window.homePageCode = "loadBalance";

window.message=function(msg){
	var vue = new Vue();
	vue.$message({
	    type: 'success',
	    message: '删除成功!'
	  });	
}

window.message=function(msg){
	var vue = new Vue();
	vue.$message({
	    type: 'success',
	    message: '删除成功!'
	  });	
}

window.error=function(msg){
	var vue = new Vue();
	vue.$message.error(msg);
}

window.callRequest = function(url, data, callBack, async){
	if(typeof(data) == "object"){
		data = JSON.stringify(data)
	}
	$.ajax({
		url: url,
		type:"post",
		data: data,
		dataType:"json",
		contentType: "application/json",
		headers:{token:localStorage.getItem("chenjwToken")},
		async:async?false: true,
		success: function(data){
			try {
				var tmp = data;
				try {var tmp = JSON.parse(data);} catch (e) {}
				
				if(tmp.code == "1002"){
					window.location.href = getContextPath() + "/getPage?pageName=login";
					return;
				}
				if(tmp.code == "1003"){
					error("您没有权限进行该操作，请联系管理员！");
					return;
				}
			} catch (e) {
				// TODO: handle exception
			}
			if(!!callBack){
				callBack(data);
			}
		}
	});
};

window.ADD_MODE=1
window.UPDATE_MODE=2
window.DETAIL_MODE=3


window.isEmptyString = function(str){
	return str == null || str.length == 0;
}

//全局事件总线
window.EventBus = new Vue(
    {
        methods: {
            //避免重复触发同一个方法
            updateEvent: function (name, f) {
                if (this._events[name] == f) {
                    return;
                } else {
                    this.$on(name, f);
                }
                ;
            }
        }
    }
);

window.getNewPrimary = function(){
	var key = "";
	var url = getContextPath() + "/finance/getNewFinanceId";
	callRequest(url,{}, function(res){
		res = JSON.parse(res);
		try {
			key = res.data;
		} catch (e) {
			console.error(e);
		}
	}, true);
	return key;
}
window.getContextPath = function(){
	return "";
}
