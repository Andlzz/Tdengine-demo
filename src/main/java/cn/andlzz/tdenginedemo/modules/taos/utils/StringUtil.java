package cn.andlzz.tdenginedemo.modules.taos.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Andlz
 */
@Slf4j
public class StringUtil {
    /**
     * 为参数添加双引号
     * @param args 参数
     * @return 添加双引号后的参数
     */
    public static List<String> change2String(Object... args) {
        LinkedList<String> res = new LinkedList<>();
        for (Object arg : args) {
            String temp;
            if (arg == null) {
                temp = "'null'";
            }else if(StringUtils.isBlank(arg.toString())){
                temp = "''";
            }
            else {
                temp = "'" + arg + "'";
            }
            res.add(temp);
        }
        return res;
    }
}
