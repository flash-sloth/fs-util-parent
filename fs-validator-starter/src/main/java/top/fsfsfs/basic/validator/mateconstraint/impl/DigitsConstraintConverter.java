package top.fsfsfs.basic.validator.mateconstraint.impl;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import top.fsfsfs.basic.validator.mateconstraint.IConstraintConverter;
import top.fsfsfs.basic.validator.utils.ValidatorConstants;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 长度 转换器
 *
 * @author tangyh
 * @since 2019-07-25 15:15
 */
public class DigitsConstraintConverter extends BaseConstraintConverter implements IConstraintConverter {

    @Override
    protected List<String> getMethods() {
        return Arrays.asList("integer", "fraction", ValidatorConstants.MESSAGE);
    }

    @Override
    protected String getType(Class<? extends Annotation> type) {
        return type.getSimpleName();
    }

    @Override
    protected List<Class<? extends Annotation>> getSupport() {
        return Collections.singletonList(Digits.class);
    }

}
