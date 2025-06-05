package org.IC.mcpServer.HTTP.Tools;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Tool {
    String value() default "";
}
