// @ts-nocheck
$(function(){
	new Vue({
	    el: '#newService',
    mixins: [mixin_basic],
    data: function () {
    	return {
    		service:{
    			id:"",
    			name:"",
    			description:"",
    			number:"",
    			status:"",
    			container:{
    				Image:"",
    				tag:"",
    				Env:[],
    				HostConfig:{
    					Memory:1024,
    					Mounts:[]
    				}
    			}
    		},
    		ExposedPorts:[],
    		newExposedPort:"",
    		
    		
    		rules:{},
    		showBasicInfo: true,
    		imageList:[],
    		tagList:[],
    		basicNodeHeadActionName:"收起",
    		newEnv:["",""],
    		newMount:{
    			Source:"",
    			Target:"",
    			Type: "bind"
    		},
    		loading: false,
    		containerList:[]
    	}
    },
    created: function(){
    	this.init();
    },
    methods: {
    	init(){
    		this.queryImageList();
    		if(this.hasService){
    			this.queryContainers();
    			this.explainStore();
    		}
    	},
    	queryContainers: function(){
    		var vueThis = this;
    		var url = getContextPath() + "/service/queryContainers";
    		callRequest(url,{serviceId:serviceStore.state.service.id}, function(res){
    			try {
    				res.data.forEach(function(i){
    					i.loading = false;
    				});
    				vueThis.containerList = res.data;
    				for(i in vueThis.containerList){
    					vueThis.queryContainerState(vueThis.containerList[i]);
    				}
    				setTimeout(vueThis.queryContainers,30000);
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	queryContainerState: function(container){
    		var vueThis = this;
    		container.loading = true;
    		var url = getContextPath() + "/service/queryContainerStatus";
    		callRequest(url,{containerId:container.CONTAINER_ID}, function(res){
    			try {
    				container.state = res.data.State;
    				container.IP = res.data.NetworkSettings.Networks.bridge.IPAddress;
				} catch (e) {
					console.error(e);
				} finally {
					container.loading = false;
				}
    		});
    	},
    	queryImageList: function(){
    		var vueThis = this;
    		var url = getContextPath() + "/image/getImageList";
    		callRequest(url,{}, function(res){
    			try {
    				vueThis.imageList = res.data;
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
