/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.mybatisflex.codegen.generator.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.config.VoConfig;
import com.mybatisflex.codegen.constant.GenTypeConst;
import com.mybatisflex.codegen.constant.TemplateConst;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.IGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * VO（Controller层出参） 生成器。
 *
 * @author tangyh
 * @since 2024年06月18日15:48:18
 */
@Slf4j
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
        if (!globalConfig.isVoGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        VoConfig voConfig = globalConfig.getVoConfig();

        String packagePath = packageConfig.getVoPackage().replace(".", "/");
        File javaFile = new File(packageConfig.getSourceDir(), packagePath + "/" +
                table.buildVoClassName() + ".java");

        if (javaFile.exists() && !voConfig.isOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, voConfig);

        log.info("Vo ---> {}", javaFile);
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, getTemplateContent(), javaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, getTemplatePath(), javaFile);
        }
    }

    private static Map<String, Object> buildParam(Table table, GlobalConfig globalConfig, PackageConfig packageConfig, VoConfig voConfig) {
        Map<String, Object> params = new HashMap<>(7);
        params.put("table", table);
        params.put("voPackageName", packageConfig.getVoPackage());
        params.put("voConfig", voConfig);
        params.put("voClassName", table.buildVoClassName());
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("packageConfig", packageConfig);
        params.put("globalConfig", globalConfig);
        return params;
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        VoConfig voConfig = globalConfig.getVoConfig();

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, voConfig);


        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        }
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, this.templatePath);
    }
}
