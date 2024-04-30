package cn.andlzz.tdenginedemo.modules.taos.service.impl;

import cn.andlzz.tdenginedemo.modules.taos.mapper.BaseTaosMapper;
import cn.andlzz.tdenginedemo.modules.taos.model.BaseTaosModel;
import cn.andlzz.tdenginedemo.modules.taos.service.BaseTaosService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author : 杜亮志
 * @date : 2023年08月 15:28
 */
@Slf4j
@DS("tdengine")
public class BaseTaosServiceImpl<M extends BaseTaosMapper<T>, T extends BaseTaosModel<T>> extends ServiceImpl<M, T> implements BaseTaosService<T> {

    @Override
    protected Class<T> currentMapperClass() {
        return (Class<T>) this.getResolvableType().as(BaseTaosServiceImpl.class).getGeneric(0).getType();
    }

    @Override
    protected Class<T> currentModelClass() {
        return (Class<T>) this.getResolvableType().as(BaseTaosServiceImpl.class).getGeneric(1).getType();
    }

    @Override
    public void createDatabase(T baseTaosModel) {
        baseTaosModel.init(baseTaosModel);
        baseMapper.createDatabase(baseTaosModel);
    }

    @Override
    public void createDatabaseDuration(T baseTaosModel, String duration, String keep) {
        baseTaosModel.init(baseTaosModel);
        baseMapper.createDatabaseDuration(baseTaosModel, duration, keep);
    }

    @Override
    public void createSTable(T baseTaosModel) {
        baseTaosModel.init(baseTaosModel);
        baseMapper.createDatabase(baseTaosModel);
        baseMapper.createSTable(baseTaosModel);
    }

    @Override
    public void dropSuperTable(T baseTaosModel) {
        baseTaosModel.init(baseTaosModel);
        baseMapper.dropSuperTable(baseTaosModel);
    }

    @Override
    public void dropTable(T baseTaosModel) {
        baseTaosModel.init(baseTaosModel);
        baseMapper.dropTable(baseTaosModel);
    }

    @Override
    public int insert(T baseTaosModel) {
        baseTaosModel.init(baseTaosModel);
        return baseMapper.insert(baseTaosModel);
    }

    @Override
    public int insertBatch(List<T> list, int count) {
        // 默认值设为1000
        if (count == 0) {
            count = 1000;
        }
        // 根据表名转换map
        Map<String, List<T>> baseModelMap = list.stream()
                .filter(baseTaosModel -> ObjectUtils.isNotEmpty(baseTaosModel.getTableName()))
                .collect(Collectors.groupingBy(BaseTaosModel::getTableName));
        Set<String> strings = baseModelMap.keySet();
        // 不同表遍历新增
        int finalCount = count;
        strings.forEach(tableName -> {
            List<T> baseTaosModelList = baseModelMap.get(tableName);
            if (!baseTaosModelList.isEmpty()) {
                // sql字段标志位
                AtomicInteger i = new AtomicInteger(0);
                // StringJoiner无法清除字符，所以采用StringBuilder拼接空格
                StringBuilder tags = new StringBuilder();
                StringBuilder values = new StringBuilder();

                baseTaosModelList.forEach(baseTaosModel -> {
                    // 如果超过传入次数
                    if (i.get() > finalCount) {
                        String insertSql = tags.append(values).toString();
                        baseTaosModel.init(baseTaosModel);
                        baseTaosModel.setBatchSql(insertSql);
                        baseMapper.insertBatch(baseTaosModel, insertSql);
                        // i重新设施为0 | tags和values置空
                        i.set(0);
                        tags.delete(0, tags.length());
                        values.delete(0, values.length());
                    }
                    // 拼接新增语句
                    if (i.getAndIncrement() == 0) {
                        baseTaosModel.init(baseTaosModel);
                        tags.append(baseTaosModel.getInsertTag()).append(" ");
                        values.append(baseTaosModel.getInsertValue()).append(" ");
                    } else {
                        values.append(baseTaosModel.getValues(baseTaosModel, false)).append(" ");
                    }
                });
                String insertSql = tags.append(values).toString();
                T baseTaosModel = baseTaosModelList.get(0);
                baseTaosModel.init(baseTaosModel);
                baseTaosModel.setBatchSql(insertSql);
                baseMapper.insertBatch(baseTaosModel, insertSql);
            }
        });
        return 0;
    }

    @Override
    public int insertBatch(List<T> list) {
        // 根据表名转换map
        Map<String, List<T>> baseModelMap = list.stream()
                .filter(baseTaosModel -> ObjectUtils.isNotEmpty(baseTaosModel.getTableName()))
                .collect(Collectors.groupingBy(BaseTaosModel::getTableName));
        Set<String> strings = baseModelMap.keySet();
        // 不同表遍历新增
        strings.forEach(tableName -> {
            List<T> baseTaosModelList = baseModelMap.get(tableName);
            if (!baseTaosModelList.isEmpty()) {
                // sql字段标志位
                AtomicInteger i = new AtomicInteger(0);
                // 拼接字段
                StringJoiner tags = new StringJoiner(" ");
                StringJoiner values = new StringJoiner(" ");

                baseTaosModelList.forEach(baseTaosModel -> {
                    // 最大字节长度
                    int maxLength = 1000000;
                    // 按照中文编码预测长度-稳健
                    if (values.length() * 3 > maxLength) {
                        String insertSql = tags.merge(values).toString();
                        baseTaosModel.init(baseTaosModel);
                        baseTaosModel.setBatchSql(insertSql);
                        baseMapper.insertBatch(baseTaosModel, insertSql);
                        // i重新设施为0 | tags和values置空
                        i.set(0);
                        tags.setEmptyValue("");
                        values.setEmptyValue("");
                    }
                    // 拼接新增语句
                    if (i.getAndIncrement() == 0) {
                        baseTaosModel.init(baseTaosModel);
                        tags.add(baseTaosModel.getInsertTag());
                        values.add(baseTaosModel.getInsertValue());
                    } else {
                        values.add(baseTaosModel.getValues(baseTaosModel, false));
                    }
                });
                String insertSql = tags.merge(values).toString();
                T baseTaosModel = baseTaosModelList.get(0);
                baseTaosModel.init(baseTaosModel);
                baseTaosModel.setBatchSql(insertSql);
                baseMapper.insertBatch(baseTaosModel, insertSql);
            }
        });

        return 0;
    }

    @Override
    public List<String> showDatabases() {
        return baseMapper.showDatabases();
    }

    @Override
    public List<String> showSTables(T baseTaosModel) {
        return baseMapper.showSTables(baseTaosModel);
    }

    @Override
    public List<String> showTables(T baseTaosModel) {
        return baseMapper.showTables(baseTaosModel);
    }

    @Override
    public void useDatabase(T baseTaosModel) {
        baseMapper.useDatabase(baseTaosModel);
    }

    @Override
    public void createStream(String streamName, String streamOptions, String stbName, String subquery) {
        baseMapper.createStream(streamName, streamOptions, stbName, subquery);
    }
}
