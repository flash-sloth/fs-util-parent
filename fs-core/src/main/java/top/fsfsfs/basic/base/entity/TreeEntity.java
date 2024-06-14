package top.fsfsfs.basic.base.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * 包括id、created_time、created_by、updated_by、updated_time、label、parent_id、sort_value 字段的表继承的树形实体
 *
 * @author tangyh
 * @since 2019/05/05
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class TreeEntity<T> extends SuperEntity<T> {

    /**
     * 父ID
     */
    @Schema(description = "父ID")
    protected T parentId;

    /**
     * 排序
     */
    @Schema(description = "排序号")
    private Integer weight;

}
