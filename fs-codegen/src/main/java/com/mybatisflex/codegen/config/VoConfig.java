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
package com.mybatisflex.codegen.config;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.core.util.StringUtil;

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
@SuppressWarnings("unused")
public class VoConfig implements Serializable {

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
    private String classSuffix = "Vo";

    /**
     * VO 类的父类，可以自定义一些 BaseEntity 类。
     */
    private Class<?> superClass;

    /** 父类泛型的类型 */
    private Class<?> genericityType;

    /**
     * 是否覆盖之前生成的文件。
     */
    private boolean overwriteEnable;

    /**
     * VO 默认实现的接口。
     */
    private Class<?>[] implInterfaces = {Serializable.class};

    /**
     * Entity 是否使用 Lombok 注解。
     */
    private boolean withLombok;

    /**
     * Entity 是否使用 Swagger 注解。
     */
    private boolean withSwagger;

    /**
     * Swagger 版本
     */
    private SwaggerVersion swaggerVersion;

    /**
     * 项目jdk版本
     */
    private int jdkVersion;


    /**
     * 继承的父类是否添加泛型
     */
    private boolean superClassGenericity = false;

    public String getSourceDir() {
        return sourceDir;
    }

    public VoConfig setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    /**
     * 获取类前缀。
     */
    public String getClassPrefix() {
        return classPrefix;
    }

    /**
     * 设置类前缀。
     */
    public VoConfig setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
        return this;
    }

    /**
     * 获取类后缀。
     */
    public String getClassSuffix() {
        return classSuffix;
    }

    /**
     * 设置类后缀。
     */
    public VoConfig setClassSuffix(String classSuffix) {
        this.classSuffix = classSuffix;
        return this;
    }

    /**
     * 获取父类。
     */
    public Class<?> getSuperClass() {
        return superClass;
    }

    /**
     * 设置父类。
     */
    public VoConfig setSuperClass(Class<?> superClass) {
        this.superClass = superClass;
        superClassGenericity = hasGenericity(superClass);
        return this;
    }

    public Class<?> getGenericityType() {
        return genericityType;
    }

    public VoConfig setGenericityType(Class<?> genericityType) {
        this.genericityType = genericityType;
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


    public Class<?> getSuperClass(Table table) {
        return superClass;
    }

    /**
     * 是否覆盖原有文件。
     */
    public boolean isOverwriteEnable() {
        return overwriteEnable;
    }

    /**
     * 设置是否覆盖原有文件。
     */
    public VoConfig setOverwriteEnable(boolean overwriteEnable) {
        this.overwriteEnable = overwriteEnable;
        return this;
    }

    /**
     * 获取实现接口。
     */
    public Class<?>[] getImplInterfaces() {
        return implInterfaces;
    }

    /**
     * 设置实现接口。
     */
    public VoConfig setImplInterfaces(Class<?>... implInterfaces) {
        this.implInterfaces = implInterfaces;
        return this;
    }

    /**
     * 是否使用 Lombok。
     */
    public boolean isWithLombok() {
        return withLombok;
    }

    /**
     * 设置是否使用 Lombok。
     */
    public VoConfig setWithLombok(boolean withLombok) {
        this.withLombok = withLombok;
        return this;
    }

    /**
     * 是否启用 Swagger。
     */
    public boolean isWithSwagger() {
        return withSwagger;
    }

    /**
     * 设置是否启用 Swagger。
     */
    public VoConfig setWithSwagger(boolean withSwagger) {
        this.withSwagger = withSwagger;
        this.swaggerVersion = SwaggerVersion.FOX;
        return this;
    }

    /**
     * Swagger 版本
     */
    public SwaggerVersion getSwaggerVersion() {
        return swaggerVersion;
    }

    /**
     * 设置 Swagger 版本
     */
    public VoConfig setSwaggerVersion(SwaggerVersion swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
        this.withSwagger = swaggerVersion != null;
        return this;
    }

    /**
     * 获取项目jdk版本
     */
    public int getJdkVersion() {
        return jdkVersion;
    }

    /**
     * 设置项目jdk版本
     */
    public VoConfig setJdkVersion(int jdkVersion) {
        this.jdkVersion = jdkVersion;
        return this;
    }

    public boolean isSuperClassGenericity() {
        return superClassGenericity;
    }

    public List<String> buildImports(Table table) {
        Set<String> imports = new HashSet<>();
        if (superClass != null) {
            imports.add(superClass.getName());
        }
        imports.add(com.mybatisflex.annotation.Table.class.getName());

        if (implInterfaces != null) {
            for (Class<?> entityInterface : implInterfaces) {
                imports.add(entityInterface.getName());
            }
        }

        List<Column> columns = table.getColumns();
        List<Column> superColumns = table.getSuperColumns();
        for (Column column : columns) {
            imports.addAll(column.getImportClasses());
        }
        for (Column column : superColumns) {
            imports.addAll(column.getImportClasses());
        }

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }

    public enum SwaggerVersion {
        /** FOX */
        FOX("FOX"),
        DOC("DOC");

        private final String name;

        SwaggerVersion(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }


    /**
     * 构建 extends 继承。
     */
    public String buildExtends(GlobalConfig globalConfig) {
        VoConfig voConfig = globalConfig.getVoConfig();
        Class<?> superClass = voConfig.getSuperClass();
        if (superClass != null) {
            String type = "";
            if (voConfig.isSuperClassGenericity()) {
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
