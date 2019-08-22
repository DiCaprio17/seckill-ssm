package org.seckill.controller;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分  /seckill/list
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 进入秒杀列表.
     *
     * @param model 模型数据,里面放置有秒杀商品的信息
     * @return 秒杀列表详情页面
     */
    @RequestMapping(value = {"/list", "", "index"}, method = RequestMethod.GET)
    public String list(Model model) {
        //list.jsp是页面模板 + model是数据 = ModelAndView
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list", seckillList);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 暴露秒杀接口的方法.
     *
     * @param seckillId 秒杀商品的id
     * @return 根据用户秒杀的商品id进行业务逻辑判断, 返回不同的json实体结果
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody//返回json
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        // 查询秒杀商品的结果
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 用户执行秒杀,在页面点击相应的秒杀连接,进入后获取对应的参数进行判断,返回相对应的json实体结果,前端再进行处理.
     *
     * @param seckillId 秒杀的商品,对应的时秒杀的id
     * @param md5       一个被混淆的md5加密值
     * @param userPhone 参与秒杀用户的额手机号码,当做账号密码使用
     * @return 参与秒杀的结果, 为json数据
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone) {
        //required = false使得userPhone不是必须的，缺少时不会报错
        // 如果用户的手机号码为空的说明没有填写手机号码进行秒杀
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "没有注册");
        }
        // 根据用户的手机号码,秒杀商品的id跟md5进行秒杀商品,没异常就是秒杀成功
        try {
            // 这里换成储存过程executeSeckill -> executeSeckillProducedure
            SeckillExecution execution = seckillService.executeSeckillProducedure(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e1) {
            // 重复秒杀
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e2) {
            // 秒杀关闭
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillException e) {
            // 不能判断的异常
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
        // 如果有异常就是秒杀失败
    }

    /**
     * 获取服务器端时间,防止用户篡改客户端时间提前参与秒杀
     *
     * @return 时间的json数据
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<LocalDateTime> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }
}