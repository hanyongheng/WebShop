//存放主要交互逻辑js代码
//js模块化
var seckillss = {

    //封装ajax请求的URl
    URL: {

        now: function () {

            return '/seckill/time/now';
        },
        exposer: function (seckillId) {

            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {

            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }

    },

    //验证手机号逻辑
    validatePhone: function (phone) {

        if (phone && phone.length == 11 && !isNaN(phone)) {

            return true;
        } else {

            return false;
        }
    },
    //执行秒杀操作
    handleSeckill: function (seckillId, node) {

        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.log('handleSeckill:');
        $.post(seckillss.URL.exposer(seckillId), {}, function (result) {

            if (result && result['success']) {
                var exposer = result['data'];
                console.log('success:');
                //开启秒杀
                if (exposer['isExposed']) {
                    console.log('isExposed:'+isExposed);
                    var md5 = exposer['md5'];
                    var killUrl = seckillss.URL.execution(seckillId, md5);
                    console.log('killUrl:   ' + killUrl);
                    //防止重复点击按钮
                    $('#killBtn').one('click', function () {
                        //这里用$this了 因为防止jquery选择器再一次选择执行一次 $this相当于$('#killBtn')
                        //先禁用按钮
                        $(this).addClass('disabled');
                        //执行秒杀
                        $.post(killUrl, {}, function (result) {

                            if (result && result['success']) {

                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示执行秒杀的结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            } else {

                            }
                        });
                    });
                    node.show();
                } else {
                    //秒杀未开始 为了防止时间偏差 重新计时
                    var nowTime = exposer['now'];
                    var startTime = exposer['start'];
                    var endTime = exposer['end'];
                    console.log('秒杀未开始 为了防止时间偏差 重新计时:');
                    seckillss.countDown(seckillId, nowTime, startTime, endTime);
                }
            } else {
                console.log('result:   ' + result);
                console.log('调用接口失败:');
            }
        });
    },

    countDown: function (seckillId, nowTime, startTime, endTime) {

        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束!');
            console.log('aaa 秒杀结束:  ');
        } else if (nowTime < startTime) {
            //秒杀未开始 开始计时
            console.log('aaa 秒杀未开始:  ');
            var killTime = new Date(startTime + 1000);
            seckillBox.countDown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                seckillss.handleSeckill(seckillId, seckillBox);
            });
        } else {
            console.log('aaa 秒杀开始:  ');
            seckillss.handleSeckill(seckillId, seckillBox);
        }
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        //验证手机号
        //规划我的交互

        init: function (params) {

            var killPhone = $.cookie('killPhone');

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
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        //put的时候会有一个效果 为了不让用户看到put的效果所以先隐藏然后在显示
                        $('#killPhoneMessage').hide().html('<lable class="label label-danger">手机号码错误!</lable>').show('300');
                    }

                });
            }
            //已经验证通过
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckillss.URL.now(), {}, function (result) {

                if (result && result['success']) {

                    var timeNow = result['data'];
                    console.log('timeNow:   '+timeNow);
                    seckillss.countDown(seckillId, timeNow, startTime, endTime);
                } else {
                    console.log('result:  ' + result);

                }
            });
        }

    }

}

