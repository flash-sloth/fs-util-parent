package top.fsfsfs.basic.mvc.service;

import com.mybatisflex.core.service.IService;
import top.fsfsfs.basic.base.entity.SuperEntity;

import java.io.Serializable;

/**
 * 业务层
 *
 * @param <Id>     ID
 * @param <Entity> 实体
 * @author tangyh
 * @date 2020年03月03日20:49:03
 */
public interface SuperService<Id extends Serializable, Entity extends SuperEntity<?>> extends IService<Entity> {

    /**
     * 获取实体的类型
     *
     * @return 实体类class类型
     */
    Class<Entity> getEntityClass();

    /**
     * 获取主键的类型
     *
     * @return 主键class类型
     */
    Class<Id> getIdClass();


    /**
     * 复制
     * @param id 主键
     */
    Entity copy(Id id);

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     * @return 是否插入成功
     */
    <SaveVO> Entity saveVo(SaveVO entity);


    /**
     * 根据 ID 修改实体中非空的字段
     *
     * @param entity 实体对象
     * @return 是否修改成功
     */
    <UpdateVO> Entity updateVoById(UpdateVO entity);

}
