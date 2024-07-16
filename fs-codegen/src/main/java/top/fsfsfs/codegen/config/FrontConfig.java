/*
 *  Copyright (c) 2024.  flash-sloth (244387066@qq.com).
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

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.fsfsfs.codegen.config.front.ButtonConfig;
import top.fsfsfs.codegen.entity.Table;

import java.io.Serial;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 前端配置。
 *
 * @author tangyh
 * @since 2024年06月25日14:30:32
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class FrontConfig extends BaseConfig {

    @Serial
    private static final long serialVersionUID = -6790274333595436008L;

    /** index页面的按钮。 */
    private List<ButtonConfig> buttonConfigList;
    /**
     * 表单打开方式。
     */
    private OpenMode openMode = OpenMode.MODAL;
    /**
     * 布局方式。
     */
    private Layout layout = Layout.SIMPLE;
    /**
     * 是否启用国际化。
     */
    private Boolean i18n = true;
    /**
     * 页面缓存。
     */
    private Boolean keepAlive = true;
    /**
     * 复选框。
     */
    private Boolean checkbox = true;

    /**
     * 单选框。
     */
    private Boolean radio = false;

    public enum OpenMode {
        /** 弹窗、抽屉、新窗口 */
        MODAL, DRAWER, WINDOW;
    }

    public enum Layout {
        /** 单表、树、主从 */
        SIMPLE, TREE, SLAVE;
    }


    public List<String> buildIndexTsxImports(GlobalConfig globalConfig, Table table) {
        Set<String> imports = new HashSet<>();
        imports.add("type { VxeGridPropTypes, VxeTableDefines } from 'vxe-table'");
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        imports.add(StrUtil.format("type { {} } from '@/service/{}/{}/{}/model'", table.buildVoClassName(),
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }

    public List<String> buildFormVueImports(GlobalConfig globalConfig, Table table) {
        Set<String> imports = new HashSet<>();
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        imports.add(StrUtil.format("import { getById, save, update } from '@/service/{}/{}/{}/api'",
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));
        imports.add(StrUtil.format("import type { {} } from '@/service/{}/{}/{}/model'", table.buildDtoClassName(),
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }
    public List<String> buildWrapperVueImports(GlobalConfig globalConfig, Table table) {
        Set<String> imports = new HashSet<>();
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        imports.add(StrUtil.format("import type { {} } from '@/service/{}/{}/{}/model'", table.buildVoClassName(),
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }
    public List<String> buildIndexVueImports(GlobalConfig globalConfig, Table table) {
        Set<String> imports = new HashSet<>();
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        imports.add(StrUtil.format("import { page, remove } from '@/service/{}/{}/{}/api'",
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));
        imports.add(StrUtil.format("import type { {}, {} } from '@/service/{}/{}/{}/model'", table.buildQueryClassName(), table.buildVoClassName(),
                packageConfig.getSubSystem(), packageConfig.getModule(), StringUtil.firstCharToLowerCase(table.buildEntityClassName())));

        return imports.stream().filter(Objects::nonNull).sorted(Comparator.naturalOrder()).toList();
    }

}
