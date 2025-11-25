# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

CleanArchHelper 是一个 JetBrains 插件，用于快速创建 Clean Architecture 目录结构。支持 Flutter、React、Node.js 和通用项目，提供标准的 Clean Architecture 基础结构和 Feature 模块创建功能。

## 开发命令

```bash
# 构建插件
./gradlew buildPlugin

# 运行插件（在 IDEA 中进行开发和测试）
./gradlew runIde

# 运行测试
./gradlew test

# 验证插件配置
./gradlew validatePlugin

# 清理构建产物
./gradlew clean
```

## 核心架构

### 技术栈
- **Kotlin 1.9.25** - 主要开发语言
- **IntelliJ Platform 2024.2.5** - 插件开发平台
- **Gson 2.10.1** - JSON 序列化
- **Java 17** - JVM 目标版本

### 关键目录结构
- `src/main/kotlin/com/baoxin/cleanarchhelper/actions/` - 核心动作类（用户交互入口）
- `src/main/kotlin/com/baoxin/cleanarchhelper/services/` - 业务逻辑服务
- `src/main/kotlin/com/baoxin/cleanarchhelper/config/` - 默认结构配置
- `src/main/kotlin/com/baoxin/cleanarchhelper/model/` - 数据模型
- `src/main/kotlin/com/baoxin/cleanarchhelper/settings/` - 设置界面
- `src/main/kotlin/com/baoxin/cleanarchhelper/ui/` - 用户对话框
- `src/main/resources/META-INF/plugin.xml` - 插件配置文件

### 核心设计模式

1. **服务层架构**: 使用 `@Service` 注解的应用级服务管理状态和业务逻辑
2. **动作模式**: 继承 `AnAction` 类实现用户交互
3. **持久化配置**: 实现 `PersistentStateComponent` 接口保存用户设置
4. **递归数据结构**: `DirectoryStructure` 类表示嵌套的目录层次

## 主要功能

### 1. 创建基础结构 (CreateBaseStructureAction)
- 快捷键: `Ctrl+Alt+B`
- 创建完整的 Clean Architecture 基础目录结构
- 支持的项目类型: Flutter、React、Node.js、通用项目

### 2. 创建 Feature 模块 (CreateFeatureAction)
- 快捷键: `Ctrl+Alt+F`
- 创建标准的三层架构 Feature 模块（data、domain、presentation）
- 自动规范化 Feature 名称（首字母大写，其余小写）

### 3. 配置结构 (ConfigureStructureAction)
- 通过设置页面自定义目录结构
- JSON 格式的配置系统
- 支持多层级嵌套目录配置

## 配置系统

### DirectoryStructure 模型
```kotlin
data class DirectoryStructure(
    val directories: Map<String, DirectoryStructure> = emptyMap()
)
```

### 项目类型检测
插件自动检测项目类型并应用相应的默认结构:
- **Flutter**: `lib/` 根目录，包含 `theme/` 等 Flutter 特定目录
- **React**: `src/` 根目录，包含 `components/`、`hooks/` 等
- **Node.js**: `src/` 根目录，包含 `middleware/`、`routes/` 等
- **通用**: 可配置的通用目录结构

### 持久化配置
用户自定义配置通过 `StructureConfigService` 保存，实现 `PersistentStateComponent<State>` 接口。

## IntelliJ 平台集成要点

### 插件配置 (plugin.xml)
- 版本: 1.1.2
- 兼容: IntelliJ IDEA 2024.2.5 - 2024.2.8
- 扩展点: Application Service、Application Configurable、Actions

### 动作注册
动作通过右键菜单和快捷键触发，在 `update()` 方法中控制可见性。

### 异步操作
目录创建使用 `ProgressManager.run()` 在后台线程执行，避免 UI 阻塞。

## 开发注意事项

### 新增项目类型支持
1. 在 `DefaultStructures.kt` 中添加新的项目类型方法
2. 更新 `DirectoryStructure.ProjectType` 枚举
3. 修改项目类型检测逻辑

### 文件系统操作
- 使用 IntelliJ 的虚拟文件系统 API (`VirtualFile`)
- 通过 `WriteCommandAction.runWriteCommandAction()` 确保操作可撤销
- 避免直接使用 Java File API

### 错误处理
- 使用 `Messages.showInfoMessage()` 等提供用户友好提示
- 在后台线程中执行耗时操作
- 验证用户输入和文件系统权限

### UI 开发
- 对话框继承 `DialogWrapper`
- 使用 Kotlin DSL 构建表单界面
- 支持实时预览和验证

## 构建和部署

构建生成的插件位于 `build/distributions/` 目录，可直接安装到 IntelliJ IDEA 或发布到 JetBrains Plugin Portal。

插件支持开发和生产环境的不同配置，通过 `gradle.properties` 管理版本号等配置信息。