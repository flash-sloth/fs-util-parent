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
package top.fsfsfs.codegen.dialect;

import com.mybatisflex.core.util.StringUtil;
import top.fsfsfs.codegen.entity.Column;
import top.fsfsfs.codegen.entity.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段类型映射。
 *
 * @author michael
 */
public class JdbcTypeMapping {

    private JdbcTypeMapping() {
    }

    private static final Map<String, String> mapping = new HashMap<>();
    private static JdbcTypeMapper typeMapper;

    static {
        registerMapping("java.sql.Timestamp", "java.time.LocalDateTime");
        registerMapping("java.sql.Time", "java.time.LocalTime");
        registerMapping("java.sql.Date", "java.time.LocalDate");
        registerMapping("[B", "byte[]");
        registerMapping("oracle.jdbc.OracleBlob", "byte[]");
    }

    public static void registerMapping(Class<?> from, Class<?> to) {
        registerMapping(from.getName(), to.getName());
    }

    public static void registerMapping(String from, String to) {
        mapping.put(from, to);
    }

    public static Map<String, String> getMapping() {
        return mapping;
    }

    public static JdbcTypeMapper getTypeMapper() {
        return typeMapper;
    }

    public static void setTypeMapper(JdbcTypeMapper typeMapper) {
        JdbcTypeMapping.typeMapper = typeMapper;
    }

    /**
     * 当只使用 date 类型来映射数据库的所有 "时间" 类型时，调用此方法
     */
    public static void registerDateTypes() {
        registerMapping("java.sql.Time", "java.util.Date");
        registerMapping("java.sql.Timestamp", "java.util.Date");
        registerMapping("java.time.LocalDateTime", "java.util.Date");
        registerMapping("java.time.LocalDate", "java.util.Date");
    }

    public static String getType(String rawType, String jdbcType, Table table, Column column) {
        if (typeMapper != null) {
            String type = typeMapper.getType(rawType, jdbcType, table, column);
            if (StringUtil.isNotBlank(type)) {
                return type;
            }
        }
        String registered = mapping.get(jdbcType);
        return StringUtil.isNotBlank(registered) ? registered : jdbcType;
    }

    public interface JdbcTypeMapper {
        /**
         * 获取字段的实体类类型
         *
         * @param rawType 数据库原始类型
         * @param javaType 自动推断的实体类类型
         * @param table 表
         * @param column 列
         * @return 实际的实体类类型
         */
        String getType(String rawType, String javaType, Table table, Column column);
    }

}
