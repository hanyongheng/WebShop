<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>详情页</title>
    <%@include file="common/header.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>

    </div>
    <div class="panel-body">
        <h2 class="text-danger">

            <!-- 显示time图标-->
            <span class="glyphicon glyphicon-time"></span>
            <!-- 显示倒计时-->
            <span class="glyphicon" id="seckill-box"></span>
        </h2>

    </div>
</div>

<!-- 登陆弹出层 出入电话-->
<div id="killPhoneModel" class="modal fade">

    <div class="modal-dialog">

        <div class="modal-content">

            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="请填手机号~~"
                               class="form-control">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <!-- 验证信息-->
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    submit
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<!-- 使用CDN获取公共JS-->
<!-- 使用JQUERY cookie插件操作-->
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<!-- 开始编写交互逻辑-->
<script src="/resource/script/seckillDetails.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        console.log(typeof ${seckill.seckillId});

        //使用EL表达式传入参数
        seckillss.detail.init({

            seckillId:${seckill.seckillId},
            startTime:${seckill.startTime.time},//毫秒
            endTime:${seckill.endTime.time}
        });
    });

</script>
</html>
