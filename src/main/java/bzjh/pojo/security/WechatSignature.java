package bzjh.pojo.security;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;

@Setter
@Getter
/**
 * 签名
 */
public class WechatSignature implements Serializable {

    public static String token="bzjh";

    public WechatSignature() {
    }

    public WechatSignature(String signature, String timestamp, String nonce) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
    }

    public WechatSignature(String signature, String timestamp, String nonce, String echostr) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.echostr = echostr;
    }

    /**
     * 签名
     */
    @NonNull
    private String signature;
    /**
     * 时间戳
     */
    @NonNull
    private String timestamp;
    /**
     * 随机串
     */
    @NonNull
    private String nonce;
    /**
     * 授权码
     */
    private String echostr;

    /**
     * 检查是否合法
     * @return
     */
    public boolean nonNullCheck(){
        return signature!=null && timestamp!=null && nonce!=null;
    }
    public boolean signatureCheck(){
        return nonNullCheck() ? checkSignature(this.signature,this.timestamp,this.nonce) : false;
    }
    public static WechatSignature getAndCheckSignature(HttpServletRequest request){
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        WechatSignature wechatSignature=new WechatSignature(signature,timestamp,nonce,echostr);
        return wechatSignature.signatureCheck() ? wechatSignature : null;
    }

    /**
     * 检查签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
     static boolean checkSignature(String signature, String timestamp, String nonce){
        String [] strs={token,timestamp,nonce};
        Arrays.sort(strs);
        String code= DigestUtils.sha1Hex(strs[0]+strs[1]+strs[2]);
        return code.equals(signature);
    }
}
