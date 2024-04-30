package cn.andlzz.tdenginedemo.modules.taos.mapper;

import cn.andlzz.tdenginedemo.modules.taos.model.BaseTaosModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Taos基础mapper，包含基础语句
 *
 * @author : 杜亮志
 * @date : 2022年12月 下午5:53
 */
public interface BaseTaosMapper<T extends BaseTaosModel<T>> extends BaseMapper<T> {

    /**
     * 创建默认数据库
     *
     * @param baseTaosModel taos实体
     */
    @Update("create database if not exists ${databaseName}")
    void createDatabase(T baseTaosModel);

    /**
     * 创建具有过期时间的数据库
     *
     * @param baseTaosModel taos基础实体
     * @param duration      过期间隔时间
     * @param keep          保留数据时间
     */
    @Update("create database if not exists ${baseTaosModel.databaseName} duration ${duration} keep ${keep}")
    void createDatabaseDuration(T baseTaosModel,
                                @Param("duration") String duration,
                                @Param("keep") String keep);

    /**
     * 创建超级表
     *
     * @param baseTaosModel taos实体
     */
    @Update("create stable if not exists ${databaseName}.${stableName} ${createValues}")
    void createSTable(T baseTaosModel);

    /**
     * 删除超级表
     *
     * @param baseTaosModel taos基础类
     */
    @Update("drop stable if ${databaseName}.${stableName}")
    void dropSuperTable(T baseTaosModel);

    /**
     * 删除表
     *
     * @param baseTaosModel taos基础类
     */
    @Update("drop table if exists ${databaseName}.${tableName}")
    void dropTable(T baseTaosModel);

    /**
     * 新增数据
     *
     * @param baseTaosModel taos基础类
     * @return 影响行数
     */
    @Insert("insert into ${databaseName}.${tableName} using ${databaseName}.${stableName} ${insertTag} ${insertValue}")
    int insert(T baseTaosModel);

    /**
     * 批量新增
     * @param baseTaosModel taos基础类
     * @param insertSql 新增语句
     * @return 影响行数
     */
    @Insert("insert into ${baseTaosModel.databaseName}.${baseTaosModel.tableName} using ${baseTaosModel.databaseName}.${baseTaosModel.stableName} ${insertSql}")
    int insertBatch(T baseTaosModel, @Param("insertSql") String insertSql);

    /**
     * 获取taos所有数据库名方法
     *
     * @return 数据库名list
     */
    @Select("show databases")
    List<String> showDatabases();

    /**
     * 获取实体类对应数据库下所有超级表名方法
     *
     * @param baseTaosModel taos实体
     * @return 数据库下所有超级表list
     */
    @Select("show ${databaseName}.stables")
    List<String> showSTables(T baseTaosModel);

    /**
     * 获取实体类对应数据库下所有表名方法
     *
     * @param baseTaosModel taos实体
     * @return 数据库下所有表list
     */
    @Select("show ${databaseName}.tables")
    List<String> showTables(T baseTaosModel);

    /**
     * 切换数据库
     *
     * @param baseTaosModel taos实体
     */
    @Update("use ${databaseName}")
    void useDatabase(T baseTaosModel);

    /**
     * 创建数据流
     *
     * @param streamName    流名称
     * @param streamOptions 流参数
     * @param stbName       超级表名称
     * @param subquery      查询语句
     */
    @Update("create stream if not exists ${streamName} ${streamOptions} INTO ${stbName} AS ${subquery}")
    void createStream(@Param("streamName") String streamName, @Param("streamOptions") String streamOptions,
                      @Param("stbName") String stbName, @Param("subquery") String subquery);
}
