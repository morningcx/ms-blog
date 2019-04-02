layui.use(["jquery", "layer"], function () {
    var $ = layui.$, layer = layui.layer;
    var articleId = getUrlParam("id");
    // 参数传递错误则不执行
    if (!articleId) {
        layer.ready(function () {
            layer.alert("参数传递错误！", {icon: 2});
        });
        return;
    }
    ajax.get({
        url: "/content/getByArticleId?articleId=" + articleId,
        success: function (res) {
            $(document).attr('title', res.data.title);
            generateViewer(res.data.content.content);
        }
    });

    function generateViewer(md) {
        var previewContainer = editormd.markdownToHTML("previewContainer", {
            /*markdown: res.content,*/
            // 解析html元素，更加灵活但是不安全，默认是关闭的
            /*htmlDecode      : "style,script,iframe",*/
            // tocDropdown: false, // 下拉toc显示
            markdown: md,
            /*tocTitle: "目录 Table of Contents",*/
            tocContainer: "#tocContainer", // toc容器
            tocm: true,
            emoji: true, // 表情图标
            tocm: true, // tocm
            taskList: true, // 任务列表，未完成已完成
            tex: true,  // 科学公式，默认不解析
            flowChart: true,  // 流程图，默认不解析
            sequenceDiagram: true,  // 序列图，默认不解析
            /*markdownSourceCode: false*/ // textarea中是否保留md源码，默认就是false
        });
        tocBind();
    }

    /**
     * 目录绑定
     */
    function tocBind() {
        /*var each = $("#htmlViewer .reference-link");
        $(window).scroll(function () {
            var start = new Date().getTime();
            var currScroll = $(this).scrollTop();
            var section;
            each.each(function () {
                var divPosition = $(this).offset().top;
                if (divPosition - 1 < currScroll) {
                    section = $(this);
                }
            });
            if (section) {
                $("#currPoint").removeAttr("id");
                $("#tocContainer a[href='#" + section.prop("name") + "']").attr("id", "currPoint");
            }
            console.log(new Date().getTime() - start);
        });*/

        // 每一个锚点距离顶部的高度

        var eachLink = $("#previewContainer .reference-link");

        if (eachLink.length === 0) {
            return;
        }

        // 每一个链接元素
        var eachElement = $("#tocContainer a");

        // 第一个a元素
        var firstElement = eachElement.eq(0);
        // toc容器
        var container = $("#tocContainer");

        // 页面滚动
        $(window).scroll(function () {
            var currTop = $(this).scrollTop();
            var currPoint = $("#currPoint");
            // 目录索引，加5为了避免偏移误差
            var index = tocPart(currTop + 5, eachLink);
            if (index !== -1) {
                var targetPoint = eachElement.eq(index);
                if (!currPoint.is(targetPoint)) {
                    currPoint.removeAttr("id");
                    targetPoint.attr("id", "currPoint");
                    // 中部显示绑定
                    var scroll = targetPoint.offset().top - firstElement.offset().top -
                        (container.height() - firstElement.height()) / 2;
                    /*container.animate({scrollTop: scroll}, 100);*/
                    container.scrollTop(scroll);
                    /*alert("" + (articleId ^ 2147483647));*/
                    /*var en = window.btoa(articleId ^ 2147483647) ;
                    alert(en + "     " + (window.atob(en) ^ 2147483647));*/
                }
            } else {
                currPoint.removeAttr("id");
            }
        });

        /**
         * 二分查找数组中最后一个小于等于num的索引(toc当前目录索引)
         * @param num
         * @param array
         * @returns {number}
         */
        function tocPart(num, array) {
            var start = 0, end = array.length - 1;
            while (start < end) {
                var mid = Math.floor((end - start) / 2) + start + 1;
                if (array.eq(mid).offset().top > num) {
                    end = mid - 1;
                } else {
                    start = mid;
                }
            }
            return array.eq(start).offset().top > num ? -1 : start;
        }
    }
});