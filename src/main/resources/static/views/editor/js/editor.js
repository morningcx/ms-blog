layui.use(["jquery", "layer", "form"], function () {
    var $ = layui.$, layer = layui.layer, form = layui.form;
    var articleId = getUrlParam("id");
    // 参数传递错误则不执行
    if (!articleId) {
        layer.ready(function () {
            layer.alert("参数传递错误！", {icon: 2});
        });
        return;
    }

    // 参数正常则请求文章内容
    ajax.get({
        url: "/content/getByArticleId?articleId=" + articleId,
        mask: false,
        success: function (res) {
            $(document).attr('title', res.data.title);
            parseMarkdown(res.data.content.content);
        }
    });

    function parseMarkdown(md) {
        var editor = editormd("editor", {
            height: "100vh",
            markdown: md,
            path: "../../library/editor/lib/",
            emoji: true,
            searchReplace: false,
            tocm: true,
            /*theme : "dark",
            previewTheme : "dark",
            editorTheme : "pastel-on-dark",*/
            taskList: true, // 任务列表，未完成已完成
            tex: true,  // 科学公式，默认不解析
            flowChart: true,  // 流程图，默认不解析
            sequenceDiagram: true,  // 序列图，默认不解析
            /*codeFold: true, // 每一个标题或者代码行折叠*/
            imageUpload: true, // 禁止本地上传图片
            imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : "/image/mdImageUpload",
            syncScrolling: "single", // 单向绑定
            toolbarIcons: function () {
                // Or return editormd.toolbarModes[name]; // full, simple, mini
                // Using "||" set icons align right.
                return [
                    "undo", "redo", "|",
                    "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                    "h1", "h2", "h3", "h4", "h5", "h6", "|",
                    "list-ul", "list-ol", "hr", "|",
                    "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "emoji", "html-entities", "pagebreak", "|",
                    /*"goto-line",*/ "watch", "preview", "see"/*, "fullscreen", "clear"*//*, "search"*/, "|",
                    /*"help", "info",*/ "save", "history", "meta", "setting", "|",
                    "publish"
                ];
            },
            onload: function () {
                // this.fullscreen();
                //this.unwatch();
                //this.watch().fullscreen();

                //this.setMarkdown("#PHP");
                //this.width("100%");
                //this.height(480);
                //this.resize("100%", 640);
            },
            toolbarIconsClass: {
                see: "fa-file-text-o",
                save: "fa-save",
                history: "fa-history",
                meta: "fa-book",
                setting: "fa-gear",
                publish: "fa-mail-forward"
            },
            toolbarHandlers: {
                see: see,
                save: saveArticle,
                history() {
                    alert("历史")
                },
                publish: publish,
                setting: setting,
                meta: saveMeta
            },
            lang: {
                toolbar: {
                    see: "查看",
                    save: "保存",
                    history: "历史",
                    meta: "信息",
                    publish: "发布",
                    setting: "设置"
                }
            }
            // imageUploadURL: "../mdImageUpload",
            // dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为 true
            // dialogShowMask : true,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为 true
            // dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为 true
            // dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为 0.1
            // dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为 #fff
            /*toolbarIcons: function () {
                return editormd.toolbarModes.simple; // 自定义工具栏
            }*/
            // 设置为true会添加一个textarea，里面包含md转化成的html，默认为false
            /*saveHTMLToTextarea: false */
            /*theme : "dark",
            previewTheme : "dark",
            editorTheme : "pastel-on-dark"*/
        });

        // 预览文章
        function see() {
            window.open("preview.html?id=" + articleId);
        }

        // 保存文章
        function saveArticle(callback) {
            ajax.post({
                url: "/content/updateByArticleId",
                data: {
                    articleId: articleId,
                    content: editor.getMarkdown()
                },
                success: function () {
                    // 回调函数，用于发布
                    (typeof callback === "function") ? callback() : layer.msg("保存成功！");
                }
            });
        }

        // 发布文章
        function publish() {
            // 先执行保存，再发布
            saveArticle(function () {
                ajax.post({
                    url: "/article/updateModifier",
                    data: {id: articleId, modifier: 0},
                    success: function () {
                        layer.msg("发布成功！");
                    }
                });
            });
        }

        // 获取文章元信息
        function saveMeta() {
            layer.ready(function () {
                layer.open({
                    type: 2,
                    title: "文章信息",
                    closeBtn: 1,
                    area: ["420px", "550px"],
                    anim: 0,
                    shadeClose: false, // 禁止关闭
                    content: '../article/meta.html?id=' + articleId
                });
            });
        }

        // editor设置
        function setting() {
            var content =

                "            <select id=\"editormd-theme-select\">\n" +
                "            </select>\n" +

                "            <select id=\"editor-area-theme-select\">\n" +
                "            </select>\n" +

                "            <select id=\"preview-area-theme-select\">\n" +
                "            </select>\n" ;

            layer.open({
                title: '页面层'
                , type: 1
                , skin: 'layui-layer-rim'
                , shadeClose: true
                , area: ['50%', '300px']
                , content: content
            });

            themeSelect("editormd-theme-select", editormd.themes, "theme", function ($this, theme) {
                editor.setTheme(theme);
            });

            themeSelect("editor-area-theme-select", editormd.editorThemes, "editorTheme", function ($this, theme) {
                editor.setCodeMirrorTheme(theme);
            });

            themeSelect("preview-area-theme-select", editormd.previewThemes, "previewTheme", function ($this, theme) {
                editor.setPreviewTheme(theme);
            });

            form.render("select");

            /**
             * 主题切换绑定
             */
            function themeSelect(id, themes, lsKey, callback) {
                var select = $("#" + id);

                for (var i = 0, len = themes.length; i < len; i++) {
                    var theme = themes[i];
                    var selected = (localStorage[lsKey] == theme) ? " selected=\"selected\"" : "";

                    select.append("<option value=\"" + theme + "\"" + selected + ">" + theme + "</option>");
                }

                select.bind("change", function () {
                    var theme = $(this).val();

                    if (theme === "") {
                        alert("theme == \"\"");
                        return false;
                    }

                    console.log("lsKey =>", lsKey, theme);

                    localStorage[lsKey] = theme;
                    callback(select, theme);
                });

                return select;
            }
        }
    }
});