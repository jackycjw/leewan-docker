	
	Vue.component('fundAddCompo', {
        template: '#fundAddCompo ',
        mixins: [mixin_basic],
        created: function(){
        	this.init();
        },
        data: function () {
        	return {
        		form:{
        			loading: false,
        			fundActualAmount:null,
        			amountCurrency:"",
        			arriveDate: "",
        			relatedAmount:"",
        			relatedAmountCurrency:"1001",
        			exchangeDate:"",
        			remark:"",
            		direction:1
        		},
        		rules: {
        			fundActualAmount: [
        	            { required: true,  message: '输入金额', trigger: 'blur' },
        	          ],
        	        amountCurrency: [
        	            { required: true, message: '选择货币', trigger: 'blur' }
        	          ],
        	        arriveDate: [
        	            { type: 'date', required: true, message: '请选择日期', trigger: 'blur' }
        	          ],
        	        relatedAmount: [
        	            { type: 'number', message: '金额为数字', trigger: 'blur'}
        	          ]
        	     }
        	};
        },
        methods: {
        	init: function(){
        		this.currencies = dicStore.getters.getDicItem("CURRENCY");
        	},
        	formatData: function(){
        		var data = {};
        		Object.assign(data, this.form);
        		data.financeId = this.financeId;
        		try {data.arriveDate = data.arriveDate.Format("yyyyMMdd");} catch (e) {}
        		try {data.exchangeDate = data.exchangeDate.Format("yyyyMMdd");} catch (e) {}
        		return data;
        	},
        	save: function(){
        		var vueThis = this;
        		this.$refs['fundForm'].validate((valid) => {
        			   var veuThis = vueThis;
        	          if (valid) {
        	        	  var data = this.formatData();
        	        		var url = getContextPath() + "/fund/saveFund";
        	        		callRequest(url,data, function(res){
        	        			res = JSON.parse(res);
        	        			try {
        	        				if(res.code == 1000){
        	        					vueThis.$message({
        	        				          message: '保存成功',
        	        				          type: 'success'
        	        				        });
        	        					var data = {};
        	        	        		Object.assign(data, veuThis.form);
        	        	        		data.fundId = res.data;
        	        					EventBus.$emit('add_fund', data);
        	        					Object.assign(veuThis.$data, veuThis.$options.data());
        	        				}
        	    				} catch (e) {
        	    				} 
        	        		});
        	          } else {
        	            console.log('error submit!!');
        	            return false;
        	          }
        	     });
        	 
        		
        		/*var vueThis = this;
        		*/
        	}
        },
        computed:{
        	form:{
        		directionProxy:{
	        		get:function(){
	        			return this.form.direction == 1;
	        		},
	        		set: function(v){
	        			this.form.direction = v?1:-1;
	        		}
        		}
        	}
        }
	});
