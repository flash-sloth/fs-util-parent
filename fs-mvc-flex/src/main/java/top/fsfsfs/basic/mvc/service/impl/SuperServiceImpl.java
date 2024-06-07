package top.fsfsfs.basic.mvc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import top.fsfsfs.basic.base.entity.SuperEntity;
import top.fsfsfs.basic.mvc.mapper.SuperMapper;
import top.fsfsfs.basic.mvc.service.SuperService;
import top.fsfsfs.util.utils.ArgumentAssert;
import top.fsfsfs.util.utils.BeanPlusUtil;

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
 * @param <Id>     ID
 * @param <Entity> 实体
 * @author tangyh
 * @date 2020年02月27日18:15:17
 */
public class SuperServiceImpl<M extends SuperMapper<Entity>, Id extends Serializable, Entity extends SuperEntity<?>>
        extends ServiceImpl<M, Entity> implements SuperService<Id, Entity> {


    @Override
    public Class<Entity> getEntityClass() {
        return null;
    }

    @Override
    public Class<Id> getIdClass() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Entity copy(Id id) {
        Entity old = getById(id);
        ArgumentAssert.notNull(old, "您要复制的数据不存在或已被删除，请刷新重试");
        Entity entity = BeanPlusUtil.toBean(old, getEntityClass());
        entity.setId(null);
        save(entity);
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
