package top.fsfsfs.basic.mybatisflex.config;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.buffer.RejectedPutBufferHandler;
import com.baidu.fsg.uid.buffer.RejectedTakeBufferHandler;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.impl.HuToolUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.CountableMessageCollector;
import com.mybatisflex.core.audit.ScheduledMessageCollector;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import top.fsfsfs.basic.db.config.FsDbConfiguration;
import top.fsfsfs.basic.db.properties.DatabaseProperties;
import top.fsfsfs.basic.mybatisflex.keygen.UidKeyGenerator;
import top.fsfsfs.basic.uid.dao.WorkerNodeDao;

/**
 * Mybatis flex 常用重用拦截器
 *
 * @author tangyh
 * @since 2018/10/24
 */
@Slf4j
public abstract class FsMybatisFlexConfiguration extends FsDbConfiguration {
    public FsMybatisFlexConfiguration(final DatabaseProperties databaseProperties) {
        super(databaseProperties);
        audit(databaseProperties);

        KeyGeneratorFactory.register("uid", new UidKeyGenerator());
    }

//    @Override
//    @Bean
//    public UidGenerator getHuToolUidGenerator(WorkerNodeDao workerNodeDao) {
//        switch (databaseProperties.getIdType()) {
//            case CACHE -> {
//                DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner(workerNodeDao);
//                CachedUidGenerator uidGenerator = new CachedUidGenerator();
//                DatabaseProperties.CacheId cacheId = databaseProperties.getCacheId();
//                BeanUtil.copyProperties(cacheId, uidGenerator);
//                if (cacheId.getRejectedPutBufferHandlerClass() != null) {
//                    RejectedPutBufferHandler rejectedPutBufferHandler = ReflectUtil.newInstance(cacheId.getRejectedPutBufferHandlerClass());
//                    uidGenerator.setRejectedPutBufferHandler(rejectedPutBufferHandler);
//                }
//                if (cacheId.getRejectedTakeBufferHandlerClass() != null) {
//                    RejectedTakeBufferHandler rejectedTakeBufferHandler = ReflectUtil.newInstance(cacheId.getRejectedTakeBufferHandlerClass());
//                    uidGenerator.setRejectedTakeBufferHandler(rejectedTakeBufferHandler);
//                }
//                uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
//
//                return uidGenerator;
//            }
//            case HU_TOOL -> {
//                DatabaseProperties.HutoolId id = databaseProperties.getHutoolId();
//                HuToolUidGenerator uidGenerator = new HuToolUidGenerator(id.getWorkerId(), id.getDataCenterId());
////                KeyGeneratorFactory.register("uid", new UidKeyGenerator(uidGenerator));
//                return uidGenerator;
//            }
//            default -> {
//                DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner(workerNodeDao);
//                DefaultUidGenerator uidGenerator = new DefaultUidGenerator();
//                BeanUtil.copyProperties(databaseProperties.getDefaultId(), uidGenerator);
//                uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
////                KeyGeneratorFactory.register("uid", new UidKeyGenerator(uidGenerator));
//                return uidGenerator;
//            }
//        }
//    }


    public static void audit(DatabaseProperties databaseProperties) {
        DatabaseProperties.Flex flex = databaseProperties.getFlex();

        //开启审计功能
        AuditManager.setAuditEnable(flex.getAudit());

        switch (flex.getAuditCollector()) {
            case CONSOLE -> AuditManager.setMessageCollector(new ConsoleMessageCollector());
            case COUNTABLE -> AuditManager.setMessageCollector(new CountableMessageCollector());
            case SCHEDULED -> AuditManager.setMessageCollector(new ScheduledMessageCollector());
            default -> AuditManager.setMessageCollector(auditMessage ->
                    log.info("""
                                    SQL:
                                    数据源: {} | 查询数据量: {} 条 | 消耗时间: {} ms
                                    {}""",
                            auditMessage.getDsName(),
                            auditMessage.getQueryCount(),
                            auditMessage.getElapsedTime(),
                            formatSql(auditMessage.getFullSql())
                    ));
        }

    }

    private static String formatSql(String sql) {
        return sql.replaceAll("\\s+", " ").replace("\\r", " ").replace("\\n", " ");
    }

}
