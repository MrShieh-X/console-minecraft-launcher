# Console Minecraft Launcher
一个在命令行上运行的 Minecraft Java 版启动器

[![最新版本](https://img.shields.io/badge/%E6%9C%80%E6%96%B0%E7%89%88%E6%9C%AC-v2.2.1-brightgreen)](https://github.com/MrShieh-X/console-minecraft-launcher/releases)
![发布日期](https://img.shields.io/badge/%E5%8F%91%E5%B8%83%E6%97%A5%E6%9C%9F-2023--08--30-brightgreen)
![软件需求](https://img.shields.io/badge/%E8%BD%AF%E4%BB%B6%E9%9C%80%E6%B1%82-Java%208-blue)

### [English README](README-en.md) | [更新日志](update_logs.md) | [疑难解答](troubleshooting.md)

## 🎬 视频教程
中文：[\[CMCL2.0\]命令行版MC启动器, 支持正版登录|外置登录|统一通行证|模组/整合包搜索安装|自定义皮肤披风](https://www.bilibili.com/video/BV1bY411R7wa/) <br/>
英语：[\[CMCL2.0\]MC Launcher operates by command, supports Microsoft login|authlib-injector|nide8auth|mod/modpack searching and installation|custom skin and cape](https://www.youtube.com/watch?v=Sqeu_Pahm-0) <br/>
如果你观看完视频后或使用时仍然有任何问题，可以参考[疑难解答](troubleshooting.md)或通过在[关于作者](#-关于作者)中找到的作者联系信息向作者提出疑问。

## 🏆 特别鸣谢
- [@Graetpro-X](https://github.com/Graetpro)：开发部分功能
- [@FZZkill](https://github.com/FZZkill)：帮助美化README
- [@mail_set](https://space.bilibili.com/435654748)（[个人网站](https://mailset.top)）：上传CMCL至AUR仓库（名称为`cmcl`）

## 📕 使用教程
- 如果您的操作系统是 Windows，则可以在 Releases 中下载`cmcl.exe`，然后打开 cmd，使用`cd /d <目录路径>`打开`cmcl.exe`所在的目录，执行命令`cmcl`，后面加上选项与参数。如果想要在任意目录都能使用CMCL，请参考[疑难解答中的“怎么做到在任何路径下都能用CMCL？”](troubleshooting.md#怎么做到在任何路径下都能用cmcl)。
- 如果您的操作系统是 ArchLinux，您可以使用yay安装CMCL，命令如下：
```shell
yay -S cmcl
```
然后直接输入命令`cmcl`即可，后面加上选项与参数。
- 如果您的操作系统是 macOS，您可以使用 Homebrew 进行安装CMCL，命令如下：
```shell
brew tap MrShieh-X/brew
brew install cmcl
```
然后直接输入命令`cmcl`即可，后面加上选项与参数。</br>
更新启动器的命令为：
```shell
brew update
brew upgrade cmcl
```
卸载的命令为：
```shell
brew uninstall cmcl
```
- 如果您是其他 Linux 用户，或者您是 macOS 用户，但不希望使用 Homebrew 进行安装，您可以在 Releases 中找到最新版本，下载`cmcl`，然后在终端打开`cmcl`所在的目录，执行`./cmcl`，后面加上选项与参数即可。
- 如果以上方法都不适用于您，您可以在 Releases 中找到最新版本，下载`cmcl.jar`，然后在终端（或 Windows 下的 cmd）打开`cmcl.jar`所在的目录，执行以下命令，后面加上选项与参数。这种方法在多数操作系统中都可用。
```shell
java -jar cmcl.jar
```
具体使用方法，请查阅[帮助文档](#-帮助文档)。 如果你想对启动器的配置进行修改，请查阅[配置](#-配置)。

## 📖 帮助文档
通过`cmcl -h`或`cmcl --help`获得总帮助文档<br/>
获得单个功能的帮助文档，在功能选项后面加 `-h` 或 `--help`，如`cmcl account --help`<br/>
注：中括号内的内容是可选的。选项中的逗号意为两边的选项都能实现相同的功能。<br/>
对于给选项指定内容，仅能在缩略选项（一条横杠）后面直接加上内容（无需空格），如`cmcl -lD:\.minecraft`，<br/>
仅能在完整选项（两条横杠）后面加上等号再输入内容，如`cmcl --list=D:\.minecraft`，<br/>
或在此两者后加上空格再输入内容，如`cmcl -l D:\.minecraft`；`cmcl --list D:\.minecraft`，详细请参考选项说明后面的例子。<br/>
对于某些可能要经常输入的命令，可设置简化命令，详见[疑难解答中的“能为经常输入的命令搞个‘快捷方式’吗？”](troubleshooting.md#能为经常输入的命令搞个快捷方式吗)。

## 🔧 配置
配置存储在程序运行目录（在 Linux 与 macOS 下则为 `用户目录/.config/cmcl`）的一个名为cmcl.json的JSON文件，你可以使用文本编辑器（需了解JSON教程，建议修改前备份）或`config <配置名称> <配置值>`的程序参数（详见帮助文档配置相关）以修改配置。<br/>
使用文本编辑器编辑的话，配置名称以及文本配置值需要在英语的双引号（""）内。<br/>
使用命令来修改时，如果配置值包含空格，则需要使用英语的双引号（""）括起来<br/>
以下表格可通过参数`config --view`查看。<br/>
注：类型为布尔值的配置，它的值可以输入表示“是”的`true`，也可以是表示“否”的`false`。

| 配置名                     | 类型     | 含义                                                                                                      |
|-------------------------|--------|---------------------------------------------------------------------------------------------------------|
| accounts                | JSON数组 | 账号（非直接修改，请通过“account -h”获得相关使用教程以进行修改）                                                                  |
| downloadSource          | 整数     | 下载源，0为官方，1为BMCLAPI，2为MCBBS                                                                              |
| language                | 文本     | 语言，zh为简体中文，en为英文，cantonese是粤语（简体）                                                                       |
| selectedVersion         | 文本     | 已选择的版本，可直接使用“cmcl”进行启动                                                                                  |
| maxMemory               | 整数     | [**游戏相关**]最大内存（单位：MB）                                                                                   |
| gameDir                 | 文本     | [**游戏相关**]自定义游戏目录路径（或设置版本隔离），默认为.minecraft                                                              |
| assetsDir               | 文本     | [**游戏相关**]自定义assets资源目录路径，若为空则为游戏目录内的assets目录                                                           |
| resourcesDir            | 文本     | [**游戏相关**]自定义资源包目录路径，若为空则为游戏目录内的resourcepacks目录                                                         |
| javaPath                | 文本     | [**游戏相关**]Java 路径（如果为空会自动获得）                                                                            |
| windowSizeWidth         | 整数     | [**游戏相关**]游戏窗口的宽                                                                                        |
| windowSizeHeight        | 整数     | [**游戏相关**]游戏窗口的高                                                                                        |
| isFullscreen            | 布尔值    | [**游戏相关**]游戏窗口是否为全屏                                                                                     |
| exitWithMinecraft       | 布尔值    | [**游戏相关**]运行游戏时，是否需要退出启动器时顺便退出游戏                                                                        |
| printStartupInfo        | 布尔值    | [**游戏相关**]开始游戏的时候，是否输出启动信息（Java 路径、最大内存等）                                                               |
| checkAccountBeforeStart | 布尔值    | [**游戏相关**]开始游戏之前，是否检查账号是否可用                                                                             |
| jvmArgs                 | JSON数组 | [**游戏相关**]自定义JVM参数（查看下方的示例或通过“jvmArgs -h”获得相关使用教程以进行修改）                                                 |
| gameArgs                | JSON对象 | [**游戏相关**]自定义游戏参数（查看下方的示例或通过“gameArgs -h”获得相关使用教程以进行修改）                                                 |
| qpLogFile               | 文本     | [**游戏相关**]快速游玩（Quick Play，Minecraft 1.20 的新功能，设置下面三项配置即可启动游戏后分别直接进入存档、服务器、领域，只能设置一项）的日志文件路径（相对于游戏目录），可选 |
| qpSaveName              | 文本     | [**游戏相关**]快速游玩直接进入的存档名称                                                                                 |
| qpServerAddress         | 文本     | [**游戏相关**]快速游玩直接进入的服务器地址（包括端口），该配置也适用于1.20之前的版本                                                         |
| qpRealmsID              | 文本     | [**游戏相关**]快速游玩直接进入的领域ID                                                                                 |
| proxyEnabled            | 布尔值    | 是否开启网络代理                                                                                                |
| proxyHost               | 文本     | 代理主机地址                                                                                                  |
| proxyPort               | 整数     | 代理端口                                                                                                    |
| proxyUsername           | 文本     | 代理验证的账户（代理可选）                                                                                           |
| proxyPassword           | 文本     | 代理验证的密码（代理可选）                                                                                           |
| modDownloadSource       | 文本     | 模组下载源，curseforge或modrinth                                                                               |
| modpackDownloadSource   | 文本     | 整合包下载源，curseforge或modrinth                                                                              |
| simplifyCommands        | JSON对象 | 简化命令（通过“simplify -h”获得相关使用教程以进行修改）                                                                      |

<details>
<summary>jvmArgs 示例</summary>

```json
["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"]
```
以上内容意为设置`-Dfile.encoding=UTF-8`、`-Djava.library.path=natives`以及`-XX:+PrintGC`的参数。
</details>

<details>
<summary>gameArgs 示例</summary>

```json
{
  "fullscreen": "",
  "arg1": "value1",
  "arg2": "value2",
  "arg3": "space value3"
}
```
以上内容将会加入游戏参数`--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value3"`
</details>

## 🌏 支持的界面语言
通过`cmcl config language <语言代号>`切换语言。

|   语言   |   语言代号    |
|:------:|:---------:|
|   英语   |    en     |
|  简体中文  |    zh     |
| 粤语（简体） | cantonese |

## ©️ 版权
MrShiehX 拥有该程序的版权。<br/>
任何人都可以对此程序提出意见和建议。

## 📄 软件协议
该软件在 [GPL v3](https://www.gnu.org/licenses/gpl-3.0.html) 下分发，附带附加条款。

    Console Minecraft Launcher
    Copyright (C) 2021-2023  MrShiehX <3553413882@qq.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your function) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

### 📑 附加条款（依据 GPLv3 协议第七条）
你<b>不得</b>移除本程序所显示的版权声明。\[[依据 GPLv3, 7(b).](https://github.com/MrShieh-X/console-minecraft-launcher/blob/f266ff87c0af3487ce66b47afbdb5d6dbc90f240/LICENSE#L368-L370)\]

## 📢 免责声明
- Minecraft 版权归 Mojang Studios 与 Microsoft 所有，使用CMCL产生的所有版权问题，软件制作方概不负责，请支持正版。
- 用户因使用CMCL而产生的一切后果由用户自己承担，任何涉及CMCL的法律纠纷与冲突与开发者无关，CMCL与开发者将不承担任何责任。

## 🎓 关于作者
MrShiehX<br/>
- 哔哩哔哩：<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>
- Youtube：<br/>
  [@MrShiehX](https://www.youtube.com/channel/UC03_vrWM8TfaU1k9VYVzW0A) <br/>

## 如果您在CMCL发现任何BUG，或者有新的想法，欢迎在哔哩哔哩私信留言或提出 Issue
