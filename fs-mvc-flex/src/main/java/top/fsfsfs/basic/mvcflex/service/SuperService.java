package top.fsfsfs.basic.mvcflex.service;

import com.mybatisflex.core.service.IService;

import java.io.Serializable;

/**
 * 业务层
 *
 * @param <Entity> 实体
 * @author tangyh
 * @since 2020年03月03日20:49:03
 */
public interface SuperService<Entity> extends IService<Entity> {

    /**
     * 获取实体的类型
     *
     * @return 实体类class类型
     */
    Class<Entity> getEntityClass();


    /**
     * 复制
     *
     * @param id 主键
     * @return 实体
     */
    Entity copy(Serializable id);

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     * @return 是否插入成功
     */
    <VO> Entity saveVo(VO entity);


    /**
     * 根据 ID 修改实体中非空的字段
     *
     * @param entity 实体对象
     * @return 是否修改成功
     */
    <VO> Entity updateVoById(VO entity);

}
