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
import com.mybatisflex.codegen.config.QueryConfig;
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
 * Query（查询方法入参） 生成器。
 *
 * @author tangyh
 * @since 2024年06月18日15:48:18
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class QueryGenerator implements IGenerator {

    private String templateContent;
    private GenTypeEnum genType;

    public QueryGenerator() {
        this(GenTypeEnum.QUERY);
    }

    public QueryGenerator(GenTypeEnum getType) {
        this.genType = getType;
    }


    @Override
    public String getFilePath(Table table, GlobalConfig globalConfig, boolean absolute) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        QueryConfig config = globalConfig.getQueryConfig();
        String layerPackage = packageConfig.getQueryPackage();
        String sourceDir = config.getSourceDir();


        String path = "";
        if (absolute) {
            path = StringUtil.isNotBlank(sourceDir) ? sourceDir : packageConfig.getSourceDir();
            if (!path.endsWith(File.separator)) {
                path += File.separator;
            }
        }

        path += StrPool.SRC_MAIN_JAVA + File.separator;
        path += layerPackage.replace(StrPool.DOT, StrPool.SLASH) + File.separator;
        path += table.buildQueryClassName() + StrPool.DOT_JAVA;
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {
        if (!globalConfig.isQueryGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        QueryConfig queryConfig = globalConfig.getQueryConfig();

        String packagePath = getFilePath(table, globalConfig, true);
        File javaFile = new File(packagePath);

        if (javaFile.exists() && !queryConfig.getOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, queryConfig);

        log.info("Query ---> {}", javaFile);
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, this.getTemplateContent(), javaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), javaFile);
        }
    }

    private static Map<String, Object> buildParam(Table table, GlobalConfig globalConfig, PackageConfig packageConfig, QueryConfig queryConfig) {
        Map<String, Object> params = new HashMap<>(7);
        params.put("table", table);
        params.put("queryPackageName", packageConfig.getQueryPackage());
        params.put("queryConfig", queryConfig);
        params.put("queryClassName", table.buildQueryClassName());
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("packageConfig", packageConfig);
        params.put("globalConfig", globalConfig);
        return params;
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        QueryConfig queryConfig = globalConfig.getQueryConfig();

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, queryConfig);


        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        }
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, this.genType.getTemplate());
    }
}
