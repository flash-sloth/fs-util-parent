/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.mybatisflex.codegen.generator.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.constant.GenTypeConst;
import com.mybatisflex.codegen.constant.TemplateConst;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.IGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class VoGenerator implements IGenerator {

    private String templatePath;
    private String templateContent;
    private String genType;

    public VoGenerator() {
        this(TemplateConst.VO);
        this.genType = GenTypeConst.VO;
    }

    public VoGenerator(String templatePath) {
        this.templatePath = templatePath;
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
    public String getGenType() {
        return genType;
    }

    public IGenerator setGenType(String genType) {
        this.genType = genType;
        return this;
    }


    @Override
    public void generate(Table table, GlobalConfig globalConfig) {
        if (!globalConfig.isEntityGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        EntityConfig entityConfig = globalConfig.getEntityConfig();

        String entityPackagePath = packageConfig.getEntityPackage().replace(".", "/");
        File entityJavaFile = new File(packageConfig.getSourceDir(), entityPackagePath + "/" +
                table.buildEntityClassName() + "Vo.java");


        if (entityJavaFile.exists() && !entityConfig.isOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("entityConfig", entityConfig);
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());

        globalConfig.getTemplateConfig().getTemplate().generate(params, getTemplatePath(), entityJavaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        EntityConfig entityConfig = globalConfig.getEntityConfig();

        Map<String, Object> params = new HashMap<>(6);
        params.put("table", table);
        params.put("entityPackageName", packageConfig.getEntityPackage());
        params.put("entityConfig", entityConfig);
        params.put("entityClassName", table.buildEntityClassName());
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("isBase", false);

        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        }
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, this.templatePath);
    }
}
