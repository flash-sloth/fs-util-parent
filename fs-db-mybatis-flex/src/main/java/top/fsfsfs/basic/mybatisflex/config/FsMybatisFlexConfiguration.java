package top.fsfsfs.basic.mybatisflex.config;


import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.CountableMessageCollector;
import com.mybatisflex.core.audit.ScheduledMessageCollector;
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
        audit(databaseProperties);
    }


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
