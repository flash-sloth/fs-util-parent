package top.fsfsfs.basic.exception.code;

/**
 * 异常编码
 *
 * @author tangyh
 * @since 2017-12-25 13:46
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     *
     * @return 异常编码
     */
    int getCode();

    /**
     * 异常消息
     *
     * @return 异常消息
     */
    String getMsg();
}
