package cn.andlzz.tdenginedemo.modules.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

/**
 * @author : 杜亮志
 * @date : 2024年04月 下午3:55
 */
public class TaosTableNameHandler implements TableNameHandler {
    //ThreadLocal存储不同线程的动态表名
    private static final ThreadLocal<String> TABLE_NAME = new ThreadLocal<>();
    //对外提供设置表名的方法
    public static void setTableName(String name) {
        TABLE_NAME.set(name);
    }
    //删除当前请求线程的day数据
    public static void removeTableName() {
        TABLE_NAME.remove();
    }
    //动态表名接口实现方法
    @Override
    public String dynamicTableName(String sql, String tableName) {
        // 检查表名是否包含"."
        if (tableName.contains(".")) {
            String table = TABLE_NAME.get();
            if (table != null) {
                // 确保移除操作成功，避免残留数据
                TABLE_NAME.remove();
                return table;
            }else {
                return tableName;
            }
        }
        // 如果表名不包含"."，则不进行替换，直接返回原表名
        return tableName;
    }
}
