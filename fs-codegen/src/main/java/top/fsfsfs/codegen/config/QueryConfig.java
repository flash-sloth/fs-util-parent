/*
 *  Copyright (c) 2022-2023, flash-sloth
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package top.fsfsfs.codegen.config;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.util.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import top.fsfsfs.codegen.constant.GenerationStrategyEnum;
import top.fsfsfs.codegen.entity.Column;
import top.fsfsfs.codegen.entity.Table;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生成 Vo 的配置。
 *
 * @author tangyh
 * @since 2024年06月18日15:51:07
 */
@Data
@Accessors(chain = true)
public class QueryConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -6790274333595436008L;

    /**
     * 代码生成目录，当未配置时，使用 PackageConfig 的配置
     */
    private String sourceDir;

    /**
     * VO 类的前缀。
     */
    private String classPrefix = "";

    /**
     * VO 类的后缀。
     */
    private String classSuffix = "Query";

    /**
     * VO 类的父类，可以自定义一些 BaseEntity 类。
     */
    private Class<?> superClass;

    /** 父类泛型的类型 */
    private Class<?> genericityType;

    /**
     * 生成策略。
     */
    private GenerationStrategyEnum generationStrategy = GenerationStrategyEnum.OVERWRITE;

    /**
     * VO 默认实现的接口。
     */
    private Class<?>[] implInterfaces = {Serializable.class};

    /**
     * Entity 是否使用 Lombok 注解。
     */
    private Boolean withLombok = true;

    /**
     * Entity 是否使用 Swagger 注解。
     */
    private Boolean withSwagger = true;
    /** 导入注解 */
    private Boolean withExcel = true;

    /**
     * Swagger 版本
     */
    private EntityConfig.SwaggerVersion swaggerVersion = EntityConfig.SwaggerVersion.DOC;

    /**
     * 项目jdk版本
     */
    private int jdkVersion;


    /**
     * 继承的父类是否添加泛型
     */
    private Boolean superClassGenericity = false;

    /**
     * 设置父类。
     */
    public QueryConfig setSuperClass(Class<?> superClass) {
        this.superClass = superClass;
        superClassGenericity = hasGenericity(superClass);
        return this;
    }

    private boolean hasGenericity(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
        if (typeParameters.length > 1) {
            throw new UnsupportedOperationException("暂不支持父类泛型数量 >1 的代码生成");
        } else {
            return typeParameters.length > 0;
        }
    }

    /**
     * 设置实现接口。
     */
    public QueryConfig setImplInterfaces(Class<?>... implInterfaces) {
        this.implInterfaces = implInterfaces;
        return this;
    }

    /**
     * 设置是否启用 Swagger。
     */
    public QueryConfig setWithSwagger(Boolean withSwagger) {
        this.withSwagger = withSwagger;
        this.swaggerVersion = EntityConfig.SwaggerVersion.DOC;
        return this;
    }


    /**
     * 设置 Swagger 版本
     */
    public QueryConfig setSwaggerVersion(EntityConfig.SwaggerVersion swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
        this.withSwagger = swaggerVersion != null;
        return this;
    }

    public List<String> buildImports(Table table) {
        Set<String> imports = new HashSet<>();
        if (superClass != null) {
            imports.add(superClass.getName());
        }

        if (implInterfaces != null) {
            for (Class<?> entityInterface : implInterfaces) {
                imports.add(entityInterface.getName());
            }
        }

        List<Column> columns = table.getAllColumns();
        for (Column column : columns) {
            imports.addAll(column.getImportClasses());
        }

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }

    /**
     * 构建 extends 继承。
     */
    public String buildExtends(GlobalConfig globalConfig) {
        QueryConfig voConfig = globalConfig.getQueryConfig();
        Class<?> superClass = voConfig.getSuperClass();
        if (superClass != null) {
            String type = "";
            if (voConfig.getSuperClassGenericity()) {
                if (voConfig.getGenericityType() == null) {
                    type = StrUtil.format("<{}>", Long.class.getSimpleName());
                } else {
                    type = StrUtil.format("<{}>", voConfig.getGenericityType().getSimpleName());
                }
            }
            return " extends " + superClass.getSimpleName() + type;
        } else {
            return "";
        }
    }

    /**
     * 构建 implements 实现。
     */
    public String buildImplements(GlobalConfig globalConfig) {
        Class<?>[] entityInterfaces = globalConfig.getEntityConfig().getImplInterfaces();
        if (entityInterfaces != null && entityInterfaces.length > 0) {
            return " implements " + StringUtil.join(", ", Arrays.stream(entityInterfaces)
                    .map(Class::getSimpleName).collect(Collectors.toList()));
        } else {
            return "";
        }
    }
}
