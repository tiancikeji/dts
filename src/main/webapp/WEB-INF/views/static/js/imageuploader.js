/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-7
 * Time: 下午3:12
 */
(function(){
    var basicEvent = {
        createBasicEvent: function(){
            this._bellLabsEvent = {}; //创建事件对象的空间
        },
        bind: function(evt, func, scope){
            this._bellLabsEvent[evt] = {
                func: func,
                scope: scope || this
            };
            return this;
        },
        trigger: function(evt){
            if(this._bellLabsEvent[evt]){
                var evtTarget = this._bellLabsEvent[evt];
                var args = $.makeArray(arguments);
                args[0] = {target: this, type: evt};
                evtTarget.func.apply(evtTarget.scope, args);
            }
            return this;
        },
        unbind: function(evt){
            this._bellLabsEvent[evt] = null;
            return this;
        }
    };
    $.Dts.basicEvent = basicEvent;
})();
/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-7
 * Time: 下午3:13
 */
/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 13-7-1
 * Time: 下午4:15
 */
(function(){
    function Html5Uploader(url, options){
        var defaultOpt = {
            compress: {
                useCompress: true, //是否启用压缩，启用后会根据limit的配置对图片进行压缩
                compressGif: false, //所否压缩gif图片
                compressPng: false //所否压缩png图片
            },
            limit: {
                size: 3 * 1024 * 1024, //图片最大kb
                width: 3000, //图片最大宽度
                height: 3000 //图片最大高度
            },
            force: null,
            thumbs: {
                useThumbs: true, //是否上传缩略图
                width: 200, //缩略图宽度
                height: 200 //缩略图高度
            }
        };
        this._url = url;
        this.createBasicEvent();
        this._options = $.extend(true, defaultOpt, options || {});
    }
    $.extend(Html5Uploader.prototype, {
        _createXHR: function(){
            var self = this,
                sf = this._successFilter;
            this._xhr = new XMLHttpRequest();

            this._xhr.onload = function(evt){
                var result;
                try{
                    result = $.parseJSON(self._xhr.responseText);
                }catch(e){
                    result = self._xhr.responseText;
                }
                if(sf){
                    if(sf(result)){
                        self.trigger('success', self._xhr, result);
                    }else {
                        self.trigger('error', self._xhr, result);
                    }
                }else {
                    self.trigger('success', self._xhr, result);
                }
            };
            this._xhr.onerror = function(evt){
                self.trigger('error', self._xhr);
            };
            this._xhr.onabort = function(evt){
                self.trigger('abort', self._xhr);
            };
            this._xhr.upload.onprogress = function(evt){
                self.trigger('progress', evt.loaded, evt.total);
            };
        },
        /***
         * @desc 上传URI
         */
        _uploadUri: function(dataURI){
            var _this = this;
            var byteString = atob(dataURI.split(',')[1]); //decode base64
            var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

            // URL.createObjectURL
            window.URL = window.URL || window.webkitURL;

            var response = 'self.onmessage = function(evt) { var byteString = evt.data;var ab = new ArrayBuffer(byteString.length);var ia = new Uint8Array(ab);'+
                'for (var i = 0; i < byteString.length; i++) {'+
                'ia[i] = byteString.charCodeAt(i);'+
                '}'+
                'postMessage(ab);'+
                'close();'+
                '};';

            var blob;
            var BlobBuilder = (window.MozBlobBuilder || window.WebKitBlobBuilder || window.BlobBuilder);

            if (BlobBuilder) {
                blob = new BlobBuilder();
                blob.append(response);
                blob = blob.getBlob(mimeString);
            } else if (Blob) {
                blob = new Blob([response], {type: 'application/javascript'});
            } else {
                _this.trigger('error', _this._xhr, '文件压缩失败');
                return;
            }

            var worker = new Worker(URL.createObjectURL(blob));
            worker.onmessage = function(evt){
                var ab = evt.data;

                if (BlobBuilder) {
                    var bb = new BlobBuilder();
                    bb.append(ab);
                    _this._uploadBlob(bb.getBlob(mimeString));
                } else if (Blob) {
                    _this._uploadBlob(new Blob([ab], { "type" : mimeString }));
                } else {
                    _this.trigger('error', _this._xhr, '文件压缩失败');
                    return;
                }
            };
            worker.postMessage(byteString);
        },
        /**
         * 压缩URI并上传
         * */
        _compressAndUploadUri: function(dataURI, file){
            var _this = this;
            var img = new Image();
            img.onload = function(){
                if(_this._options.force){
                    var force = _this._options.force;
                    if(force.width && (force.width != img.width)){
                        _this.trigger('error', _this._xhr, '图片宽度必须为' + force.width +'像素');
                        return;
                    }
                    if(force.height && (force.height != img.height)){
                        _this.trigger('error', _this._xhr, '图片高度必须为' + force.height +'像素');
                        return;
                    }
                }

                if(!_this._options.compress.useCompress){
                    _this._uploadBlob(dataURI);
                    return;
                }

                var canvas = document.createElement('canvas');
                var canvasSize = _this._getCompressSize(img.width, img.height);
                var newDataURI;
                canvas.width = canvasSize.width;
                canvas.height = canvasSize.height;
                canvas.getContext('2d').drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);
                newDataURI = canvas.toDataURL('image/jpeg', 0.8);

                //_this._uploadUri(newDataURI);
                _this._uploadBlob(newDataURI);
                img.onload = null;
                img.src="about:blank";
                img = null;
                canvas.getContext('2d').clearRect(0, 0, canvasSize.width, canvasSize.height);
                canvas.width = canvas.height = 0;
                canvas = null;

            };
            img.src = dataURI;
        },
        _uploadBlob: function(blob, blobThumb){
            if(blob.size > this._MAX_SIZE){	//压缩后还是超大
                this.trigger('error', this._xhr, '文件大小超出限制');
                return;
            }
            var formData = new FormData(),
                variables = this._variables;
            if(variables && typeof variables == 'object'){
                for(var i in variables){
                    formData.append(i, variables[i]);
                }
            }
            formData.append('data', blob, this.fileName);
            formData.append('filename', this.fileName);

            if(blobThumb){
                formData.append('thumb', blobThumb);
            }

            this._active = true;

            if(!this._xhr){	//初始化xhr
                this._createXHR();
            }

            this._xhr.open('POST', this._url, true);
            this._xhr.withCredentials = true;	//需要服务端设置 Access-Control-Allow-Credentials: true 携带cookie
            this._xhr.send(formData);


        },
        _getCompressSize: function(width, height){
            var w = this._options.limit.width,
                h = this._options.limit.height,
                d = width / height,
                size = {};
            if(w < width || h < height){
                if(d > w/h){
                    size.width = w;
                    size.height = w / d;
                }else{
                    size.height = h;
                    size.width = h * d;
                }
            }else{
                size = {
                    width : width,
                    height : height
                };
            }
            return size;
        },
        setVariable: function(variable){
            this._variables = variable;
        },
        /***
         * @desc 上传接口
         * @param	{File or Canvas or DataURI}	图片资源
         * @param	{Object}					上传参数
         * @param	{Number}	上传最大容量
         * @param	{String} dataURLType dataURL的格式
         */
        upload: function(file){
            //this.abort();
            if(!file) return;

            this.fileName = encodeURIComponent(file.name).replace(/%/g, '');

            if(this._options.compress.compressGif == true){ //把压缩后的gif文件重命名为jpg文件
                this.fileName = this.fileName.replace(/\.gif$/, '.jpg');
            }

            if(this._options.compress.compressPng == true){ //把压缩后的png文件重命名为jpg文件
                this.fileName = this.fileName.replace(/\.png/, '.jpg');
            }

            //不可压缩而且没有限死宽度高度(限制死后需要检查)，此时直接上传file
            if(!this._options.compress.useCompress && !this._options.force){
                this._uploadBlob(file);
                return;
            }

            if(this._options.compress.useCompress){
                if(file.type && this._options.compress.compressGif == false && 'image/gif'.indexOf(file.type) > -1){ //gif不压缩
                    this._options.compress.useCompress = false;
                }
                if(file.type && this._options.compress.compressPng == false && 'image/png'.indexOf(file.type) > -1){ //png不压缩
                    this._options.compress.useCompress = false;
                }
            }

            var _dataURI, self = this;

            var reader = new FileReader();
            reader.onload = function(evt){
                reader.onload = null;
                _dataURI = evt.target.result;
                self._compressAndUploadUri(_dataURI, file);
            };
            reader.readAsDataURL(file);
        },
        abort: function(){
            if(this._active) {
                this._xhr.abort();
                this._active = false;
            }
        },
        distroy: function(){
            this.abort();
            this.unbind('success');
            this.unbind('error');
            this.unbind('abort');
            this.unbind('progress');
            this._xhr = null;
        }
    }, $.Dts.basicEvent);
    $.Dts.Html5Uploader = Html5Uploader;
})();

