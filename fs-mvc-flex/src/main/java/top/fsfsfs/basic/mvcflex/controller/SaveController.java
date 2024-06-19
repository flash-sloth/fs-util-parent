package top.fsfsfs.basic.mvcflex.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * 新增
 *
 * @param <Entity> 实体
 * @param <DTO> 保存参数
 * @author tangyh
 * @since 2020年03月07日22:07:31
 */
public interface SaveController<Id extends Serializable, Entity extends BaseEntity<Id>, DTO>
        extends BaseController<Entity> {

    /**
     * 新增
     *
     * @param saveDto 保存参数
     * @return 实体
     */
    @Operation(summary = "新增")
    @PostMapping
    @WebLog(value = "新增", request = false)
    default R<Id> save(@RequestBody @Validated DTO saveDto) {
        R<Id> result = handlerSave(saveDto);
        if (result.getDefExec()) {
            Entity entity = getSuperService().saveDto(saveDto);
            return R.success(entity.getId());
        }
        return result;
    }

    /**
     * 复制
     *
     * @param id ID
     * @return 实体
     */
    @Operation(summary = "复制")
    @PostMapping("/copy")
    @WebLog(value = "复制", request = false)
    default R<Id> copy(@RequestParam("id") Id id) {
        Entity entity = getSuperService().copy(id);
        return R.success(entity.getId());
    }

    /**
     * 自定义新增
     *
     * @param model 保存对象
     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
     */
    default R<Id> handlerSave(DTO model) {
        return R.successDef();
    }

}
