/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.fsfsfs.codegen.generator.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.fsfsfs.basic.utils.StrPool;
import top.fsfsfs.codegen.config.GlobalConfig;
import top.fsfsfs.codegen.config.PackageConfig;
import top.fsfsfs.codegen.config.QueryConfig;
import top.fsfsfs.codegen.constant.GenTypeEnum;
import top.fsfsfs.codegen.constant.GenerationStrategyEnum;
import top.fsfsfs.codegen.entity.Table;
import top.fsfsfs.codegen.generator.IGenerator;
import top.fsfsfs.util.utils.DateUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.date.DatePattern.CHINESE_DATE_TIME_PATTERN;

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
            if (!path.endsWith(StrPool.SLASH)) {
                path += StrPool.SLASH;
            }
        }

        path += StrPool.SRC_MAIN_JAVA + StrPool.SLASH;
        path += layerPackage.replace(StrPool.DOT, StrPool.SLASH) + StrPool.SLASH;
        path += table.buildQueryClassName() + StrPool.DOT_JAVA;
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig, String templateContent) {
        if (!globalConfig.isQueryGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        QueryConfig config = globalConfig.getQueryConfig();

        if (config.getGenerationStrategy() == GenerationStrategyEnum.IGNORE) {
            return;
        }

        String path = getFilePath(table, globalConfig, true);
        File javaFile = new File(path);

        if (config.getGenerationStrategy() == GenerationStrategyEnum.EXIST_IGNORE) {
            if (javaFile.exists()) {
                return;
            }
        }

        String queryClassName = table.buildQueryClassName();
        if (javaFile.exists()) {
            if (config.getGenerationStrategy() == GenerationStrategyEnum.BACKUPS) {
                String now = DateUtils.format(LocalDateTime.now(), CHINESE_DATE_TIME_PATTERN);
                String newPath = StrUtil.replaceLast(path, StrPool.DOT_JAVA, "_Backups" + now + StrPool.DOT_JAVA);
                File newFile = new File(newPath);
                FileUtil.copy(javaFile, newFile, true);
            } else if (config.getGenerationStrategy() == GenerationStrategyEnum.ADD) {
                String now = DateUtils.format(LocalDateTime.now(), CHINESE_DATE_TIME_PATTERN);
                String newPath = StrUtil.replaceLast(path, StrPool.DOT_JAVA, "_Add" + now + StrPool.DOT_JAVA);
                javaFile = new File(newPath);
                queryClassName += "_Add" + now;
            }
        }

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, config, queryClassName);

        log.info("Query ---> {}", javaFile);
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, javaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), javaFile);
        }
    }

    private static Map<String, Object> buildParam(Table table, GlobalConfig globalConfig, PackageConfig packageConfig, QueryConfig queryConfig, String queryClassName) {
        Map<String, Object> params = new HashMap<>();
        params.put("table", table);
        params.put("queryClassName", queryClassName);
        params.put("queryPackageName", packageConfig.getQueryPackage());
        params.put("queryConfig", queryConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("packageConfig", packageConfig);
        params.put("globalConfig", globalConfig);
        return params;
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        QueryConfig queryConfig = globalConfig.getQueryConfig();

        Map<String, Object> params = buildParam(table, globalConfig, packageConfig, queryConfig, table.buildQueryClassName());

        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, this.genType.getTemplate());
    }
}
