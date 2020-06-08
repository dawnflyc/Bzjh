package bzjh.pojo.message;

import bzjh.pojo.message.WechatMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/***
 * 微信文本信息包
 */
@Getter
@Setter
@XmlRootElement
@XmlType(name = "text")
public class WechatTextMessage extends WechatMessage {

    public WechatTextMessage( String toUserName,  String fromUserName, long msgId,  String content) {
        super(toUserName, fromUserName, msgId);
        this.setMsgType("text");
        this.content = content;
    }

    public WechatTextMessage( WechatMessage message, String content) {
        super(message.getToUserName(),message.getFromUserName(),message.getMsgId());
        this.setMsgType("text");
        this.content = content;
    }

    public WechatTextMessage() {
    }

    /**
     * 内容
     */
    @XmlElement(name = "Content")
    private String content;

}