/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-7
 * Time: 下午3:14
 */

(function(){
    /**
     * Html5图片上传
     * @param {Object}		options 参数
     * @param {Node|jQuery} options.uploadButton 上传按钮容器 可以是node也可以是jq对象
     * @param {string}      options.uploadUrl 图片的上传地址
     * @param {int}			[options.maxSize=3*1024*1024] 上传文件压缩后的最大字节数，此参数仅代表压缩后的字节数，不限制用户选择的文件的原本字节数。当文件无法被压缩到此字节数以下时会抛出错误信息
     * @param {int}			[options.maxWidth=3000] 图片压缩后的最大宽度。所有需要压缩的图片均会被压缩到此大小内
     * @param {int}			[options.maxHeight=3000] 图片压缩后的最大高度。所有需要压缩的图片均会被压缩到此大小内
     * @param {Boolean}     [options.isAutoUp=false] 是否选择文件后立即自动上传
     * @param {int}			[options.maxParallel=undefined] 同时上传文件的最大个数，建议使用默认值
     * @param {Function}	[options.getUploadParams=null] 获取上传参数的函数句柄。每张图片上传时都会通过此方法获取上传参数。此方法参数为图片的id，返回值为上传参数的json
     * @param {Boolean}		[options.isFlash=false] 是否强制使用flash
     * @param {Boolean}		[options.useThumbs=true] 是否上传缩略图
     */
    function ImageUploadByHtml5(options){
        var defaultOpt = {
            upload: {
                url: 'pkg/server/upload.php', //上传地址
                auto: true, //自动上传
                format: 'jpg|gif' //图片格式筛选，支持jpg gif bmp png
            },
            compress: {
                useCompress: true, //是否启用压缩，启用后会根据limit的配置对图片进行压缩
                compressGif: false, //所否压缩gif图片
                compressPng: false //所否压缩png图片
            },
            limit: {
                size: 3 * 1024 * 1024, //图片最大kb
                width: 3000, //图片最大宽度
                height: 3000 //图片最大高度
            },
            thumbs: {
                useThumbs: true, //是否上传缩略图
                width: 200, //缩略图宽度
                height: 200 //缩略图高度
            }
        };

        if(options.uploadButton){ //包装上传按钮，并计算上传按钮的大小
            var $uploadButton = $(options.uploadButton);
            defaultOpt.width = $uploadButton.width();
            defaultOpt.height = $uploadButton.height();
            defaultOpt.uploadButton = $uploadButton;
        }
        this._options = $.extend(true, defaultOpt, options || {});
        this._uploadQueue = []; //文件上传器列表
        this._uploadTimer = null; //上传定时器
        this._activeTotal = 0; //正在上传中的文件总数
        this._typeFilter = this._options.upload.format.replace('png','image/png').replace('jpg','image/jpeg, image/jpg').replace('gif','image/gif').replace('bmp','image/bmp').replace(/\|/g,','); //文件类型,文件选择窗口里只会列出这些类型的文件
        this.maxParallel = 1;
        this.createBasicEvent();
        this._reset();
    }

    $.extend(ImageUploadByHtml5.prototype, {
        /**
         * 开始上传
         * @method start
         */
        start: function(){
            this._trigger('start');
            this._startProgress();
        }
        /**
         * 停止上传
         * @method stop
         */
        ,stop: function(){
            this._stopProgress();
        }
        /**
         * 删除文件
         * @method deleteFile
         * @param {String} id 需要删除文件的id
         */
        ,deleteFile: function(id){
            for(var i = 0; i < this._uploadQueue.length; i ++){
                var upload = this._uploadQueue[i];
                if(upload && upload.id == id){ //找到需要删除的文件
                    upload.uploader.distroy();
                    if(upload.isStarted && upload.percent < 100){ //正在上传中，释放资源
                        this._activeTotal--;
                    }
                    this._uploadQueue.splice(i, 1);
                    break;
                }
            }
        }
        /**
         * 清空所有文件
         * @method clearList
         */
        ,clearList: function(){
            this._uploadQueue.length = 0;
        }
        /**
         * 重传错误文件
         * @method reUploadError
         * @param {String} [id] 需要重传文件的id，若不传id，重传所有错误文件
         */
        ,reUploadError: function(id){
            this._resetErrorStatus(id);
            this.start();
        }

        ,_reset: function(){
            this._initUI();
            this._bindEvents();
        }
        ,_initUI: function(){
            this._fileInput = $('<input type="file" id="multi_file" style="display:none" multiple accept="'+this._typeFilter+'" />').get(0);
        }
        ,_bindEvents: function(){
            var self = this;
            this._fileInput.onclick = function(){
                self._fileInput.value = '';
            };
            this._fileInput.onchange = function(){
                self._selectFile(self._fileInput.files);
            };
            if(this._options.uploadButton){
                this._options.uploadButton.bind('click', function(){
                    self._fileInput.value = '';
                    self._fileInput.click();
                });
            }
        }
        ,_selectFile: function(files){ //选中文件
            var imageList = []; //当前选中文件列表
            for(var i = 0, fileLen = files.length; i < fileLen; i++){
                var file = files[i];
                if(file.type != '' && this._typeFilter.indexOf(file.type) >= 0){
                    imageList.push(this._add(file));
                }
            }
            this._trigger('selected', {
                imageList: imageList
                ,percent: 0
            });
            if(this._options.upload.auto){
                this.start();
            }
        }
        ,_add: function(source){ //加入文件到上传序列中
            var self = this
                ,queue = self._uploadQueue
                ,upload = {
                    name: source.name
                    ,size: source.size
                    ,id: 'img_' + this._makeToken()+ '_' + queue.length
                    ,percent: 0
                    ,errorCode: 0
                    ,errorMessage: null
                    ,source: source
                    ,isStarted: false
                }
                ,uploader;
            queue.push(upload);
            var validResult = this._validFile(source);
            if(validResult.errorCode){ //出错
                upload.errorCode = validResult.errorCode;
                upload.errorMessage = validResult.errorMessage;
                this._trigger('error' , upload);
                upload.percent = 100;
                upload.isStarted = true;
                return upload;
            }
            uploader = new $.Dts.Html5Uploader(this._options.upload.url, this._options);
            upload.uploader = uploader;

            uploader.id = upload.id;
            uploader.bind('success', this._uploadedHandler, this);
            uploader.bind('error', this._uploadedHandler, this);
            uploader.bind('progress', function(upload){ //为文件上传对象设置进度
                return function(evt, loaded, total){
                    upload.percent = Math.min(upload.percent + parseInt((100 - upload.percent) * loaded / total, 10) , 99); //此处最多达到99%。因为此处还未得到response
                };
            }(upload));
            return upload;
        }
        ,_upload: function(){
            var _tempPercent = 0; //总的进度计算
            var _allcomplete = true; //是否全部完成
            for(var i = 0; i < this._uploadQueue.length; i ++){
                var upload = this._uploadQueue[i];
                if(!upload){ //文件不存在
                    continue;
                }
                if(upload.percent < 100){
                    _allcomplete =false;
                }
                if(this._activeTotal < this.maxParallel && !upload.isStarted){ //触发上传
                    upload.isStarted = true; //一旦开始上传，无法撤销，isStarted值永为true
                    if(this.getUploadParams){
                        upload.uploader.setVariable(this.getUploadParams(upload.id));
                    }
                    upload.uploader.upload(upload.source);
                    this._activeTotal++;
                }

                if(upload.percent < 50){ //模拟进度
                    upload.percent += 2;
                }
                _tempPercent += upload.percent / this._uploadQueue.length;
            }
            if(_allcomplete){
                this._percent = 100;
                this.stop();
                this._trigger('complete');
            }else{
                this._percent = parseInt(_tempPercent, 10);
                this._trigger('progress');
            }
        }
        ,_uploadedHandler: function(evt, xhr, data){
            var id = evt.target.id;
            this._activeTotal --;
            this._refreshParallel();
            var upload = this._getFileById(id);
            if(!upload){ //上选序列被销毁后upload无法获取
                return false;
            }

            if(evt.type == 'error'){
                upload.errorCode = 2;
                upload.errorMessage = data || '网络错误';
                this._trigger('error' , upload);
                upload.percent = 100;
            }

            if(evt.type == 'success'){
                upload.response = data || null;
                upload.percent = 100;
            }

            evt.target.unbind('success', this._uploadedHandler);
            evt.target.unbind('error', this._uploadedHandler);
        }
        ,_trigger: function(eventName, data){
            this.trigger(eventName, data || {
                imageList: this._uploadQueue
                ,percent: this._percent
            });
        }
        ,_getFileById: function(id){
            for(var i = 0; i < this._uploadQueue.length; i ++){
                var upload = this._uploadQueue[i];
                if(upload && upload.id == id){ //找到文件
                    return upload;
                }
            }
            return null;
        },
        _validFile: function(file){
            var errorCode = 0;
            var errorMessage = '';
            if(!(file.size > 0)){
                errorCode = 1;
                errorMessage = '文件大小为0，无法上传';
            }else if(this._options.compress.useCompress == false && file.size > this._options.limit.size){ //不允许压缩且大小过大
                errorCode = 1;
                errorMessage = '文件大小超出限制，最大' + this._options.limit.size / 1024 / 1024 + 'M';
            }
            return {
                errorCode: errorCode,
                errorMessage: errorMessage
            };
        },
        _resetErrorStatus: function(id){ //重置状态以便重新上传,不传id代表重置所有错误文件状态，否则重置id为id的那个文件状态
            for(var i = 0; i < this._uploadQueue.length; i ++){
                var upload = this._uploadQueue[i];
                if(upload && upload.errorCode !== 0){//有错则重置
                    if(!id || id && upload.id == id){
                        upload.percent = 0;
                        upload.errorCode = 0;
                        upload.errorMessage = null;
                        upload.isStarted = false;
                        upload.response = null;
                        if(id){ //重置完毕
                            break;
                        }
                    }
                }
            }
        }
        ,_startProgress: function(){
            if(!this._uploadTimer){
                var self = this;
                this._uploadTimer = setInterval(function(){self._upload();}, 800);
            }
        }
        //根据剩余文件的情况判断当前的并发数量
        ,_refreshParallel: function(){
            var totalSize = 0;
            var uploadRemain = 0;
            for(var i = 0; i < this._uploadQueue.length; i ++){
                var upload = this._uploadQueue[i];
                if(upload.percent < 100){
                    totalSize += upload.source.size;
                    uploadRemain++;
                }
            }
            if(!uploadRemain){
                return;
            }
            var sizeNum = Math.min(Math.ceil(6 / (totalSize / uploadRemain / 1024 / 1024)), 50); //假定总共使用6M的资源 如果图片都大于6M，每次只上传一张
            var areaNum; //假定12000的资源，如果图片宽度高度加起来超过12000，每次只上传一张
            if(this._options.compress.useCompress){
                areaNum = Math.min(Math.ceil(12000 / (this._options.limit.width + this._options.limit.height)), 50);
                this.maxParallel = Math.floor((sizeNum + areaNum) / 2);
            }else{
                this.maxParallel = sizeNum;
            }
        }
        ,_stopProgress: function(){
            clearInterval(this._uploadTimer);
            this._uploadTimer = null;
        }
        ,_makeToken: function(){
            var S4 = function() {
                return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            };
            return (S4()+S4()+'_'+S4()+S4()+S4());
        }
    }, $.Dts.basicEvent);
    $.Dts.ImageUploadByHtml5 = ImageUploadByHtml5;
})();
/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-7
 * Time: 下午3:13
 */
