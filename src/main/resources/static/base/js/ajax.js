/**
 * 结合layui的ajax调用
 */
var ajax = {
    baseUrl: "",
    defaultSetting: {
        async: true,
        dataType: "json",
        mask: true, // 默认开启遮罩层
        beforeSend() {
            layui.use(['layer'], function () {
                layui.layer.load(2);
            });
        },
        complete() {
            layui.use(['layer'], function () {
                layui.layer.closeAll('loading');
            });
        },
        error(res) {
            layui.use(['layer'], function () {
                var errorMsg = res.responseJSON.msg;
                var callback;
                if (errorMsg === "未登录") {
                    callback = function () {
                        top.location.href = "/login/login.html";
                    }
                }
                layui.layer.alert(errorMsg, {icon: 2/*, shade: [0.0, '#FFF']*/}, callback);
            });
        }
    },
    get(setting) {
        setting.type = "get";
        this.sendRequest(setting);
    },
    post(setting) {
        setting.type = "post";
        this.sendRequest(setting);
    },
    upload(url, afterSuccess) {
        layui.use(['jquery'], function () {
            var node = "#upLoadForm", $ = layui.$;
            if ($(node).length === 0) {
                $("body").append("<form id='" + node.substring(1) + "' hidden>" +
                    "<input id='upLoadFile' type='file' name='file'></form>");
                $("#upLoadFile").change(function () {
                    // 用户取消文件上传或者每次上传完成清空
                    if ($(this).val() === "") return;
                    ajax.post({
                        url: url,
                        data: new FormData($(node)[0]),
                        contentType: false,
                        processData: false,
                        success: afterSuccess
                    });
                    // 每次改变之后需要清空，不然多次选择同一个文件不会触发change
                    $(this).val("");
                });
            }
            $("#upLoadFile").click();
        });
    },
    sendRequest(setting) {
        layui.use(['jquery'], function () {
            layui.$.ajax(ajax.parseSetting(setting));
        });
    },
    /**
     * 解析配置，自定义配置将会覆盖默认配置
     * @param setting
     * @returns {*}
     */
    parseSetting(setting) {
        layui.use(['jquery'], function () {
            layui.$.each(ajax.defaultSetting, function (k, v) {
                // 不能用||，值为false时不起作用
                if (setting[k] === undefined) {
                    setting[k] = v;
                }
            });
        });
        // url前缀
        setting.url = this.baseUrl + setting.url;
        // 若不开启遮罩层则函数置空
        if (!setting.mask) {
            setting.beforeSend = setting.complete = null;
        }
        return setting;
    }
};

/**
 * 从地址栏获取参数值
 * @param name
 * @returns {*}
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); // 匹配目标参数
    if (r != null)
        return unescape(r[2]);
    return null; // 返回参数值
}