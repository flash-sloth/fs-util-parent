package top.fsfsfs.basic.mvcflex.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * 修改Controller
 *
 * @param <Id>   id
 * @param <Entity>   实体
 * @param <DTO> 修改参数
 * @author tangyh
 * @since 2020年03月07日22:30:37
 */
public interface UpdateController<Id extends Serializable, Entity extends BaseEntity<Id>, DTO>
        extends BaseController<Entity> {

    /**
     * 修改
     *
     * @param updateDto 修改对象
     * @return 修改后的实体数据
     */
    @Operation(summary = "修改", description = "修改DTO中不为空的字段")
    @PutMapping
    @WebLog(value = "'修改:' + #updateVO?.id", request = false)
    default R<Id> update(@RequestBody @Validated(BaseEntity.Update.class) DTO updateDto) {
        R<Id> result = handlerUpdate(updateDto);
        if (result.getDefExec()) {
            Entity entity = getSuperService().updateDtoById(updateDto);
            return R.success(entity.getId());
        }
        return result;
    }

    /**
     * 自定义更新
     *
     * @param updateVO 修改VO
     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
     */
    default R<Id> handlerUpdate(DTO updateVO) {
        return R.successDef();
    }
}
