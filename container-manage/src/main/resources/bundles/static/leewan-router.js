


/**
 * 控制页面流转、前进、后退
 */

var router = {
		elements:{},
		history:[],
		vueObject:{},
		keepCurrentAlive: true,
		loadPage: function(hash){
			$.ajax({
				url:"./getPage?pageName="+hash,
				type:"get",
				success: function(data){
					$("#pageContent").append(data);
				}
			});
		},
		goBack: function(){
			this.keepCurrentAlive = false;
			location.hash = this.history.pop()
		},
		onNavigate: function(menu){
			//从这里走不保存历史
			this.keepCurrentAlive = false;
			location.hash = menu.code;
		},
		reference: function(code, obj){
			
			//从这里走保存历史
			this.keepCurrentAlive = true;
			this.history.push(getHash());
			this.vueObject[getHash()] = obj;
			location.hash = code;
		}
		
};

$(function(){
	var hash = getHash();
	location.hash = "";
	addEventListener("hashchange", function(event){
		var oldUrl = event.oldURL, oldHash;
		oldHash =  getOldHash(oldUrl);
		var ele = getContainer(oldHash);
		if(ele.length > 0){
			if(router.keepCurrentAlive){
				ele.css({display: "none"});
			}else{
				ele.remove();
			}
		}
		
		
		var hash = getHash();
		if(!$.isEmptyObject(hash)){
			ele = getContainer(hash);
			if(ele.length == 0){
				router.loadPage(hash);
			}else{
				
				ele.css({display: ""});
				if(!!router.vueObject[hash]){
					router.vueObject[hash]._init();
				}
			}
		}
	})
})