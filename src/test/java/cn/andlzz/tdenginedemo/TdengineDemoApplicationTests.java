package cn.andlzz.tdenginedemo;

import cn.andlzz.tdenginedemo.modules.equipmentInfo.entity.EquipmentInfo;
import cn.andlzz.tdenginedemo.modules.equipmentInfo.service.IEquipmentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@SpringBootTest
class TdengineDemoApplicationTests {

    @Resource
    private IEquipmentInfoService equipmentInfoService;

    @Test
    void initDatabase() {
        // 创建数据库和超级表
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfoService.createSTable(equipmentInfo);
    }

    @Test
    void insert() {
        // 输入模拟数据
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setIntValue(123);
        equipmentInfo.setStrValue("采集成功");
        equipmentInfo.setEqpId("1977607896");
        // 设置子表名
        equipmentInfo.setTableName("eqp_1977607896");
        equipmentInfoService.insert(equipmentInfo);
    }

    @Test
    void query() {
        // 查询数据
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEqpId("1977607896");
        equipmentInfo.setTableName("eqp_1977607896");
        LocalDateTime startTime = LocalDateTime.of(2024, 4, 26, 16, 58, 42);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = startTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        long timestamp = instant.toEpochMilli();
        // 格式化时间为yyyy-MM-dd HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = startTime.format(formatter);
        List<EquipmentInfo> list = equipmentInfoService.lambdaQuery()
                .ge(EquipmentInfo::getTs, timestamp)
                .eq(EquipmentInfo::getEqpId, equipmentInfo.getEqpId())
                .list();
        List<EquipmentInfo> list1 = equipmentInfoService.lambdaQuery()
                .ge(EquipmentInfo::getTs, format)
                .eq(EquipmentInfo::getEqpId, equipmentInfo.getEqpId())
                .list();
        log.info("查询结果：{}", list);
        log.info("查询结果：{}", list1);
    }

}
