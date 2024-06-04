# 快速开发平台

[![Language](https://img.shields.io/badge/langs-Java%20%7C%20SpringCloud%20%7C%20Vue3%20%7C%20...-red?style=flat-square&color=42b883)](https://github.com/flash-sloth/fs-cloud)
[![License](https://img.shields.io/github/license/flash-sloth/fs-util?color=42b883&style=flat-square)](https://github.com/flash-sloth/fs-util/blob/master/LICENSE)
[![Author](https://img.shields.io/badge/作者-tangyh|liulh|liy|hukz-orange.svg)](https://github.com/flash-sloth)
[![Version](https://img.shields.io/badge/版本-0.0.1-brightgreen.svg)](https://github.com/flash-sloth/fs-cloud)
[![Star](https://img.shields.io/github/stars/flash-sloth/fs-boot?color=42b883&logo=github&style=flat-square)](https://github.com/flash-sloth/fs-boot/stargazers)
[![Fork](https://img.shields.io/github/forks/flash-sloth/fs-boot?color=42b883&logo=github&style=flat-square)](https://github.com/flash-sloth/fs-boot/network/members)
[![Star](https://gitee.com/flash-sloth/fs-boot/badge/star.svg?theme=gray)](https://gitee.com/flash-sloth/fs-boot/stargazers)
[![Fork](https://gitee.com/flash-sloth/fs-boot/badge/fork.svg?theme=gray)](https://gitee.com/flash-sloth/fs-boot/members)


<div align="center">
	<h1>flash-sloth</h1>
赋予树懒闪电般的速度，使其工作效率如疾风骤雨般迅捷。
</div>
<br />


## 简介

flash-sloth 简称：fs，中文名：闪电树懒，ta是一款革命性的开发工具，它极大地简化了开发流程，让开发人员能够迅速完成CRUD等日常繁琐任务。通过FS，开发者能够释放宝贵的时间，专注于系统的优化、底层的深入研究，以及享受工作和生活的美好。它不仅是工作的加速器，更是激发创意和灵感的源泉，让开发者在高效工作的同时，也能有更多的精力去追求生活的品质和乐趣。

## flash-sloth 项目组成

| 名称   | 项目名     | gitee                                          | github                                                     | 备注                           |
|------|---------|------------------------------------------------|------------------------------------------------------------|------------------------------|
| 工具集  | fs-util | [fs-util](https://gitee.com/flash-sloth/fs-boot) | [fs-util](https://github.com/flash-sloth/fs-util)           | 基于java开发的核心工具集               |
| 单体版  | fs-boot | [fs-boot](https://gitee.com/flash-sloth/fs-boot) | [fs-boot](https://github.com/flash-sloth/fs-boot)       | 基于SpringBoot开发的java后端        |
| 定时任务 | fs-job  | [fs-job](https://gitee.com/flash-sloth/fs-job)   | [fs-job](https://github.com/flash-sloth/fs-job)             | 基于xxl-job、power-job的分布式定时调度器 |
| PC端  | fs-web  | [fs-web](https://gitee.com/flash-sloth/fs-web)   | [fs-web](https://github.com/flash-sloth/fs-web)                     | 基于ant-design-vue开发的web端      |
| 移动端  | fs-app  | [fs-app](https://gitee.com/flash-sloth/fs-app)   | [fs-app](https://github.com/flash-sloth/fs-app)                     | 基于uni-app开发的移动端              |

# fs-util 简介

`fs-util` 是 [fs-boot](https://github.com/flash-sloth/fs-boot) 和 [fs-cloud](https://github.com/flash-sloth/fs-cloud) 项目的工具集，开发宗旨是打造一套兼顾 SpringBoot 和 SpringCloud 项目的公共工具类。

## fs-util 亮点功能

- Mvc封装： 通用的 Controller、Service、Mapper、全局异常、全局序列化、反序列化规则
- SpringCloud封装：请求头传递、调用日志、灰度、统一配置编码解码规则等
- 数据回显：优雅解决 跨库表关联字段回显、跨服务字段回显
- 持久层增强：增强MybatisPlus Wrapper操作类、数据权限、自定义类型处理器
- 枚举、字典等字段统一传参、回显格式： 解决前端即要使用编码，有要回显中文名的场景。
- 在线文档：对swagger、knife4j二次封装，实现配置即文档。
- 前后端表单统一校验：还在为前端写一次校验规则，后端写一次校验规则而痛苦不堪？ 本组件将后端配置的jsr校验规则返回给前端，前端通过全局js，实现统一的校验规则。
- 缓存：封装redis缓存、二级缓存等，实现动态启用/禁用redis
- XSS： 对表单参数、json参数进行xss处理
- 统一的操作日志： AOP方式优雅记录操作日志
- 轻量级接口权限


# 特别鸣谢

* vue-vben-admin： [https://github.com/vbenjs/vue-vben-admin](https://github.com/vbenjs/vue-vben-admin)
* soybean-admin-antd： [https://github.com/soybeanjs/soybean-admin-antd](https://github.com/soybeanjs/soybean-admin-antd)