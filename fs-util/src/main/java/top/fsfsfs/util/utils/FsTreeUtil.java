package top.fsfsfs.util.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import top.fsfsfs.basic.utils.StrPool;

import java.util.List;
import java.util.Map;

/**
 * list列表 转换成tree列表
 * Created by Ace on 2017/6/12.
 *
 * @author tangyh
 */
public final class FsTreeUtil {
    /**
     * 默认的树节点 分隔符
     */
    public static final String TREE_SPLIT = StrPool.SLASH;

    /**
     * 默认的父id
     */
    public static final Long DEF_PARENT_ID = null;
    /**
     * 默认树层级
     */
    public static final Integer TREE_GRADE = 0;
    private static final int TOP_LEVEL = 1;

    private FsTreeUtil() {
    }

    public static String getTreePath(String parentTreePath, Long parentId) {
        return parentTreePath + parentId + TREE_SPLIT;
    }

    public static String buildTreePath(Long id) {
        return TREE_SPLIT + id + TREE_SPLIT;
    }

    /**
     * 判断id是否为根节点
     *
     * @param id
     * @return
     */
    public static boolean isRoot(Long id) {
        return id == null;
    }


    public static Long getTopNodeId(String treePath) {
        String[] pathIds = StrUtil.splitToArray(treePath, TREE_SPLIT);
        if (ArrayUtil.isNotEmpty(pathIds)) {
            return Convert.toLong(pathIds[TOP_LEVEL]);
        }
        return null;
    }

    /**
     * 构建 根节点存储null，节点ID类型为Long 的树
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param list           源数据集合; 必须继承TreeNode<Long>
     * @return List
     */
    public static <T extends TreeNode<Long>> List<Tree<Long>> build(List<T> list) {
        return build(list, TreeNodeConfig.DEFAULT_CONFIG);
    }

    /**
     * 构建 根节点存储null，节点ID类型为Long 的树
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param list           源数据集合; 必须继承TreeNode<Long>
     * @param treeNodeConfig 配置
     * @return List
     */
    public static <T extends TreeNode<Long>> List<Tree<Long>> build(List<T> list, TreeNodeConfig treeNodeConfig) {
        return build(list, DEF_PARENT_ID, treeNodeConfig);
    }

    /**
     * 构建  根节点存储 <code>rootId</code>，节点ID类型为Long 的树
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param <E>            ID类型
     * @param list           源数据集合; 必须继承TreeNode<E>
     * @param rootId         最顶层父id值 一般为 0 或 null 之类
     * @return List
     */
    public static <T extends TreeNode<E>, E> List<Tree<E>> build(List<T> list, E rootId) {
        return build(list, rootId, TreeNodeConfig.DEFAULT_CONFIG);
    }

    /**
     * 构建  根节点存储 <code>rootId</code>，节点ID类型为Long 的树
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param <E>            ID类型
     * @param list           源数据集合; 必须继承TreeNode<E>
     * @param rootId         最顶层父id值 一般为 0 或 null 之类
     * @param treeNodeConfig 配置
     * @return List
     */
    public static <T extends TreeNode<E>, E> List<Tree<E>> build(List<T> list, E rootId, TreeNodeConfig treeNodeConfig) {
        return TreeUtil.build(list, rootId, treeNodeConfig, new FsNodeParser<>());
    }


    /**
     * 构建  根节点存储 <code>rootId</code>，节点ID类型为Long 的树
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param list           源数据集合; 必须继承TreeNode<E>
     * @param nodeParser  解析器
     * @return List
     */
    public static <T extends TreeNode<Long>> List<Tree<Long>> build(List<T> list, NodeParser<T, Long> nodeParser) {
        return TreeUtil.build(list, DEF_PARENT_ID, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
    }

    public static class FsNodeParser<E, T extends TreeNode<E>> implements NodeParser<T, E> {
        @Override
        public void parse(T treeNode, Tree<E> tree) {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());

            //扩展字段
            final Map<String, Object> extra = treeNode.getExtra();
            if (MapUtil.isNotEmpty(extra)) {
                extra.forEach(tree::putExtra);
            }
        }
    }
}
