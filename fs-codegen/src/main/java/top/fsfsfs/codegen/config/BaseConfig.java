package top.fsfsfs.codegen.config;

import lombok.Data;
import lombok.experimental.Accessors;
import top.fsfsfs.codegen.constant.GenerationStrategyEnum;

import java.io.Serializable;

/**
 *
 * @author tangyh
 * @since 2024/7/7 21:26
 */
@Data
@Accessors(chain = true)
public class BaseConfig implements Serializable {
    /**
     * 生成策略。
     */
    private GenerationStrategyEnum generationStrategy = GenerationStrategyEnum.OVERWRITE;
}
