#for(importClass : config.buildIndexTsxImports(globalConfig, table))
import #(importClass);
#end


/**
 * @param actionColumn 用户定义的 操作列
 * @returns 返回默认列和用户操作列的集合
 */
export const columns = (action: VxeTableDefines.ColumnOptions): VxeGridPropTypes.Columns<#(voClassName)> => {
  return [
    { type: 'checkbox', width: 50, fixed: 'left' },
    { type: 'seq', width: 50, fixed: 'left' },
#for(column : table.getSortedListColumns())
    {
      field: '#(column.property)',
      title: '#(column.swaggerComment)'
    },
#end
    action
  ];
};

/** 搜索表单配置 */
export const searchFormConfig = (): VxeGridPropTypes.FormConfig => {
  return {
    items: [
#for(column : table.getSortedListColumns())
      { field: '#(column.property)', title: '#(column.swaggerComment)', itemRender: { name: '#(column.tsType)' } },
#end
      {
        itemRender: {
          name: 'VxeButtonGroup',
          options: [
            {
              type: 'submit',
              content: '搜索',
              status: 'primary'
            },
            { type: 'reset', content: '重置' }
          ]
        }
      }
    ]
  };
};