(function(){
    function ImageUploadByFlash(options){
        var defaultOpt = {
            upload: {
                url: 'pkg/server/upload.php', //上传地址
                auto: true, //自动上传
                format: 'jpg|gif' //图片格式筛选，支持jpg gif bmp png
            },
            compress: {
                useCompress: true, //是否启用压缩，启用后会根据limit的配置对图片进行压缩
                compressGif: false, //所否压缩gif图片
                compressPng: false //所否压缩png图片
            },
            force: null,
            limit: {
                size: 3 * 1024 * 1024, //图片最大kb
                width: 3000, //图片最大宽度
                height: 3000 //图片最大高度
            },
            thumbs: {
                useThumbs: true, //是否上传缩略图
                width: 200, //缩略图宽度
                height: 200 //缩略图高度
            }
        };
        if(options.uploadButton){ //包装上传按钮，并计算上传按钮的大小
            var $uploadButton = $(options.uploadButton);
            defaultOpt.width = $uploadButton.width();
            defaultOpt.height = $uploadButton.height();
            defaultOpt.uploadButton = $uploadButton;
        }
        this._options = $.extend(true, defaultOpt, options || {});
        this._token = this._makeToken();
        this._flashVars = '';
        this._version = '1_0_1_3' + (+new Date());
        this._EVENTS = [
            'selected'
            ,'start'
            ,'progress'
            ,'complete'
            ,'error'
        ];
        this.createBasicEvent();
        this._reset();
    }
    $.extend(ImageUploadByFlash.prototype, {
        /**
         * 开始上传
         * @method start
         */
        start: function(){
            this.flash.start();
        }
        /**
         * 停止上传
         * @method stop
         */
        ,stop: function(){
            this.flash.pause();
        }
        /**
         * 删除文件
         * @method deleteFile
         * @param {String} id 需要删除文件的id
         */
        ,deleteFile: function(id){
            this.flash.deleteFile(id);
        }
        /**
         * 清空所有文件
         * @method clearList
         */
        ,clearList: function(){
            this.flash.clearList();
        }
        /**
         * 重传错误文件
         * @method reUploadError
         * @param {String} [id] 需要重传文件的id，若不传id，重传所有错误文件
         */
        ,reUploadError: function(id){
            this.flash.resetErrorStatus(id);
        },
        _reset: function(){
            this._initFlashvars();
            this._generateCallBack();
            this._initUI();
        }
        ,_initFlashvars: function(){
            var force = this._options.force || {};
            this._flashVars = [
                'useCompress=' + encodeURIComponent(this._options.compress.useCompress),
                'compressGif=' + encodeURIComponent(this._options.compress.compressGif),
                'compressPng=' + encodeURIComponent(this._options.compress.compressPng),
                'limitSize=' + encodeURIComponent(this._options.limit.size),
                'limitWidth=' + encodeURIComponent(this._options.limit.width),
                'limitHeight=' + encodeURIComponent(this._options.limit.height),
                'isAutoUp=' + encodeURIComponent(this._options.upload.auto),
                'uploadURL=' + encodeURIComponent(this._options.upload.url),
                'useWorker=false',
                'imgFormat=' + encodeURIComponent(this._options.upload.format),
                'forceWidth=' + encodeURIComponent(force.width || ''),
                'forceHeight=' + encodeURIComponent(force.height || ''),
                'get_upload_params=' + (this._options.get_upload_params ? encodeURIComponent(this._options.get_upload_params) : '')
            ].join('&');
        }
        ,_initUI: function(){
            var flashMsg = this._flashChecker();
            var version = flashMsg.version;
            var url;
            var id = 'flashUploader' + this._token;
            if(!flashMsg.hasFlash){
                this._flashError();
            }
            url = ZBJInfo.staticURI + '/t5s/common/img/uploader/uploader.swf?v=' + this._version;

            var ie = '<object id="'+ id +'" name="'+ id +'" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="https://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="'+ this._options.width +'" height="'+ this._options.height +'"><param name="allowScriptAccess" value="always" /><param value="transparent" name="wmode"><param name="flashvars" value="'+ this._flashVars +'" /><param name="allowFullScreen" value="false" /><param name="movie" value="'+ url +'" /></object>';

            var w3c = '<object id="'+ id +'" type="application/x-shockwave-flash" data="'+ url +'" width="'+ this._options.width +'" height="'+ this._options.height +'"><param name="allowScriptAccess" value="always" /><param value="transparent" name="wmode"><param name="flashvars" value="'+ this._flashVars +'" /></object>';

            if(!this._options.uploadButton){
                this._options.uploadButton = $('<div>').appendTo(document.body).css({
                    position: 'absolute'
                    ,width: '1px'
                    ,height: '1px'
                    ,overflow: 'hidden'
                    ,left: 1
                    ,bottom: 1
                });
            }

            if (navigator.appName.indexOf("Microsoft") != -1) {
                this._options.uploadButton.html(ie);
            } else {
                this._options.uploadButton.html(w3c);
            }
            this.flash =  document[id] || window[id];
        }
        ,_generateCallBack: function(){
            var self = this;
            for(var i = 0; i < this._EVENTS.length; i++){
                var evt = this._EVENTS[i]
                    ,callBack = evt + this._token;
                window[callBack] = (function(evt){
                    return function(data){
                        self.trigger(evt, data);
                    };
                })(evt);
                this._flashVars += '&' + this._decodeCamel(evt) + '=' + callBack;
            }
        }
        ,_decodeCamel: function(camel){
            return camel.replace(/([A-Z])/g, function(all, $1){return '_' + $1.toLowerCase();});
        }
        ,_flashChecker: function() {
            var hasFlash = false; //是否安装了flash
            var flashVersion; //flash版本
            var isIE =/*@cc_on!@*/0; //是否IE浏览器
            var swf;
            if (isIE) {
                swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
                if (swf) {
                    hasFlash = true;
                    flashVersion = swf.GetVariable("$version");
                }
            } else {
                if (navigator.plugins && navigator.plugins.length > 0) {
                    swf = navigator.plugins["Shockwave Flash"];
                    if (swf) {
                        hasFlash = true;
                        flashVersion = swf.description;
                    }
                }
            }
            return {
                hasFlash: hasFlash,
                version: flashVersion.match(/\d+/g)
            };
        }
        ,_flashError: function(){
            $('<center>检测到您的浏览器没有安装最新Adobe Flash Player插件，这会影响您访问本页面的部分功能。<br />请<a href="http://get.adobe.com/cn/flashplayer/" target="_blank">点此</a>安装</center>').appendTo($('body'));
        }
        ,_makeToken: function(){
            var S4 = function() {
                return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            };
            return (S4()+S4()+'_'+S4()+S4()+S4());
        }
        ,uploadBase64: function(base64, callback){
            var flashCallBack = 'flashUploadBase64_' + this._makeToken();
            window[flashCallBack] = (function(callback){
                return function(result){
                    callback(result);
                };
            })(callback);
            this.flash.uploadBase64(base64, flashCallBack);
        }
    }, $.Dts.basicEvent);
    $.Dts.ImageUploadByFlash = ImageUploadByFlash;
})();
/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-7
 * Time: 下午3:13
 */
