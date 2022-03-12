/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2022  MrShiehX <3553413882@qq.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mrshiehx.cmcl.constants;

import java.util.HashMap;
import java.util.Map;

public class Languages {
    public static final Map<String, String> zhCN = new HashMap<>();
    public static final Map<String, String> enUS = new HashMap<>();
    public static final Map<String, String> zhCNUsage = new HashMap<>();
    public static final Map<String, String> enUSUsage = new HashMap<>();

    static {
        /*zhCN*/
        {
            zhCN.put("APPLICATION_NAME", "Console Minecraft Launcher");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_1", "Console Minecraft Launcher %1$s");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_2", "一个 Minecraft Java 版的启动器");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_3", "作者：MrShiehX      bilibili：@MrShiehX");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_4", "源代码仓库：");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_5", "https://www.github.com/MrShieh-X/console-minecraft-launcher");
            zhCN.put("DIALOG_ABOUT_DESCRIPTION_6", "依赖库：");
            zhCN.put("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE", "登录正版账号失败");
            zhCN.put("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT", "错误：%1$s\n错误信息：%2$s");
            zhCN.put("DIALOG_OFFICIAL_LOGINED_TITLE", "登录正版账号成功");
            zhCN.put("DIALOG_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT", "未设置皮肤");
            zhCN.put("DIALOG_OFFICIAL_FAILED_REFRESH_TITLE", "刷新正版账号失败");
            zhCN.put("DIALOG_UNABLE_TO_LOGIN_MICROSOFT", "无法登录，请稍后再试。");
            zhCN.put("DIALOG_OFFICIAL_LOGIN_FAILED_MESSAGE", "登录账号失败，可能是因为您的账号尚未拥有 Minecraft。");
            zhCN.put("MESSAGE_NOT_FOUND_GAME_DIR", "找不到游戏目录");
            zhCN.put("MESSAGE_STARTING_GAME", "启动游戏中...");
            zhCN.put("MESSAGE_FINISHED_GAME", "游戏结束");
            zhCN.put("MESSAGE_FAILED_TO_CONNECT_TO_LAUNCHERMETA", "连接到 https://launchermeta.mojang.com 失败，请检查您的网络连接。");
            zhCN.put("MESSAGE_VERSIONS_LIST_IS_EMPTY", "版本列表为空。");
            zhCN.put("MESSAGE_INSTALL_INPUT_NAME", "请输入新版本的名称：");
            zhCN.put("MESSAGE_INSTALL_INPUT_NAME_EXISTS", "“%s”该名称已存在，请更换一个名称。");
            zhCN.put("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", "下载或解析目标版本的 JSON 文件失败：%s");
            zhCN.put("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO", "在目标版本的JSON文件中找不到客户端文件的下载信息。");
            zhCN.put("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY", "客户端文件的下载地址为空。");
            zhCN.put("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", "安装新版本失败：%s");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE", "正在下载客户端文件...");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE", "下载客户端文件完成");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADING_ASSETS", "正在下载资源文件中...");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADED_ASSETS", "下载资源文件完成");
            zhCN.put("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX", "下载资源文件失败，找不到资源文件索引。");
            zhCN.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", "下载资源文件失败：%s");
            zhCN.put("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL", "找不到下载地址");
            zhCN.put("MESSAGE_FAILED_DOWNLOAD_FILE", "下载文件“%s”失败");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES", "正在下载依赖库文件...");
            zhCN.put("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES", "下载依赖库文件完成");
            zhCN.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", "下载依赖库文件失败：%s");
            zhCN.put("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY", "依赖库列表为空");
            zhCN.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", "下载依赖库文件“%1$s”失败：%2$s");
            zhCN.put("MESSAGE_FAILED_TO_DECOMPRESS_FILE", "解压缩文件“%1$s”失败：%2$s");
            zhCN.put("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES", "正在解压原生依赖库文件...");
            zhCN.put("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES", "解压原生依赖库文件完成");
            zhCN.put("MESSAGE_FAILED_TO_COPY_FILE", "复制文件“%1$s”到“%2$s”失败：%3$s");
            zhCN.put("MESSAGE_INSTALLED_NEW_VERSION", "安装新版本完成");
            zhCN.put("MESSAGE_UUID_ACCESSTOKEN_EMPTY", "UUID 或 AccessToken 为空");
            zhCN.put("MESSAGE_DOWNLOADING_FILE", "正在下载 %s");
            zhCN.put("MESSAGE_COPYING_FILE", "正在复制 %s 到 %s");
            zhCN.put("MESSAGE_UNZIPPING_FILE", "正在解压 %s");
            zhCN.put("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES", "复制原生依赖库文件完成");
            zhCN.put("MESSAGE_GAME_CRASH_CAUSE_TIPS", "游戏崩溃可能的错误：%s");
            zhCN.put("MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER", "旧版本 Minecraft 出现此错误可能是因为 Java 版本过高，可使用 Java 8 及更低版本以修复此问题。");
            zhCN.put("MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD", "出现此错误可能是因为原生依赖库缺失或损坏，请通过“-version -n <版本名称>”重新下载依赖库文件以修复此问题。");
            zhCN.put("MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL", "内存太小，可尝试前往设置把内存调整为一个更大的数。");
            zhCN.put("MESSAGE_REDOWNLOADED_NATIVES", "下载原生依赖库文件完成");
            zhCN.put("EXCEPTION_VERSION_JSON_NOT_FOUND", "目标启动版本的JSON文件或JAR文件不存在");
            zhCN.put("EXCEPTION_VERSION_NOT_FOUND", "目标版本不存在");
            zhCN.put("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND", "找不到原生依赖库（natives）目录或为空，您需要使用“-version -n <版本名称>”下载原生依赖库文件以启动游戏。");
            zhCN.put("EXCEPTION_MAX_MEMORY_TOO_BIG", "最大内存大于物理内存总大小");
            zhCN.put("EXCEPTION_MAX_MEMORY_IS_ZERO", "最大内存为0");
            zhCN.put("EXCEPTION_JAVA_VERSION_TOO_LOW", "此 Minecraft 版本的 Java 版本最低要求为 %d，您选择的 Java 版本为 %d，请选择一个达到要求的 Java 后重试。");
            zhCN.put("LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "请在浏览器内登录您的微软账号，\n如果登录成功，请返回到启动器，等待完成登录。\n登录需要一定的时间，请耐心等待。\n按以下的按钮，将会取消登录。");
            zhCN.put("ON_AUTHENTICATED_PAGE_TEXT", "已完成微软帐户授权，请关闭此页面并返回到启动器完成登录。");
            zhCN.put("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE", "登录微软账号 - Console Minecraft Launcher");
            zhCN.put("CONSOLE_GET_USAGE", "使用选项 -usage 或 -help 以获得使用手册。");
            zhCN.put("CONSOLE_UNKNOWN_OPTION", "未知选项：%s\n使用选项 -usage 或 -help 以获得使用手册。");
            zhCN.put("CONSOLE_INCORRECT_USAGE", "不正确的用法。\n使用选项 -usage 或 -help 以获得使用手册。");
            zhCN.put("CONSOLE_UNSUPPORTED_VALUE", "不支持的值：%s");
            zhCN.put("CONSOLE_REPLACE_ACCOUNT", "是否覆盖原来的账号？");
            zhCN.put("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "请在浏览器内登录您的微软账号，\n如果登录成功，请返回到此处，等待完成登录。\n登录需要一定的时间，请耐心等待。");
            zhCN.put("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN", "必须是正版账号登录且有 AccessToken 才能进行此操作。");
            zhCN.put("CONSOLE_ACCOUNT_UN_OPERABLE_UUID", "必须是正版账号登录且有 UUID 才能进行此操作。");
            zhCN.put("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE", "服务器无响应");
            zhCN.put("CONSOLE_FAILED_OPERATE", "操作失败：");
            zhCN.put("CONSOLE_FILE_EXISTS", "文件“%s”已存在");
            zhCN.put("CONSOLE_INCORRECT_JAVA", "请通过 “-config -s -t s -n javaPath -v <Java路径>” 修改一个正确的 Java 路径");
            zhCN.put("CONSOLE_FAILED_START", "启动游戏失败");
            zhCN.put("CONSOLE_START_COMMAND", "启动命令：");
            zhCN.put("CONSOLE_NO_SELECTED_VERSION", "请使用“-s <版本名称>”以选择一个版本");
            zhCN.put("CONSOLE_EMPTY_LIST", "列表为空");
            zhCN.put("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD", "您缺少了以上启动游戏的必要依赖库。是否下载它们？");
            zhCN.put("CONSOLE_FAILED_LIST_VERSIONS", "获得版本列表失败：%s");
            zhCN.put("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", "不正确的时间格式或第一个时间大于第二个时间。");
        }
        /*enUS*/
        {
            enUS.put("APPLICATION_NAME", "Console Minecraft Launcher");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_1", "Console Minecraft Launcher %1$s");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_2", "A Launcher of Minecraft Java Edition");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_3", "Author: MrShiehX      bilibili: @MrShiehX");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_4", "Source code repository:");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_5", "https://www.github.com/MrShieh-X/console-minecraft-launcher");
            enUS.put("DIALOG_ABOUT_DESCRIPTION_6", "Dependency libraries:");
            enUS.put("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE", "Failed to login the official account");
            enUS.put("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT", "Error: %1$s\nError Message: %2$s");
            enUS.put("DIALOG_OFFICIAL_LOGINED_TITLE", "Login into the official account successfully");
            enUS.put("DIALOG_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT", "You did not set a skin");
            enUS.put("DIALOG_UNABLE_TO_LOGIN_MICROSOFT", "Unable to login, please try again later.");
            enUS.put("DIALOG_OFFICIAL_LOGIN_FAILED_MESSAGE", "Login to your account failed, probably because your account does not have Minecraft.");
            enUS.put("DIALOG_OFFICIAL_FAILED_REFRESH_TITLE", "Failed to refresh official account");
            enUS.put("MESSAGE_NOT_FOUND_GAME_DIR", "Game directory not found");
            enUS.put("MESSAGE_STARTING_GAME", "Starting game...");
            enUS.put("MESSAGE_FINISHED_GAME", "Game finished");
            enUS.put("MESSAGE_FAILED_TO_CONNECT_TO_LAUNCHERMETA", "Failed to connect to https://launchermeta.mojang.com, please check your network connection.");
            enUS.put("MESSAGE_VERSIONS_LIST_IS_EMPTY", "The versions list is empty");
            enUS.put("MESSAGE_INSTALL_INPUT_NAME", "Please enter a name of the new version:");
            enUS.put("MESSAGE_INSTALL_INPUT_NAME_EXISTS", "The name \"%s\" already exists, please change a name:");
            enUS.put("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", "Failed to download or parse the target version of the JSON file: %s");
            enUS.put("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO", "The download information of the client file in the JSON file of the target version not found.");
            enUS.put("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY", "The download url of the client file is empty.");
            enUS.put("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", "Failed to install the new version: %s");
            enUS.put("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE", "Downloading the client file...");
            enUS.put("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE", "Download the client file complete");
            enUS.put("MESSAGE_INSTALL_DOWNLOADING_ASSETS", "Downloading the asset files...");
            enUS.put("MESSAGE_INSTALL_DOWNLOADED_ASSETS", "Download the asset files complete");
            enUS.put("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX", "Failed to download the asset files, the asset files index not found.");
            enUS.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", "Failed to download the asset files: %s");
            enUS.put("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL", "Cannot find download url");
            enUS.put("MESSAGE_FAILED_DOWNLOAD_FILE", "Failed to download the file \"%s\"");
            enUS.put("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES", "Downloading the library files...");
            enUS.put("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES", "Download the library files complete");
            enUS.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", "Failed to download the library files: %s");
            enUS.put("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY", "The libraries list is empty");
            enUS.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", "Failed to download the library file \"%1$s\": %2$s");
            enUS.put("MESSAGE_FAILED_TO_DECOMPRESS_FILE", "Failed to decompress file \"%1$s\": %2$s");
            enUS.put("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES", "Decompressing the native library files...");
            enUS.put("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES", "Decompress the native library files complete");
            enUS.put("MESSAGE_FAILED_TO_COPY_FILE", "Failed to copy file \"%1$s\" to \"%2$s\": %3$s");
            enUS.put("MESSAGE_INSTALLED_NEW_VERSION", "Install the new version complete");
            enUS.put("MESSAGE_UUID_ACCESSTOKEN_EMPTY", "UUID or accessToken is empty");
            enUS.put("MESSAGE_DOWNLOADING_FILE", "Downloading %s");
            enUS.put("MESSAGE_COPYING_FILE", "Copying %s to %s");
            enUS.put("MESSAGE_UNZIPPING_FILE", "Decompressing %s");
            enUS.put("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES", "Copy the native dependency libraries complete");
            enUS.put("MESSAGE_GAME_CRASH_CAUSE_TIPS", "Game crash possible error: %s");
            enUS.put("MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER", "Older versions of Minecraft may have this error because the Java version is too high, Java 8 and below can be used to fix this.");
            enUS.put("MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD", "This error may occur because some of the native dependency library are missing or damaged, please re-download native dependency libraries via \"-version -n <version name>\" to fix this problem.");
            enUS.put("MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL", "The memory is too small, you can try to go to the settings to adjust the memory to a larger number.");
            enUS.put("MESSAGE_REDOWNLOADED_NATIVES", "Download native dependency libraries complete");
            enUS.put("EXCEPTION_VERSION_JSON_NOT_FOUND", "The JSON file of the target startup version does not exist");
            enUS.put("EXCEPTION_VERSION_NOT_FOUND", "The target startup version does not exist");
            enUS.put("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND", "Cannot find the native libraries directory or it is empty, you can re-download the native library files via \"-version -n <version name>\" to start game.");
            enUS.put("EXCEPTION_MAX_MEMORY_TOO_BIG", "The maximum memory is larger than the total physical memory size");
            enUS.put("EXCEPTION_MAX_MEMORY_IS_ZERO", "The maximum memory is 0");
            enUS.put("EXCEPTION_JAVA_VERSION_TOO_LOW", "The minimum Java version required for this version of Minecraft is %d, the Java version you have selected is %d, please go to settings to select a Java that meets the requirements and try again.");
            enUS.put("LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "Please login your Microsoft account in the browser,\nIf the login is successful, back to launcher and wait for the login to complete.\nIt will take some time to login, please be patient.\nPress the button below to cancel login.");
            enUS.put("ON_AUTHENTICATED_PAGE_TEXT", "Microsoft account authorization has been completed. Please close this page and back to the launcher to complete login.");
            enUS.put("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE", "Login Microsoft Account - Console Minecraft Launcher");
            enUS.put("CONSOLE_GET_USAGE", "Use the option -usage or -help to get the usage manual.");
            enUS.put("CONSOLE_UNKNOWN_OPTION", "Unknown option: %s\nUse the option -usage or -help to get the usage manual.");
            enUS.put("CONSOLE_INCORRECT_USAGE", "Incorrect usage.\nUse the option -usage or -help to get the usage manual.");
            enUS.put("CONSOLE_UNSUPPORTED_VALUE", "Unsupported value: %s");
            enUS.put("CONSOLE_REPLACE_ACCOUNT", "Overwrite the original account?");
            enUS.put("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "Please login your Microsoft account in the browser,\nIf the login is successful, back to launcher and wait for the login to complete.\nIt will take some time to login, please be patient.");
            enUS.put("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN", "You must be logged in with a official account and have accessToken to perform this operation.");
            enUS.put("CONSOLE_ACCOUNT_UN_OPERABLE_UUID", "You must be logged in with a official account and have UUID to perform this operation.");
            enUS.put("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE", "Server not responding");
            enUS.put("CONSOLE_FAILED_OPERATE", "Failed to operate: ");
            enUS.put("CONSOLE_FILE_EXISTS", "The file \"%s\" already exists");
            enUS.put("CONSOLE_INCORRECT_JAVA", "Please modify a correct Java path by \"-config -s -t s -n javaPath -v <Java Path>\"");
            enUS.put("CONSOLE_FAILED_START", "Unable to start game");
            enUS.put("CONSOLE_START_COMMAND", "Launch Command: ");
            enUS.put("CONSOLE_NO_SELECTED_VERSION", "Please select a version by \"-s <Version Name>\"");
            enUS.put("CONSOLE_EMPTY_LIST", "The list is empty");
            enUS.put("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD", "You are missing the above necessary dependent libraries to start the game. Do you want to download them?");
            enUS.put("CONSOLE_FAILED_LIST_VERSIONS", "Failed to get the versions list: %s");
            enUS.put("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", "Incorrect time format or the first time is bigger than the second time.");
        }
        /*enUSUsage*/
        {
            enUSUsage.put("TITLE",
                    "Usage Manual:\n" +
                            "    Print usage manual:                    -u\n" +
                            "    Start the selected version:             Direct start game without parameters or -b\n" +
                            "    Start a specific version:               Unique parameter: <Version Name> or -b <Version Name>\n" +
                            "    List all versions:                     -l\n" +
                            "    List all versions in another game dir: -l <Target Game Directory>\n" +
                            "    Print the launch command:              -p <Version Name>\n" +
                            "    Select version:                        -s <Version Name>\n" +
                            "    Get about description:                 -a");
            enUSUsage.put("ACCOUNT",
                    "Account Related:\n" +
                            "    Logout:                   -account -t\n" +
                            "    Offline login:            -account -l -o <Offline Account Name>\n" +
                            "    Login Microsoft account:  -account -l -m\n" +
                            "    Refresh official account: -account -r\n" +
                            "    Download skin:            -account -s -d <Skin File Storage Path>");
            enUSUsage.put("CONFIG",
                    "Configuration Related:\n" +
                            "    Print a configuration:    -config -p <Configuration Name>\n" +
                            "    Print all configurations: -config -a\n" +
                            "    Clear all configurations: -config -c\n" +
                            "    Remove a configuration:   -config -r <Configuration Name>\n" +
                            "    Set a configuration:      -config -s -t <Configuration type, such as\n" +
                            "                                            i Integer,\n" +
                            "                                            b Boolean,\n" +
                            "                                            s String and\n" +
                            "                                            f Fraction> -n <Configuration Name> -v <Configuration Value>");
            enUSUsage.put("VERSION",
                    "Version Related:\n" +
                            "    Delete a version:         -version -d <Version Name>\n" +
                            "    Rename a version:         -version -r <Version Name> -t <New Version Name>\n" +
                            "    Re-download the native dependency library files:    -version -n <Version Name>\n" +
                            "    Find missing dependency library files and download: -version -l <Version Name>");
            enUSUsage.put("INSTALL",
                    "Installation Version Related:\n" +
                            "    Direct install version: -install <Version Name (if there are spaces, add double quotes)> -n <Local Version Name>\n" +
                            "    Optional parameters:\n" +
                            "                -t <Thread Count> Set the number of threads for downloading asset files (default 10)\n" +
                            "                -na Do not download asset files\n" +
                            "                -nl Do not download dependency library files\n" +
                            "                -nn Do not download native dependency library files\n" +
                            "\n" +
                            "    Show installable versions (if no range is set, all versions of this type are showed by default)：-install -s <Versions types: a All; r Releases; s Snapshots; oa Ancient Alpha; ob Ancient Beta>\n" +
                            "      Set time range (optional): -i <from year>-<from month>-<from day>/<to year>-<to month>-<to day>\n" +
                            "                        Example: -i 2020-05-09/2021-10-23");
        }
        /*zhCNUsage*/
        {
            zhCNUsage.put("TITLE",
                    "使用手册：\n" +
                            "    获得使用手册：            -u\n" +
                            "    启动选择的版本：          无参数直接运行 或 -b\n" +
                            "    启动特定的版本：          唯一参数：<版本名称> 或 -b <版本名称>\n" +
                            "    列出所有版本：           -l\n" +
                            "    列出某个游戏目录的所有版本：-l <目标游戏目录>\n" +
                            "    打印启动命令：            -p <版本名称>\n" +
                            "    选择版本：               -s <版本名称>\n" +
                            "    获得关于信息：            -a");
            zhCNUsage.put("ACCOUNT",
                    "账号相关：\n" +
                            "    退出登录：       -account -t\n" +
                            "    离线登录：       -account -l -o <离线用户名>\n" +
                            "    微软账号登录：    -account -l -m\n" +
                            "    刷新正版账号：    -account -r\n" +
                            "    下载皮肤：       -account -s -d <皮肤文件存储路径>");
            zhCNUsage.put("CONFIG",
                    "配置相关：\n" +
                            "    打印某项配置：-config -p <配置名>\n" +
                            "    打印全部配置：-config -a\n" +
                            "    清空配置：   -config -c\n" +
                            "    删除某项配置：-config -r <配置名称>\n" +
                            "    设置某项配置：-config -s -t <配置类型，如\n" +
                            "                                       i 整数、\n" +
                            "                                       b 布尔值、\n" +
                            "                                       s 字符串以及\n" +
                            "                                       f 小数> -n <配置名称> -v <配置值>");
            zhCNUsage.put("VERSION",
                    "版本相关：\n" +
                            "    删除版本：               -version -d <版本名称>\n" +
                            "    重命名版本：             -version -r <版本名称> -t <新版本名称>\n" +
                            "    重新下载原生依赖库文件：   -version -n <版本名称>\n" +
                            "    查找缺少的依赖库文件并下载：-version -l <版本名称>");
            zhCNUsage.put("INSTALL",
                    "安装版本相关：\n" +
                            "    直接安装版本：-install <版本名称（如果有空格要加双引号）> -n <存储的版本名称>\n" +
                            "      可选的参数：-t <线程数> 设置下载资源文件的线程数（默认为10）\n" +
                            "                -na 不下载资源文件\n" +
                            "                -nl 不下载依赖库文件\n" +
                            "                -nn 不下载原生依赖库文件\n" +
                            "\n" +
                            "    显示可安装的版本（若没有设置范围，默认显示该类型的全部版本）：-install -s <版本类型：a 全部；r 正式版；s 快照版；oa 远古alpha版；ob 远古beta版>\n" +
                            "      设置时间范围（可选）：-i <从年>-<从月>-<从日>/<到年>-<到月>-<到日>\n" +
                            "                     例：-i 2020-05-09/2021-10-23");
        }
    }
}
