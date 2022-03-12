# Console Minecraft Launcher
一个在控制台上运行的 Minecraft Java 版的启动器

[Click me to go to the README in English](https://github.com/MrShieh-X/console-minecraft-launcher/blob/master/README.md) <br/>
[点我转到本程序的更新日志](https://github.com/MrShieh-X/console-minecraft-launcher/blob/master/update_logs-zh.md) <br/>

## 版权
MrShiehX 拥有该程序的版权。<br/>
任何人都可以对此程序提出意见和建议。

## 版本
最新版本：<br/>
<b>1.0 (2022年3月12日)</b><br/>
历史版本：<br/>
<b>1.0 (2022年3月12日)（第一个版本）</b><br/>

## 软件协议
该软件在 [GPL v3](https://www.gnu.org/licenses/gpl-3.0.html) 下分发，附带附加条款。

### 附加条款（依据 GPLv3 协议第七条）
你<b>不得</b>移除本程序所显示的版权声明。（依据 GPLv3, 7(b).）

## 本程序需要的软件配置
* Java 8 或更高版本

## 支持的语言
- 英语
- 简体中文

## 程序截图
![程序截图](screenshot.png "程序截图")</br>

## 配置
配置存储在程序运行目录的一个名为cmcl.json的JSON文件，你可以使用文本编辑器（需了解JSON教程）或`-config -s -t <配置类型> -n <配置名称> -v <配置值>`的程序参数（详见 [使用教程配置相关](#配置相关)）以修改配置。</br>
使用文本编辑器编辑的话，只有字符串才需要双引号""，否则其他类型不需要。</br>
使用命令来修改时，注意如果字符串内有空格，就必须要加双引号""，否则可加可不加。

| 配置名|类型|含义|
| -----|:----:|:----:|
| playerName|字符串|玩家名|
| loginMethod|整数|账号类型，0为离线，2为微软账号|
| accessToken|字符串|正版登录相关|
| uuid|字符串|正版登录相关|
| tokenType|字符串|正版登录相关，token类型|
| language|字符串|语言，zh为简体中文，en为英文|
| maxMemory|整数|最大内存（单位：MB）|
| gameDir|字符串|自定义游戏目录路径，默认为.minecraft|
| assetsDir|字符串|自定义assets资源目录路径，若为空则为游戏目录内的assets目录|
| resourcesDir|字符串|自定义资源包目录路径，若为空则为游戏目录内的resourcepacks目录|
| javaPath|字符串|Java 路径（如果为空会自动获得）|
| selectedVersion|字符串|已选择的版本|
| windowSizeWidth|整数|游戏窗口的宽|
| windowSizeHeight|整数|游戏窗口的高|
| isFullscreen|布尔值|是否为全屏，是则为true，否则为false|
| exitWithMinecraft|布尔值|运行游戏时，若需要退出该程序时顺便退出游戏，则为true，否则为false|

## 使用教程
获得使用手册：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-u`</br>
启动选择的版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;无参数直接运行 或 `-b`</br>
启动特定的版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;唯一参数：`<版本名称>` 或 `-b <版本名称>`</br>
列出所有版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-l`</br>
列出某个游戏目录的所有版本：`-l <目标游戏目录>`</br>
打印启动命令：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-p <版本名称>`</br>
选择版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-s <版本名称>`</br>
获得关于信息：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-a`</br>

### 配置相关
&emsp;&emsp;打印某项配置：`-config -p <配置名>`</br>
&emsp;&emsp;打印全部配置：`-config -a`</br>
&emsp;&emsp;清空配置：&emsp;&emsp;`-config -c`</br>
&emsp;&emsp;删除某项配置：`-config -r <配置名称>`</br>
&emsp;&emsp;设置某项配置：`-config -s -t <配置类型，如`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`i 整数、`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`b 布尔值、`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`s 字符串以及`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`f 小数> -n <配置名称> -v <配置值>`</br>
### 账号相关
&emsp;&emsp;退出登录：&emsp;&emsp;&emsp;`-account -t`</br>
&emsp;&emsp;离线登录：&emsp;&emsp;&emsp;`-account -l -o <离线用户名>`</br>
&emsp;&emsp;微软账号登录：&emsp;`-account -l -m`</br>
&emsp;&emsp;刷新正版账号：&emsp;`-account -r`</br>
&emsp;&emsp;下载皮肤：&emsp;&emsp;&emsp;`-account -s -d <皮肤文件存储路径>`</br>
### 版本相关
&emsp;&emsp;删除版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -d <版本名称>`</br>
&emsp;&emsp;重命名版本：&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -r <版本名称> -t <新版本名称>`</br>
&emsp;&emsp;重新下载原生依赖库文件：&emsp;&emsp;`-version -n <版本名称>`</br>
&emsp;&emsp;查找缺少的依赖库文件并下载：`-version -l <版本名称>`</br>
### 安装版本相关
&emsp;&emsp;直接安装版本：`-install <版本名称（如果有空格要加双引号）> -n <存储的版本名称>`</br>
&emsp;&emsp;&emsp;可选的参数：`-t <线程数>`  设置下载资源文件的线程数（默认为10）</br>
&emsp;&emsp;&emsp;&emsp;`-na` 不下载资源文件</br>
&emsp;&emsp;&emsp;&emsp;`-nl` 不下载依赖库文件</br>
&emsp;&emsp;&emsp;&emsp;`-nn` 不下载原生依赖库文件</br>

&emsp;&emsp;显示可安装的版本（若没有设置范围，默认显示该类型的全部版本）：`-install -s <版本类型：a 全部；r 正式版；s 快照版；oa 远古alpha版；ob 远古beta版>`</br>
&emsp;&emsp;&emsp;设置时间范围（可选）：`-i <从年>-<从月>-<从日>/<到年>-<到月>-<到日>`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;例：`-i 2020-05-09/2021-10-23`

## 关于作者
MrShiehX<br/>
- 职业：<br/>
  学生<br/>
- 邮箱：<br/>
  3553413882@qq.com<br/>
- QQ：<br/>
  3553413882（备注来意）<br/>
- 哔哩哔哩：<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>

## 如果您在本程序发现任何BUG，或者有新的想法，欢迎发送邮件或添加我的QQ。
