package bzjh;

import bzjh.datahandle.wechat.Command;
import bzjh.datahandle.wechat.WechatMessageCommand;
import bzjh.datahandle.wechat.WechatMessageHandle;
import bzjh.pojo.message.WechatTextMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//@SpringBootTest
class BzjhApplicationTests {


    @Test
    void contextLoads() throws NoSuchMethodException {
        System.out.println("::创建".indexOf("创建")==2);
    }
}
