# NMSChunkDataHandler 
### [中文文档](README_zh.md)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)



> Minecraft Server Chunk Packet Processing Template

## 📖 Overview

This library provides a complete solution for parsing and modifying Minecraft server chunk packets (`ClientboundLevelChunkWithLightPacket`), while also supporting interception of single-block updates (`ClientboundBlockUpdatePacket`) and multi-block updates (`ClientboundSectionBlocksUpdatePacket`). It can serve as:
- A chunk data research template
- A reference implementation for dynamic block replacement
- An NMS packet processing example

## ✨ Features

### 🌐 Block-related Packet Coverage
- **Chunk Packets** - Full parsing of three bitsPerBlock modes (0, 1-8, 9+)
- **Single-block Updates** - Precise BlockUpdatePacket interception
- **Multi-block Updates** - Batch processing of SectionBlocksUpdate

### ⚡ High-performance Processing
- Direct byte array manipulation avoids serialization overhead
- Bitwise-optimized BitStorage access
- Minimized reflection calls

### 🧩 Modular Design
- Independent ChannelHandler implementation
- Easy extension for new packet types
- Clear separation of replacement logic

## ⚠️ Version Limitations

Current implementation targets **1.21.4** server core, with main limitations including:
- Uses reflection to access certain fields (may fail across NMS versions)
- `Block.stateById()` method may changes in different versions
- Data processing requires protocol adaptation

Recommended adjustments when forking for target server versions (no guarantee of functionality after version changes):
1. Update NMS class paths
2. Replace API calls
3. Test all packet structures

## 📚 Documentation

### Core Classes (The code is mostly self-explanatory)
| Class | Functionality |
|-------|---------------|
| `NMSChunkDataHandler` | Main plugin class and package registration logic |
| `NMSBlockHandler` | Chunk byte stream parsing logic |
| `ReflectionUtil` | Reflection utility class |

### Extension Suggestions
1. Create custom replacement rules
2. Clearly there's much more that can be done and optimized

## 🤝 Collaboration Guidelines

### Contribution Process
1. Fork this project
2. Create feature branch (`git checkout -b feature/your-idea`)
3. Commit changes (`git commit -am 'Add some feature'`)
4. Push branch (`git push origin feature/your-idea`)
5. Create Pull Request

### Urgent Contributions Needed
- [ ] Multi-version adaptation
- [ ] Performance benchmarking

### Discuss with Author
[![Email](https://img.shields.io/badge/Email-MoYuOwO@outlook.com-blue?logo=mail.ru)](mailto:MoYuOwO@outlook.com)