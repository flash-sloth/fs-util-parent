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
package top.fsfsfs.codegen.generator.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.fsfsfs.basic.utils.StrPool;
import top.fsfsfs.codegen.config.GlobalConfig;
import top.fsfsfs.codegen.config.MapperXmlConfig;
import top.fsfsfs.codegen.config.PackageConfig;
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
    public String getFilePath(Table table, GlobalConfig globalConfig, boolean absolute) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        String path = "";

        if (absolute) {
            path = packageConfig.getSourceDir();
            if (!path.endsWith(File.separator)) {
                path += File.separator;
            }
        }

        path += StrPool.SRC_MAIN_RESOURCES + File.separator;
        path += packageConfig.getMapperXmlPath() + File.separator;
        path += table.buildMapperXmlFileName() + StrPool.DOT_XML;
        return path;
    }

    @Override
    public void generate(Table table, GlobalConfig globalConfig) {

        if (!globalConfig.isMapperXmlGenerateEnable()) {
            return;
        }

        PackageConfig packageConfig = globalConfig.getPackageConfig();
        MapperXmlConfig config = globalConfig.getMapperXmlConfig();

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
            }
        }

        Map<String, Object> params = new HashMap<>(4);
        params.put("table", table);
        params.put("packageConfig", packageConfig);
        params.putAll(globalConfig.getCustomConfig());
        if (StrUtil.isNotEmpty(templateContent)) {
            globalConfig.getTemplateConfig().getTemplate().generateByContent(params, templateContent, javaFile);
        } else {
            globalConfig.getTemplateConfig().getTemplate().generate(params, genType.getTemplate(), javaFile);
        }

        log.info("MapperXML ---> {}", javaFile);
    }

    @Override
    public String preview(Table table, GlobalConfig globalConfig) {

        PackageConfig packageConfig = globalConfig.getPackageConfig();

        Map<String, Object> params = new HashMap<>(4);
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
