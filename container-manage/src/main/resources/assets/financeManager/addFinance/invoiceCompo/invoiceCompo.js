	
	Vue.component('invoiceCompo', {
        template: '#invoiceCompo ',
        mixins: [mixin_finance_child_compo],
        created: function(){
        	
        	EventBus.$on('add_invoice', this.addInvoice);
        },
        beforeDestroy: function(){
			EventBus.$off('add_invoice', this.addInvoice);
		},
        data: function () {
        	return {
        		loading: false,
        		invoiceList:[]
        	};
        },
        methods: {
        	initUpdateData: function(){
        		this.initDetailData();
        	},
        	initDetailData: function(){
        		var vueThis = this;
        		this.loading = true;
        		var url = getContextPath() + "/invoice/queryInvoiceList";
        		callRequest(url,{financeId: this.financeId}, function(res){
        			;
        			res = JSON.parse(res);
        			try {
        				vueThis.invoiceList = res.data;
    				} catch (e) {
    					console.error(e);
    				} finally{
    					vueThis.loading = false;
    				}
        		});
        	},
        	tableRowClassName({row, rowIndex}) {
                if (row.redFlag == "1") {
                  return 'red-flag';
                }
                return '';
            },
            addInvoice: function(data){
            	this.invoiceList.push(data);
            },
            deleteRow: function(row){
            	this.loading = true;
            	var rowData = row;
            	var url = getContextPath() + "/invoice/deleteInvoice";
            	vueThis = this;
            	callRequest(url,{invoiceId: row.invoiceId}, function(res){
        			res = JSON.parse(res);
        			try {
        				var idx = vueThis.invoiceList.indexOf(rowData);
        				vueThis.invoiceList.splice(idx, 1);
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
