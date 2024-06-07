package top.fsfsfs.basic.validator.component.extract;

import top.fsfsfs.basic.validator.model.FieldValidatorDesc;
import top.fsfsfs.basic.validator.model.ValidConstraint;

import java.util.Collection;
import java.util.List;


/**
 * 提取指定表单验证规则
 *
 * @author tangyh
 * @since 2019-06-12
 */
public interface IConstraintExtract {

    /**
     * 提取指定表单验证规则
     *
     * @param constraints 限制条件
     * @return 验证规则
     * @throws Exception 异常
     */
    Collection<FieldValidatorDesc> extract(List<ValidConstraint> constraints) throws Exception;
}
