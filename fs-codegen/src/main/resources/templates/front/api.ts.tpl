import type { AxiosRequestConfig } from 'axios';
import { RequestEnum } from '@vben/http';
import { defHttp } from '@/service/http';
import type { #(dtoClassName), #(queryClassName), #(voClassName) } from './model';

const MODULAR = '/#(packageConfig.subSystem)/#(packageConfig.module)/#(table.buildEntityVarName())';

export const Api = {
  Save: {
    url: `${MODULAR}`,
    method: RequestEnum.POST
  } as AxiosRequestConfig,
  Update: {
    url: `${MODULAR}`,
    method: RequestEnum.PUT
  }
};

/**
 * 分页查询
 *
 * @param params 分页参数
 * @returns 每页数据
 */
export const page = (params: Model.PageParam<#(queryClassName)>) =>
  defHttp.request<Model.PageResult<#(voClassName)>>({
    url: `${MODULAR}/page`,
    method: RequestEnum.POST,
    params
  });

/**
 * 批量查询
 *
 * @param params 批量查询
 * @returns 所有数据
 */
export const list = (params: #(queryClassName)) =>
  defHttp.request<#(voClassName)[]>({
    url: `${MODULAR}/list`,
    method: RequestEnum.POST,
    params
  });

/**
 * 根据id批量查询
 *
 * @param params 根据id批量查询
 * @returns 所有数据
 */
export const listByIds = (ids: string[]) =>
  defHttp.request<#(voClassName)[]>({
    url: `${MODULAR}/listByIds`,
    method: RequestEnum.POST,
    params: ids
  });

/**
 * 单体查询
 *
 * @param id id
 * @returns 单个对象
 */
export const getById = (id: string) =>
  defHttp.request<#(voClassName)>({
    url: `${MODULAR}/info/${id}`,
    method: RequestEnum.GET
  });

/**
 * 单体查询(优先查缓存)
 *
 * @param id id
 * @returns 单个对象
 */
export const getByIdCache = (id: string) =>
  defHttp.request<#(voClassName)>({
    url: `${MODULAR}/cache/${id}`,
    method: RequestEnum.GET
  });

/**
 * 刷新缓存
 *
 * @param ids id
 * @returns 是否成功
 */
export const refreshCache = (ids: string[]) =>
  defHttp.request<boolean>({
    url: `${MODULAR}/refreshCache`,
    method: RequestEnum.POST,
    params: ids
  });

/**
 * 清理缓存
 *
 * @param ids id
 * @returns 是否成功
 */
export const clearCache = (ids: string[]) =>
  defHttp.request<boolean>({
    url: `${MODULAR}/clearCache`,
    method: RequestEnum.POST,
    params: ids
  });

/**
 * 保存数据
 *
 * @param params 参数
 * @returns 是否成功
 */
export const save = (params: #(dtoClassName)) =>
  defHttp.request<number>({
    ...Api.Save,
    params
  });
/**
 * 修改数据
 *
 * @param params 参数
 * @returns 是否成功
 */
export const update = (params: #(dtoClassName)) =>
  defHttp.request<number>({
    ...Api.Update,
    params
  });

/**
 * 批量删除
 *
 * @param params id
 * @returns 是否成功
 */
export const remove = (ids: string[]) =>
  defHttp.request<boolean>({
    url: `${MODULAR}`,
    method: RequestEnum.DELETE,
    params: ids
  });
