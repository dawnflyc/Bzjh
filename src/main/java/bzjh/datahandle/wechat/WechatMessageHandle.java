package bzjh.datahandle.wechat;

import bzjh.datahandle.wechat.WechatMessageCommand;
import bzjh.pojo.message.WechatMessage;
import bzjh.pojo.message.WechatTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WechatMessageHandle {

    @Autowired
     WechatMessageCommand messageCommand;

    public WechatMessage action(WechatTextMessage message){
        WechatMessage wechatMessage=null;
        if (message.getContent().length()>2 && "::".equals(message.getContent().substring(0,2))){
            wechatMessage=messageCommand.action(message);
        }
        return wechatMessage;
    }

}
