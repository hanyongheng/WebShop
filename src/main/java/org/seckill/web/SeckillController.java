package org.seckill.web;

import b.b.U;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyh on 16/9/1.
 * 秒杀的controller
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    /**
     * 查询秒杀商品列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
            //list.jsp+model=modelAndView
        List<Seckill> seckillList=seckillService.getSeckillList();
        model.addAttribute("list",seckillList);
        return "list";//WEB_INF/jsp/"list".jsp
    }

    /**
     * 秒杀商品详情页
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){

        if (seckillId==null){

            return "redirect:/seckill/list";
        }

        Seckill seckill=seckillService.getSeckillById(seckillId);
        if (seckill==null){

            return "forward:/seckill/list";
        }

        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     *  暴露秒杀的接口地址
     * @param seckillId
     * @return
     */
    //ajax json
    @RequestMapping(value = "{/{seckillId}/exposer",method = RequestMethod.POST,
            //为了规范告诉浏览器或者移动端返回的数据格式是json和具体的字符集
            produces = {"application/json;charset=UTF-8"})
    //告诉spring返回json数据的注解
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId){

        SeckillResult<Exposer> result;

        try {
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀的操作
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,
                produces = {"application/json;charset=UTF-8"}
    )
    @ResponseBody
    public SeckillResult<SeckillExecution>execute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone",required = false) Long phone){

        SeckillResult<SeckillExecution> result;
        if (phone==null){

            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        try {

            SeckillExecution execution=seckillService.executeSeckill(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (RepeatKillException e1){

            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);

        }catch (SeckillCloseException e2){

            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }

    }

    /**
     * 获取当前系统时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    public SeckillResult<Long> time(){

        Date date=new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }
}
