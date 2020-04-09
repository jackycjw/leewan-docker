// @ts-nocheck
$(function(){
	registerComponent("financeCompo", true);
	registerComponent("invoiceCompo", true);
	registerComponent("fundCompo", true);
	registerComponent("invoiceAddCompo", true);
	registerComponent("fundAddCompo", true);
	
	
	Vue.component('addFinance', {
        template: '#addFinance ',
    mixins: [mixin_basic],
    data: function () {
    	return {
	    	invoiceAddVisible: false,
	    	fundAddVisible: false
    	}
    },
    beforeDestroy: function(){
    	
    },
    created: function(){
    	window.addFinance = this;
    },
    methods: {
    	financeCompoSave: function(){
    		this.$refs.financeCompo.save();
    	},
    	addInvoice: function(){
    		
    	},
    	back: function(){
    		router.reference("generalMG","queryFinanceList");
    	}
    }
})
})
