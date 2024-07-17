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
package top.fsfsfs.codegen.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 生成类型常量。
 *
 * @author tangyh
 * @since 2024年06月27日22:28:26
 */
@Getter
public enum GenTypeEnum {
    /** 代码生成模版 */
    DTO(GenTypeConst.DTO, TemplateConst.DTO),
    QUERY(GenTypeConst.QUERY, TemplateConst.QUERY),
    VO(GenTypeConst.VO, TemplateConst.VO),
    ENTITY(GenTypeConst.ENTITY, TemplateConst.ENTITY),
    ENTITY_BASE(GenTypeConst.ENTITY_BASE, TemplateConst.ENTITY),
    TABLE_DEF(GenTypeConst.TABLE_DEF, TemplateConst.TABLE_DEF),
    MAPPER(GenTypeConst.MAPPER, TemplateConst.MAPPER),
    MAPPER_XML(GenTypeConst.MAPPER_XML, TemplateConst.MAPPER_XML),
    SERVICE(GenTypeConst.SERVICE, TemplateConst.SERVICE),
    SERVICE_IMPL(GenTypeConst.SERVICE_IMPL, TemplateConst.SERVICE_IMPL),
    CONTROLLER(GenTypeConst.CONTROLLER, TemplateConst.CONTROLLER),
    // 前端
    API_TS(GenTypeConst.API_TS, TemplateConst.API_TS),
    MODEL_TS(GenTypeConst.MODEL_TS, TemplateConst.MODEL_TS),
    INDEX_TSX(GenTypeConst.INDEX_TSX, TemplateConst.INDEX_TSX),
    INDEX_VUE(GenTypeConst.INDEX_VUE, TemplateConst.INDEX_VUE),
    FORM_TSX(GenTypeConst.FORM_TSX, TemplateConst.FORM_TSX),
    FORM_VUE(GenTypeConst.FORM_VUE, TemplateConst.FORM_VUE),
    WRAPPER_VUE(GenTypeConst.WRAPPER_VUE, TemplateConst.WRAPPER_VUE),
    ;

    public final static List<GenTypeEnum> FRONT_LIST = Arrays.asList(INDEX_TSX, INDEX_VUE, FORM_TSX, FORM_VUE, WRAPPER_VUE, API_TS, MODEL_TS);
    public final static List<GenTypeEnum> BACKEND_LIST = Arrays.asList(DTO, QUERY, VO, ENTITY, ENTITY_BASE, TABLE_DEF, MAPPER, MAPPER_XML, SERVICE, SERVICE_IMPL, CONTROLLER);

    GenTypeEnum(String type, String template) {
        this.type = type;
        this.template = template;
    }

    private final String type;
    private final String template;

}