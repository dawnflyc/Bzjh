package bzjh.scheduler;

import bzjh.scheduler.event.Event;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 小时
 */
@Component
public class HourScheduler {

    /**
     * 事件队列
     */
    private static Map<String, Event> map=new HashMap<>();

    @Scheduled(cron = "* */1 * * * ?")
    public void taskRun(){
        for (Event value : map.values()) {
            value.taskRun();
        }
    }
    public static void subscribe(String name,Event event){
        if (map!=null){
            map.put(name,event);
        }
    }
    public static boolean unSubscribe(String name){
        return map.remove(name)!=null ? true : false;
    }
    public static boolean isSubscribe(String name){
        return map.containsKey(name);
    }

}
