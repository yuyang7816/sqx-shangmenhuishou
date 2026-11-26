## 模块说明
```
sqx-server
└── sqx-admin -- 项目入口
└── sqx-auth  -- 统一认证授权鉴权
└── sqx-common -- 公共模块
     ├── sqx-common-bom   -- common依赖包管理
     ├── sqx-common-core  -- 公共组件，工具模块
     ├── sqx-common-redis -- redis模块
     ├── sqx-common-xxxx  -- 可以自由扩展其他模块
└── sqx-domain -- 数据模块
     ├── sqx-domain-db  -- 数据持久模块
└── sqx-modules -- 业务模块组
     ├── sqx-system  -- 系统模块
     ├── sqx-app -- 业务模块
     ├── sqx-xxx  -- 可自由扩展其他模块
     
计划扩展的功能模块
└── sqx-plugins -- 第三方插件
     ├── sqx-springboot-admin -- 系统监控模块
     ├── sqx-job -- 第三方任务管理模块 xxl-job/power-job
     ├── sqx-xxx -- 其他第三方模块
```

## 创建新模块

1. 在sqx-modules下新建一个新的module （sqx-demo）
2. 在sqx-admin的pom文件中引入新创建的模块, 版本号使用maven统一版本号`${revision}`
```
<dependency>
    <groupId>com.sqx</groupId>
    <artifactId>sqx-demo</artifactId>
    <version>${revision}</version>
</dependency>
```

3. 新创建的模块必须引入核心公共模块，其他模块可选
```
<dependency>
    <groupId>com.sqx</groupId>
    <artifactId>sqx-common-core</artifactId>
</dependency>
```

4. 在sqx-domain-repository模块下创建entity，mapper等基础文件

## 统一版本号
sqx-server版本号使用maven规定的统一版本号revision<br />升级版本号时需要修改两个地方：

1. 父pom文件里修改revision的值
2. sqx-common-bom模块pom文件中的revision值
```
<properties>
    <revision>0.1</revision>
</properties>
```

## 项目部署
1. 打包命令: `mvn clean package -U`, 这个命令会在target目录下生成项目jar包和lib目录
2. 首次部署时需要部署lib目录和jar包, 后续部署只需要更新jar包. 如果有更新依赖, 部署时只需要更新相应的依赖jar包
