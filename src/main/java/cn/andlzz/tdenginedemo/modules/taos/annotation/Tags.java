package cn.andlzz.tdenginedemo.modules.taos.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 反射获取tags注解，所有tags属性添加
 *
 * @author : 杜亮志
 * @date : 2023年08月 14:39
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tags {

    /**
     * 标注tags的顺序
     *
     * @return 该tags的顺序
     */
    int value() default Integer.MAX_VALUE;
}
