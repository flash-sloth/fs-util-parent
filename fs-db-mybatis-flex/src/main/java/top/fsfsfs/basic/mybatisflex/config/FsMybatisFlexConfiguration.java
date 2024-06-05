package top.fsfsfs.basic.mybatisflex.config;


import lombok.extern.slf4j.Slf4j;
import top.fsfsfs.basic.db.config.FsDbConfiguration;
import top.fsfsfs.basic.db.properties.DatabaseProperties;

/**
 * Mybatis flex 常用重用拦截器
 *
 * @author tangyh
 * @date 2018/10/24
 */
@Slf4j
public abstract class FsMybatisFlexConfiguration extends FsDbConfiguration {
    public FsMybatisFlexConfiguration(final DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }


}
