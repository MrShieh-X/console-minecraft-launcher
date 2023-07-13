# Console Minecraft Launcher
A Minecraft Java Edition Launcher Running on the Command Line

[![Latest Version](https://img.shields.io/badge/Release-v2.2-brightgreen)](https://github.com/MrShieh-X/console-minecraft-launcher/releases)
![Release Date](https://img.shields.io/badge/Date-2023--07--13-brightgreen)
![Software Requires](https://img.shields.io/badge/Software%20Requires-Java%208-blue)

### [中文README](README.md) | [Update Logs](update_logs-en.md) | [Troubleshooting](troubleshooting-en.md)

## 🎬 Video Tutorial
English: [\[CMCL2.0\]MC Launcher operates by command, supports Microsoft login|authlib-injector|nide8auth|mod/modpack searching and installation|custom skin and cape](https://www.youtube.com/watch?v=Sqeu_Pahm-0) <br/>
Chinese: [\[CMCL2.0\]命令行版MC启动器, 支持正版登录|外置登录|统一通行证|模组/整合包搜索安装|自定义皮肤披风](https://www.bilibili.com/video/BV1bY411R7wa/)<br/>
If you still have any problems after watching the video or using it, you can refer to [Troubleshooting](troubleshooting-en.md) or ask the author through the author's contact information found in [About Author](#-about-author).

## 🏆 Special Thanks
- [@Graetpro-X](https://github.com/Graetpro): Developed some functions
- [@FZZkill](https://github.com/FZZkill): Help to beautify the README
- [@mail_set](https://space.bilibili.com/435654748) ([Personal Website](https://mailset.top)): Upload CMCL to AUR repository (named`cmcl`)

## 📕 Tutorial
- If your operating system is Windows, you can download `cmcl.exe` in Releases, then open cmd, use `cd /d <directory path>` to open the directory where `cmcl.exe` is located, and execute the command `cmcl` , followed by options and parameters. If you want to use CMCL in any directory, please refer to ["How to use CMCL in any path?" in Troubleshooting](troubleshooting-en.md#how-to-use-cmcl-in-any-path).
- If your operating system is ArchLinux, you can use yay to install CMCL, the command is as follows:
```shell
yay -S cmcl
```
Then directly enter the command `cmcl`, followed by options and parameters.
- If your operating system is macOS, you can use Homebrew to install CMCL, the command is as follows:
```shell
brew tap MrShieh-X/brew
brew install cmcl
```
Then directly enter the command `cmcl`, followed by options and parameters. </br>
The command to update the launcher is:
```shell
brew update
brew upgrade cmcl
```
The command to uninstall is:
```shell
brew uninstall cmcl
```
- If you are other Linux users, or you are a macOS user, but do not want to use Homebrew to install, you can find the latest version in Releases, download `cmcl`, and then open the directory where `cmcl` is located in the terminal, execute `. /cmcl`, followed by options and parameters.
- If none of the above methods apply to you, you can find the latest version in Releases, download `cmcl.jar`, then open the directory where `cmcl.jar` is located in the terminal (or cmd under Windows), and execute the following command, Followed by options and parameters. This method is available on most operating systems.
```shell
java -jar cmcl.jar
```
For specific usage, please refer to [Help Documentation](#-help-documentation). If you want to modify the configuration of the launcher, please refer to [Configurations](#-configurations).

## 📖 Help Documentation
Get general help documentation via `cmcl -h` or `cmcl --help`<br/>
To get help documentation of a single function, add `-h` or `--help` after the function option, such as `cmcl account --help`<br/>
Note: Content in square brackets is optional. A comma in an option means that both options can do the same thing.<br/>
For specifying content for options, you can only add content directly after the abbreviated option (a hyphen) (no spaces), such as `cmcl -lD:\.minecraft`,<br/>
you can only add an equal sign after the complete option (two hyphens) and then enter the content, such as `cmcl --list=D:\.minecraft`,<br/>
or add a space after the two and then enter the content, such as `cmcl -l D:\.minecraft`; `cmcl --list D:\.minecraft`, for details, please refer to the example after the option description.<br/>
For some commands that may be entered frequently, you can set simplified commands, see ["Can I make a 'shortcut' for a command I type often?" in Troubleshooting](troubleshooting-en.md#can-i-make-a-shortcut-for-a-command-i-type-often).

## 🔧 Configurations
The configurations are storing in a JSON file named cmcl.json in the launcher running directory (`userDirectory/.config/cmcl` under Linux and macOS), you can edit them by a file editor (need to know JSON tutorial, recommended to backup before modification) or the program arguments `config <config name> <content>` (see Help Documentation Configuration Related).<br/>
If you edit configurations by a file editor, the configuration name and text configuration content need to be enclosed in double quotes ("").<br/>
If you edit by command, if the configuration content contains spaces, it needs to be enclosed in double quotes ("").<br/>
You can view the table below through the options `config --view`.<br/>
Note: For configurations whose type is Boolean, its value can be `true` which means "yes", or `false` which means "no".

| Configuration Name      | Type        | Meaning                                                                                                                                                                                                                                                           |
|-------------------------|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| accounts                | JSON Array  | Accounts (Non-direct modification, please use "account -h" to get the relevant tutorial for modification)                                                                                                                                                         |
| downloadSource          | Integer     | Download source, 0 is the official, 1 is BMCLAPI, 2 is MCBBS                                                                                                                                                                                                      |
| language                | Text        | Language, zh is Simplified Chinese, en is English and cantonese is Cantonese (Simplified)                                                                                                                                                                         |
| selectedVersion         | Text        | Selected start version, you can directly use "cmcl" to start it                                                                                                                                                                                                   |
| maxMemory               | Integer     | [**Game related**] Maximum (Unit: MB)                                                                                                                                                                                                                             |
| gameDir                 | Text        | [**Game related**] Custom the path of the game directory (or set working directory), default is .minecraft                                                                                                                                                        |
| assetsDir               | Text        | [**Game related**] Custom assets resource directory path, if empty, it is the assets directory in the game directory                                                                                                                                              |
| resourcesDir            | Text        | [**Game related**] Custom resource pack directory path, if empty, it is the resourcepacks directory in the game directory                                                                                                                                         |
| javaPath                | Text        | [**Game related**] Java Path (It will get automatically if it is empty)                                                                                                                                                                                           |
| windowSizeWidth         | Integer     | [**Game related**] The width of the game window                                                                                                                                                                                                                   |
| windowSizeHeight        | Integer     | [**Game related**] The height of the game window                                                                                                                                                                                                                  |
| isFullscreen            | Boolean     | [**Game related**] Whether the game window is fullscreen or not                                                                                                                                                                                                   |
| exitWithMinecraft       | Boolean     | [**Game related**] When running the game, whether or not you need to exit the launcher and exit the game by the way                                                                                                                                               |
| printStartupInfo        | Boolean     | [**Game related**] When starting the game, whether to output startup information (Java path, maximum memory, etc.)                                                                                                                                                |
| checkAccountBeforeStart | Boolean     | [**Game related**] Check whether the account is available before starting the game                                                                                                                                                                                |
| jvmArgs                 | JSON Array  | [**Game related**] Customize JVM arguments (Check out the examples below or use "jvmArgs -h" to get the relevant tutorial for modification)                                                                                                                       |
| gameArgs                | JSON Object | [**Game related**] Customize game arguments (Check out the examples below or use "gameArgs -h" to get the relevant tutorial for modification)                                                                                                                     |
| qpLogFile               | Text        | [**Game related**] The log file path (relative to the game directory) of Quick Play (a new feature of Minecraft 1.20, set the following three configurations to start the game and directly enter the save, server or realms, only one item can be set), optional |
| qpSaveName              | Text        | [**Game related**] The name of the save that the quick play will join directly                                                                                                                                                                                    |
| qpServerAddress         | Text        | [**Game related**] The address (including port) of the server that the quick play will join directly, this configuration also applies to versions prior to 1.20                                                                                                   |
| qpRealmsID              | Text        | [**Game related**] The ID of the realms that the quick play will join directly                                                                                                                                                                                    |
| proxyEnabled            | Boolean     | Whether to enable network proxy                                                                                                                                                                                                                                   |
| proxyHost               | Text        | Proxy Host Address                                                                                                                                                                                                                                                |
| proxyPort               | Integer     | Proxy Port                                                                                                                                                                                                                                                        |
| proxyUsername           | Text        | Proxy authentication username(optional for proxy)                                                                                                                                                                                                                 |
| proxyPassword           | Text        | Proxy authentication password(optional for proxy)                                                                                                                                                                                                                 |
| modDownloadSource       | Text        | Mod download source, curseforge or modrinth                                                                                                                                                                                                                       |
| modpackDownloadSource   | Text        | Modpack download source, curseforge or modrinth                                                                                                                                                                                                                   |
| simplifyCommands        | JSON Object | Simplify commands (use "simplify -h" to get the relevant tutorial for modification)                                                                                                                                                                               |

<details>
<summary>Example for jvmArgs</summary>

```json
["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"]
```
The above content means that the JVM arguments `-Dfile.encoding=UTF-8`, `-Djava.library.path=natives` and `-XX:+PrintGC` will be added.
</details>

<details>
<summary>Example for gameArgs</summary>

```json
{
  "fullscreen": "",
  "arg1": "value1",
  "arg2": "value2",
  "arg3": "space value3"
}
```
The above content means that the game arguments `--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value3"` will be added.
</details>

## 🌏 Supported Interface Languages
- English
- Simplified Chinese
- Cantonese (Simplified)

## ©️ Copyright
MrShiehX owns the copyright of this program.<br/>
Anyone can take advices of this program to us.

## 📄 License
The software is distributed under [GPL v3](https://www.gnu.org/licenses/gpl-3.0.html) with additional terms.

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

### 📑 Additional terms under GPLv3 Section 7
You must <b>not</b> remove the copyright declaration displayed in the software. \[[under GPLv3, 7(b).](https://github.com/MrShieh-X/console-minecraft-launcher/blob/f266ff87c0af3487ce66b47afbdb5d6dbc90f240/LICENSE#L368-L370)\]

## 📢 Disclaimer
- The copyright of Minecraft belongs to Mojang Studios and Microsoft. The software producer is not responsible for any copyright issues arising from the use of CMCL. Please support the official game.
- All consequences arising from the use of CMCL by the user shall be borne by the user himself. Any legal disputes and conflicts involving CMCL have nothing to do with the developer, and CMCL and the developer will not bear any responsibility.

## 🎓 About Author
MrShiehX<br/>
- Bilibili:<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>
- Youtube:<br/>
  [@MrShiehX](https://www.youtube.com/channel/UC03_vrWM8TfaU1k9VYVzW0A) <br/>

## If you find any bugs in CMCL, or have new ideas, please leave a message on Bilibili or raise an issue.
