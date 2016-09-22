//存放主要交互逻辑js代码
//js模块化
var seckillss = {

    //封装ajax请求的URl
    URL: {},

    //验证手机号逻辑
    validatePhone: function (phone) {

        if (phone && phone.length == 11 && !isNaN(phone)) {

            return true;
        } else {

            return false;
        }
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        //验证手机号
        //规划我的交互

        init: function (params) {
            alert("asaa");
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = ['endTime'];
            var seckillId = ['seckillId'];
            if (!seckillss.validatePhone(killPhone)) {

                var killPhoneModel = $('#killPhoneModel');//注意此killPhoneModel已经不是一个简单的DIV是bootstrap的一个组件
                //显示弹出层 有个方法
                killPhoneModel.modal({

                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false //关闭键盘事件
                })
                $('#killPhoneBtn').click(function () {

                    var inputPhone = $('#killPhoneKey').val();
                    if (seckillss.validatePhone(inputPhone)) {
                        //写入电话
                        $.cookie('killPhone',inputPhone,{expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        //put的时候会有一个效果 为了不让用户看到put的效果所以先隐藏然后在显示
                        $('#killPhoneMessage').hide().html('<lable class="label label-danger">手机号码错误!</lable>').show('300');
                    }

                });
            }
            //已经验证通过
        }

    }

}

