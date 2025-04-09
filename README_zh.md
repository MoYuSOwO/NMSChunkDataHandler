# NMSChunkDataHandler
### [English](README_zh.md)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> Minecraft服务端区块数据包处理模板 / Minecraft server chunk packet processing template

## 📖 概述

本库提供了一套完整的Minecraft服务端区块数据包(`ClientboundLevelChunkWithLightPacket`)解析和修改方案，同时支持单方块更新(`ClientboundBlockUpdatePacket`)和多方块更新(`ClientboundSectionBlocksUpdatePacket`)的拦截处理。可作为:
- 区块数据研究模板
- 动态方块替换实现参考
- NMS数据包处理范例

## ✨ 特性

### 🌐 块相关数据包覆盖
- **区块数据包** - 完整解析bitsPerBlock三种模式(0,1-8,9+)
- **单方块更新** - 精确拦截BlockUpdatePacket
- **多方块更新** - 批量处理SectionBlocksUpdate

### ⚡ 高性能处理
- 直接操作字节数组避免序列化开销
- 位运算优化BitStorage访问
- 最小化反射调用

### 🧩 模块化设计
- 独立ChannelHandler实现
- 易于扩展新数据包类型
- 清晰的替换逻辑分离

## ⚠️ 版本限制 / Version Limitations

当前实现针对 **1.21.4** 服务端核心，主要限制包括:
- 使用反射访问部分字段 (不同NMS版本可能失效)
- `Block.stateById()` 方法可能变更
- 数据处理需适配版本协议

建议fork后根据目标服务端版本调整（不保证版本变动后仍然生效）:
1. 更新NMS类路径
2. 替换API调用
3. 测试各数据包结构

## 📚 开发文档 / Documentation

### 核心类说明（代码基本是不言自明的）
| 类                     | 功能           |
|-----------------------|--------------|
| `NMSChunkDataHandler` | 插件主类和包管理注册逻辑 |
| `NMSBlockHandler`     | 区块字节流解析逻辑    |
| `ReflectionUtil`      | 反射工具类        |

### 扩展建议
1. 创建自定义替换规则
2. 显然还有很多可以做和优化的

## 🤝 协作指南 / Collaboration

### 贡献流程
1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/your-idea`)
3. 提交修改 (`git commit -am 'Add some feature'`)
4. 推送分支 (`git push origin feature/your-idea`)
5. 发起 Pull Request

### 急需贡献
- [ ] 多版本适配
- [ ] 性能基准测试

### 与作者讨论
[![Email](https://img.shields.io/badge/Email-MoYuOwO@outlook.com-blue?logo=mail.ru)](mailto:MoYuOwO@outlook.com) 