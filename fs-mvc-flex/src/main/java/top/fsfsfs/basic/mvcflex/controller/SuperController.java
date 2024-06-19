package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.util.TypeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.service.SuperService;
import top.fsfsfs.util.utils.BeanPlusUtil;

import java.io.Serializable;
import java.util.List;

/**
 * SuperNoPoiController
 * <p>
 * 继承该类，就拥有了如下方法：
 * 1，page 分页查询，并支持子类扩展4个方法：handlerQueryParams、query、handlerWrapper、handlerResult
 * 2，save 保存，并支持子类扩展方法：handlerSave
 * 3，update 修改，并支持子类扩展方法：handlerUpdate
 * 4，delete 删除，并支持子类扩展方法：handlerDelete
 * 5，get 单体查询， 根据ID直接查询DB
 * 6，detail 单体详情查询， 根据ID直接查询DB
 * 7，list 列表查询，根据参数条件，查询列表
 * <p>
 * 若重写扩展方法无法满足，则可以重写page、save等方法，但切记不要修改 @RequestMapping 参数
 *
 * @param <S>      Service
 * @param <Id>     主键
 * @param <Entity> 实体
 * @param <DTO> 保存和修改方法入参VO
 * @param <Query> 查询方法入参VO
 * @param <VO> 查询方法出参VO
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperController<S extends SuperService<Entity>, Id extends Serializable, Entity extends BaseEntity<Id>, DTO, Query, VO>
        extends SuperSimpleController<S, Entity>
        implements SaveController<Id, Entity, DTO>,
        UpdateController<Id, Entity, DTO>,
        DeleteController<Id, Entity>,
        QueryController<Id, Entity, Query, VO> {
    protected Class<VO> resultVoClass = (Class<VO>) TypeUtil.getTypeArgument(this.getClass(), 5);

    @Override
    public Class<VO> getResultVoClass() {
        return resultVoClass;
    }

    protected Class<Entity> entityClass = (Class<Entity>) TypeUtil.getTypeArgument(this.getClass(), 2);

    @Override
    public Class<Entity> getEntityClass() {
        return this.entityClass;
    }


    /**
     * 单体查询
     *
     * @param id 主键id
     * @return 查询结果
     */
    @Parameters({
            @Parameter(name = "id", description = "主键", schema = @Schema(type = "long"), in = ParameterIn.PATH),
    })
    @Operation(summary = "单体查询(优先查缓存)", description = "单体查询(优先查缓存)")
    @GetMapping("/getByIdCache/{id}")
    @WebLog("'单体查询(优先查缓存):' + #id")
    public R<VO> getByIdCache(@PathVariable Id id) {
        Entity entity = getSuperService().getByIdCache(id);
        return success(BeanPlusUtil.toBean(entity, getResultVoClass()));
    }

    /**
     * 刷新缓存
     *
     * @return 是否成功
     */
    @Operation(summary = "刷新缓存", description = "刷新缓存")
    @PostMapping("refreshCache")
    @WebLog("'刷新缓存'")
    public R<Boolean> refreshCache(@RequestBody List<Id> ids) {
        getSuperService().refreshCache(ids);
        return success(true);
    }

    /**
     * 清理缓存
     *
     * @return 是否成功
     */
    @Operation(summary = "清理缓存", description = "清理缓存")
    @PostMapping("clearCache")
    @WebLog("'清理缓存'")
    public R<Boolean> clearCache(@RequestBody List<Id> ids) {
        getSuperService().clearCache(ids);
        return success(true);
    }

}
