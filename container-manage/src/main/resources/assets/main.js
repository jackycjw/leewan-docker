// @ts-nocheck
$(function(){
var app = new Vue({
    el: '#app',
    data: {
    	isCollapse: true,
        menu:[],
        activeIndex: "1.0",
        btnMsg:"隐藏",
        defaultNav: {},
        userInfo:{}
    },
    created: function(){
    	this.init();
    	this.loadUserInfo();
    	this.getOuterRoleList();
    },
    methods: {
    	initNav: function(data){
    		var hash = getHash();
    		var defNav = null;
    		var hashNav = null;
    		for(var i in data){
				if(data[i].code == homePageCode){
					defNav = data[i];
				}
				if(data[i].code == hash){
					hashNav = data[i];
				}
			}
    		
    		if(defNav != null){
    			this.defaultNav  = defNav;
    		}
    		if(hashNav != null){
    			this.defaultNav  = hashNav;
    		}
    	},
    	loadUserInfo: function(){
    		var vueThis = this;
    		callRequest(getContextPath() + "/user/loadUser",{}, function(res){
    			try {
    				userStore.state.user = res.data;
				} catch (e) {
					console.error(e);
				}
    		});
    	},
    	getOuterRoleList: function(){
    		var vueThis = this;
    		callRequest(getContextPath() + "/user/getOuterRoleList",{}, function(res){
    			try {
    				userStore.state.outerRoles = res.data;
				} catch (e) {
					console.error(e);
				}
    		});
    	},
    	init: function(){
    		var vueThis = this;
    		callRequest(getContextPath() + "/module/queryModules",{}, function(res){
    			try {
    				var data = res.data;
    				var map = {};
    				vueThis.initNav(data);
    				for(var i in data){
    					var id = data[i].id;
    					map[id] = data[i];
    				}
    				var rs = [];
    				for(var i in data){
    					var id = data[i].id;
    					var pid = data[i].pid;
    					if(!!pid){
    						if(map[pid].children == null){
    							map[pid].children = [];
    						}
    						map[pid].children.push(data[i]);
    					}else{
    						rs.push(data[i]);
    					}
    				}
    				vueThis.menu = rs;
    				vueThis.defaultNavigate();
				} catch (e) {
					console.error(e);
				}
    		});
    	},
    	defaultNavigate: function(){
    		this.onNavigate(this.defaultNav);
    	},
        handleMenu: function(key, path){
        },
        onNavigate: function(menu){
            router.onNavigate(menu);
        }
        
    }
})
})
