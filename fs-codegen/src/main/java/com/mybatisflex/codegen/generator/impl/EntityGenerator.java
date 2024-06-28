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
import top.fsfsfs.basic.utils.StrPool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity 生成器。
 *
 * @author Michael Yang
 * @author 王帅
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class EntityGenerator implements IGenerator {

    /** 模板内容 */
    protected String templateContent;
    private GenTypeEnum genType;

    protected String entityWithBaseTemplatePath = "/templates/enjoy/entityWithBase.tpl";


    public EntityGenerator() {
        this(GenTypeEnum.ENTITY);
    }

    public EntityGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getPath(GlobalConfig globalConfig, boolean absolute) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        EntityConfig config = globalConfig.getEntityConfig();
        String layerPackage = packageConfig.getEntityPackage();
        String sourceDir = config.getSourceDir();

        String path = "";
        if (absolute) {
            path = StringUtil.isNotBlank(sourceDir) ? sourceDir : packageConfig.getSourceDir();
            if (!path.endsWith(File.separator)) {
                path += File.separator;
            }
        }

        path += StrPool.SRC_MAIN_JAVA + File.separator;
        path += layerPackage.replace(".", "/");
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isEntityGenerateEnable()) {
            return;
        }

        EntityConfig entityConfig = globalConfig.getEntityConfig();

        String entityPackagePath = getPath(globalConfig, true);
        String entityClassName = table.buildEntityClassName();
        File entityJavaFile = new File(entityPackagePath, entityClassName + StrPool.DOT_JAVA);

        if (entityJavaFile.exists() && !entityConfig.getOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = new HashMap<>(7);

        String templatePath = getTemplatePath(params, table, globalConfig);

        log.info("Entity ---> {}", entityJavaFile);

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, entityJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templatePath, entityJavaFile);
        }

    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {

        Map<String, Object> params = new HashMap<>(7);

        String templatePath = getTemplatePath(params, table, globalConfig);

        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        } else {
            return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, templatePath);
        }
    }


    public String getTemplatePath(Map<String, Object> params, Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        EntityConfig entityConfig = globalConfig.getEntityConfig();


        // 排除忽略列
        if (globalConfig.getStrategyConfig().getIgnoreColumns() != null) {
            table.getColumns().removeIf(column -> globalConfig.getStrategyConfig().getIgnoreColumns().contains(column.getName().toLowerCase()));
        }

        params.put("table", table);
        params.put("entityPackageName", packageConfig.getEntityPackage());
        params.put("entityConfig", entityConfig);
        params.put("entityClassName", table.buildEntityClassName());
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("isBase", false);

        params.putAll(globalConfig.getCustomConfig());

        String templatePath = this.genType.getTemplate();

        // 开启生成 baseClass
        if (entityConfig.getWithBaseClassEnable()) {
            templatePath = this.entityWithBaseTemplatePath;

            String baseClassName = table.buildEntityClassName() + entityConfig.getWithBaseClassSuffix();
            params.put("baseClassName", baseClassName);

            String baseClassPackage = StringUtil.isNotBlank(entityConfig.getWithBasePackage())
                    ? entityConfig.getWithBasePackage() : packageConfig.getEntityPackage() + ".base";
            params.put("baseClassPackage", baseClassPackage);

            params.put("entityClassName", table.buildEntityClassName());
        }
        return templatePath;
    }

}
