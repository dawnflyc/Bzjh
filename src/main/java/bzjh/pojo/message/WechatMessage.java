package bzjh.pojo.message;

import bzjh.controller.Wechat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 微信消息包
 */
@Getter
@Setter
public abstract class WechatMessage implements Serializable {
    public WechatMessage( String toUserName, String fromUserName, Long msgId) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = System.currentTimeMillis();
        this.msgId = msgId;
    }

    public WechatMessage() {
    }

    /**
     * 收信息包用户Id
     */
    @XmlElement(name ="ToUserName")
    private String toUserName;
    /**
     * 发信息包用户Id
     */
    @XmlElement(name ="FromUserName")
    private String fromUserName;
    /**
     *信息包创建时间
     */
    @XmlElement(name ="CreateTime")
    private Long createTime;
    /**
     * 信息包类型
     */
    @XmlElement(name ="MsgType")
    private String msgType;
    /**
     * 信息包Id
     */
    @XmlElement(name ="MsgId")
    private Long msgId;

    public WechatMessage reverse(){
        String temp=this.fromUserName;
        this.fromUserName=this.toUserName;
        this.toUserName=temp;
        return this;
    }
}
