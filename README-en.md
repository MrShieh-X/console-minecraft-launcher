# Console Minecraft Launcher

A launcher for Minecraft Java Edition running on the console

### [中文 README](README.md) <br/>

### [View update log](update_logs.md) <br/>

## The software configuration required by this program

- Java 8 or later

## Supported interface languages

- English
- Simplified Chinese

## Existing or fixed bugs

- (Fixed in v1.4) ~~Unable to start game versions lower than 1.13 (excluding 1.13) where OptiFine and Forge coexist. ~~

## configuration

The configuration is stored in a JSON file named cmcl.json in the program running directory, you can use a text editor (need to understand the JSON tutorial) or `-config <configuration name> <configuration value>` program parameters (see [use Tutorial] (#Using Tutorial) configuration related) to modify the configuration. <br/>
If you use a text editor to edit, the configuration name and text configuration value need to be enclosed in English double quotation marks (""). <br/>
When using commands to modify, if the configuration value contains spaces, you need to use English double quotes ("") to enclose it<br/>
Note: If the type is an integer and the value is negative (or the parameter value starts with "-"), you must add a backslash "\" (or two) before the item, otherwise it will be mistaken for a Parameters, such as: `\-10` or `\\-10`. <br/>
The content can be viewed through the parameter `-config -view`.

| configuration name | type          | meaning                                                                                                                         |
| ------------------ | ------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| accounts           | JSON array    | account number, please use "-account -u" to obtain relevant tutorials for modification                                          |
| downloadSource     | integer       | download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS                                                                     |
| language           | string        | language, zh is Simplified Chinese, en is English                                                                               |
| selectedVersion    | string        | selected version                                                                                                                |
| proxyHost          | string        | proxy host address                                                                                                              |
| proxyPort          | integer       | proxy port                                                                                                                      |
| proxyUsername      | string        | account for proxy authentication (proxy optional)                                                                               |
| proxyPassword      | string        | password for proxy authentication (proxy optional)                                                                              |
| maxMemory          | integer       | [**game-related**] maximum memory (unit: MB)                                                                                    |
| minMemory          | integer       | [**game-related**] minimum memory (unit: MB)                                                                                    |
| gameDir            | string        | [**game-related**] custom game directory path (or set version isolation), the default is .minecraft                             |
| assetsDir          | String        | [**Game-related**] Custom assets resource directory path, if it is empty, it is the assets directory in the game directory      |
| resourcesDir       | String        | [**Game-related**] Custom resource pack directory path, if it is empty, it is the resourcepacks directory in the game directory |
| javaPath           | string        | [**game-related**] Java path (automatically obtained if empty)                                                                  |
| windowSizeWidth    | integer       | [**game-related**] the width of the game window                                                                                 |
| windowSizeHeight   | integer       | [**game-related**] height of the game window                                                                                    |
| isFullscreen       | zero value    | [**game related**] fullscreen if this option is enabled;                                                                        |
| exitWithMinecraft  | Boolean value | [**Game-related**] When running the game, if you need to exit the program and exit the game, it is true, otherwise it is false  |
| jvmArgs            | JSON array    | [**game-related**] custom JVM parameters (append or override)                                                                   |
| gameArgs           | JSON          | [**game-related**] custom game parameters (append or override)                                                                  |

<details>
<summary>Example of jvmArgs</summary>
<pre>
["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"\]<br/>
(set "-Dfile.encoding=UTF-8",<br/>
"-Djava.library.path=natives"<br/>
and "-XX:+PrintGC" parameters) or obtain related usage tutorials for modification through "-jvmArgs -u"</pre>
</details>

<details>
<summary>Example of gameArgs</summary>
<pre>
{"fullscreen": "",<br/>
"arg1": "value1",<br/>
"arg2": "value2",<br/>
"arg3": "space value2"}<br/>
("--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value2"" will be added after the game parameters) or use "-gameArgs -u" to obtain relevant tutorials for modification</pre>
</details>

## Tutorial

#### Get all manuals, directly add -u, such as `java -jar CMCL.jar -u`

#### To obtain a single function manual, add -u after the function parameter, such as `java -jar CMCL.jar -account -u`

| Function                               | Command                                     |
| -------------------------------------- | ------------------------------------------- |
| get usage manual                       | -u or -usage                                |
| start the selected version             | run without arguments or -b or -start       |
| Boot specific version unique parameter | <version name> or -b <version name>         |
| list all versions                      | -l or -list or -ls                          |
| list all versions of a game directory  | -l <target game directory>                  |
| print startup command                  | -p <version name> or -print <version name>  |
| select version                         | -s <version name> or -select <version name> |
| get about information                  | -a or -about                                |
| Enter immersive mode                   | -i or -immersive                            |
| check for updates                      | -cfu                                        |

<details>
   <summary><b><font size="4">Configuration related</font></b></summary>

| Function                                        | Command                                                                                                                                 |
| ----------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| View all configurable configurations            | -config -view                                                                                                                           |
| print a configuration                           | -config -p <config name>                                                                                                                |
| print all configuration                         | -config -a                                                                                                                              |
| Print the original content of the configuration | -config -o <number of spaces for indentation, can be empty, default is 2>                                                               |
| clear configuration                             | -config -c                                                                                                                              |
| Delete a configuration                          | -config -r <configuration name>                                                                                                         |
| Set configuration (regardless of type)          | -config <config name> <config value>                                                                                                    |
| Set a configuration                             | -config -s -t<configuration type: such as i: integer\|b: boolean\|s: string\|f: decimal> -n<configuration name> -v<configuration value> |

</details>
<details>
   <summary><b><font size="4">Account related</font></b></summary>

| function                                              | command                                                                                                              |
| ----------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| select account                                        | -account <serial number>                                                                                             |
| list accounts                                         | -account -p                                                                                                          |
| delete account                                        | -account -t <serial number>                                                                                          |
| Offline login                                         | -account -l -o <username> -s (optional, select this account after successful login)                                  |
| Microsoft account login                               | -account -l -m -s (optional, select this account after successful login)                                             |
| External account login                                | -account -l -a <server address> -s (optional, select this account after successful login)                            |
| refresh account                                       | -account -r                                                                                                          |
| Download skin                                         | -account -s -d <skin file storage path>                                                                              |
| Set skin (Microsoft account is not available)         | -account -s -u <skin file path (if it is an offline account, if you do not enter it, the setting will be cancelled)> |
| Set skin to Steve (Microsoft account not available)   | -account -s -e                                                                                                       |
| set skin to Alex (Microsoft account is not available) | -account -s -x                                                                                                       |
| Set the cloak (only for offline accounts)             | -account -c <cloak file path, if not entered, the setting will be cancelled>                                         |

</details>
<details>
   <summary><b><font size="4">version related</font></b></summary>

| Function                                               | Command                                                         |
| ------------------------------------------------------ | --------------------------------------------------------------- |
| version information                                    | -version -i <version name>                                      |
| delete a version                                       | -version -d <version name>                                      |
| rename version                                         | -version -r <version name> -t <new version name>                |
| Re-download native dependent library files             | -version -n <version name>                                      |
| Find missing dependent library files and download them | -version -l <version name>                                      |
| Complete the version                                   | -version -b <version name>                                      |
| Install Fabric to local version                        | -version -f <version name> -fapi (optional, install Fabric Api) |
| Install Forge to a local version                       | -version -o <version name>                                      |
| Install LiteLoader to local version                    | -version -e <version name>                                      |
| install OptiFine to local version                      | -version -p <version name>                                      |
| Install Quilt to local version                         | -version -q <version name>                                      |

**Note: You can add "-v <version>" after the command to install Fabric, Forge, LiteLoader, OptiFine and Quilt to specify the version, so as not to select the version after entering the command. **<br/>

</details>
<details>
   <summary><b><font size="4">version configuration related</font></b></summary>

| Function                                | Command                                                                                                                                                                                                |
| --------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| General                                 | -vcfg <version name> <configuration name, use "-config -view" to view the content with "[game-related]", if there is a space, add double quotes, if you not do enter it, it is a global configuration> |
| Set version isolation (same as gameDir) | -vcfg <version name> -workingDirectory <target directory, default if not entered>                                                                                                                      |

</details>
<details>
   <summary><b><font size="4">Custom JVM parameter related</font></b></summary>

| Function              | Command                                                                    |
| --------------------- | -------------------------------------------------------------------------- |
| Output all parameters | -jvmArgs -p <number of spaces for indentation, can be empty, default is 2> |
| Add parameters        | -jvmArgs -a <parameter content>                                            |
| Delete parameter      | -jvmArgs -d <number, starting from 0>                                      |

Note: You can add "-version <version name>" after the command to specify the version to be set<br/>

</details>
<details>
   <summary><b><font size="4">Custom game parameters related</font></b></summary>

| Function              | Command                                                                                            |
| --------------------- | -------------------------------------------------------------------------------------------------- |
| Output all parameters | -gameArgs -p <number of spaces for indentation, can be empty, default is 2>                        |
| Add parameter         | -gameArgs -a -n <parameter name> -v <parameter value (optional, do not enter -v if this is empty)> |
| delete parameter      | -gameArgs -d <parameter name>                                                                      |

Note: You can add "-version <version name>" after the command to specify the version to be set<br/>

</details>
<details>
   <summary><b><font size="4">Installation version related</font></b></summary>

| Function                 | Command                                                                                                 |
| ------------------------ | ------------------------------------------------------------------------------------------------------- |
| Install version directly | -install <version name (enclose double quotes if there are spaces)> -n <stored version name (optional)> |
| Optional parameters      | -f install Fabric -fapi (optional, install Fabric Api)                                                  |
| -o                       | Install Forge                                                                                           |
| -e                       | Install LiteLoader                                                                                      |
| -p                       | Install OptiFine                                                                                        |
| -q                       | Install Quilt                                                                                           |
| -t                       | <number of threads> Set the number of threads to download resource files (64 by default)                |
| -na                      | Do not download resource files                                                                          |
| -nl                      | Do not download dependent library files                                                                 |
| -nn                      | Do not download native dependent library files                                                          |

**Note: Fabric and Forge, Fabric and LiteLoader, Fabric and OptiFine cannot be installed or co-exist at the same time (Quilt is the same as Fabric, but they also cannot co-exist)**<br/>
**The version can be specified after the parameters -f, -o, -e, -p, -q, so as not to ask the version during installation, for example: "-f 0.14.8" means that the installed version is 0.14.8 Fabric. **<br/>
Display the installable version (if no range is set, all versions of this type will be displayed by default): -install -s <version type: a all; r official version; s snapshot version; oa ancient alpha version; ob ancient beta version>< br/>
Set time range (optional): -i <from year>-<from month>-<from day>/<to year>-<to month>-<to day><br/>
Example: -i 2020-05-09/2021-10-23

</details>

<details>
   <summary><b><font size="4">Module related (download source: CurseForge)</font></b></summary>

| Function                                          | Command             |
| ------------------------------------------------- | ------------------- |
| Search for mods and install them (by name)        | -mod -i <mod name>  |
| Search for mods and install them (by ID)          | -mod -i -c <mod ID> |
| search for mods and display information (by name) | -mod -s <mod name>  |
| search for mods and display info (by id)          | -mod -s -c <mod id> |

</details>

<details>
   <summary><b><font size="4">Integrated package related</font></b></summary>

| Function                                          | Command                                                                                                                                    |
| ------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| Optional parameters for installing a modpack      |                                                                                                                                            |
|                                                   | -t <number of threads> Set the number of threads to download resource files (64 by default)                                                |
|                                                   | -na do not download resource files                                                                                                         |
|                                                   | -nl Do not download dependent library files                                                                                                |
|                                                   | -nn Do not download native dependent library files                                                                                         |
| CurseForge                                        | -modpack                                                                                                                                   |
| search modpack and install (by name)              | -modpack -i <modpack name> -k (optional, keep files after installation)                                                                    |
| Search modpack and install (by ID)                | -modpack -i -c <modpack ID> -k (optional, keep file after installation)                                                                    |
| Search modpacks and display information (by name) | -modpack -s <modpack name>                                                                                                                 |
| Search modpack and display information (by ID)    | -modpack -s -c <modpack ID>                                                                                                                |
| Modrinth                                          | -modpack2                                                                                                                                  |
| Search modpacks and install them (by name)        | -modpack2 -i <modpack name> -k (optional, keep files after installation is complete) -l <optional, limit number of results, default is 50> |
| Search modpack and install (by ID)                | -modpack2 -i -c <modpack ID> -k (optional, keep file after installation)                                                                   |
| Search modpacks and display information (by name) | -modpack2 -s <modpack name> -l <optional, limit number of results, default is 50>                                                          |
| Search modpacks and display information (by ID)   | -modpack2 -s -c <modpack ID>                                                                                                               |
| Install local modpack                             | -modpack -l <modpack path>                                                                                                                 |

</details>

<details>
   <summary><b><font size="4">Mod related (download source: Modrinth)</font></b></summary>

| Function                                      | Command                                                                   |
| --------------------------------------------- | ------------------------------------------------------------------------- |
| Search for mods and install (by name)         | -mod2 -i <mod name> -l <optional, limit number of results, default is 50> |
| Search for mods and install (by ID)           | -mod2 -i -c <mod ID>                                                      |
| Search mods and display information (by name) | -mod2 -s <mod name> -l <optional, limit number of results, default is 50> |
| Search mods and display information (by ID)   | -mod2 -s -c <mod ID>                                                      |

</details>

## About the author

MrShiehX<br/>

- Occupation:<br/>
  student<br/>
- Bilibili:<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>
  -Youtube:<br/>
  [@MrShiehX](https://www.youtube.com/channel/UC03_vrWM8TfaU1k9VYVzW0A) <br/>

## If you find any bugs in this program, or have new ideas, please leave a message or raise an Issue on Bilibili

## Copyright

MrShiehX owns the copyright to this program. <br/>
Anyone can make comments and suggestions about this program.

## Latest version: 1.8 (released on November 11, 2022)

## Program screenshot

![Program Screenshot](screenshot.gif "Program Screenshot")<br/>

## Video Tutorials

### Chinese

[\[CMCL\] Pure command line Minecraft launcher | Farewell to tedious, new experience](https://www.bilibili.com/video/BV1ua41187od) <br/>
[\[CMCL\]Today, it came out again](https://www.bilibili.com/video/BV1AY411A7XU) <br/>

### English

[\[CMCL\] A Launcher for Minecraft Running On The Console](https://www.youtube.com/watch?v=SczdBQT9vOY)

## Main Developer

### [@MrShieh-X](https://github.com/MrShieh-X)

### [@Graetpro-X](https://github.com/Graetpro)

## License

The software is distributed under [GPL v3](https://www.gnu.org/licenses/gpl-3.0.html) with additional terms.

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

### Additional terms under GPLv3 Section 7

You must <b>not</b> remove the copyright declaration displayed in the software. \[[under GPLv3, 7(b).](https://github.com/MrShieh-X/console-minecraft-launcher/blob/f266ff87c0af3487ce66b47afbdb5d6dbc90f240/LICENSE#L368-L370)\]
