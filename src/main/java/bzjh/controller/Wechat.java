package bzjh.controller;

import bzjh.datahandle.wechat.WechatMessageCommand;
import bzjh.datahandle.wechat.WechatMessageHandle;
import bzjh.pojo.message.WechatMessage;
import bzjh.pojo.security.WechatSignature;
import bzjh.pojo.message.WechatTextMessage;
import bzjh.util.CodeUtil;
import bzjh.util.WechatWebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Wechat {

    @Autowired
      WechatMessageHandle messageHandle;

    /**
     * main
     * @return
     */
    @RequestMapping(value = "/wechat",method = RequestMethod.POST)
    public String wechatPost(HttpServletRequest request){
        WechatSignature wechatSignature= WechatWebUtil.getWechatSignature(request);
        WechatTextMessage wechatMessage= WechatWebUtil.getWechatMessage(request, WechatTextMessage.class);
        if (wechatSignature==null) return "";
        if (wechatSignature.getEchostr()!=null) return wechatSignature.getEchostr();
        if (wechatMessage!=null){
            return CodeUtil.messagetoXml(messageHandle.action(wechatMessage));
        }
        return "success";
       }
    @RequestMapping(value = "/wechat",method = RequestMethod.GET)
    public String wechatGet(HttpServletRequest request) {
        System.out.println("get");

        return "此网站已接入微信公众号！";
    }
    @RequestMapping(value = "/wechat_test",method = RequestMethod.POST)
    public String wechatTestPost(HttpServletRequest request){
        WechatMessage wechatMessage= WechatWebUtil.getWechatMessage(request,WechatTextMessage.class);
        return "success";
    }
    @RequestMapping(value = "/wechat_test",method = RequestMethod.GET)
    public String wechatTestGet(HttpServletRequest request){

    return "微信公众号测试！";
    }
}
