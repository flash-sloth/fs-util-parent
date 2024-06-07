package top.fsfsfs.basic.echo.manager;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.fsfsfs.basic.annotation.echo.Echo;

import java.util.Arrays;

/**
 * 封装 Echo 注解中解析出来的参数
 * <p>
 * 必须重写该类的 equals() 和 hashCode() 便于Map操作
 *
 * @author tangyh
 * @since 2020年02月03日18:48:29
 */
@Data
@NoArgsConstructor
@ToString
public class LoadKey {

    /**
     * 执行查询任务的类
     * <p/>
     * api()  和 feign() 任选其一,  使用 api时，请填写实现类， 使用feign时，填写接口即可
     * 如： @Echo(api="userServiceImpl") 等价于 @Echo(feign=UserService.class)
     * 如： @Echo(api="userController") 等价于 @Echo(feign=UserApi.class)
     */
    private String api;

    public LoadKey(Echo rf) {
        this.api = rf.api();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoadKey that = (LoadKey) o;
        return StrUtil.equals(api, that.api);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{api});
    }
}
