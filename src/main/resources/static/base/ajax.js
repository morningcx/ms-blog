/**
 * 结合layui的ajax调用
 */
var ajax = {
    defaultSetting: {
        async: true,
        dataType: "json",
        mask: true, // 默认开启遮罩层
        beforeSend: function () {
            layui.use(['layer'], function () {
                layui.layer.load(2);
            });
        },
        complete: function () {
            layui.use(['layer'], function () {
                layui.layer.closeAll('loading');
            });
        },
        error(res) {
            ajax.errorHandler(res);
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
                setting[k] = setting[k] || v;
            });
        });
        // 若不开启遮罩层则函数置空
        if (!setting.mask) {
            setting.beforeSend = setting.complete = null;
        }
        return setting;
    },
    /**
     * 错误处理，弹出layer错误信息
     * @param res
     */
    errorHandler(res) {
        layui.use(['layer'], function () {
            layui.layer.alert(res.responseJSON.msg, {icon: 2});
        });
    }
};