package cn.andlzz.tdenginedemo.modules.equipmentInfo.mapper;

import cn.andlzz.tdenginedemo.modules.equipmentInfo.entity.EquipmentInfo;
import cn.andlzz.tdenginedemo.modules.taos.mapper.BaseTaosMapper;
import com.baomidou.dynamic.datasource.annotation.DS;

/**
 * @author : 杜亮志
 * @date : 2024年04月 上午11:42
 */
@DS("tdengine")
public interface EquipmentInfoMapper extends BaseTaosMapper<EquipmentInfo> {
}

