<script setup lang="ts">
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import type { VxeFormInstance, VxeFormPropTypes } from 'vxe-table';
import { useLoading } from '@sa/hooks';
#for(importClass : config.buildFormVueImports(globalConfig, table))
import #(importClass);
#end
import { $t } from '@/locales';
import { actionCode } from '@/utils/common';
import type { FormInstance } from '@/typings/fs';
import { formItems, formRules } from '../data/form';

type FormDataType = #(dtoClassName);
const formRef = ref<VxeFormInstance>();
const formLoading = useLoading(false);
const formAction = ref<string>(actionCode.add);

const formConfig = reactive<{
  formData: FormDataType;
  formItems: VxeFormPropTypes.Items;
  formRules: VxeFormPropTypes.Rules;
  /** 表单只读状态 */
  readonly: boolean;
}>({
  formData: {} as FormDataType,
  formRules: formRules(),
  formItems: formItems(),
  readonly: false
});

/** 加载数据，并将数据设置到表单中 */
async function loadDataAndSetFormData(data?: FormDataType) {
  if (data && data.id) {
    try {
      formLoading.startLoading();
      formConfig.formData = await getById(data.id);
    } finally {
      formLoading.endLoading();
    }
  }
}

/** 初始化新增 */
async function initAdd() {}
/**
 * 初始化修改
 *
 * @param data
 */
async function initEdit(data?: FormDataType) {
  await loadDataAndSetFormData(data);
}
/**
 * 初始化复制
 *
 * @param data
 */
async function initCopy(data?: FormDataType) {
  await loadDataAndSetFormData(data);
  formConfig.formData.id = undefined;
}

async function initView(data?: FormDataType) {
  await loadDataAndSetFormData(data);
  formConfig.readonly = true;
}

async function init(action: string, data?: FormDataType) {
  formAction.value = action;
  // 清空数据
  formConfig.formData = {} as FormDataType;
  formConfig.readonly = false;
  switch (action) {
    case actionCode.add:
      await initAdd();
      break;
    case actionCode.edit:
      await initEdit(data);
      break;
    case actionCode.copy:
      await initCopy(data);
      break;
    case actionCode.view:
    default:
      await initView(data);
  }
}

/**
 * 修改时提交
 *
 * @param params 参数
 */
async function submitEdit(params: FormDataType) {
  await update(params);
  message.success($t('common.modifySuccess'));
}
/**
 * 增加时提交
 *
 * @param params 参数
 */
async function submitAdd(params: FormDataType) {
  await save(params);
  message.success($t('common.addSuccess'));
}
/**
 * 复制时提交
 *
 * @param params 参数
 */
async function submitCopy(params: FormDataType) {
  await save(params);
  message.success($t('common.modifySuccess'));
}

const handleSubmit = async () => {
  await formRef.value?.validate();
  try {
    formLoading.startLoading();
    const params = { ...formConfig.formData };
    switch (formAction.value) {
      case actionCode.add:
        await submitAdd(params);
        break;
      case actionCode.edit:
        await submitEdit(params);
        break;
      case actionCode.copy:
        await submitCopy(params);
        break;
      case actionCode.view:
      default:
        break;
    }
    return true;
  } catch (_e) {
    return false;
  } finally {
    formLoading.endLoading();
  }
};

defineExpose<FormInstance<FormDataType>>({ init, handleSubmit });
</script>

<template>
  <VxeForm
    ref="formRef"
    :loading="formLoading.loading.value"
    :rules="formConfig.formRules"
    :data="formConfig.formData"
    :items="formConfig.formItems"
    title-colon
  ></VxeForm>
</template>

<style scoped></style>
