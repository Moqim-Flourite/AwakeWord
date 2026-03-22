# AwakeWord（醒词）☀️

一个简洁高效的背单词辅助应用，专为记忆困难单词设计。

## ✨ 功能特点

### 核心功能
- **📝 单词导入** - 支持英文逗号分隔批量导入，自动查询有道词典获取音标和中文释义
- **🔒 锁屏亮屏自动弹出** - 屏幕亮起时自动显示单词卡片，随时随地复习
- **🔊 TTS发音** - 点击播放单词发音，支持美式/英式口音切换
- **✅ 勾选排序** - 记住的单词自动移到队尾，全勾选后刷新新页面
- **📝 备注功能** - 随时添加个人记忆技巧
- **♿ 无障碍服务** - 使用无障碍服务监听亮屏事件，MIUI友好

### 界面设计
- **深色主题** - 护眼紫色系配色，适合夜间学习
- **Material Design 3** - 现代化 UI 设计
- **流畅动画** - 滑动页面切换效果

## 🔧 权限要求

### 锁屏功能需要
1. **锁屏显示** - 在其他应用上层显示（MIUI特有）
2. **显示悬浮窗** - 标准Android悬浮窗权限
3. **无障碍服务** - 监听屏幕亮起事件

### MIUI设备注意事项
- 需要手动开启"锁屏显示"权限
- 需要开启无障碍服务
- 在设置中搜索"AwakeWord"可快速找到应用

## 🏗️ 项目结构

```
app/src/main/java/com/worddraft/
├── data/
│   ├── local/           # Room 数据库
│   │   ├── WordDao.kt
│   │   └── WordDatabase.kt
│   ├── model/           # 数据模型
│   │   └── Word.kt
│   └── repository/      # 数据仓库
│       └── WordRepository.kt
├── service/             # 后台服务
│   ├── LockScreenService.kt
│   ├── LockScreenAccessibilityService.kt
│   ├── LockScreenReceiver.kt
│   └── GuardService.kt
├── ui/
│   ├── components/      # 通用组件
│   │   └── WordCard.kt
│   ├── screens/         # 页面
│   │   ├── MainScreen.kt
│   │   ├── WordListScreen.kt
│   │   └── BatchDetailScreen.kt
│   └── theme/           # 主题样式
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── util/                # 工具类
│   ├── DictionaryApi.kt
│   └── TtsManager.kt
├── viewmodel/           # ViewModel
│   ├── MainViewModel.kt
│   └── MainViewModelFactory.kt
├── MainActivity.kt
├── LockScreenActivity.kt
└── WordDraftApplication.kt
```

## 🛠️ 技术栈

- **Kotlin** - 100% Kotlin 编写
- **Jetpack Compose** - 声明式 UI
- **Room Database** - 本地数据存储
- **Material Design 3** - UI 组件库
- **Navigation Compose** - 页面导航
- **ViewModel & Flow** - 响应式架构
- **Accessibility Service** - 无障碍服务监听亮屏

## 🚀 快速开始

### 环境要求
- JDK 17+
- Android SDK (API 24+)
- Gradle (已包含 Wrapper)

### 构建项目

```bash
# Linux/Mac
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

### 安装到设备

```bash
./gradlew installDebug
```

## 📱 使用说明

1. **导入单词** - 点击右下角 ➕ 按钮，输入单词（英文逗号分隔）
2. **开启锁屏服务** - 点击右上角 ⋮ 菜单 → 开启锁屏服务
3. **授予权限** - 按提示开启锁屏显示、悬浮窗、无障碍权限
4. **亮屏复习** - 每次亮屏自动弹出单词卡片
5. **勾选单词** - 记住的单词点击勾选，自动移到队尾
6. **添加备注** - 点击单词卡片添加个人记忆技巧
7. **播放发音** - 点击 🔊 图标播放单词发音

## 📦 生成的APK位置

```
app/build/outputs/apk/debug/app-debug.apk
```

## 🎨 自定义

### 修改主题颜色
编辑 `ui/theme/Color.kt`：
```kotlin
val Primary = Color(0xFF6B4CE0)        // 紫色主色
val Secondary = Color(0xFFFF9800)      // 橙色强调色
```

### 修改每页显示数量
编辑 `data/local/WordDao.kt`：
```kotlin
@Query("SELECT * FROM words WHERE isChecked = 0 ORDER BY displayOrder ASC, createdAt DESC LIMIT 5")
// 修改 LIMIT 后面的数字
```

## 📝 注意事项

- 锁屏功能需要悬浮窗权限和无障碍服务
- MIUI设备需额外开启"锁屏显示"权限
- 词典查询需要网络权限
- TTS 发音需要系统支持英语语音

## 🌐 相关资源

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Room 数据库](https://developer.android.com/training/data-storage/room)

## 📜 版本历史

### v1.0.3
- 🐛 修复锁屏界面切换按钮无响应问题
- 🧹 清理冗余代码，删除未使用的LockScreen.kt
- ✨ 版本号显示简化为v1.0.3格式

### v1.0.2
- 🐛 修复返回按钮层级导航问题
- 🐛 修复页面快闪问题
- ✨ 添加导航状态持久化

### v1.0.1
- 🐛 修复层级目录功能编译错误
- ✨ 每页单词数量从8个改为5个
- ✨ 支持空格分隔导入单词

### v1.0.0
- ✨ 首次发布
- 📝 单词导入与管理
- 🔒 锁屏亮屏自动弹出单词卡片
- 🔊 TTS发音（美式/英式）
- ♿ 无障碍服务支持
- 🎨 深色主题UI

---

Happy Learning! 🎓✨
