// @ts-nocheck
$(function(){
	
var app = new Vue({
    el: '#workerManager',
    mixins: [mixin_basic],
    data: {
    	loading: false,
    	machineIP:'',
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
    	workerList: [],
    	memUnits:[{"code":'K', "name":"KB"}, {"code":'M', "name":"MB"}, {"code":'G', "name":"GB"}],
    	memUnit:'M'
    },
    beforeDestroy: function(){
    	
    },
    created: function(){
    	debugger;
    	var vueThis = this;
    	var socket = new WebSocket("ws://localhost:88/machine?token=" + localStorage.getItem("chenjwToken"));
    	alert("111111")
    	//打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function(msg) {
        	vueThis.workerList = msg.data;
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function(e) {
        	debugger;
            console.log("websocket发生了错误");
        }
    },
    mounted:function(){
    	this.queryWorkerList();
    }, 
    methods: {
    	queryWorkerList: function(){
    		this.loading = true;
    		var vueThis = this;
    		var url = getContextPath() + "/machine/getMachineList";
    		callRequest(url,{}, function(res){
    			try {
    				vueThis.workerList = res.data;
				} catch (e) {
					console.error(e);
				} finally {
					vueThis.loading = false;
				}
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
