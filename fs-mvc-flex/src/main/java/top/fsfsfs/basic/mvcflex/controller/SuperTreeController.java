package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.lang.tree.TreeNode;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.service.SuperCacheService;

import java.io.Serializable;

/**
 * SuperCacheController
 * <p>
 * 继承该类，在SuperController类的基础上扩展了以下方法：
 * 1，get ： 根据ID查询缓存，若缓存不存在，则查询DB
 *
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperTreeController<S extends SuperCacheService<Entity>,
        Id extends Serializable, Entity extends BaseEntity<Id>, VO, QueryVO, ResultVO extends TreeNode<Id>>
        extends SuperController<S, Id, Entity, VO, QueryVO, ResultVO>
        implements TreeController<Id, Entity, QueryVO, ResultVO> {

}
