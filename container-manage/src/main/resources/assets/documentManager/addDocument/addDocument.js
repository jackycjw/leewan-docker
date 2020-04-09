// @ts-nocheck
$(function(){
	Vue.component('addDocument', {
        template: '#addDocument ',
    mixins: [mixin_basic],
    data: function () {
    	return {
    		nodeHeadActionName:"收起",
    		newNodeShow: true,
    		newNode:{
    			name:"",
    			type: "2",
    			auth:3,
    			roles:[]
    		},
    		nodeTypes:[],
    		openLevel:[
    			{label: "管理员",value: 1},
    			{label: "开发人员",value: 2},
    			{label: "测试人员",value: 3},
    			{label: "对外开放",value: 4},
    		],
	    	form:{
	    		nameCh:"",
	    		nameEn:"",
	    		remark:""
	    	}
    	}
    },
    props:{
    	parentNode:{
    		type: Object,
    	      // 对象或数组默认值必须从一个工厂函数获取
	    	default: function () {
	    		return {id: '',name: ''}
	    	}
    	}
    },
    created: function(){
    	this.init();
    },
    methods: {
    	init(){
    		this.nodeTypes = dicStore.getters.getDicItem("DOC_TREE_TYPE");
    	},
    	nodeHeadAction: function(){
    		this.newNodeShow = !this.newNodeShow;
    		this.nodeHeadActionName = this.newNodeShow? "收起":"展开";
    	},
    	
    	checkData: function(){
    		return !(this.form.nameCh=="" && this.form.nameEn == "");
    	},
    	save: function(){
    		if(!this.checkData()){
    			this.$message.error('中文名称和英文名称不可同时为空');
    			return;
    		}
    		var url = getContextPath() + "/company/saveCompany";
    		callRequest(url,this.form, function(res){
    			res = JSON.parse(res);
    			try {
    				if(res.code == 1000){
    					vueThis.$message({
    				          message: '保存成功',
    				          type: 'success'
    				        });
    				}
				} catch (e) {
				} 
    		})
    	},
    }
})
})
