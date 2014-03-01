
var setting = {
        view: {
            showIcon: showIconForTree
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
    		onClick: zTreeOnClick
    	}
    };

    function showIconForTree(treeId, treeNode) {
        return !treeNode.isParent;
    };
    
    function zTreeOnClick(event, treeId, treeNode) {
    	var url = _contextPath+"/monitor/"+ treeNode.id;
//       alert(treeNode.tId + ", " + treeNode.name +" ,"+ treeNode.id);
    	window.location.href = url;
    
    };
    $.ajax( {  
        url : _contextPath+"/settings/area.json",  
        type : "get",  
        dataType : "json",  
        success : init_tree
    });  
    function init_tree(data){
       	 $.fn.zTree.init($("#treeDemo"),setting,data );
    }
