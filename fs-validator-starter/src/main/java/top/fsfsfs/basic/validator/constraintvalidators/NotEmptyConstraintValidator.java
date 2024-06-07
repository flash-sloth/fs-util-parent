package top.fsfsfs.basic.validator.constraintvalidators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCharSequence;
import top.fsfsfs.basic.interfaces.validator.IValidatable;

/**
 * 自定义一个验证 NotEmpty 的校验器。自定义类需要实现IValidatable接口
 *
 * @author tangyh
 * @since 2020年02月02日20:59:21
 */
public class NotEmptyConstraintValidator implements ConstraintValidator<NotEmpty, IValidatable> {

    private final NotEmptyValidatorForCharSequence notEmptyValidator = new NotEmptyValidatorForCharSequence();

    @Override
    public void initialize(NotEmpty parameters) {
        notEmptyValidator.initialize(parameters);
    }

    @Override
    public boolean isValid(IValidatable value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value.value() != null && !"".equals(value.value());
    }
}
