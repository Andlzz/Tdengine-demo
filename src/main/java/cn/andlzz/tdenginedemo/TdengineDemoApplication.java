package cn.andlzz.tdenginedemo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@MapperScan({"cn.andlzz.tdenginedemo.**.mapper*"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TdengineDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdengineDemoApplication.class, args);
    }

}
