package top.fsfsfs.util.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import top.fsfsfs.basic.utils.StrPool;

/**
 * list列表 转换成tree列表
 * Created by Ace on 2017/6/12.
 *
 * @author tangyh
 */
public final class FsTreeUtil extends TreeUtil {
    /**
     * 默认的树节点 分隔符
     */
    public static final String TREE_SPLIT = StrPool.SLASH;
    /**
     * 默认的父id
     */
    public static final Long DEF_PARENT_ID = 0L;
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
        return id == null || DEF_PARENT_ID.equals(id);
    }


    public static Long getTopNodeId(String treePath) {
        String[] pathIds = StrUtil.splitToArray(treePath, TREE_SPLIT);
        if (ArrayUtil.isNotEmpty(pathIds)) {
            return Convert.toLong(pathIds[TOP_LEVEL]);
        }
        return null;
    }
}
