/*
 *  Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
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
import com.mybatisflex.codegen.config.MapperXmlConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.constant.GenTypeEnum;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.IGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.fsfsfs.basic.utils.StrPool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * MapperXml 生成器。
 *
 * @author 王帅
 * @since 2023-05-17
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class MapperXmlGenerator implements IGenerator {

    private String templateContent;
    private GenTypeEnum genType;

    public MapperXmlGenerator() {
        this(GenTypeEnum.MAPPER_XML);
    }

    public MapperXmlGenerator(GenTypeEnum genType) {
        this.genType = genType;
    }

    @Override
    public String getPath(GlobalConfig globalConfig, boolean absolute) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        String path = "";

        if (absolute) {
            path = packageConfig.getSourceDir();
            if (!path.endsWith(File.separator)) {
                path += File.separator;
            }
        }

        path += StrPool.SRC_MAIN_RESOURCES + File.separator;
        path += packageConfig.getMapperXmlPath();
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isMapperXmlGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        MapperXmlConfig mapperXmlConfig = globalConfig.getMapperXmlConfig();

        String path = getPath(globalConfig, true);
        File mapperXmlFile = new File(path, table.buildMapperXmlFileName() + StrPool.DOT_XML);

        if (mapperXmlFile.exists() && !mapperXmlConfig.getOverwriteEnable()) {
            return;
        }

        Map<String, Object> params = new HashMap<>(2);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generate(params, templateContent, mapperXmlFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), mapperXmlFile);
        }

        log.info("MapperXML ---> {}", mapperXmlFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();

        Map<String, Object> params = new HashMap<>(2);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            return globalConfig.getTemplateConfig().getTemplate().previewByContent(params, templateContent);
        } else {
            return globalConfig.getTemplateConfig().getTemplate().previewByFile(params, genType.getTemplate());
        }
    }


}
