package top.fsfsfs.basic.mvcflex.controller;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.fsfsfs.basic.annotation.log.WebLog;
import top.fsfsfs.basic.base.R;
import top.fsfsfs.basic.base.entity.BaseEntity;
import top.fsfsfs.basic.interfaces.echo.EchoService;
import top.fsfsfs.basic.mvcflex.utils.ControllerUtil;
import top.fsfsfs.util.utils.BeanPlusUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 查询Controller
 *
 * @param <Entity>    实体
 * @param <Id>        主键
 * @param <Query> 分页参数
 * @param <VO>  实体返回VO
 * @author tangyh
 * @since 2020年03月07日22:06:35
 */
public interface QueryController<Id extends Serializable, Entity extends BaseEntity<Id>, Query, VO>
        extends PageController<Entity, Query, VO> {

    /**
     * 单体查询
     *
     * @param id 主键id
     * @return 查询结果
     */
    @Parameters({
            @Parameter(name = "id", description = "主键", schema = @Schema(type = "long"), in = ParameterIn.PATH),
    })
    @Operation(summary = "单体查询", description = "单体查询")
    @GetMapping("/{id}")
    @WebLog("'单体查询:' + #id")
    default R<VO> get(@PathVariable Id id) {
        Entity entity = getSuperService().getById(id);
        return success(BeanPlusUtil.toBean(entity, getVoClass()));
    }

    /**
     * 查询详情
     *
     * @param id 主键id
     * @return 查询结果
     */
    @Operation(summary = "查询单体详情")
    @GetMapping("/detail")
    @WebLog("'查询单体详情:' + #id")
    default R<VO> getDetail(@RequestParam("id") Id id) {
        Entity entity = getSuperService().getById(id);
        VO vo = BeanPlusUtil.toBean(entity, getVoClass());
        EchoService echoService = getEchoService();
        if (echoService != null) {
            echoService.action(vo);
        }
        return success(vo);
    }

    /**
     * 批量查询
     *
     * @param data 批量查询
     * @return 查询结果
     */
    @Operation(summary = "批量查询", description = "批量查询")
    @PostMapping("/list")
    @WebLog("批量查询")
    default R<List<VO>> list(@RequestBody Query data) {
        Entity entity = BeanPlusUtil.toBean(data, getEntityClass());
        QueryWrapper wrapper = QueryWrapper.create(entity, ControllerUtil.buildOperators(entity.getClass()));
        List<Entity> list = getSuperService().list(wrapper);
        return success(BeanPlusUtil.toBeanList(list, getVoClass()));
    }


    /**
     * 批量查询
     *
     * @param ids 批量查询
     * @return 查询结果
     */
    @Operation(summary = "根据Id批量查询", description = "根据Id批量查询")
    @PostMapping("/listByIds")
    @WebLog("根据Id批量查询")
    default R<List<VO>> listByIds(@RequestBody List<Id> ids) {
        if (CollUtil.isEmpty(ids)) {
            return R.success(Collections.emptyList());
        }
        List<Entity> list = getSuperService().listByIds(ids);
        return success(BeanPlusUtil.toBeanList(list, getVoClass()));
    }

}
