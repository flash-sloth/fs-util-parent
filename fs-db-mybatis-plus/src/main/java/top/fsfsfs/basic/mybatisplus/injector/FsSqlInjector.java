package top.fsfsfs.basic.mybatisplus.injector;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mybatisplus.injector.method.UpdateAllById;

import java.util.List;

/**
 * 自定义sql 注入器
 *
 * @author tangyh
 * @since 2020年02月19日15:39:49
 */
public class FsSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        //增加自定义方法
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new UpdateAllById(field -> !ArrayUtil.containsAny(new String[]{
                BaseEntity.CREATED_AT_FIELD, BaseEntity.CREATED_BY_FIELD
        }, field.getColumn())));
        return methodList;
    }
}
