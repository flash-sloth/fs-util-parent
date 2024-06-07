package top.fsfsfs.basic.mvcflex.service;

import org.springframework.lang.NonNull;
import top.fsfsfs.basic.model.cache.CacheKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * 基于MP的 IService 新增了3个方法： getByIdCache
 * 其中：
 * 1，getByIdCache 方法 会先从缓存查询，后从DB查询 （取决于实现类）
 * 2、SuperService 上的方法
 *
 * @param <Entity> 实体
 * @author tangyh
 * @since 2020年03月03日20:49:03
 */
public interface SuperCacheService<Entity> extends SuperService<Entity> {

    /**
     * 根据id 先查缓存，再查db
     *
     * @param id 主键
     * @return 对象
     */
    Entity getByIdCache(Serializable id);

    /**
     * 根据 key 查询缓存中存放的id，缓存不存在根据loader加载并写入数据，然后根据查询出来的id查询 实体
     *
     * @param key    缓存key
     * @param loader 加载器
     * @return 对象
     */
    Entity getByKey(CacheKey key, Function<CacheKey, Object> loader);

    /**
     * 可能会缓存穿透
     *
     * @param ids    主键id
     * @param loader 回调
     * @return 对象集合
     */
    List<Entity> findByIds(@NonNull Collection<? extends Serializable> ids, Function<Collection<? extends Serializable>, Collection<Entity>> loader);

    /**
     * 根据id 批量查询缓存
     *
     * @param keyIdList    id集合
     * @param cacheBuilder 缓存key构造器
     * @param loader       加载数据的回调方法
     * @param <E>          查询的对象
     * @return
     */
    <E> Set<E> findCollectByIds(List<? extends Serializable> keyIdList, Function<Serializable, CacheKey> cacheBuilder, Function<Serializable, List<E>> loader);

    /**
     * 刷新缓存
     *
     * @param ids 主键
     */
    <Id> void refreshCache(List<Id> ids);

    /**
     * 清理缓存
     *
     * @param ids 主键
     */
    <Id> void clearCache(List<Id> ids);


    /**
     * 清理缓存
     *
     * @param ids
     */
    default void delCache(Serializable... ids) {
        delCache(Arrays.asList(ids));
    }

    /**
     * 清理缓存
     *
     * @param idList
     */
    void delCache(Collection<? extends Serializable> idList);

    /**
     * 清理缓存
     *
     * @param model
     */
    void delCache(Entity model);

    /**
     * 设置缓存
     *
     * @param model
     */
    void setCache(Entity model);
}
