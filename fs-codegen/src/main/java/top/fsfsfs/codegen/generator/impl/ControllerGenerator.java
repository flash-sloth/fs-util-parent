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

import cn.hutool.core.util.StrUtil;
import top.fsfsfs.codegen.config.ControllerConfig;
import top.fsfsfs.codegen.config.GlobalConfig;
import top.fsfsfs.codegen.config.PackageConfig;
import top.fsfsfs.codegen.constant.GenTypeEnum;
import top.fsfsfs.codegen.entity.Table;
import top.fsfsfs.codegen.generator.IGenerator;
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
 * Controller 生成器。
 *
 * @author 王帅
 * @since 2023-05-14
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class ControllerGenerator implements IGenerator {

    private String templateContent;
    private GenTypeEnum genType;


    public ControllerGenerator() {
        this(GenTypeEnum.CONTROLLER);
    }

    public ControllerGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getFilePath(Table table, GlobalConfig globalConfig, boolean absolute) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        String layerPackage = packageConfig.getControllerPackage();
        ControllerConfig config = globalConfig.getControllerConfig();
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
        path += table.buildControllerClassName() + StrPool.DOT_JAVA;
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isControllerGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ControllerConfig controllerConfig = globalConfig.getControllerConfig();

        String path = getFilePath(table, globalConfig, true);
        File controllerJavaFile = new File(path);

        if (controllerJavaFile.exists() && !controllerConfig.getOverwriteEnable()) {
            return;
        }


        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("controllerConfig", controllerConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("withSwagger", globalConfig.isEntityWithSwagger());
        params.put("swaggerVersion", globalConfig.getSwaggerVersion());
        params.putAll(globalConfig.getCustomConfig());

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, controllerJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), controllerJavaFile);
        }

        log.info("Controller ---> {}", controllerJavaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        ControllerConfig controllerConfig = globalConfig.getControllerConfig();

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.put("controllerConfig", controllerConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.put("withSwagger", globalConfig.isEntityWithSwagger());
        params.put("swaggerVersion", globalConfig.getSwaggerVersion());
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        } else {
            return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());
        }
    }


}
