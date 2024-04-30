package cn.andlzz.tdenginedemo.modules.equipmentInfo.service.impl;

import cn.andlzz.tdenginedemo.modules.equipmentInfo.entity.EquipmentInfo;
import cn.andlzz.tdenginedemo.modules.equipmentInfo.mapper.EquipmentInfoMapper;
import cn.andlzz.tdenginedemo.modules.equipmentInfo.service.IEquipmentInfoService;
import cn.andlzz.tdenginedemo.modules.taos.service.impl.BaseTaosServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

/**
 * @author : 杜亮志
 * @date : 2024年04月 上午11:43
 */
@DS("tdengine")
@Service
public class EquipmentInfoServiceImpl extends BaseTaosServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements IEquipmentInfoService {
}
