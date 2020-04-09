// @ts-nocheck
$(function(){
	Vue.component('financeFundList', {
        template: '#financeFundList ',
        mixins: [mixin_basic],
        mounted: function () {
        	this.queryList();
        },
        created: function(){
        },
        data: function () {
        	return {
        		loading: false,
        		fundList: []
        	};
        },
        methods: {
        	queryList: function(){
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
        	}
        },
        watch:{
        	financeId:function(n,o){
        		
        		this.queryList();
        	}
        }
	});
})
