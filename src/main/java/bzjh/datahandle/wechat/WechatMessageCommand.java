package bzjh.datahandle.wechat;

import bzjh.pojo.message.WechatMessage;
import bzjh.pojo.message.WechatTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WechatMessageCommand {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public WechatMessageCommand() {

    }

    public WechatMessage action(WechatTextMessage message){
        Pattern p = Pattern.compile("\\(.*?\\)");
        Matcher m = p.matcher(message.getContent());
        while (m.find()) {
            String param = m.group().replaceAll("\\(|\\)", "");
            String result="";
            WechatMessage wechatMessage=comandHandle(new WechatTextMessage(message,param));
            if (wechatMessage instanceof WechatTextMessage){
                result=((WechatTextMessage) wechatMessage).getContent();
                message.getContent().replace("("+param+")",result);
            }

        }
        return comandHandle(message);
    }

    private WechatMessage comandHandle(WechatTextMessage message){
        Class clazz=getClass();
        WechatMessage result=null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)){
                Command command=method.getAnnotation(Command.class);
                if (message.getContent().indexOf(command.value())==2){
                    if (method.getParameterTypes()[0]==WechatTextMessage.class && WechatMessage.class.isAssignableFrom(method.getReturnType())){
                        try {
                            result= (WechatMessage) method.invoke(this,message);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.getCause().printStackTrace();
                        }
                    }
                }
            }
        }
        return result;
    }

    @Command("创建")
    WechatMessage createUser(WechatTextMessage message){
        String [] params=paramSplit(message.getContent());
        WechatTextMessage wechatTextMessage= (WechatTextMessage) new WechatTextMessage(message,null).reverse();
        if (params.length==1){
           stringRedisTemplate.opsForValue().set(params[0],message.getFromUserName());
           wechatTextMessage.setContent("创建成功！");
        }else {
            wechatTextMessage.setContent("创建失败！");
        }
        return wechatTextMessage;
    }

    private Random random=new Random();

    /**
     * 随机数
     * @param message
     * @return
     */
    @Command("随机")
    WechatMessage random(WechatTextMessage message){
        String [] params=paramSplit(message.getContent());
        WechatTextMessage wechatTextMessage= (WechatTextMessage) new WechatTextMessage(message,null).reverse();
        String content="格式不合法！";
        switch (params.length){
            case 0:
                content=random.nextInt()+"";
                break;
            case 1:
                if (StringUtils.isNumeric(params[0])){
                    content=random.nextInt(Integer.parseInt(params[0]))+"";
                }else if ("?".equals(params[0])){
                    content="获得随机数。\n" +
                            "\"::随机\" 会得到随机的数值。\n" +
                            "\"::随机 *\" 会得到 0-*之间的数值，不包括*。\n" +
                            "\"::随机 *1 *2\"会得到 *1-*2之间的数值，不包括*2。\n"+
                            "\"*代表数字。";
                }
                break;
            case 2:
                if (StringUtils.isNumeric(params[1]) && StringUtils.isNumeric(params[1]) && Integer.parseInt(params[1])>Integer.parseInt(params[0])){
                    content=random.nextInt(Integer.parseInt(params[1])-Integer.parseInt(params[0]))+Integer.parseInt(params[0])+"";
                }
                break;
        }
        wechatTextMessage.setContent(content);
        return wechatTextMessage;
    }

    private String [] paramSplit(String text){
        String [] params=text.split(" ");
        String[] result = new String[params.length-1];
        System.arraycopy(params, 1, result, 0, result.length);
        return result;
    }

}
