/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-3-23
 * Time: 下午6:22
 * Desc: 实现一棵简单的树
 */
function simpleTree(node, json, status){
    var html = '<dl>';
    html += this.createFolder(json, status, true);
    html += '</dl>';
    node.innerHTML = html;
    var _this = this;
    node.onclick = function(e){
        var evt = e || window.event,
            target = evt.target || evt.srcElement;
        if(target.nodeName == 'IMG' && target.getAttribute('icon-type') == 'folderHandle'){
            var ctn = target.parentNode.parentNode.parentNode,
                isOpen = _this.hasClass(ctn, 'open');
            _this.replaceClass(ctn, isOpen?'open':'close', isOpen?'close':'open');
            target.src = _this.config.icon.folder[ctn.getAttribute('tree-position')][isOpen?1:0];
        }
    }
}
$.extend(simpleTree.prototype, {
    config: {
        icon: {
            folder: {
                root: ['../static/images/tree/o_folderNodeOpenLastFirst.gif', '../static/images/tree/o_folderNodeLastFirst.gif']
                ,branchroot: ['../static/images/tree/o_folderNodeOpenFirst.gif', '../static/images/tree/o_folderNodeFirst.gif']
                ,branch: ['../static/images/tree/o_folderNodeOpen.gif', '../static/images/tree/o_folderNode.gif']
                ,branchlast: ['../static/images/tree/o_folderNodeOpenLast.gif', '../static/images/tree/o_folderNodeLast.gif']
            }
            ,file: {
                branchleaf: '../static/images/tree/o_docNode.gif'
                ,leaf: '../static/images/tree/o_docNodeLast.gif'
            }
        }
    },
    addClass: function(node,className){
        node.className += ' ' + className;
    },
    replaceClass: function(node,oldClassName,newClassName){
        node.className = node.className.replace(oldClassName,newClassName);
    },
    hasClass: function(node,className){
        var reg = new RegExp('\\b' +className+ '\\b');
        return reg.test(node.className);
    },
    //{'海淀':['五道口','知春路'],'朝阳':['大望路','望京']}的第一次循环，item为['五道口','知春路']，key为海淀，cur为1，num为2
    //['五道口','知春路','上地']的第一次循环，item为五道口，key为0，cur为1，num为3
    getNode: function(item, key, cur, num, status, isRoot){//item待生成的节点(对象或者字符串), key当前对象的对象名，cur：当前第几次循环，num：总共几次循环
        var htm = '';
        if(typeof item == 'object'){//生成文件夹形式的dd节点
            var position = isRoot && cur == 1?(num>1?'branchroot':'root'):(cur==num?'branchlast':'branch');//根文件夹判断是独根还是多个分支的根，分支文件夹判断是否为最后一个分支
            htm += '<dd><dl class="'+status + (position=='branch'?' line':(position=='branchroot'?' line':''))+'" tree-position="'+position+'">';
            htm += '<dt><span><img src="'+this.config.icon.folder[position][status=='open'?0:1]+'" icon-type="folderHandle" /><img src="../static/images/tree/o_folder.gif" /></span>'+key+'</dt>';
            htm += this.createFolder(item, status);
            htm += '</dl></dd>';
        }else{//生成文件形式的dd节点
            htm += '<dd><span><img src="'+this.config.icon.file[cur==num?'leaf':'branchleaf']+'" /><img src="../static/images/tree/o_doc.gif" /></span>'+(item||key)+'</dd>';
        }
        return htm;
    },
    createFolder: function(json, status, isRoot){

        var html = '';
        if(Object.prototype.toString.apply(json) == '[object Array]'){//数组
            for(i=0,len=json.length;i<len;i++){
                if(typeof json[i] == 'object')
                    html += this.createFolder(json[i], status);
                else
                    html += this.getNode(json[i], i, i+1, len, status, isRoot);
            }
        }else if(typeof json == 'object'){//对象
            var len = 0,cur=1;
            for(var key in json){len++;}
            for(var key in json){
                html += this.getNode(json[key], key, cur++, len, status, isRoot);
            }
        }else{
            return '';
        }
        return html;
    }
});