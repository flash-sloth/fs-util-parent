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
package com.mybatisflex.codegen.generator.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.constant.GenTypeEnum;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.IGenerator;
import com.mybatisflex.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity Base 生成器。
 *
 * @author tangyh
 * @since 2024年06月19日23:47:34
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class EntityBaseGenerator implements IGenerator {

    /** 模板路径 */
    private GenTypeEnum genType;
    /** 模板内容 */
    protected String templateContent;

    public EntityBaseGenerator() {
        this(GenTypeEnum.ENTITY_BASE);
    }

    public EntityBaseGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isEntityGenerateEnable()) {
            return;
        }

        // 生成 base 类
        genBaseClass(table, globalConfig);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        EntityConfig entityConfig = globalConfig.getEntityConfig();


        String baseEntityPackagePath = packageConfig.getEntityPackage().replace(".", "/");
        baseEntityPackagePath = StringUtil.isNotBlank(entityConfig.getWithBasePackage()) ? entityConfig.getWithBasePackage().replace(".", "")
                : baseEntityPackagePath + "/base";

        String baseEntityClassName = table.buildEntityClassName() + entityConfig.getWithBaseClassSuffix();

        Map<String, Object> params = getTemplatePath(table, globalConfig, baseEntityPackagePath, baseEntityClassName);

        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        } else {
            return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());
        }
    }

    protected void genBaseClass(Table table, GlobalConfig globalConfig) {
        EntityConfig entityConfig = globalConfig.getEntityConfig();

        // 不需要生成 baseClass
        if (!entityConfig.getWithBaseClassEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        String sourceDir = StringUtil.isNotBlank(entityConfig.getSourceDir()) ? entityConfig.getSourceDir() : packageConfig.getSourceDir();

        String baseEntityPackagePath = packageConfig.getEntityPackage().replace(".", "/");
        baseEntityPackagePath = StringUtil.isNotBlank(entityConfig.getWithBasePackage()) ? entityConfig.getWithBasePackage().replace(".", "")
                : baseEntityPackagePath + "/base";

        String baseEntityClassName = table.buildEntityClassName() + entityConfig.getWithBaseClassSuffix();

        File baseEntityJavaFile = new File(sourceDir, baseEntityPackagePath + "/" + baseEntityClassName + ".java");

        Map<String, Object> params = getTemplatePath(table, globalConfig, baseEntityPackagePath, baseEntityClassName);

        log.info("BaseEntity ---> {}", baseEntityJavaFile);
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, baseEntityJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), baseEntityJavaFile);
        }
    }

    public Map<String, Object> getTemplatePath(Table table, GlobalConfig globalConfig, String baseEntityPackagePath, String baseEntityClassName) {
        Map<String, Object> params = new HashMap<>(8);
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        // 排除忽略列
        if (globalConfig.getStrategyConfig().getIgnoreColumns() != null) {
            table.getColumns().removeIf(column -> globalConfig.getStrategyConfig().getIgnoreColumns().contains(column.getName().toLowerCase()));
        }

        params.put("table", table);
        params.put("entityPackageName", baseEntityPackagePath.replace("/", "."));
        params.put("entityClassName", baseEntityClassName);
        params.put("entityConfig", entityConfig);
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("isBase", true);
        params.putAll(globalConfig.getCustomConfig());
        return params;
    }
}
