// @ts-nocheck
	
	Vue.component('financeCompo', {
        template: '#financeCompo ',
        mixins: [mixin_finance_child_compo],
        data: function () {
        	return {
        		loading: false,
        		clientCompanyId:"",
        		serviceCode:"",
        		amount: "",
        		amountCurrency:"",
        		remark: "",
        		companyList: [],
        		products:[],
        		currencies:[],
        		status:0
        	};
        },
        methods: {
        	initAddData: function(){
        		var vueThis = this;
        		var url = getContextPath() + "/company/queryCompanyList";
        		callRequest(url,{}, function(res){
        			res = JSON.parse(res);
        			try {
        				vueThis.companyList = res.data;
    				} catch (e) {
    					console.error(e);
    				} 
        		});
        		
        		this.products = dicStore.getters.getDicItem("PRODUCT_ITEM");
        		this.currencies = dicStore.getters.getDicItem("CURRENCY");
        	},
        	initDetailData: function(){
        		this.initAddData();
        		
        		var vueThis = this;
        		this.loading = true;
        		var url = getContextPath() + "/finance/getFinance";
        		callRequest(url,{financeId: this.financeId}, function(res){
        			res = JSON.parse(res);
        			try {
        				Object.assign(vueThis, res.data)
    				} catch (e) {
    					console.error(e);
    				} finally{
    					vueThis.loading = false;
    				}
        		});
        	},
        	save: function(){
        		var vueThis = this;
        		if(!this.checkData())
        			return;
        		
        		var data = {financeId:this.financeId, clientCompanyId:this.clientCompanyId, serviceCode: this.serviceCode, 
        				amount: this.amount, amountCurrency: this.amountCurrency, remark: this.remark};
        		
        		var url = getContextPath() + "/finance/saveFinance";
        		callRequest(url,data, function(res){
        			res = JSON.parse(res);
        			try {
        				if(res.code == 1000){
        					vueThis.$message({
        				          message: '保存成功',
        				          type: 'success'
        				        });
        					financeStore.state.dataStatus = 1;
        				}
    				} catch (e) {
    					console.error(e);
    				} 
        		});
        	},
        	checkData: function(){
        		if(isEmptyString(this.clientCompanyId)){
        			this.$notify.error({
        		          title: '错误',
        		          message: '客户公司不能为空'
        		     });
        			return false;
        		}
        		if(isEmptyString(this.serviceCode)){
        			this.$notify.error({
        		          title: '错误',
        		          message: '服务项不能为空！'
        		     });
        			return false;
        		}
        		if(isEmptyString(this.amount)){
        			this.$notify.error({
        		          title: '错误',
        		          message: '金额不能为空！'
        		     });
        			return false;
        		}
        		
        		if(isEmptyString(this.amountCurrency)){
        			this.$notify.error({
        		          title: '错误',
        		          message: '货币种类不能为空！'
        		     });
        			return false;
        		}
        		return true;
        		
        	}
        },
        
	});
