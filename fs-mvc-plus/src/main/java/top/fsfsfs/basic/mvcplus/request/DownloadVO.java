package top.fsfsfs.basic.mvcplus.request;

import lombok.Builder;
import lombok.Data;

/**
 * @author tangyh
 * @version v1.0
 * @since 2022/6/14 8:49 PM
 * @create [2022/6/14 8:49 PM ] [tangyh] [初始创建]
 */
@Data
@Builder
public class DownloadVO {
    byte[] data;
    String fileName;
}
