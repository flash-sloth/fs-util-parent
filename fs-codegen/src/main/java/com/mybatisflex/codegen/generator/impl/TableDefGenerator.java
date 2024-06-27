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
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.config.TableDefConfig;
import com.mybatisflex.codegen.constant.GenTypeConst;
import com.mybatisflex.codegen.constant.TemplateConst;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.IGenerator;
import com.mybatisflex.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * TableDef 生成器。
 *
 * @author Michael Yang
 * @author 王帅
 */
@Slf4j
public class TableDefGenerator implements IGenerator {

    private String templatePath;
    private String templateContent;
    private String genType;

    public TableDefGenerator() {
        this(TemplateConst.TABLE_DEF);
        this.genType = GenTypeConst.TABLE_DEF;
    }

    public TableDefGenerator(String templatePath) {
        this.templatePath = templatePath;
    }

    @Override
    public String getGenType() {
        return genType;
    }

    @Override
    public IGenerator setGenType(String genType) {
        this.genType = genType;
        return this;
    }

    @Override
    public String getTemplateContent() {
        return templateContent;
    }

    @Override
    public IGenerator setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
        return this;
    }

    @Override
    public String getTemplatePath() {
        return templatePath;
    }

    @Override
    public IGenerator setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isTableDefGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        TableDefConfig tableDefConfig = globalConfig.getTableDefConfig();

        String sourceDir = StringUtil.isNotBlank(tableDefConfig.getSourceDir()) ? tableDefConfig.getSourceDir() : packageConfig.getSourceDir();

        String tableDefPackagePath = packageConfig.getTableDefPackage().replace(".", "/");
        File tableDefJavaFile = new File(sourceDir, tableDefPackagePath + "/" +
                table.buildTableDefClassName() + globalConfig.getFileType());


        if (tableDefJavaFile.exists() && !tableDefConfig.isOverwriteEnable()) {
            return;
        }


        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("tableDefConfig", tableDefConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("entityConfig", globalConfig.getEntityConfig());
        params.putAll(globalConfig.getCustomConfig());

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templateContent, tableDefJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templatePath, tableDefJavaFile);
        }

        log.info("TableDef ---> {}", tableDefJavaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        TableDefConfig tableDefConfig = globalConfig.getTableDefConfig();


        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("tableDefConfig", tableDefConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("entityConfig", globalConfig.getEntityConfig());
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        }
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, templatePath);
    }
}
