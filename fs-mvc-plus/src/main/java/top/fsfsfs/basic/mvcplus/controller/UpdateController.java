package top.fsfsfs.basic.mvcplus.controller;

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
 * @param <Entity>   实体
 * @param <UpdateVO> 修改参数
 * @author tangyh
 * @since 2020年03月07日22:30:37
 */
public interface UpdateController<Id extends Serializable, Entity extends BaseEntity<Id>, UpdateVO>
        extends BaseController<Id, Entity> {

    /**
     * 修改
     *
     * @param updateVO 修改VO
     * @return 修改后的实体数据
     */
    @Operation(summary = "修改", description = "修改UpdateVO中不为空的字段")
    @PutMapping
    @WebLog(value = "'修改:' + #updateVO?.id", request = false)
    default R<Entity> update(@RequestBody @Validated(BaseEntity.Update.class) UpdateVO updateVO) {
        R<Entity> result = handlerUpdate(updateVO);
        if (result.getDefExec()) {
            return R.success(getSuperService().updateById(updateVO));
        }
        return result;
    }

    /**
     * 自定义更新
     *
     * @param updateVO 修改VO
     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
     */
    default R<Entity> handlerUpdate(UpdateVO updateVO) {
        return R.successDef();
    }
}
