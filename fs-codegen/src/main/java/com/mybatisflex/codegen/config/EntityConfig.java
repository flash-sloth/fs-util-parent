/*
 *  Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
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

import com.mybatisflex.codegen.entity.Table;

import java.io.Serializable;
import java.lang.reflect.TypeVariable;
import java.util.function.Function;

/**
 * 生成 Entity 的配置。
 *
 * @author 王帅
 * @since 2023-05-15
 */
@SuppressWarnings("unused")
public class EntityConfig implements Serializable {

    private static final long serialVersionUID = -6790274333595436008L;

    /**
     * 代码生成目录，当未配置时，使用 PackageConfig 的配置
     */
    private String sourceDir;

    /**
     * Entity 类的前缀。
     */
    private String classPrefix = "";

    /**
     * Entity 类的后缀。
     */
    private String classSuffix = "";

    /**
     * 类名
     */
    private String name;
    /**
     * swagger注释
     */
    private String description;

    /**
     * Entity 类的父类，可以自定义一些 BaseEntity 类。
     */
    private Class<?> superClass;

    /** Entity 类的父类工厂 */
    private Function<Table, Class<?>> superClassFactory;
    /** 父类泛型的类型 */
    private Class<?> genericityType;

    /**
     * 是否覆盖之前生成的文件。
     */
    private boolean overwriteEnable;

    /**
     * Entity 默认实现的接口。
     */
    private Class<?>[] implInterfaces = {Serializable.class};

    /**
     * Entity 是否使用 Lombok 注解。
     */
    private boolean withLombok;
    private boolean withChain;

    /**
     * Entity 是否使用 Swagger 注解。
     */
    private boolean withSwagger;

    /**
     * Swagger 版本
     */
    private SwaggerVersion swaggerVersion = SwaggerVersion.DOC;

    /**
     * Entity 是否启用 Active Record 功能。
     */
    private boolean withActiveRecord;

    /**
     * 实体类数据源。
     */
    private String dataSource;

    /**
     * 项目jdk版本
     */
    private int jdkVersion;

    /**
     * 当开启这个配置后，Entity 会生成两个类，比如 Account 表会生成 Account.java 以及 AccountBase.java
     * 这样的好处是，自动生成的 getter setter 字段等都在 Base 类里，而开发者可以在 Account.java 中添加自己的业务代码
     * 此时，当有数据库表结构发生变化，需要再次生成代码时，不会覆盖掉 Account.java 中的业务代码（只会覆盖 AccountBase 中的 Getter Setter）
     */
    private boolean withBaseClassEnable = false;

    /**
     * Base 类的后缀
     */
    private String withBaseClassSuffix = "Base";

    /**
     * Base 类所在的包，默认情况下是在 entity 包下，添加一个 base 文件夹。
     */
    private String withBasePackage;

    /**
     * 是否支持把 comment 添加到 @column 注解里
     */
    private boolean columnCommentEnable;

    /**
     * 是否总是生成 @Column 注解。
     */
    private boolean alwaysGenColumnAnnotation;

    /**
     * 继承的父类是否添加泛型
     */
    private boolean superClassGenericity = false;

    public String getSourceDir() {
        return sourceDir;
    }

    public EntityConfig setSourceDir(String sourceDir) {
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
    public EntityConfig setClassPrefix(String classPrefix) {
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
    public EntityConfig setClassSuffix(String classSuffix) {
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
    public EntityConfig setSuperClass(Class<?> superClass) {
        this.superClass = superClass;
        superClassGenericity = hasGenericity(superClass);
        return this;
    }

    public Class<?> getGenericityType() {
        return genericityType;
    }
    public String getGenericityTypeName() {
        return genericityType != null ? genericityType.getSimpleName() : null;
    }
    public EntityConfig setGenericityType(Class<?> genericityType) {
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
        if (superClassFactory != null) {
            return superClassFactory.apply(table);
        }
        return superClass;
    }

    public Function<Table, Class<?>> getSuperClassFactory() {
        return superClassFactory;
    }

    public EntityConfig setSuperClassFactory(Function<Table, Class<?>> superClassFactory) {
        this.superClassFactory = superClassFactory;
        return this;
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
    public EntityConfig setOverwriteEnable(boolean overwriteEnable) {
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
    public EntityConfig setImplInterfaces(Class<?>... implInterfaces) {
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
    public EntityConfig setWithLombok(boolean withLombok) {
        this.withLombok = withLombok;
        return this;
    }

    public boolean isWithChain() {
        return withChain;
    }

    public EntityConfig setWithChain(boolean withChain) {
        this.withChain = withChain;
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
    public EntityConfig setWithSwagger(boolean withSwagger) {
        this.withSwagger = withSwagger;
        this.swaggerVersion = SwaggerVersion.DOC;
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
    public EntityConfig setSwaggerVersion(SwaggerVersion swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
        this.withSwagger = swaggerVersion != null;
        return this;
    }

    /**
     * 是否启用 Active Record。
     */
    public boolean isWithActiveRecord() {
        return withActiveRecord;
    }

    /**
     * 设置是否启用 Active Record。
     */
    public EntityConfig setWithActiveRecord(boolean withActiveRecord) {
        this.withActiveRecord = withActiveRecord;
        return this;
    }

    /**
     * 获取实体类数据源。
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * 设置实体类数据源。
     */
    public EntityConfig setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
    public EntityConfig setJdkVersion(int jdkVersion) {
        this.jdkVersion = jdkVersion;
        return this;
    }

    public boolean isWithBaseClassEnable() {
        return withBaseClassEnable;
    }

    public EntityConfig setWithBaseClassEnable(boolean withBaseClassEnable) {
        this.withBaseClassEnable = withBaseClassEnable;
        return this;
    }

    public String getWithBaseClassSuffix() {
        return withBaseClassSuffix;
    }

    public EntityConfig setWithBaseClassSuffix(String withBaseClassSuffix) {
        this.withBaseClassSuffix = withBaseClassSuffix;
        return this;
    }

    public String getWithBasePackage() {
        return withBasePackage;
    }

    public EntityConfig setWithBasePackage(String withBasePackage) {
        this.withBasePackage = withBasePackage;
        return this;
    }

    public boolean isColumnCommentEnable() {
        return columnCommentEnable;
    }

    public EntityConfig setColumnCommentEnable(boolean columnCommentEnable) {
        this.columnCommentEnable = columnCommentEnable;
        return this;
    }

    public boolean isAlwaysGenColumnAnnotation() {
        return alwaysGenColumnAnnotation;
    }

    public EntityConfig setAlwaysGenColumnAnnotation(boolean alwaysGenColumnAnnotation) {
        this.alwaysGenColumnAnnotation = alwaysGenColumnAnnotation;
        return this;
    }

    public boolean isSuperClassGenericity(Table table) {
        if (this.superClassFactory != null) {
            return hasGenericity(superClassFactory.apply(table));
        }
        return superClassGenericity;
    }

    public enum SwaggerVersion {

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

    public String getName() {
        return name;
    }

    public EntityConfig setName(String name) {
        this.name = name;
        return this;
    }
}
