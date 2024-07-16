<script setup lang="ts">
import { computed, defineEmits, ref } from 'vue';
import { FsAModal, useDmSwitcherInner } from '@/components/fs/drawer-modal-switcher';
#for(importClass : config.buildWrapperVueImports(globalConfig, table))
import #(importClass);
#end
import type { FormInstance } from '@/typings/fs';
import { actionCode, titleMap } from '@/utils/common';
import type { Emits } from '../data/form';
import Form from './form.vue';

type FormDataType = #(voClassName);
const emit = defineEmits<Emits>();
const formRef = ref<FormInstance<FormDataType>>();
const actionRef = ref<string>(actionCode.add);

const getTitle = computed(() => {
  return titleMap[actionRef.value] || titleMap.add;
});

const [register, { close }] = useDmSwitcherInner<FormDataType>(async ({ action, data }) => {
  actionRef.value = action;
  formRef.value?.init(action, data);
});

const submitEvent = async () => {
  await formRef.value?.handleSubmit();
  emit('success');
  close();
};
</script>

<template>
  <FsAModal is="VxeModal" show-footer position="top" show-zoom :title="getTitle" :width="900" @register="register">
    <Form ref="formRef"></Form>
    <template #footer>
      <VxeButton content="取消" @click="close"></VxeButton>
      <VxeButton status="primary" content="提交" @click="submitEvent"></VxeButton>
    </template>
  </FsAModal>
</template>

<style scoped></style>
