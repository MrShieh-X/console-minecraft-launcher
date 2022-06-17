# Console Minecraft Launcher
A Launcher for Minecraft Java Edition Running On The Console

### [中文README](README.md) <br/>
### [See Update Logs](update_logs-en.md) <br/>

## Copyright
MrShiehX owns the copyright of this program.<br/>
Anyone can take advices of this program to us.

## Version
The latest version: <br/>
<b>1.4 (Jun. 17, 2022)</b><br/>
Historical version: <br/>
<b>1.4 (Jun. 17, 2022)</b><br/>
<b>1.3 (Jun. 12, 2022)</b><br/>
<b>1.2 (Apr. 30, 2022)</b><br/>
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
![Program Screenshot](screenshot.gif "Program Screenshot")<br/>

## Bugs found
- (Fixed in v1.4)~~Unable to launch game versions lower than 1.13 (without 1.13) where OptiFine and Forge coexist.~~

## Configurations
The configurations are storing in a JSON file named cmcl.json, you can edit them by a file editor (need to know JSON tutorial) or the program arguments `-config <Configuration Name> <Configuration Value>` (see [Usage Manual](#usage-manual) Configuration Related).<br/>
If you edit configurations by a file editor, only strings need double quotation mark, otherwise, other types do not need it.<br/>
If you edit by the program arguments, note that if there are spaces in the string, double quotation marks must be added, otherwise it can be added or not.<br/>
Note: If the type is an integer and the value is negative (or the parameter value starts with "-"), a backslash "\" must be added before the item, otherwise it will be mistaken for a parameter, such as: `\-10`.

| Configuration Name|Type|Meaning|
| -----|:----:|:----:|
| accounts|JSON Array|Accounts|
| downloadSource|Integer|Download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS|
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
| jvmArgs|JSON Array|Customize JVM virtual machine parameters (append or override),<br/> example: "jvmArgs":<br/>\["-Dfile.encoding=UTF-8", "-Djava.library.path=natives", "-XX:+PrintGC"\]<br/>(Set the parameters of "-Dfile.encoding=UTF-8", <br/>"-Djava.library.path=natives"<br/> and "-XX:+PrintGC")|
| gameArgs|JSON Object|Customize game parameters (append or override), example: "gameArgs":<br/>{"fullscreen": "",<br/>  "arg1": "value1",<br/>  "arg2": "value2",<br/>  "arg3": "space value2"}<br/>(will add "--fullscreen --arg1 value1 --arg2 value2 --arg3 "space value2" " after the game parameters)|
| proxyHost|String|Proxy Host Address|
| proxyPort|Integer|Proxy Port|
| proxyUsername|String|Proxy authentication username(optional for proxy)|
| proxyPassword|String|Proxy authentication password(optional for proxy)|

## Usage Manual
### To get all the manuals, add -u directly, such as `java -jar CMCL.jar -u`
### To get a single function manual, add -u after the function parameter, such as `java -jar CMCL.jar -account -u`
&emsp;&emsp;Print usage manual:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-u` or `-usage`<br/>
&emsp;&emsp;Start the selected version:&emsp;&emsp;&emsp;&emsp;&emsp;Direct start game without parameters or `-b` or `-start`<br/>
&emsp;&emsp;Start a specific version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Unique parameter: `<Version Name>` or `-b <Version Name>`<br/>
&emsp;&emsp;List all versions:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-l` or `-list` or `-ls`<br/>
&emsp;&emsp;List all versions in another game dir: `-l <Target Game Directory>`<br/>
&emsp;&emsp;Print the launch command:&emsp;&emsp;&emsp;&emsp; `-p <Version Name>` or `-print <Version Name>`<br/>
&emsp;&emsp;Select version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; `-s <Version Name>` or `-select <Version Name>`<br/>
&emsp;&emsp;Get about description:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-a` or `-about`<br/>
&emsp;&emsp;To enter immersive mode:&emsp;&emsp;&emsp;&emsp;&emsp;`-i` or `-immersive`<br/>
<details>
  <summary><b><font size="4">Configuration Related</font></b></summary>

&emsp;&emsp;Print a configuration:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -p <Configuration Name>`<br/>
&emsp;&emsp;Print all configurations:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -a`<br/>
&emsp;&emsp;Print the original content of the configuration: `-config -o <The number of spaces to indent, can be empty, defaults to 2>`<br/>
&emsp;&emsp;Clear all configurations:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-config -c`<br/>
&emsp;&emsp;Remove a configuration:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-config -r <Configuration Name>`<br/>
&emsp;&emsp;Set a configuration(regardless of type):&emsp;&emsp;&emsp;`-config <Configuration Name> <Configuration Value>`<br/>
&emsp;&emsp;Set a configuration: `-config -s -t <Configuration type, such as`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`i Integer,`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`b Boolean,`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`s String and`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`f Fraction> -n <Configuration Name> -v <Configuration Value>`<br/>
</details>
<details>
  <summary><b><font size="4">Account Related</font></b></summary>

&emsp;&emsp;Select a account:&emsp;&emsp;&emsp;&ensp;`-account <Order Number>`<br/>
&emsp;&emsp;List all accounts:&emsp;&emsp;&emsp;&emsp;`-account -p`<br/>
&emsp;&emsp;Delete a account:&emsp;&emsp;&emsp;&ensp;`-account -t <Order Number>`<br/>
&emsp;&emsp;Offline Login:&emsp;&emsp;&emsp;&emsp;&emsp; `-account -l -o <Offline Playername> -s(Optional, select this account after successful login)`<br/>
&emsp;&emsp;Microsoft Account Login:  `-account -l -m -s(Optional, select this account after successful login)`<br/>
&emsp;&emsp;authlib-injector Login:&emsp;&emsp;`-account -l -a <Server Address> -s(Optional, select this account after successful login)`<br/>
&emsp;&emsp;Refresh account:&emsp;&emsp;&emsp;&emsp;`-account -r`<br/>
&emsp;&emsp;Download skin:&emsp;&emsp;&emsp;&emsp;&emsp;`-account -s -d <Skin File Storage Path>`<br/>
&emsp;&emsp;Set skin (Microsoft account not available):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-account -s -u <Skin file path (if it is an offline account, if you do not enter it, you will cancel the skin setting)>`<br/>
&emsp;&emsp;Set the skin to Steve (Microsoft account not available): `-account -s -e`<br/>
&emsp;&emsp;Set the skin to Alex (Microsoft account not available):&emsp;`-account -s -x`<br/>
&emsp;&emsp;Set a cape (only for offline account):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;`-account -c <Cape file path, if not entered it will unset the cape>`<br/>
</details>
<details>
  <summary><b><font size="4">Version Related</font></b></summary>

&emsp;&emsp;View Version Information: `-version -i <Version Name>`<br/>
&emsp;&emsp;Delete a version:&emsp;&emsp;&emsp;&emsp;`-version -d <Version Name>`<br/>
&emsp;&emsp;Rename a version:&emsp;&emsp;&emsp; `-version -r <Version Name> -t <New Version Name>`<br/>
&emsp;&emsp;Re-download the native dependency library files:&emsp;&emsp;`-version -n <Version Name>`<br/>
&emsp;&emsp;Find missing dependency library files and download: `-version -l <Version Name>`<br/>
&emsp;&emsp;Complete version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -b <Version Name>`<br/>
&emsp;&emsp;Install Fabric to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -f <Version Name>`<br/>
&emsp;&emsp;Install Forge to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-version -o <Version Name>`<br/>
&emsp;&emsp;Install LiteLoader to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-version -e <Version Name>`<br/>
&emsp;&emsp;Install OptiFine to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-version -p <Version Name>`<br/>
&emsp;&emsp;Install Quilt to local version:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-version -q <Version Name>`<br/>
&emsp;**Note: You can specify the version by adding "-v <Version>" after the commands to install Fabric, Forge, LiteLoader, OptiFine and Quilt, so as to avoid entering the command and then selecting the version.**<br/>
</details>
<details>
  <summary><b><font size="4">Custom JVM Virtual Machine Parameters Related</font></b></summary>

&emsp;&emsp;Print all parameters: `-jvmArgs -p <The number of spaces to indent, can be empty, defaults to 2>`<br/>
&emsp;&emsp;Add a parameter:&emsp; `-jvmArgs -a <Parameter Content>`<br/>
&emsp;&emsp;Delete a parameter:   `-jvmArgs -d <Order number, starting from 0>`<br/>
</details>
<details>
  <summary><b><font size="4">Custom Game Parameters Related</font></b></summary>

&emsp;&emsp;Print all parameters: `-gameArgs -p <The number of spaces to indent, can be empty, defaults to 2>`<br/>
&emsp;&emsp;Add a parameter:&emsp; `-gameArgs -a -n <Parameter Name> -v <Parameter Value(optional, do not enter -v if this is empty)>`<br/>
&emsp;&emsp;Delete a parameter:   `-gameArgs -d <Parameter Name>`<br/>
</details>
<details>
  <summary><b><font size="4">Installation Version Related</font></b></summary>

&emsp;&emsp;Direct install version: -install `<Version Name (if there are spaces, add double quotes)> -n <Local Version Name (optional)>`<br/>
&emsp;&emsp;Optional parameters:<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-f` Install Fabric<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-o` Install Forge<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-e` Install LiteLoader<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-p` Install OptiFine<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-q` Install Quilt<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-t <Thread Count> Set the number of threads for downloading asset files (default 64)`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-na` Do not download asset files<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-nl` Do not download dependency library files<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-nn` Do not download native dependency library files<br/>
&emsp;&emsp;&emsp;&emsp;&ensp;&emsp;**Note: Fabric and Forge, Fabric and LiteLoader, Fabric and OptiFine cannot be installed at the same time or coexist (Quilt is the same as Fabric, but they also cannot coexist)**<br/>
&emsp;&emsp;&emsp;&emsp;&ensp;&emsp;&emsp;**You can specify the version after the parameters -f, -o, -e, -p, -q to avoid asking for the version during installation. For example: "-f 0.14.8" means to install Fabric with version 0.14.8.**<br/>
&emsp;&emsp;Show installable versions (if no range is set, all versions of this type are showed by default): -`install -s <Versions types: a All; r Releases; s Snapshots; oa Ancient Alpha; ob Ancient Beta>`<br/>
&emsp;&emsp;  Set time range (optional): `-i <from year>-<from month>-<from day>/<to year>-<to month>-<to day>`<br/>
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Example: `-i 2020-05-09/2021-10-23`<br/>
</details>
<details>
  <summary><b><font size="4">Mod Related (Download Source: CurseForge)</font></b></summary>

&emsp;&emsp;Search for mods and install (by name):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-mod -i <Mod Name>`<br/>
&emsp;&emsp;Search for mods and install (by ID):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; &emsp; `-mod -i -c <Mod ID>`<br/>
&emsp;&emsp;Search for mods and display information (by name): `-mod -s <Mod Name>`<br/>
&emsp;&emsp;Search for mods and display information (by ID):&emsp;&emsp;`-mod -s -c <Mod ID>`
</details>
<details>
  <summary><b><font size="4">Modpack Related (Download Source: CurseForge)</font></b></summary>

&emsp;Optional parameters for installing the modpack:<br/>
&emsp;&emsp;&emsp;`-t <Thread Count>` Set the number of threads for downloading asset files (default 64)<br/>
&emsp;&emsp;&emsp;`-na` Do not download asset files<br/>
&emsp;&emsp;&emsp;`-nl` Do not download dependency library files<br/>
&emsp;&emsp;&emsp;`-nn` Do not download native dependency library files<br/>
&emsp;&emsp;Search for modpacks and install (by name):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-modpack -i <Modpack Name> -k(Optional, keep the file after installation)`<br/>
&emsp;&emsp;Search for modpacks and install (by ID):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-modpack -i -c <Modpack ID> -k(Optional, keep the file after installation)`<br/>
&emsp;&emsp;Search for modpacks and display information (by name): `-modpack -s <Modpack Name>`<br/>
&emsp;&emsp;Search for modpacks and display information (by ID):&emsp;&emsp;`-modpack -s -c <Modpack ID>`<br/>
&emsp;&emsp;Install local modpack:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-modpack -l <Modpack Path>`
</details>
<details>
  <summary><b><font size="4">Mod Related (Download Source: Modrinth)</font></b></summary>

&emsp;&emsp;Search for mods and install (by name):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-mod2 -i <Mod Name> -l <Optional, limit the number of results, default is 50>`<br/>
&emsp;&emsp;Search for mods and install (by ID):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; &emsp; `-mod2 -i -c <Mod ID>`<br/>
&emsp;&emsp;Search for mods and display information (by name): `-mod2 -s <Mod Name> -l <Optional, limit the number of results, default is 50>`<br/>
&emsp;&emsp;Search for mods and display information (by ID):&emsp;&emsp;`-mod2 -s -c <Mod ID>`
</details>
<details>
  <summary><b><font size="4">Modpack Related (Download Source: Modrinth)</font></b></summary>

&emsp;Optional parameters for installing the modpack:<br/>
&emsp;&emsp;&emsp;`-t <Thread Count>` Set the number of threads for downloading asset files (default 64)<br/>
&emsp;&emsp;&emsp;`-na` Do not download asset files<br/>
&emsp;&emsp;&emsp;`-nl` Do not download dependency library files<br/>
&emsp;&emsp;&emsp;`-nn` Do not download native dependency library files<br/>
&emsp;&emsp;Search for modpacks and install (by name):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`-modpack2 -i <Modpack Name> -k(Optional, keep the file after installation) -l <Optional, limit the number of results, default is 50>`<br/>
&emsp;&emsp;Search for modpacks and install (by ID):&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; `-modpack2 -i -c <Modpack ID> -k(Optional, keep the file after installation)`<br/>
&emsp;&emsp;Search for modpacks and display information (by name): `-modpack2 -s <Modpack Name> -l <Optional, limit the number of results, default is 50>`<br/>
&emsp;&emsp;Search for modpacks and display information (by ID):&emsp;&emsp;`-modpack2 -s -c <Modpack ID>`
</details>

## About Author
MrShiehX<br/>
- Occupation: <br/>
  Student<br/>
- Bilibili:<br/>
  [@MrShiehX](https://space.bilibili.com/323674091) <br/>

## If you find any bugs in this program, or have new ideas, please leave a message on Bilibili or raise an issue
