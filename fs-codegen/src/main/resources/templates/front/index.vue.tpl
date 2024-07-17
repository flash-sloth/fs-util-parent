<script lang="ts" setup>
import { reactive, ref } from 'vue';
import type { VxeGridConstructor, VxeGridDefines, VxeGridInstance, VxeGridPropTypes, VxeGridProps } from 'vxe-table';
import { VxeGrid } from 'vxe-table';
import { VxeGridProxyEventCode } from '@/enum';
import { defGridConfig } from '@/constants/vxeUiCurdDefConfig';
import { useDmSwitcher } from '@/components/fs/drawer-modal-switcher';

#for(importClass : config.buildIndexVueImports(globalConfig, table))
import #(importClass);
#end
import { useMessage } from '@/hooks/web/useMessage';

import { queryBefore } from '@/plugins/vxe-table/common';
import { $t } from '@/locales';
import { actionCode } from '@/utils/common';
import { columns, searchFormConfig } from './data/index';
import FormWrapper from './modules/wrapper.vue';

// 获取表格实例
type FormDataType = #(voClassName);
const xGrid = ref<VxeGridInstance<FormDataType>>();
const [register, { show: showForm }] = useDmSwitcher<Partial<FormDataType>>();
const { createConfirm, createMessage } = useMessage();

const formRef = ref();
const gridOptions = reactive<VxeGridProps<FormDataType>>(
  defGridConfig<FormDataType>({
    columns: columns({
      title: $t('common.action'),
      fixed: 'right',
      width: 200,
      cellRender: {
        name: 'VxeButtonGroup',
        props: {
          mode: 'text'
        },
        options: [
          { content: $t('common.view'), name: actionCode.view },
          { content: $t('common.copy'), name: actionCode.copy },
          { content: $t('common.edit'), name: actionCode.edit },
          { content: $t('common.delete'), status: 'error', name: actionCode.delete }
        ],
        events: {
          click({ row }, { name }) {
            switch (name) {
              case actionCode.view:
                handleView(row);
                break;
              case actionCode.copy:
                handleCopy(row);
                break;
              case actionCode.edit:
                handleEdit(row);
                break;
              case actionCode.delete:
              default:
                confirmRemove([row.id]);
                break;
            }
          }
        }
      }
    }),
    // 搜索表单
    formConfig: searchFormConfig(),
    pagerConfig: {},
    proxyConfig: { ajax: { query: loadData } },
    toolbarConfig: {
      buttons: [
        { code: actionCode.add, name: $t('common.add'), icon: 'vxe-icon-add' },
        { code: actionCode.deleteBatch, name: $t('common.batchDelete'), icon: 'vxe-icon-delete' }
      ]
    }
  })
);

/**
 * 加载数据
 *
 * @param params
 */
async function loadData(params: VxeGridPropTypes.ProxyAjaxQueryParams) {
  const param: Model.PageParam<#(queryClassName)> = queryBefore(params);
  return await page(param);
}

/** 重新加载数据 */
async function reloadData() {
  const $grid = xGrid.value;
  if ($grid) {
    $grid.commitProxy(VxeGridProxyEventCode.QUERY);
  }
}

/** 处理新增事件 */
function handleAdd() {
  showForm({ action: actionCode.add, data: {} });
}

/**
 * 处理复制事件
 *
 * @param row 参数
 */
function handleCopy(row: FormDataType) {
  showForm({ action: actionCode.copy, data: { ...row } });
}

/**
 * 处理编辑事件
 *
 * @param row 参数
 */
function handleEdit(row: FormDataType) {
  showForm({ action: actionCode.edit, data: { ...row } });
}

/**
 * 处理查看事件
 *
 * @param row 参数
 */
function handleView(row: FormDataType) {
  showForm({ action: actionCode.view, data: { ...row } });
}

function handleRemove($grid: VxeGridConstructor<FormDataType>) {
  const checkedRows = $grid.getCheckboxRecords();
  if (!checkedRows || checkedRows.length === 0) {
    createMessage.error($t('common.chooseText', [$t('common.deleteData')]));
  } else {
    confirmRemove(checkedRows.map((x: FormDataType) => x.id));
  }
}

function confirmRemove(ids: string[]) {
  createConfirm({
    iconType: 'warning',
    title: $t('common.tip'),
    content: $t('common.confirmDelete'),
    onOk: async () => {
      await remove(ids);
      createMessage.success($t('common.deleteSuccess'));
      await reloadData();
    }
  });
}

function toolbarButtonClick({ code, $grid }: VxeGridDefines.ToolbarButtonClickEventParams<FormDataType>) {
  switch (code) {
    case actionCode.deleteBatch:
      handleRemove($grid);
      break;
    case actionCode.add:
      handleAdd();
      break;
    default:
      break;
  }
}
</script>

<template>
  <div class="h-full p-2">
    <VxeGrid ref="xGrid" v-bind="gridOptions" @toolbar-button-click="toolbarButtonClick"></VxeGrid>
    <FormWrapper ref="formRef" @register="register" @success="reloadData"></FormWrapper>
  </div>
</template>
