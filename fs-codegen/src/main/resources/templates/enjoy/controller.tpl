#set(tableComment = table.getComment())
#set(primaryKeyType = table.getPrimaryKey().getPropertySimpleType())
#set(entityClassName = table.buildEntityClassName())
#set(dtoClassName = table.buildDtoClassName())
#set(voClassName = table.buildVoClassName())
#set(queryClassName = table.buildQueryClassName())
#set(entityVarName = firstCharToLowerCase(entityClassName))
#set(serviceVarName = firstCharToLowerCase(table.buildServiceClassName()))
package #(packageConfig.controllerPackage);

#for(importClass : table.buildControllerImports())
import #(importClass);
#end

/**
 * #(tableComment) 控制层。
 *
 * @author #(javadocConfig.getAuthor())
 * @since #(javadocConfig.getSince())
 */
#if(controllerConfig.restStyle)
@RestController
#else
@Controller
#end
@Validated
#if(swaggerVersion.getName() == "FOX")
@Api("#(tableComment)接口")
#end
#if(swaggerVersion.getName() == "DOC")
@Tag(name = "#(tableComment)接口")
#end
@RequestMapping("#(table.buildControllerRequestMappingPrefix())/#(firstCharToLowerCase(entityClassName))")
public class #(table.buildControllerClassName()) #if(controllerConfig.superClass)extends #(controllerConfig.buildSuperClassName(table)) #end {

    @Autowired
    private #(table.buildServiceClassName()) #(serviceVarName);

    /**
     * 添加#(tableComment)。
     *
     * @param dto #(tableComment)
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping
    #if(swaggerVersion.getName() == "FOX")
    @ApiOperation("新增")
    #end
    #if(swaggerVersion.getName() == "DOC")
    @Operation(summary="新增", description="保存#(tableComment)")
    #end
    public R<#(primaryKeyType)> save(@Validated @RequestBody #if(swaggerVersion.getName() == "FOX")@ApiParam("#(tableComment)") #end #if(swaggerVersion.getName() == "DOC")@Parameter(description="#(tableComment)") #end #(dtoClassName) dto) {
        return R.success(#(serviceVarName).saveDto(dto).getId());
    }

    /**
     * 根据主键删除#(tableComment)。
     *
     * @param ids 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping
    #if(swaggerVersion.getName() == "FOX")
    @ApiOperation("删除")
    #end
    #if(swaggerVersion.getName() == "DOC")
    @Operation(summary="删除", description="根据主键删除#(tableComment)")
    #end
    public R<Boolean> delete(@RequestBody List<#(primaryKeyType)> ids) {
        return R.success(#(serviceVarName).removeByIds(ids));
    }

    /**
     * 根据主键更新#(tableComment)。
     *
     * @param dto #(tableComment)
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping
    #if(swaggerVersion.getName() == "FOX")
    @ApiOperation("修改")
    #end
    #if(swaggerVersion.getName() == "DOC")
    @Operation(summary="修改", description="根据主键更新#(tableComment)")
    #end
    public R<#(primaryKeyType)> update(@Validated(BaseEntity.Update.class) @RequestBody #if(swaggerVersion.getName() == "FOX")@ApiParam("#(tableComment)主键") #end #if(swaggerVersion.getName() == "DOC")@Parameter(description="#(tableComment)主键")#end#(dtoClassName) dto) {
        return R.success(#(serviceVarName).updateDtoById(dto).getId());
    }

    /**
     * 根据#(tableComment)主键获取详细信息。
     *
     * @param id #(tableComment)主键
     * @return #(tableComment)详情
     */
    @GetMapping("/{id}")
    #if(swaggerVersion.getName() == "FOX")
    @ApiOperation("单体查询")
    #end
    #if(swaggerVersion.getName() == "DOC")
    @Operation(summary="单体查询", description="根据主键获取#(tableComment)")
    #end
    public R<#(voClassName)> get(@PathVariable #if(swaggerVersion.getName() == "FOX")@ApiParam("#(tableComment)主键") #if(swaggerVersion.getName() == "DOC")@Parameter(description="#(tableComment)主键")#end#end #(primaryKeyType) id) {
        #(entityClassName) entity = #(serviceVarName).getById(id);
        return R.success(BeanUtil.toBean(entity, #(voClassName).class));
    }

    /**
     * 分页查询#(tableComment)。
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    #if(swaggerVersion.getName() == "FOX")
    @ApiOperation("分页列表查询")
    #end
    #if(swaggerVersion.getName() == "DOC")
    @Operation(summary="分页列表查询", description="分页查询#(tableComment)")
    #end
    public R<Page<#(voClassName)>> page(@RequestBody @Validated  PageParams<#(queryClassName)> params) {
        Page<#(voClassName)> page = Page.of(params.getCurrent(), params.getSize());
        QueryWrapper wrapper = QueryWrapper.create(params.getModel(), ControllerUtil.buildOperators(params.getModel().getClass()));
        ControllerUtil.buildOrder(wrapper, params);
        #(serviceVarName).pageAs(page, wrapper, #(voClassName).class);
        return R.success(page);
    }

}
