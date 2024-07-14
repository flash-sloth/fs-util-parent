/*
 *  Copyright (c) 2022-2024, Mybatis-Flex (fuhai999@gmail.com).
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
package top.fsfsfs.codegen.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.util.StringUtil;
import top.fsfsfs.basic.model.cache.CacheKeyBuilder;
import top.fsfsfs.codegen.config.ControllerConfig;
import top.fsfsfs.codegen.config.DtoConfig;
import top.fsfsfs.codegen.config.EntityConfig;
import top.fsfsfs.codegen.config.GlobalConfig;
import top.fsfsfs.codegen.config.MapperConfig;
import top.fsfsfs.codegen.config.MapperXmlConfig;
import top.fsfsfs.codegen.config.PackageConfig;
import top.fsfsfs.codegen.config.QueryConfig;
import top.fsfsfs.codegen.config.ServiceConfig;
import top.fsfsfs.codegen.config.ServiceImplConfig;
import top.fsfsfs.codegen.config.TableConfig;
import top.fsfsfs.codegen.config.TableDefConfig;
import top.fsfsfs.codegen.config.VoConfig;
import top.fsfsfs.codegen.constant.PackageConst;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据库表信息。
 */
public class Table {

    /**
     * 表名。
     */
    private String name;

    /**
     * schema（模式）。
     */
    private String schema;

    /**
     * 表注释。
     */
    private String comment;

    /**
     * 主键。
     */
    private Set<String> primaryKeys;

    /**
     * 子类的的列。
     */
    private List<Column> columns = new ArrayList<>();
    /**
     * 所有列。
     */
    private List<Column> allColumns = new ArrayList<>();
    /**
     * 父类的列。
     */
    private List<Column> superColumns = new ArrayList<>();
    private List<String> superColumnNames = new ArrayList<>();

    /**
     * 表配置。
     */
    private TableConfig tableConfig;

    private EntityConfig entityConfig;

