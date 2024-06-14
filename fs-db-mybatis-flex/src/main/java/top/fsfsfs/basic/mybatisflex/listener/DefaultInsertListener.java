package top.fsfsfs.basic.mybatisflex.listener;

import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.utils.ContextUtil;
import top.fsfsfs.basic.utils.StrPool;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 新增时数据填充监听器
 * @author tangyh
 * @since 2024年06月07日16:10:28
 */
public class DefaultInsertListener implements InsertListener {
    @Override
    public void onInsert(Object param) {

        if (param instanceof BaseEntity entity) {

//            if (entity.getId() == null) {
//                Long id = uidGenerator.getUid();
//
//                Field idField = ReflectUtil.getField(param.getClass(), SuperEntity.ID_FIELD);
//                if (idField != null) {
//                    entity.setId(StrPool.STRING_TYPE_NAME.equals(idField.getGenericType().getTypeName()) ? String.valueOf(id) : id);
//                } else {
//                    entity.setId(id);
//                }
//            }

            if (entity.getCreatedBy() == null) {

                Field createdByField = ReflectUtil.getField(param.getClass(), BaseEntity.CREATED_BY);
                Object userIdVal = ContextUtil.getUserId();
                if (createdByField != null) {
                    userIdVal = StrPool.STRING_TYPE_NAME.equals(createdByField.getGenericType().getTypeName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();
                }
                entity.setCreatedBy(userIdVal);
            }
            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDateTime.now());
            }

            return;
        }

        Field createdByField = ReflectUtil.getField(param.getClass(), BaseEntity.CREATED_BY);
        if (createdByField != null) {
            Object fieldValue = ReflectUtil.getFieldValue(param, createdByField);
            if (fieldValue == null) {

                Object userIdVal = StrPool.STRING_TYPE_NAME.equals(createdByField.getGenericType().getTypeName()) ? String.valueOf(ContextUtil.getUserId()) : ContextUtil.getUserId();

                ReflectUtil.setFieldValue(param, createdByField, userIdVal);
            }
        }

        Field createdTimeField = ReflectUtil.getField(param.getClass(), BaseEntity.CREATED_AT);
        if (createdTimeField != null) {
            Object fieldValue = ReflectUtil.getFieldValue(param, createdTimeField);
            if (fieldValue == null) {
                ReflectUtil.setFieldValue(param, createdTimeField, LocalDateTime.now());
            }
        }

    }
}
