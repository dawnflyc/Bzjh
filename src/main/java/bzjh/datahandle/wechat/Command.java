package bzjh.datahandle.wechat;

import lombok.NonNull;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface Command {
     String value();
}
