package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.lang.tree.TreeNode;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.request.PageParams;
import top.fsfsfs.basic.mvcflex.service.SuperService;
import top.fsfsfs.basic.mybatisflex.utils.BeanPageUtil;

import java.io.Serializable;

/**
 * SuperTreeController
 * <p>
 *
 * @author tangyh
 * @since 2020年03月06日11:06:46
 */
public abstract class SuperTreeController<S extends SuperService<Entity>,
        Id extends Serializable, Entity extends BaseEntity<Id>, DTO, Query, VO extends TreeNode<Id>>
        extends SuperController<S, Id, Entity, DTO, Query, VO>
        implements TreeController<Id, Entity, Query, VO> {

    @Override
    public Page<VO> query(PageParams<Query> params) {
        // 处理查询参数，如：覆盖前端传递的 current、size、sort 等参数 以及 model 中的参数 【提供给子类重写】【无默认实现】
        handlerParams(params);

        // 构建分页参数(current、size)和排序字段等
        Page<Entity> page = Page.of(params.getCurrent(), params.getSize());

        // 根据前端传递的参数，构建查询条件【提供给子类重写】【有默认实现】
        QueryWrapper wrapper = handlerWrapper(params);

        // 执行单表分页查询
        getSuperService().page(page, wrapper);

        Page<VO> voPage = BeanPageUtil.toBeanPage(page, getVoClass());

        // 处理查询后的分页结果， 如：调用EchoService回显字典、关联表数据等 【提供给子类重写】【有默认实现】
        handlerResult(voPage);
        return voPage;
    }

}
