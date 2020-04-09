registerComponent("fundExchangeCompo", true);

	Vue.component('fundCompo', {
        template: '#fundCompo ',
        mixins: [mixin_finance_child_compo],
        created: function(){
        	window.fundCompo = this;
        	EventBus.$on('add_fund', this.addFund);
        	EventBus.$on('update_fund', this.updateFund);
        	
        },
        beforeDestroy: function(){
			EventBus.$off('add_fund', this.addFund);
			EventBus.$off('update_fund', this.updateFund);
		},
        data: function () {
        	return {
        		loading: false,
        		exchangeVisible: false,
        		fundList: [],
        		currentFundId:""
        	};
        },
        methods: {
        	initUpdateData: function(){
        		this.initDetailData();
        	},
        	initDetailData: function(){
        		var vueThis = this;
        		this.loading = true;
        		var url = getContextPath() + "/fund/queryFundList";
        		callRequest(url,{financeId: this.financeId}, function(res){
        			res = JSON.parse(res);
        			try {
        				vueThis.fundList = res.data;
    				} catch (e) {
    					console.error(e);
    				} finally{
    					vueThis.loading = false;
    				}
        		});
        	},
            addFund: function(data){
            	this.fundList.push(data);
            },
            updateFund: function(row){
            	
            	for(var key in this.fundList){
            		if(this.fundList[key].fundId == row.fundId){
            			Object.assign(this.fundList[key], row);
            		}
            	}
            },
            exchange: function(row){
            	this.currentFundId = row.fundId;
            	this.exchangeVisible = true;
            },
            deleteRow: function(row){
            	this.loading = true;
            	var rowData = row;
            	var url = getContextPath() + "/fund/deleteFund";
            	vueThis = this;
            	callRequest(url,{fundId: row.fundId}, function(res){
        			res = JSON.parse(res);
        			try {
        				var idx = vueThis.fundList.indexOf(rowData);
        				vueThis.fundList.splice(idx, 1);
    				} catch (e) {
    					console.error(e);
    				} finally{
    					vueThis.loading = false;
    				}
        		});
            }
        },
        watch:{
        	financeId:function(n,o){
        		if(this.mode == UPDATE_MODE || this.mode == DETAIL_MODE){
    				this.initDetailData();
    			}
        	}
        }
	});
