# Update Logs
Currently, the latest version is 1.1, which was updated on March 27, 2022.

## 1.1 (Mar. 27, 2022)
- Support for installing Fabric:</br>
  1. Install Fabric when installing the version: add "-f" after the installation command, such as: `-install 1.18.2 -f`</br>
  2. Install Fabric after installing the version: `-version -f <Target Version Name>`
- Support for installing mods (CurseForge):</br>
  1. Search for mods and install: `-mod -i <Mod Name></br>`
  2. Search for mods and display information: `-mod -s <Mod Name>`
- Support authlib-injector account login, use parameters: `-account -l -a -d <Server Address> -s(Optional, select this account after successful login)`
- Support multi-account login, please refer to the user manual for details. If you are already logged in, be sure to log in again.
- Supports custom skins and capes for offline accounts:</br>
  Set skin (Microsoft account not available): `-account -s -u <Skin file path (if it is an offline account, if you do not enter it, you will cancel the skin setting)>`</br>
  Set the skin to Steve (Microsoft account not available): `-account -s -e`</br>
  Set the skin to Alex (Microsoft account not available): `-account -s -x`</br>
  Set a cape (only for offline account): `-account -c <Cape file path, if not entered it will unset the cape>`
- Support setting proxy, proxy information is stored in configuration as storage. Proxy configuration:

| Configuration Name |  Type   |                 Meaning                 |
|--------------------|:-------:|:---------------------------------------:|
| proxyHost          | String  |     Host Address(no proxy if empty)     |
| proxyPort          | Integer |                  Port                   |
| proxyUsername      | String  | Proxy authentication username(optional) |
| proxyPassword      | String  | Proxy authentication password(optional) |

- Support viewing installed version information: `-version -i <Version Name>`
- Support printing the original content of the configuration file, parameter: `-config -o <The number of spaces to indent, can be empty, defaults to 2>`
- Support custom JVM virtual machine and game parameters, please refer to [configurations](README.md#configurations) for details.
- Added more download sources, modify the download source: `-config downloadSource <target download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS>`. Note, if the file cannot be downloaded, please try to change the download source.
- Added a new way to change the configuration (regardless of type, stored as a string, boolean or integer can be read normally): `-config <Configuration Name> <Configuration Value>`
- Changed the display of printing all configurations.
- Changed the default resource download thread count to 64.
- When installing a version, the stored version name can be empty, and the default is the version name to be installed. For example: `-install 1.18.2`, the default storage is "1.18.2"
- If max memory is empty, memory will be allocated automatically.
- Fixed an issue where dependency library files would not be downloaded without downloading resource files.

## 1.0 (Mar. 12, 2022)
- First version.