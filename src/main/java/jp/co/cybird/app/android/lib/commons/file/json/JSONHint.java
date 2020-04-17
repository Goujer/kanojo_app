package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONHint {
    String anonym() default "";

    String format() default "";

    boolean ignore() default false;

    String name() default "";

    int ordinal() default -1;

    boolean serialized() default false;

    Class<?> type() default Object.class;
}
