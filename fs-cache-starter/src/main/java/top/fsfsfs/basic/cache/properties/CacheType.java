package top.fsfsfs.basic.cache.properties;

/**
 * 缓存类型
 *
 * @author tangyh
 * @since 2020/9/22 3:34 下午
 */
public enum CacheType {
    /**
     * redis
     */
    REDIS,
    ;

    public boolean eq(CacheType cacheType) {
        return cacheType != null && this.name().equals(cacheType.name());
    }
}
