package top.fsfsfs.basic.mvcplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcplus.service.SuperCacheService;
import top.fsfsfs.util.utils.BeanPlusUtil;

import java.io.Serializable;
import java.util.List;

/**
 * SuperCacheController
 * <p>
 * 继承该类，在SuperController类的基础上扩展了以下方法：
 * 1，get ： 根据ID查询缓存，若缓存不存在，则查询DB
 *
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperCacheController<S extends SuperCacheService<Id, Entity>,
        Id extends Serializable, Entity extends BaseEntity<Id>, SaveVO, UpdateVO, PageQuery, ResultVO>
        extends SuperController<S, Id, Entity, SaveVO, UpdateVO, PageQuery, ResultVO> {
    @Override
    public SuperCacheService<Id, Entity> getSuperService() {
        return superService;
    }

    /**
     * 查询
     *
     * @param id 主键id
     * @return 查询结果
     */
    @Override
    @WebLog("'查询:' + #id")
    public R<ResultVO> get(@PathVariable Id id) {
        Entity entity = getSuperService().getByIdCache(id);
        return success(BeanPlusUtil.toBean(entity, getResultVOClass()));
    }

    /**
     * 刷新缓存
     *
     * @return 是否成功
     */
    @Operation(summary = "刷新缓存", description = "刷新缓存")
    @PostMapping("refreshCache")
    @WebLog("'刷新缓存'")
    public R<Boolean> refreshCache(@RequestBody List<Long> ids) {
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
    public R<Boolean> clearCache(@RequestBody List<Long> ids) {
        getSuperService().clearCache(ids);
        return success(true);
    }
}
