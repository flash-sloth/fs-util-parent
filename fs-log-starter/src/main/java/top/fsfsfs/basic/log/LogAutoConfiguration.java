package top.fsfsfs.basic.log;


import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import top.fsfsfs.basic.log.aspect.SysLogAspect;
import top.fsfsfs.basic.log.event.SysLogListener;
import top.fsfsfs.basic.log.monitor.PointUtil;
import top.fsfsfs.basic.log.properties.OptLogProperties;
import top.fsfsfs.util.utils.JsonUtil;

/**
 * 日志自动配置
 * <p>
 *
 * @author tangyh
 * @date 2019/2/1
 */
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(OptLogProperties.class)
@ConditionalOnProperty(prefix = OptLogProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = OptLogProperties.PREFIX, name = "type", havingValue = "LOGGER", matchIfMissing = true)
    public SysLogListener sysLogListener() {
        return new SysLogListener(log -> PointUtil.debug("0", "OPT_LOG", JsonUtil.toJson(log)));
    }
}
