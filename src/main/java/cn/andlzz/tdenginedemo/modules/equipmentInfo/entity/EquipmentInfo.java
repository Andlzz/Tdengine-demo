package cn.andlzz.tdenginedemo.modules.equipmentInfo.entity;

import cn.andlzz.tdenginedemo.modules.taos.annotation.Tags;
import cn.andlzz.tdenginedemo.modules.taos.model.BaseTaosModel;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : 杜亮志
 * @date : 2024年04月 上午11:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "equipment.equipment_info")
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentInfo extends BaseTaosModel<EquipmentInfo> {

    @ApiModelProperty("int值")
    Integer intValue;

    @ApiModelProperty("String值")
    String  strValue;

    @Tags
    @ApiModelProperty("设备id")
    String eqpId;
}

