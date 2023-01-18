# Console Minecraft Launcher
A Launcher for Minecraft Java Edition Running On The Console

[![Latest Version](https://img.shields.io/badge/Release-v2.0-brightgreen)](https://github.com/MrShieh-X/console-minecraft-launcher/releases)
![Release Date](https://img.shields.io/badge/Date-2023--01--18-brightgreen)
![Software Requires](https://img.shields.io/badge/Software%20Requires-Java%208-blue)

### [中文README](README.md) | [View Update Logs](update_logs-en.md) 

## Video Tutorial
English: [\[CMCL2.0\]MC Launcher operates by command, supports Microsoft login|authlib-injector|nide8auth|mod/modpack searching and installation|custom skin and cape](https://www.youtube.com/watch?v=Sqeu_Pahm-0) <br/>
Chinese: [\[CMCL2.0\]命令行版MC启动器, 支持正版登录|外置登录|统一通行证|模组/整合包搜索安装|自定义皮肤披风](https://www.bilibili.com/video/BV1bY411R7wa/)

## Supported Interface Languages
- English
- Simplified Chinese
- Cantonese (Simplified)

## Special Thanks
- [@Graetpro-X](https://github.com/Graetpro): Developed some functions
- [@FZZkill](https://github.com/FZZkill): Help to beautify the README

## Help Documentation
To get all the help documentations, directly add `-h` or `--help`, such as `cmcl -h`<br/>
To get help documentation of a single function, add `-h` or `--help` after the function option, such as `cmcl account --help`<br/>
Note: Content in square brackets is optional. A comma in an option means that both options can do the same thing.<br/>
For specifying content for options, you can only add content directly after the abbreviated option (a hyphen) (no spaces), such as `cmcl -lD:\.minecraft`,<br/>
you can only add an equal sign after the complete option (two hyphens) and then enter the content, such as `cmcl --list=D:\.minecraft`,<br/>
or add a space after the two and then enter the content, such as `cmcl -l D:\.minecraft`; `cmcl --list D:\.minecraft`, for details, please refer to the example after the option description.

## Configurations
The configurations are storing in a JSON file named cmcl.json in the launcher running directory (userDirectory/.config/cmcl under Linux), you can edit them by a file editor (need to know JSON tutorial, backup before modification) or the program arguments `config <config name> <content>` (see Help Documentation Configuration Related).<br/>
If you edit configurations by a file editor, the configuration name and text configuration content need to be enclosed in double quotes ("").<br/>
If you edit by command, if the configuration content contains spaces, it needs to be enclosed in double quotes ("").<br/>
You can view this content through the options `config --view`.

| Configuration Name      | Type        | Meaning                                                                                                                                       |
|-------------------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| accounts                | JSON Array  | Accounts (Non-direct modification, please use "account -h" to get the relevant tutorial for modification)                                     |
| downloadSource          | Integer     | Download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS                                                                                   |
| language                | String      | Language, zh is Simplified Chinese, en is English and cantonese is Cantonese (Simplified)                                                     |
| selectedVersion         | String      | Selected start version                                                                                                                        |
| maxMemory               | Integer     | [**Game related**] Maximum (Unit: MB)                                                                                                         |
| gameDir                 | String      | [**Game related**] Custom the path of the game directory (or set working directory), default is .minecraft                                    |
| assetsDir               | String      | [**Game related**] Custom assets resource directory path, if empty, it is the assets directory in the game directory                          |
| resourcesDir            | String      | [**Game related**] Custom resource pack directory path, if empty, it is the resourcepacks directory in the game directory                     |
| javaPath                | String      | [**Game related**] Java Path (It will get automatically if it is empty)                                                                       |
| windowSizeWidth         | Integer     | [**Game related**] The width of the game window                                                                                               |
| windowSizeHeight        | Integer     | [**Game related**] The height of the game window                                                                                              |
| isFullscreen            | Boolean     | [**Game related**] Whether it is full screen, true if yes, false otherwise                                                                    |
| exitWithMinecraft       | Boolean     | [**Game related**] When running the game, if you need to exit the launcher and exit the game by the way, it is true, otherwise it is false    |
| printStartingInfo       | Boolean     | [**Game related**] When starting the game, whether to output startup information (Java path, maximum memory, etc.)                            |
| checkAccountBeforeStart | Boolean     | [**Game related**] Check whether the account is available before starting the game                                                            |
| jvmArgs                 | JSON Array  | [**Game related**] Customize JVM arguments (Check out the examples below or use "jvmArgs -h" to get the relevant tutorial for modification)   |
| gameArgs                | JSON Object | [**Game related**] Customize game arguments (Check out the examples below or use "gameArgs -h" to get the relevant tutorial for modification) |
| proxyHost               | String      | Proxy Host Address                                                                                                                            |
| proxyPort               | Integer     | Proxy Port                                                                                                                                    |
| proxyUsername           | String      | Proxy authentication username(optional for proxy)                                                                                             |
| proxyPassword           | String      | Proxy authentication password(optional for proxy)                                                                                             |

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

## Copyright
MrShiehX owns the copyright of this program.<br/>
Anyone can take advices of this program to us.

## License
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

### Additional terms under GPLv3 Section 7
You must <b>not</b> remove the copyright declaration displayed in the software. \[[under GPLv3, 7(b).](https://github.com/MrShieh-X/console-minecraft-launcher/blob/f266ff87c0af3487ce66b47afbdb5d6dbc90f240/LICENSE#L368-L370)\]

## Disclaimer
- The copyright of Minecraft belongs to Mojang Studios and Microsoft. The software producer is not responsible for any copyright issues arising from the use of CMCL. Please support the official game.
- All consequences arising from the use of CMCL by the user shall be borne by the user himself. Any legal disputes and conflicts involving CMCL have nothing to do with the developer, and CMCL and the developer will not bear any responsibility.

## About Author
MrShiehX<br/>
- Occupation: <br/>
  Student<br/>
- Bilibili:<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>
- Youtube:<br/>
  [@MrShiehX](https://www.youtube.com/channel/UC03_vrWM8TfaU1k9VYVzW0A) <br/>

## If you find any bugs in CMCL, or have new ideas, please leave a message on Bilibili or raise an issue.
