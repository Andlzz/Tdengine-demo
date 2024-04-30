package cn.andlzz.tdenginedemo.modules.taos.model;

import cn.andlzz.tdenginedemo.modules.taos.model.TaosBaseInterface;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author : 杜亮志
 * @date : 2023年08月 10:20
 * <p>
 * taos基础类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTaosModel<T extends Model<T>> extends Model<T> implements TaosBaseInterface {

    private static final long serialVersionUID = 2537647301574172972L;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("时间戳")
    private Timestamp ts;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("数据库名")
    private String databaseName;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("超级表名")
    private String stableName;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("表名")
    private String tableName;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("insertValue相关语句")
    private String insertValue;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("insertTag相关语句")
    private String insertTag;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("建表相关语句")
    private String createValues;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty("批量新增语句")
    private String batchSql;

    /**
     * 时间戳构造方法
     *
     * @param ts 时间戳
     */
    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    /**
     * 时间戳构造方法
     *
     * @param ts 时间戳
     */
    public void setTs(Long ts) {
        this.ts = new Timestamp(ts);
    }

    /**
     * 时间戳构造方法
     *
     * @param ts 时间戳
     */
    public void setTs(LocalDateTime ts) {
        this.ts = Timestamp.valueOf(ts);
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    public Long getTsLong(){
        if (ObjectUtils.isNotEmpty(ts)) {
            return ts.getTime();
        }
        return null;
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getTsLocalDateTime() {
        if (ObjectUtils.isNotEmpty(ts)) {
            return ts.toLocalDateTime();
        }
        return null;
    }

    /**
     * 初始化相关语句方法
     *
     * @param baseTaosModel taos基础实体类
     */
    public void init(BaseTaosModel<?> baseTaosModel) {
        // 如果时间戳为空，默认当前时间
        if(ObjectUtils.isEmpty(this.ts)){
            this.ts = new Timestamp(System.currentTimeMillis());
        }
        // 如果数据库或超级表为空，默认从@TableName获取
        if (StringUtils.isEmpty(this.stableName)) {
            this.databaseName = getDatabaseName(baseTaosModel);
        }
        if (StringUtils.isEmpty(this.stableName)) {
            this.stableName = getSTableName(baseTaosModel);
        }
        // 获取value和tag语句
        this.insertValue = getInsertValue(baseTaosModel);
        this.insertTag = getInsertTag(baseTaosModel);
        this.createValues = getCreateSql();
    }

    /**
     * 重写构造方法，初始化成员变量，减少taos的null值存储
     */
    public BaseTaosModel() {
        setStringFieldsToEmpty(this);
    }

}