(function(){
    /**
     * 图片上传器
     * @class ImageUploader
     * @constructor
     * @param {Object}      options 参数
     * @param {Node|jQuery} options.uploadButton 上传按钮容器 可以是node也可以是jq对象
     * @param {string}      options.uploadUrl 图片的上传地址
     * @param {int}			[options.maxSize=3*1024*1024] 上传文件压缩后的最大字节数，此参数仅代表压缩后的字节数，不限制用户选择的文件的原本字节数。当文件无法被压缩到此字节数以下时会抛出错误信息
     * @param {int}			[options.maxWidth=3000] 图片压缩后的最大宽度。所有需要压缩的图片均会被压缩到此大小内
     * @param {int}			[options.maxHeight=3000] 图片压缩后的最大高度。所有需要压缩的图片均会被压缩到此大小内
     * @param {Boolean}     [options.isAutoUp=false] 是否选择文件后立即自动上传
     * @param {int}			[options.maxParallel=undefined] 同时上传文件的最大个数，建议使用默认值
     * @param {Function}	[options.getUploadParams=null] 获取上传参数的函数句柄。每张图片上传时都会通过此方法获取上传参数。此方法参数为图片的id，返回值为上传参数的json
     * @param {Boolean}		[options.isFlash=false] 是否强制使用flash
     * @param {Boolean}		[options.useThumbs=true] 是否上传缩略图
     * @example
     //—————————— 事件列表和绑定方式 ——————————
     {
         'selected': '从文件选择框中选择文件后触发',
         'start': '开始上传触发',
         'progress': '正在上传中触发',
         'complete': '上传完成触发',
         'error': '出错时触发'
     }
     //selected/start/progress/complete事件的返回数据都为所有文件信息的数组，而error事件的返回数据仅有错误文件的信息
     //绑定selected/start/progress/complete事件例子
     uploader.bind('selected/start/progress/complete', function(evt, data){
                //evt为事件信息：
                {
                    target: uploader //事件对应的实例
                    type: "selected" //事件类型
                }
                //data为上传图片的数据：
                {
                    imageList: [ //文件序列
                        {
                            errorCode: 0 //错误码，0代表没有错误，否则代表有错
                            errorMessage: null //错误信息，如果有错，将会指定错误信息
                            id: "img_4744bbcb_f6f1954c46f9_0" //某一个上传图片的唯一标识，可以用于从上传序列中删除此文件时使用
                            isStarted: true //是否已经在上传了
                            name: "TM截图1.png" //上传图片的文件名
                            percent: 100 //上传进度
                            response: {} //服务器返回的json
                            size: 146 //上传文件的大小，单位为byte
                            source: File //如果是使用的html5技术上传，source指向上传文件的源对象
                            uploader: Uploader //内部使用
                        },
                        {
                            errorCode: 0
                            errorMessage: null
                            id: "img_2532ahbs_g6f8326c32g8_1"
                            isStarted: true
                            name: "TM截图2.png"
                            percent: 100
                            response: {}
                            size: 146
                            source: File
                            uploader: Uploader
                        }

                    ],
                    percent: 100 //总上传进度
                }
            });
     //绑定error事件例子
     uploader.bind('error', function(evt, data){
                //evt为事件信息：
                {
                    target: uploader //事件对应的实例
                    type: "error" //事件类型
                }
                //data为错误图片的数据：
                {
                    errorCode: 2 //错误码，0代表没有错误，否则代表有错
                    errorMessage: "网络错误" //错误信息，如果有错，将会指定错误信息
                    id: "img_4744bbcb_f6f1954c46f9_0" //某一个上传图片的唯一标识，可以用于从上传序列中删除此文件时使用
                    isStarted: true //是否已经在上传了
                    name: "TM截图1.png" //上传图片的文件名
                    percent: 100 //上传进度
                    response: {} //如果还没上传就已经出错，则没有response字段
                    size: 146 //上传文件的大小，单位为byte
                    source: File //如果是使用的html5技术上传，source指向上传文件的源对象
                    uploader: Uploader //内部使用
                }
            });
     * @example
     //—————————— 完整的初始化例子 ——————————
     var uploader = new BellLabs.ImageUpload({
                uploadButton: $('#image_upload_btn'),
                uploadUrl: 'upload.php'
            });
     uploader.bind('selected', function(evt, data){
                console.log('文件选择完毕');
                for(var i = 0; i < data.length; i++){
                    //渲染文件ui
                }
                uploader.start(); //开始上传
            }
     uploader.bind('progress', function(evt, data){
                console.log('文件正在上传');
                for(var i = 0; i < data.length; i++){
                    //处理进度
                }
            }
     uploader.bind('complete', function(evt, data){
                console.log('文件上传完毕');
                for(var i = 0; i < data.length; i++){
                    //设置完成状态
                }
            }
     uploader.bind('error', function(evt, data){
                console.log('文件上传出错');
                console.log('出错文件的文件名为：', data.name);
                console.log('错误信息：', data.errorMessage);
            }
     */
    function ImageUploader(options){
        var defaultOpt = {
            upload: {
                url: 'pkg/server/upload.php', //上传地址
                auto: true, //自动上传
                format: 'jpg|gif|png' //图片格式筛选，支持jpg gif bmp png
            },
            compress: {
                useCompress: true, //是否启用压缩，启用后会根据limit的配置对图片进行压缩
                compressGif: false, //所否压缩gif图片
                compressPng: false //所否压缩gif图片
            },
            limit: {
                size: 3 * 1024 * 1024, //图片最大byte
                width: 3000, //图片最大宽度
                height: 3000 //图片最大高度
            },
            thumbs: {
                useThumbs: true, //是否上传缩略图
                width: 200, //缩略图宽度
                height: 200 //缩略图高度
            },
            listener: {}
        };
        this._options = $.extend(true, defaultOpt, options || {});
        this._uploader = null;
        this.EVENTS = [
            'selected',
            'start',
            'progress',
            'complete',
            'error'
        ];
        this.createBasicEvent();
        this._reset();
    }
    $.extend(ImageUploader.prototype, {
        /**
         * 开始上传
         * @method start
         */
        start: function(){
            this._uploader.start();
        },
        /**
         * 停止上传（内部处理为暂停上传）
         * @method stop
         */
        stop: function(){
            this._uploader.stop();
        },
        /**
         * 通过imageList里的某一项的id来删除文件
         * @method deleteFile
         */
        deleteFile: function(id){
            this._uploader.deleteFile(id);
        },
        /**
         * 清空所有已选文件
         * @method clearList
         */
        clearList: function(){
            this._uploader.clearList();
        },
        /**
         * 重传错误文件
         * @method reUploadError
         * @param {String} [id] 需要重传文件的id，若不传id，重传所有错误文件
         */
        reUploadError: function(id){
            this._uploader.resetErrorStatus(id);
        },
        _reset: function(){
            //非高级浏览器
            if(!window.FormData){
                this._options.isFlash = true;
            }
            this._startImageUploader();
        },
        _startImageUploader: function(){
            if(!this._options.isFlash){
                this._uploader = new $.Dts.ImageUploadByHtml5(this._options);
            }else{
                this._uploader = new $.Dts.ImageUploadByFlash(this._options);
            }
            this._generateCallBack();
        },
        _generateCallBack: function(){
            var self = this;
            for(var i = 0; i < this.EVENTS.length; i++){
                var evt = this.EVENTS[i];
                this._uploader.bind(evt,  (function(evt){
                    return function(e , data){
                        self.trigger(evt, data || e);
                    };
                })(evt));
            }
        }
    }, $.Dts.basicEvent);
    $.Dts.ImageUploader = ImageUploader;
    $.fn.extend({
        uploader: function(opt, callback){
            opt.uploadButton = this;
            var upManager = new $.Dts.ImageUploader(opt);
            for(var e in opt.listener){
                upManager.bind(e, (function(e){
                    return function(evt, data){
                        opt.listener[e].call(upManager, evt, data);
                    }
                })(e));
            }
            return upManager;
        }
    });
})();

