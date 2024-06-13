package top.fsfsfs.basic.mybatisflex.logicdelete;

import cn.hutool.core.util.ArrayUtil;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.logicdelete.impl.TimeStampLogicDeleteProcessor;
import com.mybatisflex.core.table.TableInfo;
import lombok.RequiredArgsConstructor;
import top.fsfsfs.basic.base.entity.SuperEntity;
import top.fsfsfs.basic.utils.ContextUtil;

import static com.mybatisflex.core.constant.SqlConsts.EQUALS;


/**
 * @since 2024年06月13日11:44:31
 * @author tangyh
 */
@RequiredArgsConstructor
public class TimeStampDelByLogicDeleteProcessor extends TimeStampLogicDeleteProcessor {
    private final String delByColumn;
    @Override
    public String buildLogicDeletedSet(String logicColumn, TableInfo tableInfo, IDialect dialect) {
        String sql = dialect.wrap(logicColumn) + EQUALS + getLogicDeletedValue();

        if (ContextUtil.getUserId() != null) {
            // 实体类中存在 delBy 字段，才记录删除人ID
            if (ArrayUtil.contains(tableInfo.getColumns(), delByColumn)) {
                sql += "," + dialect.wrap(delByColumn) + EQUALS + ContextUtil.getUserId();
            }
        }

        return sql;
    }

}
