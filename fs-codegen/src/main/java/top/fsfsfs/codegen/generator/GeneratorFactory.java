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
package top.fsfsfs.codegen.generator;

import top.fsfsfs.codegen.constant.GenTypeConst;
import top.fsfsfs.codegen.constant.GenTypeEnum;
import top.fsfsfs.codegen.generator.impl.ControllerGenerator;
import top.fsfsfs.codegen.generator.impl.DtoGenerator;
import top.fsfsfs.codegen.generator.impl.EntityBaseGenerator;
import top.fsfsfs.codegen.generator.impl.EntityGenerator;
import top.fsfsfs.codegen.generator.impl.MapperGenerator;
import top.fsfsfs.codegen.generator.impl.MapperXmlGenerator;
import top.fsfsfs.codegen.generator.impl.QueryGenerator;
import top.fsfsfs.codegen.generator.impl.ServiceGenerator;
import top.fsfsfs.codegen.generator.impl.ServiceImplGenerator;
import top.fsfsfs.codegen.generator.impl.TableDefGenerator;
import top.fsfsfs.codegen.generator.impl.VoGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代码生成器工厂，用于创建各种类型文件的生成。
 *
 * @author tangyh
 * @see GenTypeConst
 */
public class GeneratorFactory {

    private static final Map<GenTypeEnum, IGenerator> GENERATORS = new LinkedHashMap<>();

    static {
        registerGenerator(GenTypeEnum.ENTITY, new EntityGenerator());
        registerGenerator(GenTypeEnum.ENTITY_BASE, new EntityBaseGenerator());
        registerGenerator(GenTypeEnum.DTO, new DtoGenerator());
        registerGenerator(GenTypeEnum.QUERY, new QueryGenerator());
        registerGenerator(GenTypeEnum.VO, new VoGenerator());
        registerGenerator(GenTypeEnum.MAPPER, new MapperGenerator());
        registerGenerator(GenTypeEnum.SERVICE, new ServiceGenerator());
        registerGenerator(GenTypeEnum.SERVICE_IMPL, new ServiceImplGenerator());
        registerGenerator(GenTypeEnum.CONTROLLER, new ControllerGenerator());
        registerGenerator(GenTypeEnum.TABLE_DEF, new TableDefGenerator());
        registerGenerator(GenTypeEnum.MAPPER_XML, new MapperXmlGenerator());
//        registerGenerator(GenTypeConst.PACKAGE_INFO, new PackageInfoGenerator());
    }

    private GeneratorFactory() {
    }

    /**
     * 获取指定类型文件的生成器。
     *
     * @param genType 生成类型
     * @return 该类型的文件生成器
     */
    public static IGenerator getGenerator(GenTypeEnum genType) {
        return GENERATORS.get(genType);
    }

    /**
     * 获取所有的文件生成器。
     *
     * @return 所有的文件生成器
     */
    public static Collection<IGenerator> getGenerators() {
        return GENERATORS.values();
    }

    /**
     * 注册文件生成器。
     *
     * @param name      生成器名称
     * @param generator 生成器
     */
    public static void registerGenerator(GenTypeEnum name, IGenerator generator) {
        GENERATORS.put(name, generator);
    }

}
