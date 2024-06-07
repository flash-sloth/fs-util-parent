package top.fsfsfs.basic.mvcflex.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import top.fsfsfs.basic.base.entity.SuperEntity;
import top.fsfsfs.basic.mvcflex.mapper.SuperMapper;
import top.fsfsfs.basic.mvcflex.service.SuperService;
import top.fsfsfs.util.utils.ArgumentAssert;

import java.io.Serializable;

/**
 * 不含缓存的Service实现
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db
 * 3，removeByIds：重写 ServiceImpl 类的方法，删除db
 * 4，updateAllById： 新增的方法： 修改数据（所有字段）
 * 5，updateById：重写 ServiceImpl 类的方法，修改db后
 *
 * @param <M>      Manager
 * @param <Entity> 实体
 * @author tangyh
 * @since 2020年02月27日18:15:17
 */
public class SuperServiceImpl<M extends SuperMapper<Entity>, Entity>
        extends ServiceImpl<M, Entity> implements SuperService<Entity> {


    @Override
    public Class<Entity> getEntityClass() {
        return (Class<Entity>) TypeUtil.getTypeArgument(this.getClass(), 1).getClass();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Entity copy(Serializable id) {
        Entity old = getById(id);
        ArgumentAssert.notNull(old, "您要复制的数据不存在或已被删除，请刷新重试");
        Entity entity = BeanUtil.toBean(old, getEntityClass());
        if (entity instanceof SuperEntity<?> superEntity) {
            superEntity.setId(null);
            save((Entity) superEntity);
        } else {
            save(entity);
        }
        return entity;
    }


    /**
     * 保存之前处理参数等操作
     *
     * @param saveVO 保存VO
     */
    protected <SaveVO> Entity saveBefore(SaveVO saveVO) {
        return BeanUtil.toBean(saveVO, getEntityClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <SaveVO> Entity saveVo(SaveVO saveVO) {
        Entity entity = saveBefore(saveVO);
        save(entity);
        saveAfter(saveVO, entity);
        return entity;
    }

    /**
     * 保存之后设置参数值，淘汰缓存等操作
     *
     * @param saveVO 保存VO
     * @param entity 实体
     */
    protected <SaveVO> void saveAfter(SaveVO saveVO, Entity entity) {
    }

    /**
     * 修改之前处理参数等操作
     *
     * @param updateVO 修改VO
     */
    protected <UpdateVO> Entity updateBefore(UpdateVO updateVO) {
        return BeanUtil.toBean(updateVO, getEntityClass());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public <UpdateVO> Entity updateVoById(UpdateVO updateVO) {
        Entity entity = updateBefore(updateVO);
        updateById(entity);
        updateAfter(updateVO, entity);
        return entity;
    }

    /**
     * 修改之后设置参数值，淘汰缓存等操作
     *
     * @param updateVO 修改VO
     * @param entity   实体
     */
    protected <UpdateVO> void updateAfter(UpdateVO updateVO, Entity entity) {
    }
}
