package top.fsfsfs.basic.mvc.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.SuperEntity;
import top.fsfsfs.basic.interfaces.echo.EchoService;
import top.fsfsfs.basic.mvc.request.PageFlexUtil;
import top.fsfsfs.basic.mvc.request.PageParams;

import java.io.Serializable;

/**
 * 分页控制器
 *
 * @param <Entity>    实体
 * @param <PageQuery> 分页参数
 * @author tangyh
 * @date 2020年03月07日22:06:35
 */
public interface PageController<Id extends Serializable, Entity extends SuperEntity<Id>, PageQuery, ResultVO>
        extends BaseController<Id, Entity> {

    /**
     * 获取返回VO的类型
     *
     * @return 实体的类型
     */
    Class<ResultVO> getResultVOClass();

    /**
     * 处理查询参数
     *
     * @param params 前端传递的参数
     * @author tangyh
     * @date 2021/7/3 3:25 下午
     * @create [2021/7/3 3:25 下午 ] [tangyh] [初始创建]
     */
    default void handlerQueryParams(PageParams<PageQuery> params) {
    }

    /**
     * 执行分页查询
     * <p>
     * 子类可以覆盖后重写查询逻辑
     *
     * @param params 分页参数
     * @return 分页信息
     */
    default Page<Entity> query(PageParams<PageQuery> params) {
        // 处理查询参数，如：覆盖前端传递的 current、size、sort 等参数 以及 model 中的参数 【提供给子类重写】【无默认实现】
        handlerQueryParams(params);

        // 构建分页参数(current、size)和排序字段等
        Page<Entity> page = params.buildPage(getEntityClass());
        Entity model = BeanUtil.toBean(params.getModel(), getEntityClass());

        // 根据前端传递的参数，构建查询条件【提供给子类重写】【有默认实现】
        QueryWrapper wrapper = handlerWrapper(model, params);

        // 执行单表分页查询
        getSuperService().page(page, wrapper);

        return page;
    }

    /**
     * 处理对象中的非空参数和扩展字段中的区间参数，可以覆盖后处理组装查询条件
     *
     * @param model  实体类
     * @param params 分页参数
     * @return 查询构造器
     */
    default QueryWrapper handlerWrapper(Entity model, PageParams<PageQuery> params) {
        QueryWrapper wrapper = QueryWrapper.create(model);
        return wrapper;
    }

    /**
     * 获取echo Service
     *
     * @return 回显服务
     */
    default EchoService getEchoService() {
        return null;
    }

    /**
     * 处理查询后的数据
     * <p>
     * 如：执行@Echo回显
     *
     * @param page 分页对象
     */
    default void handlerResult(Page<ResultVO> page) {
        EchoService echoService = getEchoService();
        if (echoService != null) {
            echoService.action(page);
        }
    }

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return 分页数据s
     */
    @Operation(summary = "分页列表查询")
    @PostMapping(value = "/page")
    @WebLog(value = "'分页列表查询:第' + #params?.current + '页, 显示' + #params?.size + '行'", response = false)
    default R<Page<ResultVO>> page(@RequestBody @Validated PageParams<PageQuery> params) {
        Page<Entity> page = query(params);
        Page<ResultVO> voPage = PageFlexUtil.toBeanPage(page, getResultVOClass());
        // 处理查询后的分页结果， 如：调用EchoService回显字典、关联表数据等 【提供给子类重写】【有默认实现】
        handlerResult(voPage);
        return success(voPage);
    }
}
