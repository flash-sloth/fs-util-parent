package top.fsfsfs.codegen.config.front;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.fsfsfs.codegen.config.BaseConfig;

/**
 * 前端列表配置
 * @author tangyh
 * @since 2024/7/9 08:34
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class FormConfig extends BaseConfig {
    /** 是否生成该字段 */
    private Boolean show;
    /** 是否隐藏字段 */
    private Boolean hidden;
    /** 对齐方式 */
    private String align;
    /** label 超出隐藏 */
    private String titleOverflow;
    /**
     * 组件类型
     */
    private String componentType;
    /**
     * 顺序 升序
     */
    private int sequence;
}
