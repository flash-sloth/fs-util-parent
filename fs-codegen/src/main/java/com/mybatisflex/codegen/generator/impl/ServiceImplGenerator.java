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
import com.mybatisflex.codegen.config.ServiceImplConfig;
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

    private String templateContent;
    private GenTypeEnum genType;

    public ServiceImplGenerator() {
        this(GenTypeEnum.SERVICE_IMPL);
    }

    public ServiceImplGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getPath(GlobalConfig globalConfig, boolean absolute) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig config = globalConfig.getServiceImplConfig();
        String layerPackage = packageConfig.getServiceImplPackage();
        String sourceDir = config.getSourceDir();


        String path = null;
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

        if (!globalConfig.isServiceImplGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig serviceImplConfig = globalConfig.getServiceImplConfig();

        String serviceImplPackagePath = getPath(globalConfig, true);
        File serviceImplJavaFile = new File(serviceImplPackagePath, table.buildServiceImplClassName() + StrPool.DOT_JAVA);

        if (serviceImplJavaFile.exists() && !serviceImplConfig.getOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("serviceImplConfig", serviceImplConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.putAll(globalConfig.getCustomConfig());

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templateContent, serviceImplJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), serviceImplJavaFile);
        }

        log.info("ServiceImpl ---> {}", serviceImplJavaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ServiceImplConfig serviceImplConfig = globalConfig.getServiceImplConfig();

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("serviceImplConfig", serviceImplConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        }
        return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());

    }
}
