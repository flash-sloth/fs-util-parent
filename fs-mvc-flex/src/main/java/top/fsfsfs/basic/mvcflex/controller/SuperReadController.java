package top.fsfsfs.basic.mvcflex.controller;


import cn.hutool.core.util.TypeUtil;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.service.SuperService;

import java.io.Serializable;


/**
 * SuperReadController
 * <p>
 * 继承该类，就拥有了如下方法：
 * 1，page 分页查询，并支持子类扩展4个方法：handlerQueryParams、query、handlerWrapper、handlerResult
 * 2，get 单体查询， 根据ID直接查询DB
 * 3，detail 单体详情查询， 根据ID直接查询DB
 * 4，list 列表查询，根据参数条件，查询列表
 * <p>
 * 若重写扩展方法无法满足，则可以重写page、save等方法，但切记不要修改 @RequestMapping 参数
 *
 * @param <S>         Service
 * @param <Id>        主键
 * @param <Entity>    实体
 * @param <Query> 分页参数
 * @param <VO>  实体返回VO
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperReadController<S extends SuperService<Entity>, Id extends Serializable, Entity extends BaseEntity<Id>, Query, VO>
        extends SuperSimpleController<S, Entity>
        implements QueryController<Id, Entity, Query, VO> {

    protected Class<VO> voClass = (Class<VO>) TypeUtil.getTypeArgument(this.getClass(), 4);

    @Override
    public Class<VO> getVoClass() {
        return this.voClass;
    }

}
