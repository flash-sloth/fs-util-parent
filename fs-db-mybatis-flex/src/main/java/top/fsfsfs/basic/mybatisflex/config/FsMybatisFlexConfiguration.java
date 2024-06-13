package top.fsfsfs.basic.mybatisflex.config;


import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.CountableMessageCollector;
import com.mybatisflex.core.audit.ScheduledMessageCollector;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.logicdelete.impl.BooleanLogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.DateTimeLogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.IntegerLogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.PrimaryKeyLogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.TimeStampLogicDeleteProcessor;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import lombok.extern.slf4j.Slf4j;
import top.fsfsfs.basic.db.config.FsDbConfiguration;
import top.fsfsfs.basic.db.properties.DatabaseProperties;
import top.fsfsfs.basic.mybatisflex.keygen.UidKeyGenerator;
import top.fsfsfs.basic.mybatisflex.logicdelete.TimeStampDelByLogicDeleteProcessor;

/**
 * Mybatis flex 常用重用拦截器
 *
 * @author tangyh
 * @since 2018/10/24
 */
@Slf4j
public abstract class FsMybatisFlexConfiguration extends FsDbConfiguration implements MyBatisFlexCustomizer {
    protected final MybatisFlexProperties mybatisFlexProperties;

    public FsMybatisFlexConfiguration(final DatabaseProperties databaseProperties, MybatisFlexProperties mybatisFlexProperties) {
        super(databaseProperties);
        this.mybatisFlexProperties = mybatisFlexProperties;
    }

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        audit();

        uid(globalConfig);

        logicDelete();
    }

    /** UID注册 */
    private static void uid(FlexGlobalConfig globalConfig) {
        FlexGlobalConfig.KeyConfig keyConfig = globalConfig.getKeyConfig();
        if (keyConfig != null && StrUtil.isNotEmpty(keyConfig.getValue())) {
            KeyGeneratorFactory.register(keyConfig.getValue(), new UidKeyGenerator());
        }
    }

    /** 逻辑删除处理器 */
    public void logicDelete() {
        DatabaseProperties.Flex flex = databaseProperties.getFlex();

        switch (flex.getLogicDeleteProcessor()) {
            case INTEGER_LOGIC_DELETE_PROCESSOR -> LogicDeleteManager.setProcessor(new IntegerLogicDeleteProcessor());
            case BOOLEAN_LOGIC_DELETE_PROCESSOR -> LogicDeleteManager.setProcessor(new BooleanLogicDeleteProcessor());
            case DATE_TIME_LOGIC_DELETE_PROCESSOR ->
                    LogicDeleteManager.setProcessor(new DateTimeLogicDeleteProcessor());
            case TIME_STAMP_LOGIC_DELETE_PROCESSOR ->
                    LogicDeleteManager.setProcessor(new TimeStampLogicDeleteProcessor());
            case PRIMARY_KEY_LOGIC_DELETE_PROCESSOR ->
                    LogicDeleteManager.setProcessor(new PrimaryKeyLogicDeleteProcessor());
            default -> LogicDeleteManager.setProcessor(new TimeStampDelByLogicDeleteProcessor(flex.getDelByColumn()));
        }
    }

    /** SQL审计 */
    public void audit() {
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
