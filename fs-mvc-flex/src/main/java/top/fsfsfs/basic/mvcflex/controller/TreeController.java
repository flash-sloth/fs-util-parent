package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.mvcflex.utils.ControllerUtil;
import top.fsfsfs.util.utils.BeanPlusUtil;
import top.fsfsfs.util.utils.FsTreeUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 新增
 *
 * @param <Entity> 实体
 * @param <Query> 参数
 * @param <VO> 返回值
 * @author tangyh
 * @since 2020年03月07日22:07:31
 */
public interface TreeController<Id extends Serializable, Entity extends BaseEntity<Id>, Query, VO extends TreeNode<Id>>
        extends PageController<Entity, Query, VO> {
    /**
     * 按树结构查询
     *
     * @param pageQuery 查询参数
     * @return
     */
    @Operation(summary = "按树结构查询")
    @PostMapping("/tree")
    @WebLog("按树结构查询")
    default R<List<Tree<Id>>> tree(@RequestBody Query pageQuery) {
        Entity entity = BeanPlusUtil.toBean(pageQuery, getEntityClass());
        QueryWrapper wrapper = QueryWrapper.create(entity, ControllerUtil.buildOperators(entity.getClass()));

        List<Entity> list = getSuperService().list(wrapper);

        List<VO> treeList = BeanUtil.copyToList(list, getVoClass());
        return success(FsTreeUtil.build(treeList, (Id) FsTreeUtil.DEF_PARENT_ID));
    }
}
