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
import top.fsfsfs.codegen.config.ServiceImplConfig;
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
 * ServiceImpl 生成器。
 *
 * @author 王帅
 * @since 2023-05-14
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class ServiceImplGenerator implements IGenerator {

    private GenTypeEnum genType;

    public ServiceImplGenerator() {
        this(GenTypeEnum.SERVICE_IMPL);
    }

    public ServiceImplGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getFilePath(Table table, GlobalConfig globalConfig, boolean absolute) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig config = globalConfig.getServiceImplConfig();
        String layerPackage = packageConfig.getServiceImplPackage();
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
        path += table.buildServiceImplClassName() + StrPool.DOT_JAVA;
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig, String templateContent) {
        if (!globalConfig.isServiceImplGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig config = globalConfig.getServiceImplConfig();

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

        String serviceClassName = table.buildServiceImplClassName();
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
                serviceClassName += "_Add" + now;
            }
        }

        Map<String, Object> params = getParams(table, globalConfig, packageConfig, serviceClassName, config);

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, javaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), javaFile);
        }

        log.info("ServiceImpl ---> {}", javaFile);
    }

    private static Map<String, Object> getParams(Table table, GlobalConfig globalConfig, PackageConfig packageConfig, String serviceClassName, ServiceImplConfig config) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("serviceImplClassName", serviceClassName);
        params.put("serviceImplConfig", config);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.putAll(globalConfig.getCustomConfig());
        return params;
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig config = globalConfig.getServiceImplConfig();

        Map<String, Object> params = getParams(table, globalConfig, packageConfig, table.buildServiceImplClassName(), config);
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());

    }
}
