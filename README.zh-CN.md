# LeapCarPlay

LeapCarPlay 是 DiPlay/xcertplay 的社区分支，针对零跑安卓车机适配 CarPlay 独立仪表盘副屏。主 APK 的 application ID 为 `com.signxer.leapcarplay`，Automotive APK 为 `com.signxer.leapcarplay.automotive`。

[代码仓库](https://github.com/signxer/leapcarplay) · [完整说明](README.md) · [报告问题](https://github.com/signxer/leapcarplay/issues/new/choose)

目前尚未发布 LeapCarPlay 安装包；请参阅[构建说明](docs/BUILD.md)。HDMI2 副屏功能和注意事项见[兼容性说明](docs/COMPATIBILITY.md)。

新 application ID 会创建独立的应用数据空间；旧 DiPlay 可以保留，但需要重新配对 iPhone 并重新设置。

0.1.0 为公开预览版，未经 Apple 认证。请安装在车机上，而非 iPhone。无需越狱、转接盒或认证服务器。无线连接需要 Android 10 或更高版本及可用的 Wi-Fi Direct。

认证使用从公开固件中提取的实验性配件身份，无法保证未来持续可用。部分车机仍可能卡顿或无法应用图标大小设置。应用界面目前为英文。源代码、构建说明及许可证随版本提供。
