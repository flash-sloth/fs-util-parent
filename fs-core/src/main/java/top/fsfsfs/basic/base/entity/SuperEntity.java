package top.fsfsfs.basic.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 包括id、created_time、created_by字段的表继承的基础实体
 *
 * @author tangyh
 * @since 2019/05/05
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
public class SuperEntity<T> implements Serializable {
    public static final String ID_FIELD = "id";
    /** 创建时间 实体字段 */
    public static final String CREATED_TIME = "createdTime";
    /** 创建时间 数据库字段 */
    public static final String CREATED_TIME_FIELD = "created_time";
    /** 创建人ID 实体字段 */
    public static final String CREATED_BY = "createdBy";
    /** 创建人ID 数据库字段 */
    public static final String CREATED_BY_FIELD = "created_by";
    /** 删除人 实体字段 */
    public static final String DEL_BY = "delBy";
    /** 删除人 数据库字段 */
    public static final String DEL_BY_FIELD = "del_by";
    /** 删除标记 实体字段 */
    public static final String DEL_FLAG = "delFlag";
    /** 删除标记 数据库字段 */
    public static final String DEL_FLAG_FIELD = "del_flag";
    /** 创建单位ID 实体字段 */
    public static final String CREATED_COMPANY_ID = "createdCompanyId";
    /** 创建单位ID 数据库字段 */
    public static final String CREATED_COMPANY_ID_FIELD = "created_company_id";
    /** 创建部门ID 实体字段 */
    public static final String CREATED_DEPT_ID = "createdDeptId";
    /** 创建部门ID 数据库字段 */
    public static final String CREATED_DEPT_ID_FIELD = "created_dept_id";

    @Serial
    private static final long serialVersionUID = -4603650115461757622L;

    @TableId(value = "id", type = IdType.INPUT)
    @Id(keyType = KeyType.Generator, value = "uid")
    @Schema(description = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    protected T id;

    @Schema(description = "创建时间")
    @TableField(value = CREATED_TIME_FIELD, fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    @Schema(description = "创建人ID")
    @TableField(value = CREATED_BY_FIELD, fill = FieldFill.INSERT)
    protected T createdBy;

    /**
     * 保存验证组
     */
    public interface Save extends Default {

    }

    /**
     * 更新验证组
     */
    public interface Update extends Default {

    }
}
