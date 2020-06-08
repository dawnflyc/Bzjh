package bzjh.util;

import bzjh.pojo.message.WechatMessage;
import bzjh.pojo.security.WechatSignature;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信网络类
 */
public class WechatWebUtil {

    /**
     * 获取微信签名
     * @param request
     * @return
     */
    public static WechatSignature getWechatSignature(HttpServletRequest request){
        return WechatSignature.getAndCheckSignature(request);
    }

    /**
     * 获取微信信息包
     * @param request
     * @return
     */
    public static <T extends WechatMessage> T getWechatMessage(HttpServletRequest request,Class<T> clazz){
        T wechatMessage= null;
        try {
            wechatMessage = CodeUtil.xmlToMessage(clazz,CodeUtil.ReadInputStreamToString(request.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wechatMessage;
    }

}
