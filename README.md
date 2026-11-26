<div align="center">

# **省钱兄 JAVA 上门回收全套开源系统（JAVA+Uni-app+Vue2）**

**服务端 · 管理端 · 用户端 · 师傅端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-运行教程.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F.svg)](docs/服务端-运行教程.md)
[![Vue](https://img.shields.io/badge/Vue-2-42b883.svg)](docs/管理端-运行教程.md)
[![uni-app](https://img.shields.io/badge/uni--app-用户%2F师傅端-2b9939.svg)](docs/用户端-运行教程.md)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄上门回收全套开源系统**（「上门回收」）是一套围绕废品预约回收、师傅抢单上门、线下结算、保证金与提现、邀请分销、实名认证与后台运营配置的回收业务系统，采用前后端分离架构，服务端、管理端、用户端、师傅端全套代码开源。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）多模块构建，负责接口、数据库，以及支付、短信、对象存储、地图等外部能力。  
管理端基于 **Vue 2 + Element UI + Webpack** 开发，供运营人员在电脑浏览器中使用，系统名称是「省钱兄上门回收系统」。  
用户端基于 **uni-app**（**uView Plus**）开发，覆盖手机网页和微信小程序，应用名称是「上门回收」。  
师傅端同样基于 **uni-app**（**uView Plus**）开发，应用名称是「上门回收师傅端」，供回收员抢单、上门服务与钱包提现。

本仓库为服务端、管理端、用户端、师傅端全套源码。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-shangmenhuishou.git](https://gitee.com/maxiaodong1996/sqx-shangmenhuishou.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-shangmenhuishou.git](https://github.com/yuyang7816/sqx-shangmenhuishou.git)

## **2. 用户端 / 师傅端能力概览**

### **2.1 用户端**

底部三个主入口：**首页、订单、我的**。

| 模块 | 能做什么 |
|---|---|
| 首页 | 选城市、轮播与回收分类、查看回收项目与价格、进入预约上门流程 |
| 订单 | 查看预约订单、订单详情、取消订单 |
| 我的 | 资料、地址管理、邀请好友、招募回收员入口、消息、客服协议、设置与注销 |

核心回收路径：**选分类 / 项目 → 填地址与预约时间 → 下单待接单 → 师傅上门服务 → 线下结算完成**。

### **2.2 师傅端**

底部两个主入口：**回收池、我的**。

| 模块 | 能做什么 |
|---|---|
| 回收池 | 查看待接单池、抢单、进入接单详情 |
| 我的 | 实名认证、钱包与保证金、提现、消息、设置与注销 |

师傅履约路径：**认证入驻 →（可选）缴保证金 → 抢单 → 开始服务 → 完成订单**。

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端 / 师傅端。** 管理端和两端 App 都要等服务端已经启动，才能登录和加载数据。

### **4.1 服务端**

安装 JDK 8、Maven、MySQL 5.7 / 8.0。可选安装 Redis（当前配置默认关闭）。

开发库名默认 **`smhs`**，连接信息见 `shangmenhuishou-server/sqx-application/src/main/resources/application-dev.yml`。  
在 `shangmenhuishou-server` 目录编译并启动（启动类 `SqxApplication`，模块 `sqx-application`），默认端口 **9692**，上下文路径 `/sqx_fast`。

> 说明：演示库脚本见 `shangmenhuishou-server/db/smhs.sql`（菜单权限保留；客服微信、支付 / 短信 / OSS / 地图等密钥及业务隐私数据已清空）。默认激活环境为 **prod**，本地开发请改为 **dev**，或启动时指定 `--spring.profiles.active=dev`。

接口文档（Knife4j / OpenAPI3）示例：

`http://127.0.0.1:9692/sqx_fast/doc.html`

详细步骤见 [服务端运行教程](docs/服务端-运行教程.md)。

### **4.2 管理端**

安装 Node.js 18 或以上（工程使用 `--openssl-legacy-provider`），在 `shangmenhuishou-admin` 里执行安装并启动。开发默认端口 **8001**。  
开发代理默认指向线上演示环境；联调本机服务端时，请把 `config/index.js` 中 `/sqx_fast` 代理改到 `http://127.0.0.1:9692/sqx_fast/`。  
详细步骤见 [管理端运行教程](docs/管理端-运行教程.md)。

### **4.3 用户端**

用 HBuilderX 打开 `shangmenhuishou-user`，运行到浏览器或微信开发者工具。  
默认接口地址见 `shangmenhuishou-user/common/config.js`；联调本机时改为 `http://本机IP:9692/sqx_fast`。  
详细步骤见 [用户端运行教程](docs/用户端-运行教程.md)。

### **4.4 师傅端**

用 HBuilderX 打开 `shangmenhuishou-shop`，运行到浏览器或微信开发者工具。  
默认接口地址见 `shangmenhuishou-shop/common/config.js`；联调本机时同样指向本机服务端。

短信、微信、支付宝、对象存储、地图相关密钥需要自行申请，并在管理端「系统配置」中填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录放四端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `shangmenhuishou-server` | **服务端**。多模块 Maven 工程；接口、支付、短信、文件上传、回收订单业务等都在这里 |
| `shangmenhuishou-admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `shangmenhuishou-user` | **用户端**。消费者预约回收用的手机应用 |
| `shangmenhuishou-shop` | **师傅端**。回收员抢单、上门服务与钱包提现用的手机应用 |
| `docs` | 功能说明和运行教程 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `shangmenhuishou-server`**

| 路径 | 是什么 |
|---|---|
| `sqx-application` | 启动模块。入口 `SqxApplication.java`；配置在 `src/main/resources` |
| `sqx-common` | 公共能力。核心工具、Redis、文件、支付、短信、Shiro、文档等 BOM / 子模块 |
| `sqx-domain` | 领域仓储与 Mapper |
| `sqx-modules/sqx-system` | 系统模块。管理员登录、角色、菜单、字典、配置等 |
| `sqx-modules/sqx-app` | **上门回收核心业务**。废品分类 / 项目 / 价格 / 订单、地址、招募认证、保证金、钱包、支付、邀请、消息、帮助反馈等 |
| `sqx-modules/sqx-generator` | 代码生成相关 |

### **6.3 管理端 `shangmenhuishou-admin`**

| 路径 | 是什么 |
|---|---|
| `src/views/common/login` | 登录页（「省钱兄上门回收系统」） |
| `src/views/orderCenter` | 回收订单相关页面 |
| `src/views/locality` | 废品分类、规格、素材等本地业务配置 |
| `src/views/masterWorker` | 师傅列表、位置、钱包等 |
| `src/views/autonym` | 个人认证 / 招募审核 |
| `src/views/finance` | 财务相关列表 |
| `src/views/fitment` / `banner` | 首页装修、轮播等 |
| `src/views/user` | App 用户相关 |
| `src/router` | 页面地址和菜单对应关系 |
| `src/store` | Vuex 登录状态等临时数据 |
| `src/utils/httpRequest.js` | Axios 请求封装与系统名称 |
| `config/index.js` | 开发端口（默认 `8001`）与接口代理 |

### **6.4 用户端 `shangmenhuishou-user`**

| 路径 | 是什么 |
|---|---|
| `pages` | 底部三个主入口：首页、订单、我的，以及登录注册等 |
| `my` | 分包页面。预约填写、订单详情、地址、消息、招募、设置等 |
| `common` | 接口地址、公共逻辑 |
| `components` | 页面里重复使用的组件 |
| `static` | 图标、默认图片 |
| `uni_modules/uview-plus` | 界面组件库 **uView Plus** |
| `pages.json` | 有哪些页面、底部导航怎么排 |
| `manifest.json` | 应用名称「上门回收」、微信小程序 AppId 等 |

### **6.5 师傅端 `shangmenhuishou-shop`**

| 路径 | 是什么 |
|---|---|
| `pages` | 底部两个主入口：回收池、我的，以及登录注册等 |
| `my` | 分包页面。接单详情、实名认证、钱包 / 保证金 / 提现、消息、设置等 |
| `common` | 接口地址、公共逻辑 |
| `uni_modules/uview-plus` | 界面组件库 **uView Plus** |
| `pages.json` | 有哪些页面、底部导航怎么排 |
| `manifest.json` | 应用名称「上门回收师傅端」、微信小程序 AppId 等 |

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **用户预约主流程：选项目 → 填地址与时间 → 下单 → 待接单**  
· **师傅抢单履约：回收池抢单 → 开始服务 → 完成订单（线下结算）**  
· **订单状态机：待接单 → 待服务 → 进行中 → 已完成 / 已取消**  
· **废品分类、回收项目与价格维护**  
· **师傅实名认证、招募入驻审核**  
· **师傅保证金缴纳、钱包与提现申请**  
· **邀请好友与分销相关能力**  
· **后台订单 / 师傅 / 财务 / 首页装修 / 权限与系统配置**  
· **服务端统一处理登录、订单流转、支付回调与业务入账**

每项怎么用，见 [用户端功能说明](docs/用户端-功能说明.md)、[管理端功能说明](docs/管理端-功能说明.md)、[服务端功能说明](docs/服务端-功能说明.md)。

版本以仓库里的配置文件为准。没写死版本号的，按运行教程安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端。不能换成 11 或 17 |
| 框架 | **Spring Boot** | **2.6.11** | 提供网页接口，端口 `9692`，路径 `/sqx_fast` |
| 构建 | **Maven** | 3.6 及以上 | 多模块下载依赖、编译、启动 |
| 数据库 | **MySQL** | 5.7 / 8.0 及以上 | 业务数据。配置库名默认 `smhs` |
| 数据库访问 | **MyBatis-Plus** | 3.5.3.2 | 读写表 |
| 连接池 | **Druid** | 1.2.x | 管理数据库连接 |
| 缓存 | **Redis / Redisson** | 可选 | 当前默认关闭；打开后用于登录状态等 |
| 管理端登录 | **Apache Shiro** | 1.4.0 | 后台账号、角色、菜单权限 |
| 用户 / 师傅端登录 | **JWT** | 0.9.1 等 | 登录后的凭证（请求头 `token`） |
| 接口文档 | **Knife4j / springdoc** | OpenAPI3 | 浏览器打开 `doc.html` 查看接口 |
| JSON | **Fastjson2** | 2.0.41 | 接口数据格式 |
| 工具库 | **Hutool** | 5.8.16 | 日期、字符串等常用处理 |
| 对象存储 | 阿里云 OSS 等 | 随文件模块 | 可选。密钥要自己申请 |
| 支付 | 微信支付、支付宝等 | 随支付模块 | 当前业务侧常见用途含保证金缴纳等。密钥要自己申请 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | 18 及以上 | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | 2.5 | 后台每一个页面 |
| 构建 | **Webpack** | 3.x | 开发时热更新，默认端口 `8001` |
| 界面组件 | **Element UI** | 2.15 | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | 3.0 | 登录页、首页和各业务页的地址 |
| 状态 | **Vuex** | 3.0 | 记住是否已登录 |
| 请求 | **Axios** | 0.17 | 把操作发给服务端 |
| 地图 | 腾讯地图等 | 随页面 | 师傅位置、地址相关展示 |
| 样式 | **Sass** | 随工程 | 后台样式 |

### **7.4 用户端 / 师傅端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | 用 HBuilderX 打开 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** | 正式版 | 编译并运行到 Chrome 或微信开发者工具 |
| 界面组件 | **uView Plus** | 3.3.x | 按钮、输入框、轮播、弹窗 |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以扫码联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付宝、对象存储、地图相关密钥需要自行申请。仍解决不了时，再扫下面的码说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
