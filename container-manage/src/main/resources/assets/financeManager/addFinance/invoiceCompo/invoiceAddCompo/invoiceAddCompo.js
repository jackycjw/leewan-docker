	
	Vue.component('invoiceAddCompo', {
        template: '#invoiceAddCompo ',
        mixins: [mixin_basic],
        created: function(){
        	this.init();
        },
        data: function () {
        	return {
        		form:{
        			loading: false,
            		invoiceNumber:"",
            		invoiceAmount:"",
            		invoiceRate:"",
            		invoiceDate:"",
            		invoiceType:"1001",
            		redFlag: false,
            		remark:"",
            		invoiceTypes:[]
        		}
        	};
        },
        methods: {
        	init: function(){
        		this.invoiceTypes = dicStore.getters.getDicItem("INVOICE_TYPE");
        	},
        	formatData: function(){
        		var data = {};
        		Object.assign(data, this.form);
        		data.financeId = this.financeId;
        		data.invoiceDate = data.invoiceDate.Format("yyyyMMdd");
        		data.redFlag = data.redFlag? 1: 0;
        		return data;
        	},
        	save: function(){
        		var vueThis = this;
        		var data = this.formatData();
        		var url = getContextPath() + "/invoice/saveInvoice";
        		callRequest(url,data, function(res){
        			res = JSON.parse(res);
        			try {
        				if(res.code == 1000){
        					vueThis.$message({
        				          message: '保存成功',
        				          type: 'success'
        				        });
        					data.invoiceDate = vueThis.form.invoiceDate;
        					data.invoiceId = res.data;
        					EventBus.$emit('add_invoice', data);
        				}
    				} catch (e) {
    					console.error(e);
    				} 
        		});
        	}
        }
	});
