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
import com.mybatisflex.codegen.config.MapperConfig;
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
 * Mapper 生成器。
 *
 * @author Michael Yang
 * @author 王帅
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class MapperGenerator implements IGenerator {

    private String templateContent;
    private GenTypeEnum genType;

    public MapperGenerator() {
        this(GenTypeEnum.MAPPER);
    }

    public MapperGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getPath(GlobalConfig globalConfig, boolean absolute) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        MapperConfig config = globalConfig.getMapperConfig();
        String layerPackage = packageConfig.getMapperPackage();
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

        if (!globalConfig.isMapperGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        MapperConfig mapperConfig = globalConfig.getMapperConfig();

        String mapperPackagePath = getPath(globalConfig, true);
        File mapperJavaFile = new File(mapperPackagePath, table.buildMapperClassName() + ".java");

        if (mapperJavaFile.exists() && !mapperConfig.getOverwriteEnable()) {
            return;
        }


        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("mapperConfig", mapperConfig);
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.putAll(globalConfig.getCustomConfig());

        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templateContent, mapperJavaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), mapperJavaFile);
        }

        log.info("Mapper ---> {}", mapperJavaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        MapperConfig mapperConfig = globalConfig.getMapperConfig();

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("mapperConfig", mapperConfig);
        params.put("packageConfig", packageConfig);
        params.put("javadocConfig", globalConfig.getJavadocConfig());
        params.putAll(globalConfig.getCustomConfig());

        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        } else {
            return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());
        }
    }
}
