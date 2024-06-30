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

import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.Serial;
import java.io.Serializable;

/**
 * 表字段的单独设置。
 * @author tangyh
 * @since 2024年06月28日11:43:13
 */
@Data
@Accessors(chain = true)
public class ColumnConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -1511605303951623381L;

    /**
     * 字段名称。
     */
    private String columnName;

    /**
     * insert 的时候默认值，这个值会直接被拼接到 sql 而不通过参数设置。
     */
    private String onInsertValue;

    /**
     * update 的时候自动赋值，这个值会直接被拼接到 sql 而不通过参数设置。
     */
    private String onUpdateValue;

    /**
     * 是否是大字段，大字段 APT 不会生成到 DEFAULT_COLUMNS 里。
     */
    private Boolean large;

    /**
     * 是否是逻辑删除字段，一张表中只能存在 1 一个逻辑删除字段。
     */
    private Boolean logicDelete;

    /**
     * 是否为乐观锁字段。
     */
    private Boolean version;

    /**
     * 是否是租户 ID。
     */
    private Boolean tenantId;

    /**
     * 配置的 jdbcType。
     */
    private JdbcType jdbcType;

    /**
     * <p>属性的类型。
     *
     * <p>原始类型直接写类型名称，例如：int/long/float/double/boolean<br/>
     * 对象类型请写对应类的全限定名，例如：java.lang.String/com.example.enums.Gender
     */
    private String propertyType;

    /**
     * 属性的默认值，例如：long 类型默认值：0L，枚举类型默认值：Gender.MALE。
     */
    private String propertyDefaultValue;

    /**
     * 自定义 TypeHandler。
     */
    private Class<? extends TypeHandler<?>> typeHandler;

    /**
     * 脱敏方式。
     */
    private String maskType;

    /**
     * 字段是否为主键。
     */
    private Boolean primaryKey = false;

    /**
     * ID 生成策略。
     */
    private KeyType keyType;

    /**
     * ID 生成器值。
     */
    private String keyValue;

    /**
     * sequence 序列执行顺序。
     */
    private Boolean keyBefore;


    public static ColumnConfig create() {
        return new ColumnConfig();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ColumnConfig columnConfig;

        private Builder() {
            this.columnConfig = new ColumnConfig();
        }

        public Builder columnName(String columnName) {
            this.columnConfig.setColumnName(columnName);
            return this;
        }

        public Builder onInsertValue(String onInsertValue) {
            this.columnConfig.setOnInsertValue(onInsertValue);
            return this;
        }

        public Builder onUpdateValue(String onUpdateValue) {
            this.columnConfig.setOnUpdateValue(onUpdateValue);
            return this;
        }

        public Builder large(Boolean large) {
            this.columnConfig.setLarge(large);
            return this;
        }

        public Builder logicDelete(Boolean logicDelete) {
            this.columnConfig.setLogicDelete(logicDelete);
            return this;
        }

        public Builder version(Boolean version) {
            this.columnConfig.setVersion(version);
            return this;
        }

        public Builder tenantId(Boolean tenantId) {
            this.columnConfig.setTenantId(tenantId);
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            this.columnConfig.setJdbcType(jdbcType);
            return this;
        }

        public Builder propertyType(String propertyType) {
            this.columnConfig.setPropertyType(propertyType);
            return this;
        }

        public Builder propertyDefaultValue(String propertyDefaultValue) {
            this.columnConfig.setPropertyDefaultValue(propertyDefaultValue);
            return this;
        }

        public Builder typeHandler(Class<? extends TypeHandler<?>> typeHandler) {
            this.columnConfig.setTypeHandler(typeHandler);
            return this;
        }

        public Builder maskType(String maskType) {
            this.columnConfig.setMaskType(maskType);
            return this;
        }

        public Builder keyType(KeyType keyType) {
            this.columnConfig.setKeyType(keyType);
            return this;
        }

        public Builder keyValue(String keyValue) {
            this.columnConfig.setKeyValue(keyValue);
            return this;
        }

        public Builder keyBefore(Boolean keyBefore) {
            this.columnConfig.setKeyBefore(keyBefore);
            return this;
        }

        public ColumnConfig build() {
            return this.columnConfig;
        }

    }

}
