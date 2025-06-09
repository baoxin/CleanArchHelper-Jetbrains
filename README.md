# Clean Architecture Helper

一个用于快速创建 Clean Architecture 目录结构的 JetBrains 插件，支持 Flutter、React、Node.js 等项目。

## 功能特性

### 🏗️ 创建基础结构
- 快速创建完整的 Clean Architecture 基础目录结构
- 包含 `core`、`features`、`shared` 等标准目录
- 支持多种项目类型（Flutter、React、Node.js 等）

### 🎯 创建 Feature 模块
- 为每个功能模块快速创建标准的三层架构目录
- 包含 `data`、`domain`、`presentation` 层
- 自动规范化 Feature 名称

### ⚙️ 可配置的目录结构
- 完全可自定义的目录结构配置
- 支持多层级嵌套目录
- 可以保存和重置配置

## 使用方法

### 1. 创建基础结构
- 在文件资源管理器中右键点击目录
- 选择 "创建 Clean Architecture 基础结构"
- 或使用快捷键：`Ctrl+Alt+B`

### 2. 创建 Feature 模块
- 在文件资源管理器中右键点击目录（推荐在 `features` 目录中）
- 选择 "创建 Feature 模块"
- 输入 Feature 名称
- 或使用快捷键：`Ctrl+Alt+F`

### 3. 配置目录结构
- 使用设置面板：`File > Settings > Tools > Clean Architecture Helper`
- 选择要编辑的配置类型
- 支持查看、编辑、重置配置

## 默认目录结构

### 基础结构
```
lib/
├── core/
│   ├── constants/
│   ├── errors/
│   ├── network/
│   ├── usecases/
│   └── utils/
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
        ├── controllers/
        ├── pages/
        └── widgets/
```

### Feature 结构
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

## 开发

### 构建插件
```bash
./gradlew buildPlugin
```

### 运行插件
```bash
./gradlew runIde
```

### 测试
```bash
./gradlew test
```

## 配置说明

插件支持通过设置页面进行配置：

### 基础结构配置
目录结构使用嵌套的 JSON 对象表示，每个键代表一个目录名，值为该目录的子目录结构。

### Feature 结构配置
同样使用 JSON 格式定义 Feature 模块的内部结构。

## 支持的项目类型

- **Flutter**: 使用 `lib/` 作为根目录，包含 `theme/` 等 Flutter 特定目录
- **React**: 使用 `src/` 作为根目录，包含 `components/`、`hooks/` 等 React 特定目录
- **Node.js**: 使用 `src/` 作为根目录，包含 `middleware/`、`routes/` 等 Node.js 特定目录
- **通用**: 可配置的通用目录结构

## 许可证

MIT License

## 贡献

欢迎提交 Issue 和 Pull Request！
