package top.fsfsfs.basic.mvcflex.utils;

import cn.hutool.core.util.ArrayUtil;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.constant.SqlOperator;
import com.mybatisflex.core.query.SqlOperators;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.StringUtil;
import org.apache.ibatis.util.MapUtil;
import top.fsfsfs.basic.exception.BizException;
import top.fsfsfs.util.utils.ArgumentAssert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerUtil {

    private static final Map<Class<?>, SqlOperators> sqlOperatorsMap = new ConcurrentHashMap<>();

    public static SqlOperators buildOperators(Class<?> entityClass) {
        return new SqlOperators(MapUtil.computeIfAbsent(sqlOperatorsMap, entityClass, aClass -> {
            SqlOperators sqlOperators = new SqlOperators();
            List<Field> allFields = ClassUtil.getAllFields(entityClass);
            allFields.forEach(field -> {
                if (field.getType() == String.class) {
                    Column column = field.getAnnotation(Column.class);
                    if (column != null && column.ignore()) {
                        return;
                    }
                    sqlOperators.set(field.getName(), SqlOperator.LIKE);
                }
            });
            return sqlOperators;
        }));
    }

    public static String buildOrderBy(String sortKey, String sortType) {
        return buildOrderBy(sortKey, sortType, "");
    }

    public static String buildOrderBy(String sortKey, String sortType, String defaultOrderBy) {
        if (StringUtil.isBlank(sortKey)) {
            return defaultOrderBy;
        }

        sortKey = sortKey.trim();
        if (StringUtil.isBlank(sortType)) {
            return sortKey;
        }

        sortType = sortType.toLowerCase().trim();
        if (!"asc".equals(sortType) && !"desc".equals(sortType)) {
            throw new IllegalArgumentException("sortType only support asc or desc");
        }

        com.mybatisflex.core.util.SqlUtil.keepOrderBySqlSafely(sortKey);

        return sortKey + " " + sortType;
    }

    /**
     * 将 bean字段 转换为 数据库字段
     *
     * @param beanProperty 字段
     * @param clazz     类型
     * @return 数据库字段名
     */

    public static String getColumnByProperty(String beanProperty, Class<?> clazz) {
        ArgumentAssert.notNull(clazz, "实体类不能为空");
        ArgumentAssert.notEmpty(beanProperty, "字段名不能为空");
        TableInfo tableInfo = TableInfoFactory.ofEntityClass(clazz);

        String column = tableInfo.getColumnByProperty(beanProperty);

        if (ArrayUtil.contains(tableInfo.getColumns(), column)) {
            throw BizException.wrap("实体类{} 中没有字段：{}， 排序请传递实体类的字段名，而非数据库字段名", clazz.getSimpleName(), beanProperty);
        }
        return column;
    }
}
