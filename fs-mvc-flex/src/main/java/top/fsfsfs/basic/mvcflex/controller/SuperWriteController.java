package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.util.TypeUtil;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.service.SuperService;

import java.io.Serializable;


/**
 * SuperReadController
 * <p>
 * 继承该类，就拥有了如下方法：
 * 1，save 保存，并支持子类扩展方法：handlerSave
 * 2，update 修改，并支持子类扩展方法：handlerUpdate
 * 3，delete 删除，并支持子类扩展方法：handlerDelete
 * <p>
 * 若重写扩展方法无法满足，则可以重写page、save等方法，但切记不要修改 @RequestMapping 参数
 *
 * @param <S>      Service
 * @param <Id>     主键
 * @param <Entity> 实体
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperWriteController<S extends SuperService<Entity>, Id extends Serializable, Entity extends BaseEntity<Id>, VO>
        extends SuperSimpleController<S, Entity>
        implements SaveController<Id, Entity, VO>,
        UpdateController<Id, Entity, VO>,
        DeleteController<Id, Entity> {
    protected Class<Entity> entityClass = (Class<Entity>) TypeUtil.getTypeArgument(this.getClass(), 2);

    @Override
    public Class<Entity> getEntityClass() {
        return this.entityClass;
    }
}
