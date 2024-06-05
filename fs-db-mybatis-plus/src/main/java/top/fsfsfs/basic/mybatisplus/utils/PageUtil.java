package top.fsfsfs.basic.mybatisplus.utils;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.fsfsfs.basic.utils.BeanPlusUtil;

import java.util.List;

/**
 * Bean增强类工具类
 *
 * <p>
 * 把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。
 * </p>
 *
 * @author tangyh
 * @since 3.1.2
 */
public class PageUtil extends BeanPlusUtil {


    /**
     * 转化Page 对象
     *
     * @param page             分页对象
     * @param destinationClass 目标类型
     * @return 目录分页对象
     */
    public static <T, E> IPage<T> toBeanPage(IPage<E> page, Class<T> destinationClass) {
        if (page == null || destinationClass == null) {
            return null;
        }
        IPage<T> newPage = new Page<>(page.getCurrent(), page.getSize());
        newPage.setPages(page.getPages());
        newPage.setTotal(page.getTotal());

        List<E> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return newPage;
        }

        List<T> destinationList = toBeanList(list, destinationClass);

        newPage.setRecords(destinationList);
        return newPage;
    }

}
