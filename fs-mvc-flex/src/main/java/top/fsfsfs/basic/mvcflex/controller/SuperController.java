package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.util.TypeUtil;
import top.fsfsfs.basic.mvcflex.service.SuperService;

import java.io.Serializable;

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
 * @param <SaveVO> 保存方法入参VO
 * @param <UpdateVO> 修改方法入参VO
 * @param <PageQuery> 查询方法入参VO
 * @param <ResultVO> 查询方法出参VO
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperController<S extends SuperService<Entity>, Id extends Serializable, Entity, SaveVO, UpdateVO, PageQuery, ResultVO>
        extends SuperSimpleController<S, Entity>
        implements SaveController<Id, Entity, SaveVO>,
        UpdateController<Entity, UpdateVO>,
        DeleteController<Id, Entity>,
        QueryController<Id, Entity, PageQuery, ResultVO> {
    protected Class<ResultVO> resultVoClass = (Class<ResultVO>) TypeUtil.getTypeArgument(this.getClass(), 6).getClass();

    @Override
    public Class<ResultVO> getResultVoClass() {
        return resultVoClass;
    }
}
