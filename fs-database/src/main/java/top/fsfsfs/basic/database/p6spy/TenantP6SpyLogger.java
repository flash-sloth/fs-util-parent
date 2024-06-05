package top.fsfsfs.basic.database.p6spy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import top.fsfsfs.basic.context.ContextConstants;
import top.fsfsfs.basic.context.ContextUtil;

/**
 * 用于 p6spy 在输出的sql日志中，打印当前租户、当前用户ID、当前数据源连接url
 *
 * @author tangyh
 * @date 2020/9/3 3:39 下午
 */
public class TenantP6SpyLogger implements MessageFormattingStrategy {
    public static final String REGX = "\\s+";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        String msg = """
                线程参数：{}: {} {}: {}
                消耗时间：{} ms, 执行时间 {}
                数据源: {}
                执行的SQL：{}
                """;
        return StringUtils.isNotBlank(sql) ?
                StrUtil.format(msg,
                        ContextConstants.TENANT_ID_HEADER, ContextUtil.getTenantId(),
                        ContextConstants.USER_ID_HEADER, ContextUtil.getUserId(),
                        elapsed, now, url, sql.replaceAll(REGX, StringPool.SPACE)) :
                StringPool.EMPTY;
    }
}