/**
 * Created with JetBrains PhpStorm.
 * Author: limengjun
 * Date: 14-1-8
 * Time: 下午5:10
 */
/**
 * 用于生成图片上传UI的类
 * @param $container 上传容器 程序将在这个容器下查找类名j_image_list作为图片列表的容器，类名j_image_upbtn对应上传按钮，类名j_image_remain对应还可上传的张数
 * @param options maxPhotoNum对应最多传多少张图，uploadUrl对应上传地址，coverNum对应封面数，如果为0则视为无封面
 */
;(function(){
    function uploaderUi($container, options){
        var defaultOptions = {
            ui: {
                maxPhotoNum: 4, //最多传多少张图
                coverNum: 1, //封面数，为2代表前两张图片为封面,有封面的图片会显示封面
                dragAble: true, //是否可以拖动排序
                selector: { //查找各个元素的筛选器
                    uploadList: '.j_up_list', //上传列表
                    uploadBtn: '.j_up_btn', //上传按钮
                    progressWithNumber: '.j_progress_number', //持续更新此元素的innerHTML为当前进度(0-100)
                    progressWithBar: '.j_progress_bar', //持续更新此元素的width为当前进度(0%-100%)
                    errorContainer: '.j_error', //上传出错后设置此元素的innerHTML为错误文本信息
                    cover: '.j_cover', //如果当前图片为封面，则会显示此j_cover元素，否则会隐藏此元素
                    remain: '.j_remain', //还可上传多少张图片
                    deleteBtn: '.j_delete', //删除按钮，点击此元素会删除当前图片
                    hideWhenProgress: '.j_hide_progress', //上传过程中将始终保持此元素为隐藏状态
                    hideWhenComplete: '.j_hide_complete', //上传完成后将隐藏此元素
                    hideWhenError: '.j_hide_error', //上传出错后将隐藏此元素
                    showWhenProgress: '.j_show_progress', //上传过程中将始终保持此元素为显示状态
                    showWhenComplete: '.j_show_complete', //上传完成后将显示此元素
                    showWhenError: '.j_show_error' //上传出错后将显示此元素
                },
                itemTMPL: '<div class="upload-list-item">'+
                    '<div class="item-cover j_cover"></div>'+
                    '<img class="item-img j_img_bg j_hide_error" src="/dts/static/images/uploader/i.gif">'+
                    '<span class="item-progress j_hide_complete"><b class="j_progress_bar" style="width: 1%;"></b></span>'+
                    '<div class="item-error j_error j_show_error">上传失败</div>'+
                    '<span class="item-close j_delete" title="删除此图"></span>'+
                    '</div>', //图片节点的html
                callBack: function(response){
                    //在此function里进行图片上传完成的处理
                    //this指向当前图片的外层容器
                    //response是服务器返回的json
                }
            }
        };
        this._options = $.extend(true, defaultOptions, options);
        this.$container = $container;
        this.createBasicEvent();
        this._init();
    }

    $.extend(uploaderUi.prototype, {
        /**
         * 获取还可上传图片的张数
         * @method getRemainNum
         * @returns {Number}
         */
        getRemainNum: function (){
            return this._options.ui.maxPhotoNum - this.getNumItems();
        },
        getNumItems: function () {
            return this.$listContainer.find(this.itemSelector).length;
        },
        setMaxPhotoNum: function (num) {
            this._options.ui.maxPhotoNum = num;
            this.$listContainer.find(this.itemSelector).slice(num).remove();
            this._resetRemainNum();
        },
        /**
         * 显示还可上传图片的张数
         * @method _resetRemainNum
         */
        _resetRemainNum: function (){
            this.$remainNum.html(this.getRemainNum());
        },
        /**
         * 设置图片上传成功的状态
         * @method _setSuccess
         */
        _setSuccess: function ($listPhoto, data){
            var options = this._options.ui;
            $listPhoto.find(options.selector.progressWithBar).css('width', '100%');
            $listPhoto.find(options.selector.progressWithNumber).html(100);
            $listPhoto.find(options.selector.hideWhenComplete).hide();
            $listPhoto.find(options.selector.showWhenComplete).show();
            try{
                var callBack = options.callBack;
                if(callBack){
                    callBack.call($listPhoto, data.response);
                }
            }catch(e){
                this._setError($listPhoto, {
                    errorMessage: '网络错误，请重新上传'
                });
            }
            this._setComplete($listPhoto);
        },
        /**
         * 设置已经上传完成
         * @method _setComplete
         */
        _setComplete: function($listPhoto){
            $listPhoto.data('complete', true);
        },
        /**
         * 判断是否已经上传完成
         * @method _isComplete
         */
        _isComplete: function($listPhoto){
            return !!$listPhoto.data('complete');
        },
        /**
         * 设置图片上传失败的状态
         * @method _setError
         */
        _setError: function ($listPhoto, data){
            var options = this._options.ui;
            $listPhoto.find(options.selector.errorContainer).html('上传失败：' + data.errorMessage);
            $listPhoto.find(options.selector.hideWhenError + ',' + options.selector.hideWhenComplete).hide();
            $listPhoto.find(options.selector.showWhenError + ',' + options.selector.showWhenComplete).show();
            this._setComplete($listPhoto);
        },
        /**
         * 设置封面
         * @method _resetCover
         */
        _resetCover:function (){
            var options = this._options.ui;
            this.$container.find(options.selector.cover).hide().slice(0, options.coverNum).show();
        },
        _init: function(){
            //初始化各个属性
            var ctn = this.$container;
            var options = this._options.ui;
            var selectorOptions = options.selector;
            this.$upBtn = ctn.find(selectorOptions.uploadBtn);
            this.$remainNum = ctn.find(selectorOptions.remain).html(options.maxPhotoNum);
            this.$listContainer = ctn.find(selectorOptions.uploadList);

            //检测模板容器类名等信息
            var $tempItem = $(options.itemTMPL).empty(); //通过itemTMPL确定拖动参数
            this.itemSelector = $tempItem.attr('class') ? ('.' + $tempItem.attr('class')) : $tempItem[0].nodeName;
            var itemHtml = $tempItem[0].outerHTML;
            $tempItem = null;

            var _this = this;
            this.manager = this.$upBtn.uploader($.extend({
                listener: {
                    selected: function(evt, data){
                        var imageList = data.imageList;

                        //把选中文件加入图片列表容器
                        for(var i = 0; i < imageList.length; i++){
                            var item = imageList[i];
                            if(_this.getRemainNum() < 1){ //超限
                                _this.manager.deleteFile(item.id);
                                break;
                            }
                            if(_this.getNumItems() < 1){ //第一张图片
                                $(options.itemTMPL).prependTo(_this.$listContainer).attr('id', 'j_' + item.id);
                            }else{
                                $(options.itemTMPL).insertAfter(_this.$listContainer.find(_this.itemSelector).last()).attr('id', 'j_' + item.id);
                            }
                        }

                        //初始化拖动
                        if(options.dragAble){
                            _this.$listContainer.dragsort('destroy'); //防止重复绑定拖动
                            _this.$listContainer.dragsort({
                                dragBetween: true,
                                placeHolderTemplate: itemHtml,
                                itemSelector: _this.itemSelector,
                                dragSelectorExclude: selectorOptions.deleteBtn,
                                dragEnd: function() {
                                    _this._resetCover();
                                }
                            });
                        }
                        _this._resetRemainNum();
                        _this._resetCover();
                        _this.trigger('selected', data);
                    },
                    progress: function(evt, data){
                        var imageList = data.imageList;
                        for(var i = 0; i < imageList.length; i++){
                            var item = imageList[i];
                            var $listPhoto = $('#j_' + item.id); //图片的容器
                            if(_this._isComplete($listPhoto)){ //已经上传完成
                                continue;
                            }
                            var percent = Math.max(10, item.percent);
                            $listPhoto.find(selectorOptions.progressWithBar).css('width', percent + '%');
                            $listPhoto.find(selectorOptions.progressWithNumber).html(percent);
                            $listPhoto.find(selectorOptions.hideWhenProgress).hide();
                            $listPhoto.find(selectorOptions.showWhenProgress).show();
                            if(item.percent == 100){
                                _this._setSuccess($listPhoto, item);
                            }
                        }
                        _this.trigger('progress', data);
                    },
                    complete: function(evt, data){
                        var imageList = data.imageList;
                        for(var i = 0, len = imageList.length; i < len; i++){
                            var item = imageList[i];
                            var $listPhoto = $('#j_' + item.id);
                            if(_this._isComplete($listPhoto)){ //已经上传完成
                                continue;
                            }
                            _this._setSuccess($listPhoto, item);
                        }
                        _this.manager.clearList();
                        _this.trigger('complete', data);
                    },
                    error: function(evt, data){
                        var $listPhoto = $('#j_' + data.id); //图片的容器
                        _this._setError($listPhoto, data);
                        _this.trigger('error', data);
                    }
                }
            }, this._options));
            this._bindEvent();
        },
        _bindEvent: function(){
            var _this = this;
            var options = this._options.ui;
            this.$listContainer.delegate(options.selector.deleteBtn, 'click', function(e){
                var $listPhoto = $(this).closest(_this.itemSelector);
                _this.manager.deleteFile($listPhoto.attr('id').replace('j_', ''));
                $listPhoto.remove();
                _this._resetRemainNum();
                _this._resetCover();
                _this.trigger('delete', $listPhoto);
            });
        }
    }, $.Dts.basicEvent);
    $.Dts.ImageUploaderUi = uploaderUi;
})();