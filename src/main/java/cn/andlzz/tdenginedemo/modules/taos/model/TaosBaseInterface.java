package cn.andlzz.tdenginedemo.modules.taos.model;

import cn.andlzz.tdenginedemo.modules.taos.annotation.Tags;
import cn.andlzz.tdenginedemo.modules.taos.utils.StringUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * taos基础方法
 *
 * @author : 杜亮志
 * @date : 2023年08月 13:07
 */
public interface TaosBaseInterface {

    default String getDatabaseName(BaseTaosModel<?> baseTaosModel){
        TableName tableName = baseTaosModel.getClass().getAnnotation(TableName.class);
        if(StringUtils.isNotBlank(tableName.value())){
            // 如果格式为DatabaseName.STableName则返回DatabaseName
            String[] split = tableName.value().split("\\.");
            if(StringUtils.isNotBlank(split[0])){
                return split[0];
            }
        }
        return null;
    }


    default String getSTableName(BaseTaosModel<?> baseTaosModel){
        TableName tableName = baseTaosModel.getClass().getAnnotation(TableName.class);
        if(StringUtils.isNotBlank(tableName.value())){
            // 如果格式为DatabaseName.STableName则返回DatabaseName
            String[] split = tableName.value().split("\\.");
            if(StringUtils.isNotBlank(split[1])){
                return split[1];
            }else {
                return tableName.value();
            }
        }
        return null;
    }


    /**
     * 根据实体类动态创建超级表语句
     * @return 建表属性语句
     */
    @JsonIgnore
    default String getCreateSql(){
        Field[] fields = this.getClass().getDeclaredFields();
        // 获取对应字段列表
        List<Field> valueFields = getTagsFields(fields, false);
        List<Field> tagFields = getTagsFields(fields, true);
        StringJoiner values = new StringJoiner(",", "(", ")");
        StringJoiner tags = new StringJoiner(",", "(", ")");
        // 添加默认时间戳属性
        values.add("ts timestamp");
        // 拼接values
        for (Field field : valueFields) {
            try {
                // 获取字段名和状态名
                String name = field.getName();
                String typeName = field.getType().getSimpleName();
                values.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)+" "+typeName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 拼接tags
        for (Field field : tagFields) {
            try {
                // 获取字段名和状态名
                String name = field.getName();
                String typeName = field.getType().getSimpleName();
                tags.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)+" "+typeName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 拼接建表属性相关语句,工具类替换优化性能 | 替换string类型为nchar(32) | 替换boolean为bool
        return StringUtils.replaceEach(values + " tags " + tags,
                new String[]{"String", "boolean", "Boolean","Long"},
                new String[]{"nchar(32)", "bool", "bool","bigint unsigned"});
    }

    /**
     * 通过反射拼接所有values
     *
     * @return values 所有taos属性value字符串
     */
    default String getInsertValue(BaseTaosModel<?> baseTaosModel) {
        return getValues(baseTaosModel,true) + " values " + getValues(baseTaosModel,false);
    }

    default String getInsertTag(BaseTaosModel<?> baseTaosModel) {
        return getTags(baseTaosModel,true) + " tags " + getTags(baseTaosModel,false);
    }

    /**
     * 通过反射拼接所有values
     *
     * @param baseTaosModel taos基础类
     * @param isColumn 返回字段还是value值(true:返回字段 | false:返回value)
     * @return values 所有taos属性value字符串
     */
    default String getValues(BaseTaosModel<?> baseTaosModel,boolean isColumn) {
        Field[] fields = this.getClass().getDeclaredFields();
        List<Field> orderedField = getTagsFields(fields, false);
        StringJoiner values = new StringJoiner(",", "(", ")");
        StringJoiner keys = new StringJoiner(",", "(", ")");
        keys.add("ts");
        values.add(Long.toString(baseTaosModel.getTs().getTime()));
        for (Field field : orderedField) {

            boolean annotationPresent = field.isAnnotationPresent(TableField.class);
            if(annotationPresent){
                //有此注解
                TableField annotation = field.getAnnotation(TableField.class);
                boolean exist = annotation.exist();
                if(!exist){
                    //无视其存在,不参与语句拼接
                    continue;
                }
            }


            try {
                // 添加字段名
                String name = field.getName();
                keys.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name));
                // 添加字段值
                field.setAccessible(true);
                Object o = field.get(baseTaosModel);
                List<String> valueList = StringUtil.change2String(o);
                values.add(valueList.get(0));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if(isColumn){
            return keys.toString();
        }
        return values.toString();
    }

    /**
     * 通过反射拼接所有tags
     *
     * @param baseTaosModel taos基础类
     * @param isColumn 返回字段还是tags值(true:返回字段 | false:返回tags)
     * @return values 所有taos属性tags字符串
     */
    default String getTags(BaseTaosModel<?> baseTaosModel,boolean isColumn) {
        Field[] fields = this.getClass().getDeclaredFields();
        List<Field> orderedField = getTagsFields(fields, true);
        StringJoiner values = new StringJoiner(",", "(", ")");
        StringJoiner keys = new StringJoiner(",", "(", ")");
        for (Field field : orderedField) {
            try {
                // 添加字段名
                String name = field.getName();
                keys.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name));
                // 添加字段值
                field.setAccessible(true);
                Object o = field.get(baseTaosModel);
                List<String> valueList = StringUtil.change2String(o);
                values.add(valueList.get(0));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if(isColumn){
            return keys.toString();
        }
        return values.toString();
    }

    /**
     * 获取values或tags对应的字段
     *
     * @param fields 反射获取的Field数组
     * @param isTags 是否时tags
     * @return 排序后Field列表
     */
    default List<Field> getTagsFields(Field[] fields, boolean isTags) {
        // 用来存放所有的属性域
        List<Field> values = new ArrayList<>();
        List<Field> tags = new ArrayList<>();
        // 判断是否是tags字段
        for (Field f : fields) {
            if (f.getAnnotation(Tags.class) != null) {
                tags.add(f);
            } else {
                values.add(f);
            }
        }
        if (isTags) {
            return tags;
        }
        return values;
    }

    default void setStringFieldsToEmpty(Object obj) {
        Field[] fields = this.getClass().getDeclaredFields();
        // tag不能为空字符串
        List<Field> tagsFields = getTagsFields(fields, false);
        for (Field field : tagsFields) {
            if (field.getType() == String.class) {
                try {
                    field.setAccessible(true);
                    field.set(obj, "");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
