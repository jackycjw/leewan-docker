// @ts-nocheck
$(function(){
	new Vue({
	    el: '#newLoadBalance',
    mixins: [mixin_basic],
    data: function () {
    	return {
    		balance:{
    			name:"",
    			port:"",
    			containerPort:80,
    			serviceId:"",
    			description:"",
    			workerId:""
    		},
    		balanceList:[],
    		serviceList:[],
    		checkPortUsed:false,
    		loading: false
    	}
    },
    created: function(){
    	window.tt = this
    	this._init();
    },
    methods: {
    	_init(){
    		this.queryBalanceList();
    	},
    	queryBalanceList: function(){
    		var vueThis = this;
    		var url = getContextPath() + "/machine/getBalanceList";
    		callRequest(url,{}, function(res){
    			try {
    				vueThis.balanceList = res.data;
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	checkPort: function(){
    		var vueThis = this;
    		var url = getContextPath() + "/loadBalance/checkPortUsed";
    		var param= {workerId:this.balance.workerId ,port:this.balance.port};
    		callRequest(url, param, function(res){
    			try {
    				vueThis.checkPortUsed = res.data;
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	queryBalanceList: function(){
    		var vueThis = this;
    		var url = getContextPath() + "/machine/getBalanceList";
    		callRequest(url,{}, function(res){
    			try {
    				vueThis.balanceList = res.data;
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	queryTagList() {
    		var vueThis = this;
    		var url = getContextPath() + "/image/getImageTags";
    		callRequest(url,{name:this.service.container.Image}, function(res){
    			try {
    				var tags = [];
    				vueThis.tagList = res.data
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	
    	changeImage: function(){
    		this.service.container.tag = "";
    		if(!this.service.container.Image){
    			this.tagList = []
    		}else{
    			this.queryTagList();
    		}
    	},
    	deleteEnv: function(idx){
    		this.service.container.Env.splice(idx,1);
    	},
    	addEnv: function(){
    		if(!this.newEnv[0]){
    			this.$message.error('key值不能为空！');
    		}else{
    			this.service.container.Env.push(this.newEnv);
    			this.newEnv = ["",""]
    		}
    	},
    	deleteMount: function(idx){
    		this.service.container.HostConfig.Mounts.splice(idx,1);
    	},
    	addMount: function(){
    		if(!this.newMount.Source){
    			this.$message.error('主机目录不能为空！');
    			return;
    		}
    		if(!this.newMount.Target){
    			this.$message.error('容器目录不能为空！');
    			return;
    		}
    		this.service.container.HostConfig.Mounts.push(this.newMount);
    		this.newMount = {
        			Source:"",
        			Target:"",
        			Type: "bind"
        		};
    	},
    	deletePort: function(idx){
    		this.ExposedPorts.splice(idx,1);
    	},
    	addPort: function(){
    		var reg = /^\d+$/;
    		if(reg.test(this.newExposedPort.trim())){
    			this.ExposedPorts.push(this.newExposedPort.trim());
    			this.newExposedPort = "";
    		}else{
    			this.$message.error('端口格式不正确！');
    		}
    	},
    	saveBasicInfo: function(){
    		debugger;
    		this.loading = true;
    		var vueThis = this;
    		this.formateService();
    		url = getContextPath() + "/service/save";
    		callRequest(url,JSON.stringify(this.service), function(res){
    			try {
    				if(res.code=1000){
    					vueThis.$message({
				          message: '操作成功！',
				          type: 'success'
				        });
    					
    				}
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	
    	formateService:function(){
    		//端口结构化
    		var ExposedPorts = {};
    		for(i in this.ExposedPorts){
    			var key = this.ExposedPorts[i]+"/tcp"
    			var val = {};
    			ExposedPorts[key] = val;
    		}
    		this.service.container.ExposedPorts = ExposedPorts;
    		
    		this.service.container.Image = this.service.container.Image+":"+ this.service.container.tag;
    	},
    	explainStore: function(){
    		this.service = serviceStore.state.service
    		
    		//端口反解析
    		var ExposedPorts = this.service.container.ExposedPorts
    		this.ExposedPorts = [];
    		for(key in ExposedPorts){
    			var port = key.split("/")[0];
    			this.ExposedPorts.push(port);
    		}
    		
    		//镜像反解析
    		var image = this.service.container.Image;
    		this.service.container.Image = image.split(":")[0];
    		this.service.container.tag = image.split(":")[1];
    	},
		
    	
    	
    	updateEditMode:function(){
    		serviceStore.state.isEdit = !serviceStore.state.isEdit
    	},
    	startService : function(){
    		if(serviceStore.state.service.status == 1){
    			url = getContextPath() + "/service/startService";
    		}else{
    			url = getContextPath() + "/service/reStartService";
    		}
    		
    		callRequest(url,{serviceId:serviceStore.state.service.id}, function(res){
    			try {
    				if(res.code=1000){
    					vueThis.$message({
				          message: '操作成功！',
				          type: 'success'
				        });
    				}else{
    					vueThis.$notify.error({
    				          title: '失败',
    				          message: res.msg
    				        });
    				}
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	}
    }
})
})
