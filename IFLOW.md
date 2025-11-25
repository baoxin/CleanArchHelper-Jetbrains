# Clean Architecture Helper - JetBrains 插件

## 项目概述

Clean Architecture Helper 是一个 JetBrains IDE 插件，用于快速创建 Clean Architecture 目录结构。该插件支持 Flutter、React、Node.js 等多种项目类型，提供可配置的目录结构模板，帮助开发者快速搭建符合 Clean Architecture 原则的项目结构。

### 主要技术栈
- **语言**: Kotlin
- **框架**: IntelliJ Platform SDK
- **构建工具**: Gradle with Kotlin DSL
- **JSON 处理**: Gson
- **目标平台**: IntelliJ IDEA Community Edition (2024.2.5+)

### 核心功能
1. **创建基础结构**: 快速创建完整的 Clean Architecture 基础目录结构
2. **创建 Feature 模块**: 为每个功能模块创建标准的三层架构目录
3. **可配置结构**: 支持完全自定义的目录结构配置
4. **多项目类型支持**: 针对不同技术栈提供优化的目录结构

## 项目架构

### 目录结构
```
src/main/kotlin/com/baoxin/cleanarchhelper/
├── actions/           # 用户操作类
│   ├── ConfigureStructureAction.kt
│   ├── CreateBaseStructureAction.kt
│   └── CreateFeatureAction.kt
├── config/           # 默认配置
│   └── DefaultStructures.kt
├── model/            # 数据模型
│   └── DirectoryStructure.kt
├── services/         # 核心服务
│   ├── DirectoryCreatorService.kt
│   └── StructureConfigService.kt
├── settings/         # 设置界面
│   └── CleanArchConfigurable.kt
├── ui/              # 用户界面组件
│   ├── FeatureNameDialog.kt
│   └── StructureConfigDialog.kt
└── utils/           # 工具类
    └── JsonFormatter.kt
```

### 核心组件说明

#### 1. Actions (操作层)
- **CreateBaseStructureAction**: 创建 Clean Architecture 基础结构
- **CreateFeatureAction**: 创建功能模块
- **ConfigureStructureAction**: 配置目录结构

#### 2. Services (服务层)
- **StructureConfigService**: 管理和持久化目录结构配置
- **DirectoryCreatorService**: 负责实际的目录创建操作

#### 3. Models (数据模型)
- **DirectoryStructure**: 表示目录结构的数据模型，支持 JSON 序列化

#### 4. Configuration (配置)
- **DefaultStructures**: 提供不同项目类型的默认目录结构配置

## 构建和运行

### 环境要求
- IntelliJ IDEA 2024.2.5 或更高版本
- JDK 17
- Kotlin 1.9.25

### 构建命令
```bash
# 构建插件
./gradlew buildPlugin

# 运行插件（在开发环境中）
./gradlew runIde

# 运行测试
./gradlew test

# 构建并发布到插件仓库
./gradlew publishPlugin
```

### 快捷键
- `Ctrl+Alt+B`: 创建 Clean Architecture 基础结构
- `Ctrl+Alt+F`: 创建 Feature 模块

## 开发约定

### 代码规范
- 使用 Kotlin 编码规范
- 类名使用 PascalCase
- 方法名和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE
- 包名使用小写字母

### 架构模式
- 遵循 Clean Architecture 原则
- 使用依赖注入
- 服务层通过 Application 级别的 Service 管理
- 配置使用 IntelliJ 的 PersistentStateComponent

### 测试策略
- 单元测试覆盖核心业务逻辑
- 集成测试验证插件功能
- 使用 Gradle 测试框架

## 默认目录结构

### 基础结构 (Flutter)
```
lib/
├── core/
│   ├── constants/
│   ├── errors/
│   ├── network/
│   ├── usecases/
│   ├── utils/
│   └── theme/
├── features/
└── shared/
    ├── data/
    │   ├── datasources/
    │   ├── models/
    │   └── repositories/
    ├── domain/
    │   ├── entities/
    │   ├── repositories/
    │   └── usecases/
    └── presentation/
        ├── bloc/
        ├── pages/
        └── widgets/
```

### Feature 模块结构
```
feature_name/
├── data/
│   ├── datasources/
│   ├── models/
│   └── repositories/
├── domain/
│   ├── entities/
│   ├── repositories/
│   └── usecases/
└── presentation/
    ├── bindings/
    ├── controllers/
    ├── pages/
    └── widgets/
```

## 配置系统

插件支持通过 JSON 格式配置目录结构：
- 基础结构配置：定义项目的整体架构
- Feature 结构配置：定义单个功能模块的内部结构
- 支持多层级嵌套目录
- 配置持久化存储在 IDE 的配置文件中

## 扩展性

插件设计具有良好的扩展性：
- 可以轻松添加新的项目类型支持
- 支持自定义目录结构模板
- 插件架构支持添加新的操作和功能

## 版本信息
- 当前版本: 1.1.2
- 兼容 IntelliJ IDEA: 242-252.*
- 最后更新: 2024年