# Console Minecraft Launcher
A Launcher for Minecraft Java Edition Running On The Console

### [中文README](README-zh.md) <br/>
### [See Update Logs](update_logs.md) <br/>

## Copyright
MrShiehX owns the copyright of this program.<br/>
Anyone can take advices of this program to us.

## Version
The latest version: <br/>
<b>1.1 (Mar. 27, 2022)</b><br/>
Historical version: <br/>
<b>1.1 (Mar. 27, 2022)</b><br/>
<b>1.0 (Mar. 12, 2022) (First version)</b><br/>

## Main Developers
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

## Software Configuration Required for This Program
* Java 8 or higher

## Supported Languages
- English
- Simplified Chinese

## Program Screenshot
![Program Screenshot](screenshot.png "Program Screenshot")</br>

## Configurations
The configurations are storing in a JSON file named cmcl.json, you can edit them by a file editor (need to know JSON tutorial) or the program arguments `-config <Configuration Name> <Configuration Value>` (see [Usage Manual Configuration Related](#configuration-related)).</br>
If you edit configurations by a file editor, only strings need double quotation mark, otherwise, other types do not need it.</br>
If you edit by the program arguments, note that if there are spaces in the string, double quotation marks must be added, otherwise it can be added or not.</br>
Note: If the type is an integer and the value is negative (or the parameter value starts with "-"), a backslash "\" must be added before the item, otherwise it will be mistaken for a parameter, such as: \-10.

| Configuration Name|Type|Meaning|
| -----|:----:|:----:|
| accounts|JSON Array|Accounts|
| downloadSource|Integer|Download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS|
| playerName|String|Player Name|
| loginMethod|Integer|Account type, 0 is offline, 2 is Microsoft account|
| accessToken|String|Official account related|
| uuid|String|Official account related|
| tokenType|String|Official account related, the type of token|
| language|String|Language, zh is Simplified Chinese and en is English|
| maxMemory|Integer|Maximum (Unit: MB)|
| gameDir|String|Custom the path of the game directory, default is .minecraft|
| assetsDir|String|Custom assets resource directory path, if empty, it is the assets directory in the game directory|
| resourcesDir|String|Custom resource pack directory path, if empty, it is the resourcepacks directory in the game directory|
| javaPath|String|Java Path (It will get automatically if it is empty)|
| selectedVersion|String|Selected start version|
| windowSizeWidth|Integer|The width of the game window|
| windowSizeHeight|Integer|The height of the game window|
| isFullscreen|Boolean|Whether it is full screen, true if yes, false otherwise|
| exitWithMinecraft|Boolean|When running the game, if you need to exit the program and exit the game by the way, it is true, otherwise it is false|
| jvmArgs|JSON Array|Customize JVM virtual machine parameters (append or override),</br> example: "jvmArgs":</br>\["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"\]</br>(Set the parameters of "-Dfile.encoding=UTF-8", </br>"-Djava.library.path=natives"</br> and "-XX:+PrintGC")|
| gameArgs|JSON Object|Customize game parameters (append or override), example: "gameArgs":</br>{"fullscreen": "",</br>  "arg1": "value1",</br>  "arg2": "value2",</br>  "arg3": "space value2"}</br>(will add "--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value2" " after the game parameters)|
| proxyHost|String|Proxy Host Address|
| proxyPort|Integer|Proxy Port|
| proxyUsername|String|Proxy authentication username(optional for proxy)|
| proxyPassword|String|Proxy authentication password(optional for proxy)|

## Usage Manual
### To get all the manuals, add -u directly, such as `java -jar CMCL.jar -u`
### To get a single function manual, add -u after the function parameter, such as `java -jar CMCL.jar -account -u`
### Usage Manual
&emsp;&emsp;Print usage manual:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-u`</br>
&emsp;&emsp;Start the selected version:&emsp;&emsp;&emsp;&emsp;&emsp;Direct start game without parameters or `-b`</br>
&emsp;&emsp;Start a specific version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Unique parameter: <`Version Name>` or `-b <Version Name>`</br>
&emsp;&emsp;List all versions:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-l`</br>
&emsp;&emsp;List all versions in another game dir: `-l <Target Game Directory>`</br>
&emsp;&emsp;Print the launch command:&emsp;&emsp;&emsp;&emsp; `-p <Version Name>`</br>
&emsp;&emsp;Select version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; `-s <Version Name>`</br>
&emsp;&emsp;Get about description:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-a`</br>
### Configuration Related
&emsp;&emsp;Print a configuration:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -p <Configuration Name>`</br>
&emsp;&emsp;Print all configurations:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -a`</br>
&emsp;&emsp;Print the original content of the configuration: `-config -o <The number of spaces to indent, can be empty, defaults to 2>`</br>
&emsp;&emsp;Clear all configurations:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -c`</br>
&emsp;&emsp;Remove a configuration:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-config -r <Configuration Name>`</br>
&emsp;&emsp;Set a configuration(regardless of type):&emsp;&emsp;&emsp;`-config <Configuration Name> <Configuration Value>`</br>
&emsp;&emsp;Set a configuration: `-config -s -t <Configuration type, such as`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`i Integer,`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`b Boolean,`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`s String and`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`f Fraction> -n <Configuration Name> -v <Configuration Value>`</br>
### Account Related
&emsp;&emsp;Select a account:&emsp;&emsp;&emsp;&ensp;`-account <Order Number>`</br>
&emsp;&emsp;List all accounts:&emsp;&emsp;&emsp;&emsp;`-account -p`</br>
&emsp;&emsp;Delete a account:&emsp;&emsp;&emsp;&ensp;`-account -t <Order Number>`</br>
&emsp;&emsp;Offline Login:&emsp;&emsp;&emsp;&emsp;&emsp; `-account -l -o <Offline Playername> -s(Optional, select this account after successful login)`</br>
&emsp;&emsp;Microsoft Account Login:  `-account -l -m -s(Optional, select this account after successful login)`</br>
&emsp;&emsp;authlib-injector Login:&emsp;&emsp;`-account -l -a -d <Server Address> -s(Optional, select this account after successful login)`</br>
&emsp;&emsp;Refresh account:&emsp;&emsp;&emsp;&emsp;`-account -r`</br>
&emsp;&emsp;Download skin:&emsp;&emsp;&emsp;&emsp;&emsp;`-account -s -d <Skin File Storage Path>`</br>
&emsp;&emsp;Set skin (Microsoft account not available):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-account -s -u <Skin file path (if it is an offline account, if you do not enter it, you will cancel the skin setting)>`</br>
&emsp;&emsp;Set the skin to Steve (Microsoft account not available): `-account -s -e`</br>
&emsp;&emsp;Set the skin to Alex (Microsoft account not available):&emsp;`-account -s -x`</br>
&emsp;&emsp;Set a cape (only for offline account):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-account -c <Cape file path, if not entered it will unset the cape>`</br>
### Version Related
&emsp;&emsp;View Version Information: `-version -i <Version Name>`</br>
&emsp;&emsp;Delete a version:&emsp;&emsp;&emsp;&emsp;`-version -d <Version Name>`</br>
&emsp;&emsp;Rename a version:&emsp;&emsp;&emsp; `-version -r <Version Name> -t <New Version Name>`</br>
&emsp;&emsp;Re-download the native dependency library files:&emsp;&emsp;`-version -n <Version Name>`</br>
&emsp;&emsp;Find missing dependency library files and download: `-version -l <Version Name>`</br>
&emsp;&emsp;Install Fabric to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -f <Version Name>`</br>
### Custom JVM Virtual Machine Parameters Related
&emsp;&emsp;Print all parameters: `-jvmArgs -p <The number of spaces to indent, can be empty, defaults to 2>`</br>
&emsp;&emsp;Add a parameter:&emsp; `-jvmArgs -a <Parameter Content>`</br>
&emsp;&emsp;Delete a parameter:   `-jvmArgs -d <Order number, starting from 0>`</br>
### Custom Game Parameters Related
&emsp;&emsp;Print all parameters: `-gameArgs -p <The number of spaces to indent, can be empty, defaults to 2>`</br>
&emsp;&emsp;Add a parameter:&emsp; `-gameArgs -a -n <Parameter Name> -v <Parameter Value(optional, do not enter -v if this is empty)>`</br>
&emsp;&emsp;Delete a parameter:   `-gameArgs -d <Parameter Name>`</br>
### Installation Version Related
&emsp;&emsp;Direct install version: -install `<Version Name (if there are spaces, add double quotes)> -n <Local Version Name (optional)> -f (optional, install Fabric)`</br>
&emsp;&emsp;Optional parameters:</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-t <Thread Count> Set the number of threads for downloading asset files (default 64)`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-na` Do not download asset files</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-nl` Do not download dependency library files</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-nn` Do not download native dependency library files</br>
&emsp;&emsp;Show installable versions (if no range is set, all versions of this type are showed by default)：-`install -s <Versions types: a All; r Releases; s Snapshots; oa Ancient Alpha; ob Ancient Beta>`</br>
&emsp;&emsp;  Set time range (optional): `-i <from year>-<from month>-<from day>/<to year>-<to month>-<to day>`</br>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Example: `-i 2020-05-09/2021-10-23`</br>
### Mod Related (CurseForge)
&emsp;&emsp;Search for mods and install:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-mod -i <Mod Name>`</br>
&emsp;&emsp;Search for mods and display information: `-mod -s <Mod Name>`</br>
	

## About Author
MrShiehX<br/>
- Occupation: <br/>
  Student<br/>
- Email address: <br/>
  3553413882@qq.com<br/>
- QQ:<br/>
  3553413882 (Remember to tell me why you add me)<br/>
- Bilibili:<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>

## If you find any bugs in this program or have new ideas, please send an email or add my QQ (remember to tell me why you add me).
