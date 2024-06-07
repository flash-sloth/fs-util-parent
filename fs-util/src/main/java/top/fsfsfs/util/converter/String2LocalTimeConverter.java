package top.fsfsfs.util.converter;

import org.springframework.core.convert.converter.Converter;
import top.fsfsfs.util.utils.DateUtils;

import java.time.LocalTime;

/**
 * 解决 @RequestParam LocalTime Date 类型的入参，参数转换问题。
 * <p>
 * HH:mm:ss
 * HH时mm分ss秒
 *
 * @author tangyh
 * @since 2019-04-30
 */
public class String2LocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return DateUtils.parseAsLocalTime(source);
    }
}
