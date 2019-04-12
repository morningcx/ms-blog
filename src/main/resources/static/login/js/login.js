layui.use(["layer"], function () {
    var layer = layui.layer;
    var code = {
        seed: ['abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '0123456789'],
        container: $("#code"),
        value: null
    };

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            $("#btn").click();
        }
    });

    particleground(document.getElementById('particles'), {
        dotColor: '#65b4e6',
        lineColor: '#65b4e6'
    });
    createCode();
    code.container.click(createCode);

    $("#btn").click(function () {
        var account = $("#username");
        var password = $("#password");
        var codeInput = $("#codeInput");
        if (!account.val()) {
            account.focus();
            layer.msg("账号不能为空");
            return;
        }
        if (!password.val()) {
            password.focus();
            layer.msg("密码不能为空");
            return;
        }
        if (!codeInput.val() || code.value.toLowerCase() !== codeInput.val().toLowerCase()) {
            codeInput.focus();
            layer.msg("验证码错误");
            createCode();
            return;
        }
        $.ajax({
            url: "/login",
            type: "POST",
            data: {
                account: account.val(),
                password: hex_sha1(password.val())
            },
            dataType: "json",
            success: function () {
                window.location.href = "../views/index.html";
            },
            error: function (data) {
                createCode();
                layer.msg(data.responseJSON.msg);
            }
        });
    });

    function createCode() {
        var len = 5;
        var idx, i, result = '';
        for (i = 0; i < len; i++) {
            idx = Math.floor(Math.random() * 3);
            result += code.seed[idx].substr(Math.floor(Math.random() * (code.seed[idx].length)), 1);
        }
        code.value = result;
        code.container.text(result);
        return result;
    }
});