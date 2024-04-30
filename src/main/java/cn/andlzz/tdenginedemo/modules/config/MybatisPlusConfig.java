package cn.andlzz.tdenginedemo.modules.config;

import cn.andlzz.tdenginedemo.modules.interceptor.TaosDynamicTableNameInnerInterceptor;
import cn.andlzz.tdenginedemo.modules.interceptor.TaosTableNameHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 杜亮志
 * @date : 2024年04月 下午3:53
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 动态表名插件
        TaosDynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new TaosDynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler(new TaosTableNameHandler());
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);

        return interceptor;
    }

}
