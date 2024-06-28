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

package top.fsfsfs.codegen.config;

import com.mybatisflex.core.util.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生成软件包的配置。
 *
 * @author 王帅
 * @since 2023-05-15
 */
@Data
@Accessors(chain = true)
public class PackageConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -8257632247633439537L;
    /**
     * 代码生成目录。
     */
    private String sourceDir;

    /**
     * 根包。
     */
    private String basePackage = "com.mybatisflex";
    /**
     * Vo 所在包。
     */
    private String voPackage;
    /**
     * Dto 所在包。
     */
    private String dtoPackage;
    /**
     * Query 所在包。
     */
    private String queryPackage;

    /**
     * Entity 所在包。
     */
    private String entityPackage;
    /**
     * Mapper 所在包。
     */
    private String mapperPackage;

    /**
     * Service 所在包。
     */
    private String servicePackage;

    /**
     * ServiceImpl 所在包。
     */
    private String serviceImplPackage;

    /**
     * Controller 所在包。
     */
    private String controllerPackage;

    /**
     * TableDef 所在包。
     */
    private String tableDefPackage;

    /**
     * MapperXml 文件所在位置。
     */
    private String mapperXmlPath;

    /**
     * 获取生成目录。
     */
    public String getSourceDir() {
        if (StringUtil.isBlank(sourceDir)) {
            return System.getProperty("user.dir");
        }
        return sourceDir;
    }


    /**
     * 获取实体类层包路径。
     */
    public String getEntityPackage() {
        if (StringUtil.isBlank(entityPackage)) {
            return basePackage.concat(".entity");
        }
        return entityPackage;
    }

    /**
     * 获取VO层包路径。
     */
    public String getVoPackage() {
        if (StringUtil.isBlank(voPackage)) {
            return basePackage.concat(".vo");
        }
        return voPackage;
    }


    /**
     * 获取DTO层包路径。
     */
    public String getDtoPackage() {
        if (StringUtil.isBlank(dtoPackage)) {
            return basePackage.concat(".dto");
        }
        return dtoPackage;
    }


    /**
     * 获取Query层包路径。
     */
    public String getQueryPackage() {
        if (StringUtil.isBlank(queryPackage)) {
            return basePackage.concat(".query");
        }
        return queryPackage;
    }


    /**
     * 获取映射层包路径。
     */
    public String getMapperPackage() {
        if (StringUtil.isBlank(mapperPackage)) {
            return basePackage.concat(".mapper");
        }
        return mapperPackage;
    }


    /**
     * 获取服务层包路径。
     */
    public String getServicePackage() {
        if (StringUtil.isBlank(servicePackage)) {
            return basePackage.concat(".service");
        }
        return servicePackage;
    }


    /**
     * 获取服务层实现包路径。
     */
    public String getServiceImplPackage() {
        if (StringUtil.isBlank(serviceImplPackage)) {
            if (StringUtil.isBlank(servicePackage)) {
                return getServicePackage().concat(".impl");
            }
            return servicePackage.concat(".impl");
        }
        return serviceImplPackage;
    }


    /**
     * 获取控制层包路径。
     */
    public String getControllerPackage() {
        if (StringUtil.isBlank(controllerPackage)) {
            return basePackage.concat(".controller");
        }
        return controllerPackage;
    }


    /**
     * 获取表定义层包路径。
     */
    public String getTableDefPackage() {
        if (StringUtil.isBlank(tableDefPackage)) {
            return getEntityPackage().concat(".table");
        }
        return tableDefPackage;
    }

    /**
     * 获取 Mapper XML 文件路径。
     */
    public String getMapperXmlPath() {
        if (StringUtil.isBlank(mapperXmlPath)) {
            return "/mapper";
        }
        return mapperXmlPath;
    }

}
