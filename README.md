# Console Minecraft Launcher

一个在控制台上运行的 Minecraft Java 版的启动器

### [English README](README-en.md) <br/>

### [查看更新日志](update_logs.md) <br/>

## 本程序需要的软件配置

- Java 8 或更高版本

## 支持的界面语言

- 英语
- 简体中文

## 存在或已修复的 BUG

- （已在 v1.4 中修复）~~无法启动 OptiFine 和 Forge 共存的低于 1.13（不包括 1.13）的游戏版本。~~

## 配置

配置存储在程序运行目录的一个名为 cmcl.json 的 JSON 文件，你可以使用文本编辑器（需了解 JSON 教程）或`-config <配置名称> <配置值>`的程序参数（详见[使用教程](#使用教程)配置相关）以修改配置。<br/>
使用文本编辑器编辑的话，配置名称以及文本配置值需要在英语的双引号（""）内。<br/>
使用命令来修改时，如果配置值包含空格，则需要使用英语的双引号（""）括起来<br/>
注意：如果类型为整数且值为负数（或参数值是“-”开头的），须在该项前加上一个反斜杠“\”（也可能是两个），否则会被误认为是个参数，如：`\-10`或`\\-10`。<br/>
该内容可通过参数`-config -view`查看。

| 配置名            | 类型      | 含义                                                                               |
| ----------------- | --------- | ---------------------------------------------------------------------------------- |
| accounts          | JSON 数组 | 账号，请通过“-account -u”获得相关使用教程以进行修改                                |
| downloadSource    | 整数      | 下载源，0 为默认，1 为 BMCLAPI，2 为 MCBBS                                         |
| language          | 字符串    | 语言，zh 为简体中文，en 为英文                                                     |
| selectedVersion   | 字符串    | 已选择的版本                                                                       |
| proxyHost         | 字符串    | 代理主机地址                                                                       |
| proxyPort         | 整数      | 代理端口                                                                           |
| proxyUsername     | 字符串    | 代理验证的账户（代理可选）                                                         |
| proxyPassword     | 字符串    | 代理验证的密码（代理可选）                                                         |
| maxMemory         | 整数      | [**游戏相关**] 最大内存（单位：MB）                                                |
| minMemory         | 整数      | [**游戏相关**] 最小内存（单位：MB）                                                |
| gameDir           | 字符串    | [**游戏相关**] 自定义游戏目录路径（或设置版本隔离），默认为.minecraft              |
| assetsDir         | 字符串    | [**游戏相关**] 自定义 assets 资源目录路径，若为空则为游戏目录内的 assets 目录      |
| resourcesDir      | 字符串    | [**游戏相关**] 自定义资源包目录路径，若为空则为游戏目录内的 resourcepacks 目录     |
| javaPath          | 字符串    | [**游戏相关**] Java 路径（如果为空会自动获得）                                     |
| windowSizeWidth   | 整数      | [**游戏相关**] 游戏窗口的宽                                                        |
| windowSizeHeight  | 整数      | [**游戏相关**] 游戏窗口的高                                                        |
| isFullscreen      | 零值      | [**游戏相关**] 如果启用此选项，则为全屏；                                          |
| exitWithMinecraft | 布尔值    | [**游戏相关**] 运行游戏时，若需要退出该程序时顺便退出游戏，则为 true，否则为 false |
| jvmArgs           | JSON 数组 | [**游戏相关**] 自定义 JVM 参数（追加或覆盖）                                       |
| gameArgs          | JSON      | [**游戏相关**] 自定义游戏参数（追加或覆盖                                          |

<details>
<summary>jvmArgs的示例</summary>
<pre>
["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"\]<br/>
（设置“-Dfile.encoding=UTF-8”、<br/>
“-Djava.library.path=natives”<br/>
以及“-XX:+PrintGC”的参数）或通过“-jvmArgs -u”获得相关使用教程以进行修改</pre>
</details>

<details>
<summary>gameArgs的示例</summary>
<pre>）
{"fullscreen": "",<br/>
"arg1": "value1",<br/>
"arg2": "value2",<br/>
"arg3": "space value2"}<br/>
（将会在游戏参数后加入“--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value2"”）或通过“-gameArgs -u”获得相关使用教程以进行修改</pre>
</details>

## 使用教程

#### 获得全部使用手册，直接加 -u，如`java -jar CMCL.jar -u`

#### 获得单个功能使用手册，在功能参数后面加 -u，如`java -jar CMCL.jar -account -u`

| 作用                       | 命令                                |
| -------------------------- | ----------------------------------- |
| 获得使用手册               | -u 或 -usage                        |
| 启动选择的版本             | 无参数直接运行 或 -b 或 -start      |
| 启动特定的版本唯一参数     | <版本名称> 或 -b <版本名称>         |
| 列出所有版本               | -l 或 -list 或 -ls                  |
| 列出某个游戏目录的所有版本 | -l <目标游戏目录>                   |
| 打印启动命令               | -p <版本名称> 或 -print <版本名称>  |
| 选择版本                   | -s <版本名称> 或 -select <版本名称> |
| 获得关于信息               | -a 或 -about                        |
| 进入沉浸模式               | -i 或 -immersive                    |
| 检查更新                   | -cfu                                |

<details>
  <summary><b><font size="4">配置相关</font></b></summary>

| 作用                 | 命令                                                                                  |
| -------------------- | ------------------------------------------------------------------------------------- |
| 查看所有可设置的配置 | -config -view                                                                         |
| 打印某项配置         | -config -p <配置名>                                                                   |
| 打印全部配置         | -config -a                                                                            |
| 打印配置原内容       | -config -o <缩进的空格数，可为空，默认为 2>                                           |
| 清空配置             | -config -c                                                                            |
| 删除某项配置         | -config -r <配置名称>                                                                 |
| 设置配置（不分类型） | -config <配置名称> <配置值>                                                           |
| 设置某项配置         | -config -s -t<配置类型:如 i:整数\|b:布尔值\|s:字符串\|f:小数> -n<配置名称> -v<配置值> |

</details>
<details>
  <summary><b><font size="4">账号相关</font></b></summary>

| 作用                               | 命令                                                            |
| ---------------------------------- | --------------------------------------------------------------- |
| 选择账号                           | -account <序号>                                                 |
| 列出账号                           | -account -p                                                     |
| 删除账号                           | -account -t <序号>                                              |
| 离线登录                           | -account -l -o <用户名> -s(可选，登录成功后选择此账号)          |
| 微软账号登录                       | -account -l -m -s(可选，登录成功后选择此账号)                   |
| 外置账号登录                       | -account -l -a <服务器地址> -s(可选，登录成功后选择此账号)      |
| 刷新账号                           | -account -r                                                     |
| 下载皮肤                           | -account -s -d <皮肤文件存储路径>                               |
| 设置皮肤（微软账户不可用）         | -account -s -u <皮肤文件路径(如果是离线账号，不输入则取消设置)> |
| 设置皮肤为 Steve（微软账户不可用） | -account -s -e                                                  |
| 设置皮肤为 Alex（微软账户不可用）  | -account -s -x                                                  |
| 设置披风（仅离线账号）             | -account -c <披风文件路径，不输入则取消设置>                    |

</details>
<details>
  <summary><b><font size="4">版本相关</font></b></summary>

| 作用                       | 命令                                                |
| -------------------------- | --------------------------------------------------- |
| 版本信息                   | -version -i <版本名称>                              |
| 删除某个版本               | -version -d <版本名称>                              |
| 重命名版本                 | -version -r <版本名称> -t <新版本名称>              |
| 重新下载原生依赖库文件     | -version -n <版本名称>                              |
| 查找缺少的依赖库文件并下载 | -version -l <版本名称>                              |
| 把版本补充完整             | -version -b <版本名称>                              |
| 安装 Fabric 到本地版本     | -version -f <版本名称> -fapi(可选，安装 Fabric Api) |
| 安装 Forge 到本地版本      | -version -o <版本名称>                              |
| 安装 LiteLoader 到本地版本 | -version -e <版本名称>                              |
| 安装 OptiFine 到本地版本   | -version -p <版本名称>                              |
| 安装 Quilt 到本地版本      | -version -q <版本名称>                              |

**注：可在安装 Fabric、Forge、LiteLoader、OptiFine 和 Quilt 的命令后添加上“-v <版本>”以指定版本，以免输入命令后再选择版本。**<br/>

</details>
<details>
  <summary><b><font size="4">版本配置相关</font></b></summary>

| 作用                     | 命令                                                                                                                       |
| ------------------------ | -------------------------------------------------------------------------------------------------------------------------- |
| 通用                     | -vcfg <版本名称> <配置名称，使用“-config -view”查看的内容中带有“[游戏相关]”的内容，有空格则加上双引号，不输入则为全局配置> |
| 设置版本隔离(同 gameDir) | -vcfg <版本名称> -workingDirectory <目标目录，不输入则为默认>                                                              |

</details>
<details>
  <summary><b><font size="4">自定义JVM参数相关</font></b></summary>

| 作用         | 命令                                         |
| ------------ | -------------------------------------------- |
| 输出所有参数 | -jvmArgs -p <缩进的空格数，可为空，默认为 2> |
| 添加参数     | -jvmArgs -a <参数内容>                       |
| 删除参数     | -jvmArgs -d <序号，从 0 开始>                |

注：可在命令后面添上“-version <版本名称>”以指定要设置的版本<br/>

</details>
<details>
  <summary><b><font size="4">自定义游戏参数相关</font></b></summary>

| 作用         | 命令                                                                   |
| ------------ | ---------------------------------------------------------------------- |
| 输出所有参数 | -gameArgs -p <缩进的空格数，可为空，默认为 2>                          |
| 添加参数     | -gameArgs -a -n <参数名称> -v <参数值(可选，如果此项为空则不要输入-v)> |
| 删除参数     | -gameArgs -d <参数名称>                                                |

注：可在命令后面添上“-version <版本名称>”以指定要设置的版本<br/>

</details>
<details>
  <summary><b><font size="4">安装版本相关</font></b></summary>

| 作用         | 命令                                                                  |
| ------------ | --------------------------------------------------------------------- |
| 直接安装版本 | -install <版本名称（如果有空格要加双引号）> -n <存储的版本名称(可选)> |
| 可选的参数   | -f 安装 Fabric -fapi(可选，安装 Fabric Api)                           |
| -o           | 安装 Forge                                                            |
| -e           | 安装 LiteLoader                                                       |
| -p           | 安装 OptiFine                                                         |
| -q           | 安装 Quilt                                                            |
| -t           | <线程数> 设置下载资源文件的线程数（默认为 64）                        |
| -na          | 不下载资源文件                                                        |
| -nl          | 不下载依赖库文件                                                      |
| -nn          | 不下载原生依赖库文件                                                  |

**注：Fabric 和 Forge、Fabric 和 LiteLoader、Fabric 和 OptiFine 不能同时安装或共存（Quilt 与 Fabric 相同，但它们也不能共存）**<br/>
**可以在参数-f、-o、-e、-p、-q 后指定版本，以免安装时询问版本，例:“-f 0.14.8”则为安装版本为 0.14.8 的 Fabric。**<br/>
显示可安装的版本（若没有设置范围，默认显示该类型的全部版本）：-install -s <版本类型：a 全部；r 正式版；s 快照版；oa 远古 alpha 版；ob 远古 beta 版><br/>
设置时间范围（可选）：-i <从年>-<从月>-<从日>/<到年>-<到月>-<到日><br/>
例：-i 2020-05-09/2021-10-23

</details>

<details>
  <summary><b><font size="4">模组相关（下载源：CurseForge）</font></b></summary>

| 作用                           | 命令                 |
| ------------------------------ | -------------------- |
| 搜索模组并安装（通过名称）     | -mod -i <模组名称>   |
| 搜索模组并安装（通过 ID）      | -mod -i -c <模组 ID> |
| 搜索模组并显示信息（通过名称） | -mod -s <模组名称>   |
| 搜索模组并显示信息（通过 ID）  | -mod -s -c <模组 ID> |

</details>

<details>
  <summary><b><font size="4">整合包相关</font></b></summary>

| 作用                             | 命令                                                                                      |
| -------------------------------- | ----------------------------------------------------------------------------------------- |
| 安装整合包的可选参数             |                                                                                           |
|                                  | -t <线程数> 设置下载资源文件的线程数（默认为 64）                                         |
|                                  | -na 不下载资源文件                                                                        |
|                                  | -nl 不下载依赖库文件                                                                      |
|                                  | -nn 不下载原生依赖库文件                                                                  |
| CurseForge                       | -modpack                                                                                  |
| 搜索整合包并安装（通过名称）     | -modpack -i <整合包名称> -k(可选，安装完成后保留文件)                                     |
| 搜索整合包并安装（通过 ID）      | -modpack -i -c <整合包 ID> -k(可选，安装完成后保留文件)                                   |
| 搜索整合包并显示信息（通过名称） | -modpack -s <整合包名称>                                                                  |
| 搜索整合包并显示信息（通过 ID）  | -modpack -s -c <整合包 ID>                                                                |
| Modrinth                         | -modpack2                                                                                 |
| 搜索整合包并安装（通过名称）     | -modpack2 -i <整合包名称> -k(可选，安装完成后保留文件) -l <可选，限制结果数量，默认为 50> |
| 搜索整合包并安装（通过 ID）      | -modpack2 -i -c <整合包 ID> -k(可选，安装完成后保留文件)                                  |
| 搜索整合包并显示信息（通过名称） | -modpack2 -s <整合包名称> -l <可选，限制结果数量，默认为 50>                              |
| 搜索整合包并显示信息（通过 ID）  | -modpack2 -s -c <整合包 ID>                                                               |
| 安装本地整合包                   | -modpack -l <整合包路径>                                                                  |

</details>

<details>
  <summary><b><font size="4">模组相关（下载源：Modrinth）</font></b></summary>

| 作用                           | 命令                                                   |
| ------------------------------ | ------------------------------------------------------ |
| 搜索模组并安装（通过名称）     | -mod2 -i <模组名称> -l <可选，限制结果数量，默认为 50> |
| 搜索模组并安装（通过 ID）      | -mod2 -i -c <模组 ID>                                  |
| 搜索模组并显示信息（通过名称） | -mod2 -s <模组名称> -l <可选，限制结果数量，默认为 50> |
| 搜索模组并显示信息（通过 ID）  | -mod2 -s -c <模组 ID>                                  |

</details>

## 关于作者

MrShiehX<br/>

- 职业：<br/>
  学生<br/>
- 哔哩哔哩：<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>
- Youtube：<br/>
  [@MrShiehX](https://www.youtube.com/channel/UC03_vrWM8TfaU1k9VYVzW0A) <br/>

## 如果您在本程序发现任何 BUG，或者有新的想法，欢迎在哔哩哔哩私信留言或提出 Issue

## 版权

MrShiehX 拥有该程序的版权。<br/>
任何人都可以对此程序提出意见和建议。

## 最新版本：1.8 (发布于 2022 年 11 月 11 日)

## 程序截图

![程序截图](screenshot.gif "程序截图")<br/>

## 视频教程

### 中文

[\[CMCL\]纯命令行的 Minecraft 启动器 | 告别繁琐，全新体验](https://www.bilibili.com/video/BV1ua41187od) <br/>
[\[CMCL\]今天，它，重出江湖了](https://www.bilibili.com/video/BV1AY411A7XU) <br/>

### 英语

[\[CMCL\] A Launcher for Minecraft Running On The Console](https://www.youtube.com/watch?v=SczdBQT9vOY)

## 主要开发者

### [@MrShieh-X](https://github.com/MrShieh-X)

### [@Graetpro-X](https://github.com/Graetpro)

## 软件协议

该软件在 [GPL v3](https://www.gnu.org/licenses/gpl-3.0.html) 下分发，附带附加条款。

    Console Minecraft Launcher
    Copyright (C) 2021-2022  MrShiehX <3553413882@qq.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

### 附加条款（依据 GPLv3 协议第七条）

你<b>不得</b>移除本程序所显示的版权声明。\[[依据 GPLv3, 7(b).](https://github.com/MrShieh-X/console-minecraft-launcher/blob/f266ff87c0af3487ce66b47afbdb5d6dbc90f240/LICENSE#L368-L370)\]
