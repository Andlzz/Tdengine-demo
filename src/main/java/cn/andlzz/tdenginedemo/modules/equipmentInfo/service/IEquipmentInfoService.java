package cn.andlzz.tdenginedemo.modules.equipmentInfo.service;

import cn.andlzz.tdenginedemo.modules.equipmentInfo.entity.EquipmentInfo;
import cn.andlzz.tdenginedemo.modules.taos.service.BaseTaosService;
import com.baomidou.dynamic.datasource.annotation.DS;

/**
 * @author : 杜亮志
 * @date : 2024年04月 上午11:43
 */
@DS("tdengine")
public interface IEquipmentInfoService extends BaseTaosService<EquipmentInfo> {
}
