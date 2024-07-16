import type { VxeFormPropTypes } from 'vxe-table';

/** @returns 表单校验规则 */
export const formRules = (): VxeFormPropTypes.Rules => {
  return {};
};

/** @returns 返回表单配置 */
export const formItems = (): VxeFormPropTypes.Items => {
  return [
#for(column : table.getSortedFormColumns())
    { field: '#(column.property)', title: '#(column.propertyConfig?.swaggerDescription)', span: 24, itemRender: { name: '#(column.formConfig?.componentType)' } },
#end
  ];
};

export interface Emits {
  (e: 'success'): void;
}
