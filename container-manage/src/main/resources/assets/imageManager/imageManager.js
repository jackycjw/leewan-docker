// @ts-nocheck
$(function(){
	
var app = new Vue({
    el: '#imageManager',
    mixins: [mixin_basic],
    data: {
    	loading: false,
    	imageName:'',
    	activeName:'',
    	condition: {
    		compName: "",
    		invoiceDate: [],
    		invoiceType:"",
    		arriveDate:[],
    	},
    	pageInfo:{
    		pageIndex: 1,
    		pageSize: 15,
    		total: 0
    	},
    	imageList: [],
    	memUnits:[{"code":'K', "name":"KB"}, {"code":'M', "name":"MB"}, {"code":'G', "name":"GB"}],
    	memUnit:'G'
    },
    beforeDestroy: function(){
    	
    },
    created: function(){
    },
    mounted:function(){
    	this.queryImageList();
    }, 
    methods: {
    	queryImageList: function(){
    		this.loading = true;
    		var vueThis = this;
    		var url = getContextPath() + "/image/getImageList";
    		callRequest(url,{}, function(res){
    			try {
    				var imageList = [];
    				for(var item in res.data){
    					var name = res.data[item];
    					var image = {"keyId": name, "name": name , hasChildren: true, children:[]};
    					imageList.push(image);
    				}
    				vueThis.imageList = imageList;
    				
    				if(vueThis.activeName != null && vueThis.activeName.length > 0){
    					vueThis.queryTagList(vueThis.activeName);
    				}
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	queryTagList(name) {
    		var vueThis = this;
    		var url = getContextPath() + "/image/getImageTags";
    		callRequest(url,{name:name}, function(res){
    			try {
    				var tags = [];
    				for(var item in res.data){
    					var tag = res.data[item];
    					var image = {"keyId": name + ":" + tag, "name": name , "tag": tag};
    					tags.push(image);
    					
    				}
    				tags.reverse();
    				for(var i in vueThis.imageList){
    	    			if(vueThis.imageList[i].name == name){
    	    				vueThis.imageList[i].children = tags;
    	    				break;
    	    			}
    	    		}
    				
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
    		});
    	},
    	deleteImage: function(name, tag){
    		var vueThis = this;
    		this.$confirm('是否确认删除'+name+':'+tag+'?', '提示', {
    	          confirmButtonText: '确定',
    	          cancelButtonText: '取消',
    	          type: 'warning'
    	        }).then(() => {
    	        	var url = getContextPath() + "/image/deleteImage";
    	    		callRequest(url,{name:name, tag:tag}, function(res){
    	    			try {
    	    				if(res.code == "1000"){
    	    					vueThis.$message({
    	    	    	            type: 'success',
    	    	    	            message: '删除成功!'
    	    	    	          });
    	    				}
    	    				vueThis.queryImageList();
    					} catch (e) {
    						console.error(e);
    					} finally {
    						vueThis.loading = false;
    					}
    	    		});
    	          
    	        }).catch(() => {
    	        });
    	},
    	formatCondition: function(){
    		var vueThis = this;
    		var condition = this.condition;
    		condition.pageInfo = this.pageInfo;

    		if(condition.invoiceDate != null && condition.invoiceDate.length >= 2){
    			condition.startInvoiceDate = condition.invoiceDate[0].Format('yyyyMMdd');
    			condition.endInvoiceDate = condition.invoiceDate[1].Format('yyyyMMdd');
    		}else{
    			condition.startInvoiceDate = "";
    			condition.endInvoiceDate = "";
    		}
    		
    		if(condition.arriveDate != null && condition.arriveDate.length >= 2){
    			condition.startArriveDate = condition.arriveDate[0].Format('yyyyMMdd');
    			condition.endArriveDate = condition.arriveDate[1].Format('yyyyMMdd');
    		}else{
    			condition.startArriveDate = "";
    			condition.endArriveDate = "";
    		}
    		return condition;
    	},
    	chooseInvoiceType: function(row){
    		if(this.condition.invoiceType == row.code){
    			this.condition.invoiceType = "";
    		}else{
    			this.condition.invoiceType = row.code;
    		}
    		this.queryFinanceList();
    	},
    	showInvoiceList: function(row){
    		this.mode = DETAIL_MODE;
    		this.financeId = row.financeId;
    		this.invoiceVisible = true;
    	},
    	showFundList: function(row){
    		this.mode = DETAIL_MODE;
    		this.financeId = row.financeId;
    		this.fundVisible = true;
    	},
    	showDetail: function(row){
    		this.mode = DETAIL_MODE;
    		this.financeId = row.financeId;
    		this.financeDetailVisible = true;
    	},
    	close: function(){
    		this.queryFinanceList();
    	}
    }
})
})
