package cn.andlzz.tdenginedemo;

import cn.andlzz.tdenginedemo.modules.equipmentInfo.entity.EquipmentInfo;
import cn.andlzz.tdenginedemo.modules.equipmentInfo.service.IEquipmentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
        List<EquipmentInfo> list = equipmentInfoService.lambdaQuery()
                .eq(EquipmentInfo::getEqpId, equipmentInfo.getEqpId())
                .list();
        log.info("查询结果：{}", list);
    }

}
