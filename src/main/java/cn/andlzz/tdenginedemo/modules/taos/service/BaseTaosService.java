package cn.andlzz.tdenginedemo.modules.taos.service;

import cn.andlzz.tdenginedemo.modules.taos.model.BaseTaosModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * taos基础方法，定义taos相关所需基础方法
 *
 * @author : 杜亮志
 * @date : 2023年08月 15:27
 */
public interface BaseTaosService<T extends BaseTaosModel<T>> extends IService<T> {

    /**
     * 创建默认数据库
     * 重点:最好手动在taos中创建数据库 | 本方法使用于taos初始化默认创建
     *
     * @param baseTaosModel taos基础实体
     */
    void createDatabase(T baseTaosModel);

    /**
     * 创建具有过期时间的数据库
     * 重点:最好手动在taos中创建数据库 | 本方法使用于taos初始化默认创建
     *
     * @param baseTaosModel taos基础实体
     * @param duration      过期间隔时间
     * @param keep          保留数据时间
     */
    void createDatabaseDuration(T baseTaosModel, String duration, String keep);

    /**
     * 创建超级表
     * 重点:最好手动在taos中创建超级表 | 本方法使用于taos初始化默认创建
     * 重点:如需使用,请了解@TableName规则--
     * --多数据库同结构超级表请手动设置数据库
     *
     * @param baseTaosModel taos实体
     */
    void createSTable(T baseTaosModel);

    /**
     * 删除超级表
     *
     * @param baseTaosModel taos基础类
     */
    void dropSuperTable(T baseTaosModel);

    /**
     * 删除表
     *
     * @param baseTaosModel taos基础类
     */
    void dropTable(T baseTaosModel);

    /**
     * 新增数据
     *
     * @param baseTaosModel taos基础类
     * @return 影响行数
     */
    int insert(T baseTaosModel);

    /**
     * 批量新增方法
     *
     * @param baseTaosModels taos基础实体类列表
     * @param count          批量数据上限 需自己预估 留好容错空间，默认1000行
     * @return 影响行数
     */
    int insertBatch(List<T> baseTaosModels, int count);

    /**
     * 批量新增方法
     *
     * @param baseTaosModels taos基础实体类列表
     */
    int insertBatch(List<T> baseTaosModels);

    /**
     * 获取taos所有数据库名方法
     *
     * @return 数据库名list
     */
    List<String> showDatabases();

    /**
     * 获取实体类对应数据库下所有超级表名方法
     *
     * @param baseTaosModel taos实体
     * @return 数据库下所有超级表list
     */
    List<String> showSTables(T baseTaosModel);

    /**
     * 获取实体类对应数据库下所有表名方法
     *
     * @param baseTaosModel taos实体
     * @return 数据库下所有表list
     */
    List<String> showTables(T baseTaosModel);

    /**
     * 切换数据库
     * 重点 : 当前使用restFul连接，无法切换数据库 使用前确认连接方式
     *
     * @param baseTaosModel taos实体
     */
    void useDatabase(T baseTaosModel);

    /**
     * 创建流式计算方法
     *
     * @param streamName    流名称
     * @param streamOptions 流参数
     * @param stbName       表名
     * @param subquery      查询语句
     */
    void createStream(String streamName, String streamOptions, String stbName, String subquery);
}