    /**
     * 全局配置。
     */
    private GlobalConfig globalConfig;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        if (StringUtil.isNotBlank(comment)) {
            return globalConfig.getJavadocConfig().formatTableComment(comment);
        }
        return null;
    }

    public String getSwaggerComment() {
        if (StringUtil.isNotBlank(comment)) {
            return globalConfig.getJavadocConfig().formatTableSwaggerComment(comment);
        }
        return null;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Column getPrimaryKey() {
        // 这里默认表中一定会有字段，就不做空判断了
        List<Column> allColumns = new ArrayList<>();
        allColumns.addAll(columns);
        allColumns.addAll(superColumns);
        return allColumns.stream()
                .filter(Column::getPrimaryKey)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("PrimaryKey can't be null"));
    }

    public Set<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(Set<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public void addPrimaryKey(String primaryKey) {
        if (primaryKeys == null) {
            primaryKeys = new LinkedHashSet<>();
        }
        primaryKeys.add(primaryKey);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Column> getSuperColumns() {
        return superColumns;
    }

    public List<Column> getAllColumns() {
        return allColumns;
    }

    public List<Column> getSortedColumns() {
        ArrayList<Column> arrayList = new ArrayList<>(columns);
        // 生成字段排序
        arrayList.sort(Comparator.comparingInt((Column c) -> c.getProperty().length())
                .thenComparing(Column::getProperty));
        return arrayList;
    }

    public List<Column> getSortedListColumns() {
        ArrayList<Column> arrayList = new ArrayList<>(columns);
        // 生成字段排序
        arrayList.sort(Comparator.comparingInt((Column c) -> c.getListConfig().getSequence())
                .thenComparing(Column::getProperty));
        return arrayList;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }


    public boolean containsColumn(String columnName) {
        if (columns == null || columns.isEmpty() || StringUtil.isBlank(columnName)) {
            return false;
        }
        for (Column column : columns) {
            if (columnName.equals(column.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsColumn(String... columnNames) {
        for (String columnName : columnNames) {
            if (!containsColumn(columnName)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAnyColumn(String... columnNames) {
        for (String columnName : columnNames) {
            if (containsColumn(columnName)) {
                return true;
            }
        }
        return false;
    }

    public void addColumn(Column column) {
        if (CollUtil.isEmpty(superColumnNames)) {
            Class<?> superClass = entityConfig.getSuperClass(this);
            //获取所有 private字段
            if (superClass != null) {
                Field[] fields = ReflectUtil.getFields(superClass);
                for (Field field : fields) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers)) {
                        superColumnNames.add(field.getName());
                    }
                }
            }
        }

        //主键
        if (primaryKeys != null && primaryKeys.contains(column.getName())) {
            column.setPrimaryKey(true);
            if (column.getAutoIncrement() == null && (column.getPropertyType().equals(Integer.class.getName()) || column.getPropertyType().equals(BigInteger.class.getName()))) {
                column.setAutoIncrement(true);
            }
        }
        // 自增
        if (column.getAutoIncrement() == null) {
            column.setAutoIncrement(false);
        }

        column.setColumnConfig(globalConfig.getStrategyConfig().getColumnConfig(name, column.getName()));
        column.setEntityConfig(globalConfig.getEntityConfig());
        column.setJavadocConfig(globalConfig.getJavadocConfig());

        if (superColumnNames.contains(column.getProperty())) {
            superColumns.add(column);
            allColumns.add(column);
            return;
        }

        columns.add(column);
        allColumns.add(column);
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    // ===== 构建实体类文件 =====

    /**
     * 构建 import 导包。
     */
    public List<String> buildImports(Boolean isBase) {
        Set<String> imports = new HashSet<>();

        //base 类不需要添加 Table 的导入，没有 @Table 注解
        if (!isBase) {
            imports.add("com.mybatisflex.annotation.Table");
        }

        EntityConfig entityConfig = globalConfig.getEntityConfig();

        //未开启基类生成，或者是基类的情况下，添加 Column 类型的导入
        if (!entityConfig.getWithBaseClassEnable() || (entityConfig.getWithBaseClassEnable() && isBase)) {
            for (Column column : columns) {
                imports.addAll(column.getImportClasses());
            }

            Class<?> superClass = entityConfig.getSuperClass(this);
            if (superClass != null) {
                imports.add(superClass.getName());
            }

            if (entityConfig.getImplInterfaces() != null) {
                for (Class<?> entityInterface : entityConfig.getImplInterfaces()) {
                    imports.add(entityInterface.getName());
                }
            }
        }


        if (!entityConfig.getWithBaseClassEnable() || (entityConfig.getWithBaseClassEnable() && !isBase)) {
            if (tableConfig != null) {
                if (tableConfig.getInsertListenerClass() != null) {
                    imports.add(tableConfig.getInsertListenerClass().getName());
                }
                if (tableConfig.getUpdateListenerClass() != null) {
                    imports.add(tableConfig.getUpdateListenerClass().getName());
                }
                if (tableConfig.getSetListenerClass() != null) {
                    imports.add(tableConfig.getSetListenerClass().getName());
                }
            }
        }

        return imports.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public List<String> buildServiceImplImports() {
        Set<String> imports = new HashSet<>();

        ServiceImplConfig serviceImplConfig = globalConfig.getServiceImplConfig();
        if (BooleanUtil.isTrue(serviceImplConfig.getCache())) {
            imports.add(CacheKeyBuilder.class.getName());
        }

        return imports.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public List<String> buildControllerImports() {
        Set<String> imports = new HashSet<>();
        String entityClassName = buildEntityClassName();
        String voClassName = buildVoClassName();
        String dtoClassName = buildDtoClassName();
        String queryClassName = buildQueryClassName();
        String serviceClassName = buildServiceClassName();
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ControllerConfig controllerConfig = globalConfig.getControllerConfig();
        EntityConfig.SwaggerVersion swaggerVersion = globalConfig.getSwaggerVersion();

        if (controllerConfig.getRestStyle()) {
            imports.add(PackageConst.REST_CONTROLLER);
        } else {
            imports.add(PackageConst.CONTROLLER);
        }

        if (EntityConfig.SwaggerVersion.FOX.getName().equals(swaggerVersion.getName())) {
            imports.add(PackageConst.API);
            if (controllerConfig.getWithCrud()) {
                imports.add(PackageConst.API_OPERATION);
                imports.add(PackageConst.API_PARAM);
            }
        } else {
            imports.add(PackageConst.TAG);
            if (controllerConfig.getWithCrud()) {
                imports.add(PackageConst.OPERATION);
                imports.add(PackageConst.PARAMETER);
            }
        }

        imports.add(PackageConst.VALIDATED);
        if (controllerConfig.getWithCrud()) {
            imports.add(PackageConst.AUTOWIRED);
            imports.add(PackageConst.R);
            imports.add(PackageConst.PAGE);
            imports.add(PackageConst.BASE_ENTITY);
            imports.add(PackageConst.PATH_VARIABLE);
            imports.add(PackageConst.REQUEST_BODY);
            imports.add(PackageConst.GET_MAPPING);
            imports.add(PackageConst.DELETE_MAPPING);
            imports.add(PackageConst.POST_MAPPING);
            imports.add(PackageConst.PUT_MAPPING);
            imports.add(PackageConst.PAGE_PARAMS);
            imports.add(PackageConst.QUERY_WRAPPER);
            imports.add(PackageConst.CONTROLLER_UTIL);
            imports.add(List.class.getName());
            imports.add(BeanUtil.class.getName());
            imports.add(StrUtil.format("{}.{}", packageConfig.getServicePackage(), serviceClassName));
        }
        imports.add(PackageConst.REQUEST_MAPPING);

        Class<?> superClass = controllerConfig.getSuperClass();
        if (superClass == null) {
            return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
        }

        TypeVariable<? extends Class<?>>[] typeParameters = superClass.getTypeParameters();

        for (TypeVariable<? extends Class<?>> typeParameter : typeParameters) {

            switch (typeParameter.getTypeName()) {
                case "Id" -> imports.add(null);
                case "Entity" ->
                        imports.add(StrUtil.format("{}.{}", packageConfig.getEntityPackage(), entityClassName));
                case "VO" -> imports.add(StrUtil.format("{}.{}", packageConfig.getVoPackage(), voClassName));
                case "Query" -> imports.add(StrUtil.format("{}.{}", packageConfig.getQueryPackage(), queryClassName));
                case "DTO" -> imports.add(StrUtil.format("{}.{}", packageConfig.getDtoPackage(), dtoClassName));
                default -> imports.add(StrUtil.format("{}.{}", packageConfig.getServicePackage(), serviceClassName));
            }
        }

        imports.add(superClass.getName());

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }

    /**
     * 构建 @Table(...) 注解。
     */
    public String buildTableAnnotation() {
        StringBuilder tableAnnotation = new StringBuilder();

        String baseEntityClassName = buildEntityClassName();
        if (entityConfig.getWithBaseClassEnable()) {
            baseEntityClassName = buildEntityClassName() + entityConfig.getWithBaseClassSuffix();
        }
        String tableName = StrUtil.format("{}.TABLE_NAME", baseEntityClassName);
        tableAnnotation.append("@Table(value = ").append(tableName);

        String globalSchema;

        if (tableConfig == null) {
            // 未配置 tableConfig 以策略中的 schema 为主
            globalSchema = schema;
        } else if (StringUtil.isBlank(tableConfig.getSchema())) {
            // 配置 tableConfig 但未指定 schema 还是以策略中的 schema 为主
            globalSchema = schema;
        } else {
            // 以用户设置的 tableConfig 中的 schema 为主
            globalSchema = null;
        }

        if (StringUtil.isNotBlank(globalSchema)) {
            tableAnnotation.append(", schema = \"").append(globalSchema).append("\"");
        }

        // 添加 dataSource 配置，因为代码生成器是一个数据源生成的，所以这些实体类应该都是一个数据源。
        String dataSource = globalConfig.getEntityDataSource();
        if (StringUtil.isNotBlank(dataSource)) {
            tableAnnotation.append(", dataSource = \"").append(dataSource).append("\"");
        }


        if (tableConfig != null) {
            if (StringUtil.isNotBlank(tableConfig.getSchema())) {
                tableAnnotation.append(", schema = \"").append(tableConfig.getSchema()).append("\"");
            }
            if (tableConfig.getCamelToUnderline() != null) {
                tableAnnotation.append(", camelToUnderline = ").append(tableConfig.getCamelToUnderline());
            }
            if (tableConfig.getInsertListenerClass() != null) {
                tableAnnotation.append(", onInsert = ").append(tableConfig.getInsertListenerClass().getSimpleName()).append(".class");
            }
            if (tableConfig.getUpdateListenerClass() != null) {
                tableAnnotation.append(", onUpdate = ").append(tableConfig.getUpdateListenerClass().getSimpleName()).append(".class");
            }
            if (tableConfig.getSetListenerClass() != null) {
                tableAnnotation.append(", onSet = ").append(tableConfig.getSetListenerClass().getSimpleName()).append(".class");
            }
            if (Boolean.FALSE.equals(tableConfig.getMapperGenerateEnable())) {
                tableAnnotation.append(", mapperGenerateEnable = false");
            }
        }


        if (entityConfig != null && entityConfig.getColumnCommentEnable() && StringUtil.isNotBlank(comment)) {
            tableAnnotation.append(", comment = \"")
                    .append(this.comment.replace("\n", "").replace("\"", "\\\"").trim())
                    .append("\"");
        }

        // @Table(value = "sys_user") -> @Table("sys_user")
        int index = tableAnnotation.indexOf(",");
        if (index == -1) {
            int start = tableAnnotation.indexOf("value");
            if (start != -1) {
                tableAnnotation.delete(start, start + 8);
            }
        }

        return tableAnnotation.append(")").toString();
    }

    /**
     * 构建 extends 继承。
     */
    public String buildExtends(Boolean isBase) {
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        Class<?> superClass = entityConfig.getSuperClass(this);
        if (superClass != null) {
            String type = "";
            if (entityConfig.isSuperClassGenericity(this)) {
                if (entityConfig.getGenericityType() == null) {
                    type = StrUtil.format("<{}{}>", buildEntityClassName(), isBase ? entityConfig.getWithBaseClassSuffix() : "");
                } else {
                    type = StrUtil.format("<{}>", entityConfig.getGenericityType().getSimpleName());
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
    public String buildImplements() {
        Class<?>[] entityInterfaces = globalConfig.getEntityConfig().getImplInterfaces();
        if (entityInterfaces != null && entityInterfaces.length > 0) {
            return " implements " + StringUtil.join(", ", Arrays.stream(entityInterfaces)
                    .map(Class::getSimpleName).collect(Collectors.toList()));
        } else {
            return "";
        }
    }

    /**
     * 构建 kt 继承
     */
    public String buildKtExtends(Boolean isBase) {
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        Class<?> superClass = entityConfig.getSuperClass(this);
        List<String> s = new ArrayList<>();
        if (superClass != null) {
            String name = superClass.getSimpleName();
            if (entityConfig.isSuperClassGenericity(this)) {
                name += "<" + buildEntityClassName() + (isBase ? entityConfig.getWithBaseClassSuffix() : "") + ">";
            }
            name += "()";
            s.add(name);
        }
        Class<?>[] entityInterfaces = globalConfig.getEntityConfig().getImplInterfaces();
        if (entityInterfaces != null && entityInterfaces.length > 0) {
            for (Class<?> inter : entityInterfaces) {
                s.add(inter.getSimpleName());
            }
        }
        if (s.isEmpty()) {
            return "";
        }
        return " :" + String.join(",", s);
    }

    // ===== 构建相关类名 =====

    /**
     * 获取生成 Java 文件名。
     */
    public String getEntityJavaFileName() {
        String entityName = entityConfig.getName();
        if (StrUtil.isNotEmpty(entityName)) {
            return entityName;
        }
        String entityJavaFileName = name;
        String tablePrefix = globalConfig.getStrategyConfig().getTablePrefix();
        if (tablePrefix != null) {
            String[] tablePrefixes = tablePrefix.split(",");
            for (String prefix : tablePrefixes) {
                String trimPrefix = prefix.trim();
                if (trimPrefix.length() > 0 && name.startsWith(trimPrefix)) {
                    entityJavaFileName = name.substring(trimPrefix.length());
                    break;
                }
            }
        }
        return StringUtil.firstCharToUpperCase(StringUtil.underlineToCamel(entityJavaFileName));
    }

    /**
     * 构建 entity 的 Class 名称。
     */
    public String buildEntityClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        return entityConfig.getClassPrefix()
                + entityJavaFileName
                + entityConfig.getClassSuffix();
    }

    /**
     * 构建 entity 的 Class 名称。
     */
    public String buildEntityBaseClassName() {
        return buildEntityClassName() + entityConfig.getWithBaseClassSuffix();
    }

    /**
     * 构建 Vo 的 Class 名称。
     */
    public String buildVoClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        VoConfig voConfig = globalConfig.getVoConfig();
        return voConfig.getClassPrefix()
                + entityJavaFileName
                + voConfig.getClassSuffix();
    }

    /**
     * 构建 Dto 的 Class 名称。
     */
    public String buildDtoClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        DtoConfig voConfig = globalConfig.getDtoConfig();
        return voConfig.getClassPrefix()
                + entityJavaFileName
                + voConfig.getClassSuffix();
    }

    /**
     * 构建 Dto 的 Class 名称。
     */
    public String buildQueryClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        QueryConfig voConfig = globalConfig.getQueryConfig();
        return voConfig.getClassPrefix()
                + entityJavaFileName
                + voConfig.getClassSuffix();
    }


    /**
     * 构建 tableDef 的 Class 名称。
     */
    public String buildTableDefClassName() {
        String tableDefJavaFileName = getEntityJavaFileName();
        TableDefConfig tableDefConfig = globalConfig.getTableDefConfig();
        return tableDefConfig.getClassPrefix()
                + tableDefJavaFileName
                + tableDefConfig.getClassSuffix();
    }

    /**
     * 构建 mapper 的 Class 名称。
     */
    public String buildMapperClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        MapperConfig mapperConfig = globalConfig.getMapperConfig();
        return mapperConfig.getClassPrefix()
                + entityJavaFileName
                + mapperConfig.getClassSuffix();
    }

    /**
     * 构建 service 的 Class 名称。
     */
    public String buildServiceClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        ServiceConfig serviceConfig = globalConfig.getServiceConfig();
        return serviceConfig.getClassPrefix()
                + entityJavaFileName
                + serviceConfig.getClassSuffix();
    }

    /**
     * 构建 serviceImpl 的 Class 名称。
     */
    public String buildServiceImplClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        ServiceImplConfig serviceImplConfig = globalConfig.getServiceImplConfig();
        return serviceImplConfig.getClassPrefix()
                + entityJavaFileName
                + serviceImplConfig.getClassSuffix();
    }

    /**
     * 构建 controller 的 Class 名称。
     */
    public String buildControllerClassName() {
        String entityJavaFileName = getEntityJavaFileName();
        ControllerConfig controllerConfig = globalConfig.getControllerConfig();
        return controllerConfig.getClassPrefix()
                + entityJavaFileName
                + controllerConfig.getClassSuffix();
    }

    /**
     * 构建访问路径的前缀
     */
    public String buildControllerRequestMappingPrefix() {
        String mappingPrefix = globalConfig.getControllerConfig().getRequestMappingPrefix();
        return mappingPrefix == null ? "" : mappingPrefix.trim();
    }

    /**
     * 构建 MapperXml 的文件名称。
     */
    public String buildMapperXmlFileName() {
        String tableDefJavaFileName = getEntityJavaFileName();
        MapperXmlConfig mapperXmlConfig = globalConfig.getMapperXmlConfig();
        return mapperXmlConfig.getFilePrefix()
                + tableDefJavaFileName
                + mapperXmlConfig.getFileSuffix();
    }

    @Override
    public String toString() {
        return "Table{" +
                "schema'" + schema + '\'' +
                "name='" + name + '\'' +
                ", remarks='" + comment + '\'' +
                ", primaryKeys='" + primaryKeys + '\'' +
                ", columns=" + columns +
                '}';
    }

}
