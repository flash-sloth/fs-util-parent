package top.fsfsfs.basic.jwt;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.fsfsfs.basic.jwt.properties.JwtProperties;

/**
 * 认证服务端配置
 *
 * @author tangyh
 * @date 2018/11/20
 */
@EnableConfigurationProperties(value = {
        JwtProperties.class,
})
public class JwtConfiguration {

    @Bean
    public TokenHelper getTokenUtil(JwtProperties authServerProperties) {
        return new TokenHelper(authServerProperties);
    }
}
