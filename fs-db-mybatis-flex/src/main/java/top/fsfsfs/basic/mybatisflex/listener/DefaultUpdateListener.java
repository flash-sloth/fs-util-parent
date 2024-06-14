package top.fsfsfs.basic.mybatisflex.listener;

import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.UpdateListener;
import top.fsfsfs.basic.base.entity.SuperEntity;
import top.fsfsfs.basic.utils.ContextUtil;
import top.fsfsfs.basic.utils.StrPool;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 修改时数据填充监听器
 * @author tangyh
 * @since 2024年06月07日16:10:28
 */
public class DefaultUpdateListener implements UpdateListener {
    @Override
    public void onUpdate(Object param) {
        if (param instanceof SuperEntity entity) {
            if (entity.getUpdatedBy() == null) {
                Field updatedByField = ReflectUtil.getField(param.getClass(), SuperEntity.UPDATED_BY_FIELD);
                Object userIdVal = ContextUtil.getUserId();
                if (updatedByField != null) {
                    userIdVal = StrPool.STRING_TYPE_NAME.equals(updatedByField.getGenericType().getTypeName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();
                }

                entity.setUpdatedBy(userIdVal);
            }

            if (entity.getUpdatedAt() == null) {
                entity.setUpdatedAt(LocalDateTime.now());
            }
            return;
        }


        Field updatedByField = ReflectUtil.getField(param.getClass(), SuperEntity.UPDATED_BY_FIELD);
        if (updatedByField != null) {
            Object fieldValue = ReflectUtil.getFieldValue(param, updatedByField);
            if (fieldValue == null) {
                Object userIdVal = StrPool.STRING_TYPE_NAME.equals(updatedByField.getGenericType().getTypeName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();

                ReflectUtil.setFieldValue(param, updatedByField, userIdVal);
            }
        }

        Field updatedTimeField = ReflectUtil.getField(param.getClass(), SuperEntity.UPDATED_AT_FIELD);
        if (updatedTimeField != null) {
            Object fieldValue = ReflectUtil.getFieldValue(param, updatedTimeField);
            if (fieldValue == null) {
                ReflectUtil.setFieldValue(param, updatedTimeField, LocalDateTime.now());
            }
        }
    }
}
