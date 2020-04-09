// @ts-nocheck
$(function(){
	registerComponent("addDocument", true);

	var app = new Vue({
    el: '#documentManager',
    mixins: [mixin_basic],
    data: {
    	treeLoading: false,
    	filterText:"",
    	docTree:[],
    	docTreeProp: {
	        children: 'children',
	        label: 'name',
	        isLeaf: function(data, node){
	        	return node.data.type != '3';
	        }
    	},
    	pageMode:"QUERY",
    	currentNode:{id:"",name:""}
    },
    mounted:function(){
    	window.documentManager = this;
    	this.getDocTree();
    }, 
    methods: {
    	getDocTree: function(){
    		this.treeLoading = true;
    		var vueThis = this;
    		callRequest(getContextPath() + "/doc/getDocTree",{}, function(res){
    			try {
    				vueThis.docTree = res.data;
				} catch (e) {
				} finally{
					vueThis.treeLoading = false;
				}
    		});
    	},
    	filterNode(value, data) {
    		
            if (!value) return true;
            return data.name.indexOf(value) !== -1;
        },
        beginAdd: function(node,event){
        	this.pageMode = "ADD";
        	this.currentNode = node.data;
        	event.stopPropagation();
        },
        beginDel: function(node,event){
        	alert("2222");
        	event.stopPropagation();
        },
        nodeClass: function(node){
        	if(node.data.type == '1')
        		return "tree_node_project";
        	if(node.data.type == '2')
        		return "tree_node_moudule";
        	if(node.data.type == '3')
        		return "tree_node_api";
        	return "tree_node_moudule";
        },
        nodeIcon: function(node){
        	if(node.data.type == '1')
        		return "el-icon-folder";
        	if(node.data.type == '2')
        		return "el-icon-folder";
        	if(node.data.type == '3')
        		return "el-icon-tickets";
        	return "tree_node_moudule";
        }
    },
    watch: {
        filterText(val) {
          this.$refs.tree.filter(val);
        }
    }
})
})
