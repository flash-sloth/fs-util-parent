package top.fsfsfs.basic.mybatisplus.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import top.fsfsfs.basic.db.config.FsDbConfiguration;
import top.fsfsfs.basic.db.properties.DatabaseProperties;
import top.fsfsfs.basic.mybatisplus.injector.FsSqlInjector;
import top.fsfsfs.basic.mybatisplus.mybatis.WriteInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis 常用重用拦截器
 * <p>
 * 拦截器执行一定是：
 * WriteInterceptor > DataScopeInterceptor > PaginationInterceptor
 *
 * @author tangyh
 * @since 2018/10/24
 */
@Slf4j
public abstract class FsMybatisPlusConfiguration extends FsDbConfiguration {

    public FsMybatisPlusConfiguration(final DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    /**
     * 演示环境权限拦截器
     *
     * @return 写入拦截器
     */
    @Bean
    @Order(15)
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DatabaseProperties.PREFIX, name = "isNotWrite", havingValue = "true")
    public WriteInterceptor getWriteInterceptor() {
        return new WriteInterceptor(databaseProperties);
    }


    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     * <p>
     * 注意:
     * 如果内部插件都是使用,需要注意顺序关系,建议使用如下顺序
     * 多租户插件,动态表名插件
     * 分页插件,乐观锁插件
     * sql性能规范插件,防止全表更新与删除插件
     * 总结: 对sql进行单次改造的优先放入,不对sql进行改造的最后放入
     * <p>
     * 参考：
     * https://mybatis.plus/guide/interceptor.html#%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-%E4%BB%A5%E5%88%86%E9%A1%B5%E6%8F%92%E4%BB%B6%E4%B8%BE%E4%BE%8B
     */
    @Bean
    @Order(5)
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        List<InnerInterceptor> beforeInnerInterceptor = getPaginationBeforeInnerInterceptor();
        if (!beforeInnerInterceptor.isEmpty()) {
            beforeInnerInterceptor.forEach(interceptor::addInnerInterceptor);
        }

        DatabaseProperties.Plus plus = databaseProperties.getPlus();

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 单页分页条数限制
        paginationInterceptor.setMaxLimit(plus.getMaxLimit());
        // 数据库类型
        if (databaseProperties.getDbType() != null) {
            paginationInterceptor.setDbType(databaseProperties.getDbType());
        }
        // 溢出总页数后是否进行处理
        paginationInterceptor.setOverflow(plus.getOverflow());
        // 生成 countSql 优化掉 join 现在只支持 left join
        paginationInterceptor.setOptimizeJoin(plus.getOptimizeJoin());
        interceptor.addInnerInterceptor(paginationInterceptor);


        List<InnerInterceptor> afterInnerInterceptor = getPaginationAfterInnerInterceptor();
        if (!afterInnerInterceptor.isEmpty()) {
            afterInnerInterceptor.forEach(interceptor::addInnerInterceptor);
        }

        //防止全表更新与删除插件
        if (plus.getIsBlockAttack()) {
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }
        // sql性能规范插件
        if (plus.getIsIllegalSql()) {
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }

        return interceptor;
    }


    /**
     * 分页拦截器之后的插件
     *
     * @return 分页拦截器之后的插件
     */
    protected List<InnerInterceptor> getPaginationAfterInnerInterceptor() {
        return new ArrayList<>();
    }

    /**
     * 分页拦截器之前的插件
     *
     * @return 分页拦截器之前的插件
     */
    protected List<InnerInterceptor> getPaginationBeforeInnerInterceptor() {
        return new ArrayList<>();
    }

    /**
     * Mybatis Plus 注入器
     *
     * @return 注入器
     */
    @Bean("myMetaObjectHandler")
    @ConditionalOnMissingBean
    public MetaObjectHandler getMyMetaObjectHandler() {
        return new FsMetaObjectHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public FsSqlInjector getMySqlInjector() {
        return new FsSqlInjector();
    }

}
