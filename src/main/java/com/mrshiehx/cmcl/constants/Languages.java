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
    public static final Map<String, String> zh = new HashMap<>();
    public static final Map<String, String> en = new HashMap<>();
    public static final Map<String, String> zhUsage = new HashMap<>();
    public static final Map<String, String> enUsage = new HashMap<>();

    /*zhCN*/
    public static Map<String, String> getZh() {
        if (zh.size() == 0) {
            zh.put("APPLICATION_NAME", "Console Minecraft Launcher");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_1", "Console Minecraft Launcher %1$s");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_2", "一个 Minecraft Java 版的启动器");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_4", "源代码仓库：");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_6", "依赖库：");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_MAIN_DEVELOPERS", "主要开发者：");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS", "特别鸣谢：");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_AUTHLIB_INJECTOR", "authlib-injector 相关支持");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_BMCLAPI", "提供BMCLAPI下载源");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS_NAME", "MCBBS 我的世界中文论坛....");
            zh.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS", "提供MCBBS下载源");
            zh.put("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE", "登录正版账号失败");
            zh.put("MESSAGE_LOGINED_TITLE", "登录账号成功");
            zh.put("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT", "未设置皮肤");
            zh.put("MESSAGE_FAILED_REFRESH_TITLE", "刷新账号失败");
            zh.put("MESSAGE_UNABLE_TO_LOGIN_MICROSOFT", "无法登录，请稍后再试。");
            zh.put("MESSAGE_OFFICIAL_LOGIN_FAILED_MESSAGE", "登录账号失败，可能是因为您的账号尚未拥有 Minecraft。");
            zh.put("MESSAGE_NOT_FOUND_GAME_DIR", "找不到游戏目录");
            zh.put("MESSAGE_STARTING_GAME", "启动游戏中...");
            zh.put("MESSAGE_FINISHED_GAME", "游戏结束");
            zh.put("MESSAGE_FAILED_TO_CONNECT_TO_URL", "连接到 %s 失败，请检查您的网络连接。");
            zh.put("MESSAGE_VERSIONS_LIST_IS_EMPTY", "游戏版本列表为空。");
            zh.put("MESSAGE_INSTALL_INPUT_NAME", "请输入新版本的名称：");
            zh.put("MESSAGE_INSTALL_INPUT_NAME_EXISTS", "“%s”该名称已存在，请更换一个名称。");
            zh.put("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", "下载或解析目标版本的 JSON 文件失败：%s");
            zh.put("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO", "在目标版本的JSON文件中找不到客户端文件的下载信息。");
            zh.put("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY", "客户端文件的下载链接为空。");
            zh.put("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", "安装新版本失败：%s");
            zh.put("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE", "正在下载客户端文件...");
            zh.put("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE", "下载客户端文件完成");
            zh.put("MESSAGE_INSTALL_DOWNLOADING_ASSETS", "正在下载资源文件中...");
            zh.put("MESSAGE_INSTALL_DOWNLOADED_ASSETS", "下载资源文件完成");
            zh.put("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX", "下载资源文件失败，找不到资源文件索引。");
            zh.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", "下载资源文件失败：%s");
            zh.put("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL", "找不到下载链接");
            zh.put("MESSAGE_FAILED_DOWNLOAD_FILE", "下载文件“%s”失败");
            zh.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", "下载文件“%s”失败：%s");
            zh.put("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES", "正在下载依赖库文件...");
            zh.put("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES", "下载依赖库文件完成");
            zh.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", "下载依赖库文件失败：%s");
            zh.put("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY", "依赖库列表为空");
            zh.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", "下载依赖库文件“%1$s”失败：%2$s");
            zh.put("MESSAGE_FAILED_TO_DECOMPRESS_FILE", "解压缩文件“%1$s”失败：%2$s");
            zh.put("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES", "正在解压原生依赖库文件...");
            zh.put("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES", "解压原生依赖库文件完成");
            zh.put("MESSAGE_FAILED_TO_COPY_FILE", "复制文件“%1$s”到“%2$s”失败：%3$s");
            zh.put("MESSAGE_INSTALLED_NEW_VERSION", "安装新版本完成");
            zh.put("MESSAGE_UUID_ACCESSTOKEN_EMPTY", "UUID 或 AccessToken 为空");
            zh.put("MESSAGE_DOWNLOADING_FILE", "正在下载 %s");
            zh.put("MESSAGE_DOWNLOADING_FILE_TO", "正在下载 %s 到 %s");
            zh.put("MESSAGE_COPYING_FILE", "正在复制 %s 到 %s");
            zh.put("MESSAGE_UNZIPPING_FILE", "正在解压 %s");
            zh.put("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES", "复制原生依赖库文件完成");
            zh.put("MESSAGE_GAME_CRASH_CAUSE_TIPS", "游戏崩溃可能的错误：%s");
            zh.put("MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER", "旧版本 Minecraft 出现此错误可能是因为 Java 版本过高，可使用 Java 8 及更低版本以修复此问题。");
            zh.put("MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD", "出现此错误可能是因为原生依赖库缺失或损坏，请通过“-version -n <版本名称>”重新下载依赖库文件以修复此问题。");
            zh.put("MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL", "内存不足，可尝试把内存调整为一个更大的数。");
            zh.put("MESSAGE_REDOWNLOADED_NATIVES", "下载原生依赖库文件完成");
            zh.put("MESSAGE_FAILED_SEARCH", "搜索失败：%s");
            zh.put("MESSAGE_FAILED_RENAME_VERSION", "重命名版本失败：%s");
            zh.put("MESSAGE_START_INSTALLING_FORGE", "开始安装 Forge");
            zh.put("MESSAGE_INSTALLED_FORGE", "安装 Forge 成功");
            zh.put("MESSAGE_NOT_FOUND_LIBRARY_DOWNLOAD_URL", "找不到依赖库 %s 的下载链接");
            zh.put("MESSAGE_INSTALL_NATIVES_EMPTY_JAR", "无需要解压的原生依赖库文件");
            zh.put("MESSAGE_INSTALL_NATIVES_EMPTY_NATIVE_FILE", "无需要复制的原生依赖库文件");
            zh.put("MESSAGE_INSTALL_FORGE_FAILED_EXECUTE_PROCESSOR", "执行任务失败：%s");
            zh.put("MESSAGE_AUTHLIB_INJECTOR_LOGIN_SELECT_PROFILE", "请选择一个角色(%d-%d)：");
            zh.put("MESSAGE_INPUT_VERSION_NAME", "请输入要存储为的版本名称：");
            zh.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL", "下载文件失败：%s，文件链接为：%s，可自行下载并存储到 %s 中");
            zh.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL_WITH_NAME", "下载文件失败：%s，文件链接为：%s，可自行下载并存储到 %s 中，并把名称修改为“%s”");
            zh.put("MESSAGE_START_INSTALLING_LITELOADER", "开始安装 LiteLoader");
            zh.put("MESSAGE_INSTALLED_LITELOADER", "安装 LiteLoader 完成");
            zh.put("MESSAGE_START_INSTALLING_OPTIFINE", "开始安装 OptiFine");
            zh.put("MESSAGE_INSTALLED_OPTIFINE", "安装 OptiFine 完成");
            zh.put("MESSAGE_INSTALL_MODPACK_UNKNOWN_TYPE", "安装整合包失败：未知整合包类型。");
            zh.put("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION", "安装整合包失败：找不到要安装的游戏版本。");
            zh.put("MESSAGE_INSTALL_MODPACK_COEXIST", "安装整合包失败：%1$s 和 %2$s 不能同时安装。");
            zh.put("MESSAGE_COMPLETE_VERSION_IS_COMPLETE", "该版本为完整的版本，无需补充。");
            zh.put("MESSAGE_COMPLETED_VERSION", "补充版本成功");
            zh.put("MESSAGE_SELECT_DOWNLOAD_SOURCE", "首次下载请选择下载源(默认为%d)：");
            zh.put("MESSAGE_SELECT_ACCOUNT", "请输入要选择账号的序号(%d-%d)：");
            zh.put("MESSAGE_SELECT_ACCOUNT_TYPE", "暂无可用的账号，请选择新账号的账号类型(%d-%d)：");
            zh.put("MESSAGE_FAILED_TO_CHECK_FOR_UPDATES", "检查更新失败");
            zh.put("MESSAGE_NEW_VERSION", "新版本：%s\n更新日期：%s\n下载地址：\n%s更新内容：\n%s");
            zh.put("MESSAGE_CURRENT_IS_LATEST_VERSION", "当前已是最新版本");
            zh.put("MESSAGE_FIRST_USE", "########################################################\n" +
                    "   欢迎使用 Console Minecraft Launcher %s\n" +
                    "\n" +
                    "   如果您的运行方式是双击运行，且没有安装或选择版本，\n" +
                    "   恳请您通过以下方式启动该程序：\n" +
                    "   使用终端或命令提示符打开当前目录（即CMCL所在目录），\n" +
                    "   运行命令“java -jar CMCL.jar”或“CMCL.exe”，\n" +
                    "   后面可接上运行参数。\n" +
                    "   如果您通过以上运行方式安装并选择了一个游戏版本，\n" +
                    "   即可双击直接运行。\n" +
                    "   \n" +
                    "   首次使用，\n" +
                    "   可通过参数“-u”或“-usage”获得使用手册，\n" +
                    "   即命令“java -jar CMCL.jar -u”或“CMCL.exe -u”，\n" +
                    "   也可以观看以下的视频教程：\n" +
                    "     https://www.bilibili.com/video/BV1ua41187od\n" +
                    "     https://www.bilibili.com/video/BV1AY411A7XU\n" +
                    "     https://www.youtube.com/watch?v=SczdBQT9vOY（英语）\n" +
                    "########################################################");
            zh.put("ERROR_WITH_MESSAGE", "错误：%1$s\n错误信息：%2$s");
            zh.put("EXCEPTION_VERSION_JSON_NOT_FOUND", "目标启动版本的JSON文件或JAR文件不存在，请使用“-s <版本名称>”选择一个可启动的版本或使用“-install <版本名称>”安装一个新的版本并选择。");
            zh.put("EXCEPTION_VERSION_NOT_FOUND", "目标游戏版本不存在");
            zh.put("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND", "找不到原生依赖库（natives）目录或为空，您需要使用“-version -n <版本名称>”下载原生依赖库文件以启动游戏。");
            zh.put("EXCEPTION_MAX_MEMORY_TOO_BIG", "最大内存大于物理内存总大小");
            zh.put("EXCEPTION_MAX_MEMORY_IS_ZERO", "最大内存为零");
            zh.put("EXCEPTION_JAVA_VERSION_TOO_LOW", "此 Minecraft 版本的 Java 版本最低要求为 %d，您选择的 Java 版本为 %d，请选择一个达到要求的 Java 后重试。");
            zh.put("LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "请在浏览器内登录您的微软账户，\n如果登录成功，请返回到启动器，等待完成登录。\n登录需要一定的时间，请耐心等待。\n按以下的按钮，将会取消登录。");
            zh.put("ON_AUTHENTICATED_PAGE_TEXT", "已完成微软账户授权，请关闭此页面并返回到启动器完成登录。");
            zh.put("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE", "登录微软账户 - Console Minecraft Launcher");
            zh.put("CONSOLE_GET_USAGE", "使用选项 -usage 或 -help 以获得使用手册。");
            zh.put("CONSOLE_UNKNOWN_OPTION", "未知选项：%s\n使用选项 -usage 或 -help 以获得使用手册。");
            zh.put("CONSOLE_INCORRECT_USAGE", "不正确的用法。\n使用选项 -usage 或 -help 以获得使用手册。");
            zh.put("CONSOLE_UNSUPPORTED_VALUE", "不支持的值：%s");
            zh.put("CONSOLE_REPLACE_ACCOUNT", "是否覆盖原来的账号？");
            zh.put("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "请在浏览器内登录您的微软账户，\n如果登录成功，请返回到此处，等待完成登录。\n登录需要一定的时间，请耐心等待。");
            zh.put("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN", "必须是正版账号登录且有 accessToken 才能进行此操作。");
            zh.put("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE", "服务器无响应");
            zh.put("CONSOLE_FAILED_OPERATE", "操作失败：");
            zh.put("CONSOLE_FILE_EXISTS", "文件“%s”已存在");
            zh.put("CONSOLE_INCORRECT_JAVA", "请通过 “-config javaPath <Java路径>” 修改一个正确的 Java 路径");
            zh.put("CONSOLE_FAILED_START", "启动游戏失败");
            zh.put("CONSOLE_START_COMMAND", "启动命令：");
            zh.put("CONSOLE_NO_SELECTED_VERSION", "请使用“-s <版本名称>”以选择一个版本以启动，或通过“-b <版本名称>”或“-start <版本名称>”带上版本名称以启动。");
            zh.put("CONSOLE_EMPTY_LIST", "列表为空");
            zh.put("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD", "您缺少了以上启动游戏的必要依赖库。是否下载它们？");
            zh.put("CONSOLE_FAILED_LIST_VERSIONS", "获得版本列表失败：%s");
            zh.put("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", "不正确的时间格式或第一个时间大于第二个时间。");
            zh.put("CONSOLE_REPLACE_LOGGED_ACCOUNT", "您已经登录了此账号（序号为 %d），是否覆盖原来的账号？");
            zh.put("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_UUID_AND_URL_AND_TOKEN", "如果是外置账号，必须有UUID、accessToken以及外置登录服务器的地址才能进行此操作。");
            zh.put("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_ACCESS_TOKEN_AND_CLIENT_TOKEN_AND_URL", "如果是外置账号，必须有 accessToken、clientToken 以及外置登录服务器的地址才能进行此操作。");
            zh.put("CONSOLE_ACCOUNT_UN_OPERABLE_UUID", "必须是正版账号或外置账号登录且有 UUID 才能进行此操作。如果是外置账号还需要目标服务器地址。");
            zh.put("CONSOLE_INPUT_INT_WRONG", "请输入一个正确的、在范围内的数字。");
            zh.put("CONSOLE_INPUT_STRING_NOT_FOUND", "找不到“%s”。");
            zh.put("CONSOLE_IMMERSIVE_UNKNOWN", "找不到命令：%s。请使用 usage 或 help 获取使用手册。");
            zh.put("CONSOLE_ENTER_EXIT", "按回车键以退出...");
            zh.put("DATATYPE_STRING", "字符串");
            zh.put("DATATYPE_INTEGER", "整数");
            zh.put("DATATYPE_BOOLEAN", "布尔值");
            zh.put("DATATYPE_FRACTION", "小数");
            zh.put("TIME_FORMAT", "yyyy年MM月dd日 HH:mm:ss");
            zh.put("ACCOUNT_TYPE_MICROSOFT", "微软账户");
            zh.put("ACCOUNT_TYPE_OFFLINE", "离线账号");
            zh.put("ACCOUNT_TYPE_OAS", "外置账号");
            zh.put("ACCOUNT_SELECTED", "已选择");
            zh.put("ACCOUNT_NOT_EXISTS", "账号不存在：%d");
            zh.put("ACCOUNT_TYPE_OAS_WITH_DETAIL", "外置账号：%s %s");
            zh.put("ACCOUNT_INVALID", "账号无效：%d");
            zh.put("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME", "请输入离线登录玩家名：");
            zh.put("ACCOUNT_TIP_LOGIN_OAS_ADDRESS", "请输入外置登录服务器地址：");
            zh.put("NOT_SELECTED_AN_ACCOUNT", "未选择账号。请登录了您的账号之后，使用“-account -p”列出账号，记住您要选择的账号的序号，然后使用“-account <序号>”选择账号；或者在登录账号命令后面加上“-s”的参数。");
            zh.put("DATATYPE_JSON_ARRAY", "JSON数组");
            zh.put("DATATYPE_JSON_OBJECT", "JSON对象");
            zh.put("INPUT_ACCOUNT", "账号：");
            zh.put("INPUT_PASSWORD", "密码：");
            zh.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT", "登录外置账号失败：%s");
            zh.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_UNAVAILABLE_SERVER", "目标服务器访问失败");
            zh.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_FAILED_AUTHENTICATE", "认证失败");
            zh.put("FAILED_TO_LOGIN_OAA_NO_SELECTED_CHARACTER", "登录失败，请选择一个可用的角色后重试。");
            zh.put("WARNING_SHOWING_PASSWORD", "警告：在非控制台执行该操作，您的密码将不会被隐藏！");
            zh.put("WARNING_REFRESH_NOT_SELECTED", "警告：当前账号未选择角色，本地此账号已无法使用，请前往外置登录服务器选择一个可用的角色后刷新！");
            zh.put("FILE_NOT_FOUND_OR_IS_A_DIRECTORY", "找不到目标文件或目标文件是个目录");
            zh.put("SUCCESSFULLY_SET_SKIN", "设置皮肤成功");
            zh.put("UNAVAILABLE_AUTHLIB_ACCOUNT_REASON", "authlib-injector 配置错误：%s");
            zh.put("UNAVAILABLE_AUTHLIB_ACCOUNT", "外置账号不可用，游戏将使用离线账号。");
            zh.put("UNAVAILABLE_CUSTOM_SKIN", "自定义皮肤不可用");
            zh.put("PRINT_COMMAND_NOT_SUPPORT_OFFLINE_CUSTOM_SKIN", "注意：如果你是离线账号，使用命令启动游戏，自定义皮肤将不可用。");
            zh.put("EMPTY_UUID", "UUID为空");
            zh.put("EMPTY_PLAYERNAME", "玩家名称为空");
            zh.put("EMPTY_SKIN", "皮肤为空");
            zh.put("ONLY_OFFLINE", "该功能仅支持离线账号");
            zh.put("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE", "非外置账号或离线账号不支持设置皮肤。");
            zh.put("SKIN_TYPE_DEFAULT_OR_SLIM", "是否将皮肤模型设置为苗条（Alex）？");
            zh.put("SKIN_STEVE_UNABLE_READ", "设置失败，Steve 皮肤文件读取失败！");
            zh.put("SKIN_ALEX_UNABLE_READ", "设置失败，Alex 皮肤文件读取失败！");
            zh.put("SKIN_STEVE_NOT_FOUND", "设置失败，找不到 Steve 皮肤文件！");
            zh.put("SKIN_ALEX_NOT_FOUND", "设置失败，找不到 Alex 皮肤文件！");
            zh.put("CAPE_FILE_NOT_FOUND", "找不到披风文件“%s”");
            zh.put("CAPE_FILE_FAILED_LOAD", "披风文件“%s”加载失败");
            zh.put("UNABLE_TO_START_OFFLINE_SKIN_SERVER", "无法使用离线账号自定义皮肤");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_NOT_FOUND", "无法使用自定义皮肤：找不到 Steve 皮肤文件！");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_NOT_FOUND", "无法使用自定义皮肤：找不到 Alex 皮肤文件！");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_UNABLE_LOAD", "无法使用自定义皮肤：读取 Steve 皮肤文件失败！");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_UNABLE_LOAD", "无法使用自定义皮肤：读取 Alex 皮肤文件失败！");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_NOT_FOUND", "无法使用自定义皮肤：找不到皮肤文件“%s”");
            zh.put("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_FAILED_LOAD", "无法使用自定义皮肤：皮肤文件“%s”加载失败");
            zh.put("UNABLE_GET_VERSION_INFORMATION", "读取版本信息失败");
            zh.put("UNABLE_SET_SKIN", "设置皮肤失败");
            zh.put("INSTALL_MODLOADER_FAILED_TO_GET_INSTALLABLE_VERSION", "无法安装 %s：获得可安装的版本失败。");
            zh.put("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", "无法安装 %1$s：没有可安装的 %1$s 版本，可能是因为 %1$s 不支持此游戏版本。");
            zh.put("INSTALL_MODLOADER_FAILED_TO_GET_TARGET_JSON", "无法安装 %1$s：获得目标 %1$s JSON 失败。");
            zh.put("INSTALL_MODLOADER_SELECT", "请输入您要安装的 %s 版本：");
            zh.put("INSTALL_MODLOADER_SELECT_NOT_FOUND", "版本“%1$s”找不到，请输入您要安装的 %2$s 版本：");
            zh.put("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", "请问是否继续安装原版（无 %s）？");
            zh.put("INSTALL_MODLOADER_FAILED_TO_PARSE_TARGET_JSON", "无法安装 %1$s：解析目标 %1$s JSON 失败。");
            zh.put("INSTALL_MODLOADER_ALREADY_INSTALL", "无法安装 %1$s：目标版本已安装 %1$s。");
            zh.put("INSTALL_MODLOADER_EMPTY_MC_VERSION", "无法安装 %s：无法获得目标版本的游戏版本。");
            zh.put("INSTALL_MODLOADER_FAILED_WITH_REASON", "安装 %s 失败：%s");
            zh.put("INSTALLED_MODLOADER", "安装 %s 成功");
            zh.put("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", "无法安装 %1$s：目标版本已安装 %2$s，%2$s 和 %1$s 不能共存。");
            zh.put("INSTALL_MODLOADER_NO_FILE", "无法安装 %1$s：您输入的 %1$s 版本没有可下载的文件，请换一个 %1$s 版本再试。");
            zh.put("INSTALL_MODLOADER_FAILED_DOWNLOAD", "无法安装 %s：下载文件失败");
            zh.put("INSTALL_MODLOADER_DOWNLOADING_FILE", "下载文件中...");
            zh.put("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION_2", "无法安装 %1$s：没有可安装的 %1$s 版本。");
            zh.put("INSTALL_MODLOADER_FAILED_UNKNOWN_TYPE", "无法安装 %1$s：未知 %1$s 类型。");
            zh.put("INSTALL_MODLOADER_FAILED_MC_VERSION_MISMATCH", "无法安装 %1$s：目标 %1$s 的游戏版本与目标游戏版本不匹配。");
            zh.put("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", "${NAME} 版本“%s”找不到。");
            zh.put("INSTALL_MODLOADER_SELECT_NOT_FOUND_GAME_OR_TARGET_EXTRA", "找不到目标游戏版本或 ${NAME} 版本。");
            zh.put("VERSION_INFORMATION_GAME_VERSION", "   游戏版本：       ");
            zh.put("VERSION_INFORMATION_RELEASE_TIME", "   版本发布时间：   ");
            zh.put("VERSION_INFORMATION_FABRIC_VERSION", "   Fabric 版本：    ");
            zh.put("VERSION_INFORMATION_FORGE_VERSION", "   Forge 版本：     ");
            zh.put("VERSION_INFORMATION_JAVA_COMPONENT", "   Java 组件：      ");
            zh.put("VERSION_INFORMATION_JAVA_VERSION", "   Java 版本要求：  ");
            zh.put("VERSION_INFORMATION_ASSETS_VERSION", "   资源版本：       ");
            zh.put("VERSION_INFORMATION_LITELOADER_VERSION", "   LiteLoader 版本：");
            zh.put("VERSION_INFORMATION_OPTIFINE_VERSION", "   OptiFine 版本：  ");
            zh.put("VERSION_INFORMATION_QUILT_VERSION", "   Quilt 版本：     ");
            zh.put("VERSION_INFORMATION_VERSION_TYPE", "   版本类型：       ");
            zh.put("VERSION_INFORMATION_VERSION_TYPE_RELEASE", "正式版");
            zh.put("VERSION_INFORMATION_VERSION_TYPE_SNAPSHOT", "快照版");
            zh.put("VERSION_INFORMATION_VERSION_TYPE_OLD_BETA", "远古 Beta 版");
            zh.put("VERSION_INFORMATION_VERSION_TYPE_OLD_ALPHA", "远古 Alpha 版");
            zh.put("VERSION_INFORMATION_VERSION_PATH", "   版本位置：       ");
            zh.put("VERSION_INFORMATION_GAME_VERSION_FAILED_GET", "获取失败");
            zh.put("EXCEPTION_JAVA_NOT_FOUND", "无法启动游戏，找不到 Java 文件。");
            zh.put("EXCEPTION_READ_FILE", "读取文件失败");
            zh.put("EXCEPTION_READ_FILE_WITH_PATH", "读取文件“%s”失败");
            zh.put("EXCEPTION_PARSE_FILE", "解析文件失败");
            zh.put("EXCEPTION_PARSE_FILE_WITH_PATH", "解析文件“%s”失败");
            zh.put("EXCEPTION_WRITE_FILE", "写入文件失败");
            zh.put("EXCEPTION_WRITE_FILE_WITH_PATH", "写入内容到文件“%s”失败");
            zh.put("EXCEPTION_UNABLE_PARSE", "解析失败");
            zh.put("EXCEPTION_INSTALL_MODPACK", "安装整合包失败：%s");
            zh.put("EXCEPTION_EXECUTE_COMMAND", "执行命令失败");
            zh.put("EXCEPTION_INCOMPLETE_VERSION", "该版本不完整，请通过“-version -b <版本名称>”把该版本补充完整后再启动。");
            zh.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK", "找不到文件下载地址");
            zh.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK_WITH_FILENAME", "找不到文件“%s”的下载地址");
            zh.put("CF_FAILED_TO_SHOW_SOMEONE", "显示第%d个${NAME}失败：%s");
            zh.put("CF_AUTHOR_MORE", "等%d位作者");
            zh.put("CF_SELECT_TARGET", "请选择目标${NAME}(%d-%d)：");
            zh.put("CF_SUPPORTED_GAME_VERSION", "%s 支持的游戏版本：");
            zh.put("CF_INPUT_GAME_VERSION", "请输入您要下载的版本：");
            zh.put("CF_INPUT_VERSION", "请选择您要下载的${NAME}版本(%d-%d，-1为退出)：");
            zh.put("CF_STORAGE_FILE_EXISTS", "文件“%s”已存在，请输入一个存储该${NAME}文件的目录：");
            zh.put("CF_NO_VERSION_FOR_GAME_VERSION", "没有适用于此游戏版本的%s版本。");
            zh.put("CF_INFORMATION_NOTHING", "无任何关于此%s的信息");
            zh.put("CF_INFORMATION_MOD_NAME", "   模组名称：          ");
            zh.put("CF_INFORMATION_MOD_ID", "   模组ID：            ");
            zh.put("CF_INFORMATION_MODPACK_NAME", "   整合包名称：        ");
            zh.put("CF_INFORMATION_MODPACK_ID", "   整合包ID：          ");
            zh.put("CF_INFORMATION_AUTHORS", "   作者：              ");
            zh.put("CF_INFORMATION_AUTHOR", "   作者：              ");
            zh.put("CF_INFORMATION_SUMMARY", "   简介：              ");
            zh.put("CF_INFORMATION_LATEST_GAME_VERSION", "   最新支持的游戏版本：");
            zh.put("CF_INFORMATION_MOD_LOADERS", "   模组加载器：        ");
            zh.put("CF_INFORMATION_DATE_MODIFIED", "   修改日期：          ");
            zh.put("CF_INFORMATION_DATE_CREATED", "   创建日期：          ");
            zh.put("CF_INFORMATION_DATE_RELEASED", "   发布日期：          ");
            zh.put("CF_INFORMATION_ISSUE_TRACKER_URL", "   问题反馈：          ");
            zh.put("CF_INFORMATION_SOURCE_URL", "   源代码仓库：        ");
            zh.put("CF_INFORMATION_WEBSITE_URL", "   网页介绍：          ");
            zh.put("CF_INFORMATION_WIKI_URL", "   维基网站：          ");
            zh.put("CF_INFORMATION_DOWNLOADS", "   下载量：            ");
            zh.put("CF_INFORMATION_CATEGORIES", "   类别：              ");
            zh.put("CF_INFORMATION_DISCORD_URL", "   Discord 链接：      ");
            zh.put("CF_INFORMATION_DONATION", "   捐赠：              ");
            zh.put("CF_INFORMATION_DONATION_URL", "         地址：");
            zh.put("CF_INFORMATION_AUTHOR_URL", "         主页：");
            zh.put("CF_GET_BY_ID_FAILED", "无法获得目标${NAME}：%s\n出现该错误有可能的原因：\n1.目标${NAME}不存在\n2.网络异常\n3.服务器出现问题");
            zh.put("CF_GET_BY_ID_NOT_OF_MC", "目标游戏组件不是 Minecraft 的${NAME}，该组件的游戏ID为%d。");
            zh.put("CF_DEPENDENCIES_TIP", "此${NAME}需要以下前置${NAME}才能正常运行，请在安装完此${NAME}之后安装以下前置${NAME}。");
            zh.put("CF_DEPENDENCY_INFORMATION_ID", "   ID：  %d");
            zh.put("CF_DEPENDENCY_INFORMATION_ID_STRING", "   ID：  %s");
            zh.put("CF_DEPENDENCY_INFORMATION_NAME", "   名称：%s");
            zh.put("CF_BESEARCHED_MOD_ALC", "模组");
            zh.put("CF_BESEARCHED_MOD_FUC", "模组");
            zh.put("CF_BESEARCHED_MODPACK_ALC", "整合包");
            zh.put("CF_BESEARCHED_MODPACK_FUC", "整合包");
            zh.put("CF_GET_BY_ID_INCORRECT_CATEGORY", "目标游戏组件不是一个${NAME}，该组件的类别ID为%d。");
            zh.put("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL", "目标游戏组件不是一个${NAME}，该游戏组件是一个${TARGET}。");
            zh.put("MOD_FAILED_TO_GET_ALL_FILES", "获得${NAME}文件列表失败：%s");
            zh.put("NO_SEARCH_RESULTS", "无任何搜索结果。");
            zh.put("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", "下载 projectId 为 %d 的模组失败：%s");
            zh.put("INSTALL_MODPACK_EACH_MOD_GET_URL", "正在遍历获得各个模组的下载链接，请耐心等待");
            zh.put("INSTALL_MODPACK_COMPLETE", "安装整合包完成");
            zh.put("INSTALL_MODPACK_MODRINTH_UNKNOWN_MODLOADER", "未知模组加载器：%s");
            zh.put("INSTALL_OPTIFINE_INCOMPATIBLE_WITH_FORGE_17", "无法安装 OptiFine：当前游戏版本的 Forge 与低于 H1 Pre2 版本的 OptiFine 不兼容，请换一个版本更新的 OptiFine 后重试。");
            zh.put("DOWNLOAD_SOURCE_OFFICIAL", "官方");
            zh.put("DOWNLOAD_SOURCE_BMCLAPI", "BMCLAPI");
            zh.put("DOWNLOAD_SOURCE_MCBBS", "MCBBS 我的世界中文论坛（中国大陆用户推荐）");
        }
        return zh;
    }

    /*enUS*/
    public static Map<String, String> getEn() {
        if (en.size() == 0) {
            en.put("APPLICATION_NAME", "Console Minecraft Launcher");
            en.put("MESSAGE_ABOUT_DESCRIPTION_1", "Console Minecraft Launcher %1$s");
            en.put("MESSAGE_ABOUT_DESCRIPTION_2", "A Launcher for Minecraft Java Edition");
            en.put("MESSAGE_ABOUT_DESCRIPTION_4", "Source code repository: ");
            en.put("MESSAGE_ABOUT_DESCRIPTION_6", "Dependency Libraries: ");
            en.put("MESSAGE_ABOUT_DESCRIPTION_MAIN_DEVELOPERS", "Main Developers:");
            en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS", "Special Thanks：");
            en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_AUTHLIB_INJECTOR", "authlib-injector support");
            en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_BMCLAPI", "BMCLAPI download source provider");
            en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS_NAME", "MCBBS.....................");
            en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS", "MCBBS download source provider");
            en.put("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE", "Failed to login the official account");
            en.put("MESSAGE_LOGINED_TITLE", "Login into the account successfully");
            en.put("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT", "You did not set a skin");
            en.put("MESSAGE_UNABLE_TO_LOGIN_MICROSOFT", "Unable to login, please try again later.");
            en.put("MESSAGE_OFFICIAL_LOGIN_FAILED_MESSAGE", "Login to your account failed, probably because your account does not have Minecraft.");
            en.put("MESSAGE_FAILED_REFRESH_TITLE", "Failed to refresh account");
            en.put("MESSAGE_NOT_FOUND_GAME_DIR", "Game directory not found");
            en.put("MESSAGE_STARTING_GAME", "Starting game...");
            en.put("MESSAGE_FINISHED_GAME", "Game finished");
            en.put("MESSAGE_FAILED_TO_CONNECT_TO_URL", "Failed to connect to %s, please check your network connection.");
            en.put("MESSAGE_VERSIONS_LIST_IS_EMPTY", "The game versions list is empty");
            en.put("MESSAGE_INSTALL_INPUT_NAME", "Please enter a name of the new version: ");
            en.put("MESSAGE_INSTALL_INPUT_NAME_EXISTS", "The name \"%s\" already exists, please change a name.");
            en.put("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", "Failed to download or parse the target version of the JSON file: %s");
            en.put("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO", "The download information of the client file in the JSON file of the target version not found.");
            en.put("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY", "The download url of the client file is empty.");
            en.put("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", "Failed to install the new version: %s");
            en.put("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE", "Downloading the client file...");
            en.put("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE", "Download the client file complete");
            en.put("MESSAGE_INSTALL_DOWNLOADING_ASSETS", "Downloading the asset files...");
            en.put("MESSAGE_INSTALL_DOWNLOADED_ASSETS", "Download the asset files complete");
            en.put("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX", "Failed to download the asset files, the asset files index not found.");
            en.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", "Failed to download the asset files: %s");
            en.put("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL", "Cannot find download url");
            en.put("MESSAGE_FAILED_DOWNLOAD_FILE", "Failed to download the file \"%s\"");
            en.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", "Failed to download the file \"%s\": %s");
            en.put("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES", "Downloading the library files...");
            en.put("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES", "Download the library files complete");
            en.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", "Failed to download the library files: %s");
            en.put("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY", "The libraries list is empty");
            en.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", "Failed to download the library file \"%1$s\": %2$s");
            en.put("MESSAGE_FAILED_TO_DECOMPRESS_FILE", "Failed to decompress file \"%1$s\": %2$s");
            en.put("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES", "Decompressing the native library files...");
            en.put("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES", "Decompress the native library files complete");
            en.put("MESSAGE_FAILED_TO_COPY_FILE", "Failed to copy file \"%1$s\" to \"%2$s\": %3$s");
            en.put("MESSAGE_INSTALLED_NEW_VERSION", "Install the new version complete");
            en.put("MESSAGE_UUID_ACCESSTOKEN_EMPTY", "UUID or accessToken is empty");
            en.put("MESSAGE_DOWNLOADING_FILE", "Downloading %s");
            en.put("MESSAGE_DOWNLOADING_FILE_TO", "Downloading %s to %s");
            en.put("MESSAGE_COPYING_FILE", "Copying %s to %s");
            en.put("MESSAGE_UNZIPPING_FILE", "Decompressing %s");
            en.put("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES", "Copy the native dependency libraries complete");
            en.put("MESSAGE_GAME_CRASH_CAUSE_TIPS", "Game crash possible error: %s");
            en.put("MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER", "Older versions of Minecraft may have this error because the Java version is too high, Java 8 and below can be used to fix this.");
            en.put("MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD", "This error may occur because some of the native dependency library are missing or damaged, please re-download native dependency libraries via \"-version -n <version name>\" to fix this problem.");
            en.put("MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL", "The memory is not enough, you can try to adjust the memory to a larger number.");
            en.put("MESSAGE_REDOWNLOADED_NATIVES", "Download native dependency libraries complete");
            en.put("MESSAGE_FAILED_SEARCH", "Failed to search: %s");
            en.put("MESSAGE_FAILED_RENAME_VERSION", "Failed to rename the version: %s");
            en.put("MESSAGE_START_INSTALLING_FORGE", "Start installing Forge");
            en.put("MESSAGE_INSTALLED_FORGE", "Forge installed successfully");
            en.put("MESSAGE_NOT_FOUND_LIBRARY_DOWNLOAD_URL", "Could not find the download URL of the dependency library %s");
            en.put("MESSAGE_INSTALL_NATIVES_EMPTY_JAR", "No native dependency library files that need to be decompressed");
            en.put("MESSAGE_INSTALL_NATIVES_EMPTY_NATIVE_FILE", "No native dependency library files that need to be copied");
            en.put("MESSAGE_INSTALL_FORGE_FAILED_EXECUTE_PROCESSOR", "Failed to execute processor: %s");
            en.put("MESSAGE_AUTHLIB_INJECTOR_LOGIN_SELECT_PROFILE", "Please choose a character (%d-%d): ");
            en.put("MESSAGE_INPUT_VERSION_NAME", "Please enter the version name to store as: ");
            en.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL", "Failed to download the file: %s, the file link is: %s, you can download and store it in %s by yourself");
            en.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL_WITH_NAME", "Failed to download the file: %s, the file link is: %s, you can download and store it in %s by yourself, and change the name to \"%s\"");
            en.put("MESSAGE_START_INSTALLING_LITELOADER", "Start installing LiteLoader");
            en.put("MESSAGE_INSTALLED_LITELOADER", "LiteLoader installed successfully");
            en.put("MESSAGE_START_INSTALLING_OPTIFINE", "Start installing OptiFine");
            en.put("MESSAGE_INSTALLED_OPTIFINE", "OptiFine installed successfully");
            en.put("MESSAGE_INSTALL_MODPACK_UNKNOWN_TYPE", "Unable to install the modpack: Unknown modpack type.");
            en.put("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION", "Failed to install the modpack: Could not find the version of the game to install.");
            en.put("MESSAGE_INSTALL_MODPACK_COEXIST", "Failed to install the modpack: %1$s and %2$s cannot be installed at the same time.");
            en.put("MESSAGE_COMPLETE_VERSION_IS_COMPLETE", "This version is complete and does not need to be completed.");
            en.put("MESSAGE_COMPLETED_VERSION", "Version completed successfully");
            en.put("MESSAGE_SELECT_DOWNLOAD_SOURCE", "Please select the download source for the first download (default is %d): ");
            en.put("MESSAGE_SELECT_ACCOUNT", "Please enter the order number of the account you want to select (%d-%d): ");
            en.put("MESSAGE_SELECT_ACCOUNT_TYPE", "No account is currently available, please select the account type of the new account (%d-%d): ");
            en.put("MESSAGE_FAILED_TO_CHECK_FOR_UPDATES", "Failed to check for updates");
            en.put("MESSAGE_NEW_VERSION", "New Version: %s\nUpdated Date: %s\nDownload urls:\n%sUpdated Content:\n%s");
            en.put("MESSAGE_CURRENT_IS_LATEST_VERSION", "The current version is the latest version");
            en.put("MESSAGE_FIRST_USE", "#########################################################################\n" +
                    "   Welcome to Console Minecraft Launcher %s\n" +
                    "\n" +
                    "   If your running mode is to double-click to run,\n" +
                    "   and there is no version installed or selected,\n" +
                    "   please start the program in the following ways:\n" +
                    "   use a terminal or command prompt to open\n" +
                    "   the current directory (that is, the directory where CMCL is located),\n" +
                    "   run the command \"java -jar CMCL.jar\" or \"CMCL.exe\",\n" +
                    "   followed by running parameters.\n" +
                    "   If you installed and selected a game version\n" +
                    "   through the above operation method,\n" +
                    "   you can double-click to run it directly.\n" +
                    "   \n" +
                    "   For the first use,\n" +
                    "   the user manual can be obtained through the parameter \"-u\" or \"-usage\",\n" +
                    "   that is, the command \"java -jar CMCL.jar -u\" or \"CMCL.exe -u\",\n" +
                    "   or you can watch the following video tutorials:\n" +
                    "     https://www.youtube.com/watch?v=SczdBQT9vOY\n" +
                    "     https://www.bilibili.com/video/BV1ua41187od (Chinese)\n" +
                    "     https://www.bilibili.com/video/BV1AY411A7XU (Chinese)\n" +
                    "#########################################################################");
            en.put("ERROR_WITH_MESSAGE", "Error: %1$s\nError Message: %2$s");
            en.put("EXCEPTION_VERSION_JSON_NOT_FOUND", "The JSON file or JAR file of the target version does not exist, please use \"-s <Version Name>\" to select a launch-able version or \"-install <Version Name>\" to install a new version and select it.");
            en.put("EXCEPTION_VERSION_NOT_FOUND", "The target game version does not exist");
            en.put("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND", "Cannot find the native libraries directory or it is empty, you can re-download the native library files via \"-version -n <version name>\" to start game.");
            en.put("EXCEPTION_MAX_MEMORY_TOO_BIG", "The maximum memory is larger than the total physical memory size");
            en.put("EXCEPTION_MAX_MEMORY_IS_ZERO", "The maximum memory is 0");
            en.put("EXCEPTION_JAVA_VERSION_TOO_LOW", "The minimum Java version required for this version of Minecraft is %d, the Java version you have selected is %d, please select a Java that meets the requirements and try again.");
            en.put("LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "Please login your Microsoft account in the browser,\nIf the login is successful, back to launcher and wait for the login to complete.\nIt will take some time to login, please be patient.\nPress the button below to cancel login.");
            en.put("ON_AUTHENTICATED_PAGE_TEXT", "Microsoft account authorization has been completed. Please close this page and back to the launcher to complete login.");
            en.put("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE", "Login Microsoft Account - Console Minecraft Launcher");
            en.put("CONSOLE_GET_USAGE", "Use the option -usage or -help to get the usage manual.");
            en.put("CONSOLE_UNKNOWN_OPTION", "Unknown option: %s\nUse the option -usage or -help to get the usage manual.");
            en.put("CONSOLE_INCORRECT_USAGE", "Incorrect usage.\nUse the option -usage or -help to get the usage manual.");
            en.put("CONSOLE_UNSUPPORTED_VALUE", "Unsupported value: %s");
            en.put("CONSOLE_REPLACE_ACCOUNT", "Overwrite the original account?");
            en.put("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "Please login your Microsoft account in the browser,\nIf the login is successful, back to launcher and wait for the login to complete.\nIt will take some time to login, please be patient.");
            en.put("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN", "You must be logged in with a official account and have accessToken to perform this operation.");
            en.put("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE", "Server not responding");
            en.put("CONSOLE_FAILED_OPERATE", "Failed to operate: ");
            en.put("CONSOLE_FILE_EXISTS", "The file \"%s\" already exists");
            en.put("CONSOLE_INCORRECT_JAVA", "Please modify a correct Java path by \"-config javaPath <Java Path>\"");
            en.put("CONSOLE_FAILED_START", "Unable to start game");
            en.put("CONSOLE_START_COMMAND", "Launch Command: ");
            en.put("CONSOLE_NO_SELECTED_VERSION", "Please use \"-s <Version Name>\" to select a version to start, or start via \"-b <Version Name>\" or \"-start <Version Name>\" with the version name.");
            en.put("CONSOLE_EMPTY_LIST", "The list is empty");
            en.put("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD", "You are missing the above necessary dependent libraries to start the game. Do you want to download them?");
            en.put("CONSOLE_FAILED_LIST_VERSIONS", "Failed to get the versions list: %s");
            en.put("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", "Incorrect time format or the first time is bigger than the second time.");
            en.put("CONSOLE_REPLACE_LOGGED_ACCOUNT", "You have already logged in to this account (order number is %d). Do you want to overwrite the original account?");
            en.put("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_UUID_AND_URL_AND_TOKEN", "If it is an authlib-injector account, you must have the UUID, accessToken and the address of the authlib-injector server to perform this operation.");
            en.put("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_ACCESS_TOKEN_AND_CLIENT_TOKEN_AND_URL", "If it is an authlib-injector account, you must have accessToken, clientToken and the address of the authlib-injector server to perform this operation.");
            en.put("CONSOLE_ACCOUNT_UN_OPERABLE_UUID", "You must be logged in with an official account or an authlib-injector account and have a UUID to perform this operation. If it is an authlib-injector account, the target server address is also required.");
            en.put("CONSOLE_INPUT_INT_WRONG", "Please enter a correct number within range. ");
            en.put("CONSOLE_INPUT_STRING_NOT_FOUND", "Not found \"%s\". ");
            en.put("CONSOLE_IMMERSIVE_UNKNOWN", "Command not found: %s. Please use command \"usage\" or \"help\" to get the manual.");
            en.put("CONSOLE_ENTER_EXIT", "Press enter to exit...");
            en.put("DATATYPE_STRING", "String");
            en.put("DATATYPE_INTEGER", "Integer");
            en.put("DATATYPE_BOOLEAN", "Boolean");
            en.put("DATATYPE_FRACTION", "Fraction");
            en.put("TIME_FORMAT", "EEE, MMM d, yyyy HH:mm:ss");
            en.put("ACCOUNT_TYPE_MICROSOFT", "Microsoft Account");
            en.put("ACCOUNT_TYPE_OFFLINE", "Offline Account");
            en.put("ACCOUNT_TYPE_OAS", "authlib-injector Account");
            en.put("ACCOUNT_SELECTED", "Selected");
            en.put("ACCOUNT_NOT_EXISTS", "Account does not exist: %d");
            en.put("ACCOUNT_TYPE_OAS_WITH_DETAIL", "authlib-injector Account: %s %s");
            en.put("ACCOUNT_INVALID", "Invalid Account: %d");
            en.put("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME", "Please enter the offline login player name: ");
            en.put("ACCOUNT_TIP_LOGIN_OAS_ADDRESS", "Please enter the authlib-injector login server address: ");
            en.put("NOT_SELECTED_AN_ACCOUNT", "No account selected. Please log in to your account, use \"-account -p\" to list the accounts, remember the order number of the account you want to select, and then use \"-account <Order Number>\" to select the account; Or add \"-s\" parameter after login account command.");
            en.put("DATATYPE_JSON_ARRAY", "JSON Array");
            en.put("DATATYPE_JSON_OBJECT", "JSON Object");
            en.put("INPUT_ACCOUNT", "Account: ");
            en.put("INPUT_PASSWORD", "Password: ");
            en.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT", "Failed to login authlib-injector account: %s");
            en.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_UNAVAILABLE_SERVER", "Target server access failed");
            en.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_FAILED_AUTHENTICATE", "Failed to authenticate");
            en.put("FAILED_TO_LOGIN_OAA_NO_SELECTED_CHARACTER", "Login failed, please select an available character and try again.");
            en.put("WARNING_SHOWING_PASSWORD", "Warning: Do this from a non-console and your password will not be hidden!");
            en.put("WARNING_REFRESH_NOT_SELECTED", "Warning: The current account has not selected a character, and this local account cannot be used. Please go to the authlib-injector server to select an available character and refresh it!");
            en.put("FILE_NOT_FOUND_OR_IS_A_DIRECTORY", "Target file not found or target file is a directory");
            en.put("SUCCESSFULLY_SET_SKIN", "Set skin successfully");
            en.put("UNAVAILABLE_AUTHLIB_ACCOUNT_REASON", "authlib-injector is misconfigured: %s");
            en.put("UNAVAILABLE_AUTHLIB_ACCOUNT", "authlib-injector account is not available, the game will use offline account.");
            en.put("UNAVAILABLE_CUSTOM_SKIN", "Custom skin is not available");
            en.put("PRINT_COMMAND_NOT_SUPPORT_OFFLINE_CUSTOM_SKIN", "Note: If you are using an offline account and use the command to start the game, custom skin will not be available.");
            en.put("EMPTY_UUID", "UUID is empty");
            en.put("EMPTY_PLAYERNAME", "Player name is empty");
            en.put("EMPTY_SKIN", "Skin is empty");
            en.put("ONLY_OFFLINE", "This feature only supports offline accounts");
            en.put("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE", "Non authlib-injector accounts or offline accounts do not support setting skins.");
            en.put("SKIN_TYPE_DEFAULT_OR_SLIM", "Do you want to set the skin model to slim (Alex)?");
            en.put("SKIN_STEVE_UNABLE_READ", "Failed to set, failed to read the Steve skin file!");
            en.put("SKIN_ALEX_UNABLE_READ", "Failed to set, failed to read the Alex skin file!");
            en.put("SKIN_STEVE_NOT_FOUND", "Failed to set, Steve skin file not found!");
            en.put("SKIN_ALEX_NOT_FOUND", "Failed to set, Alex skin file not found!");
            en.put("CAPE_FILE_NOT_FOUND", "The cape file \"%s\" not found");
            en.put("CAPE_FILE_FAILED_LOAD", "Failed to load the cape file \"%s\"");
            en.put("UNABLE_TO_START_OFFLINE_SKIN_SERVER", "Unable to customize skins with offline account");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_NOT_FOUND", "Can't use custom skin: Steve skin file not found!");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_NOT_FOUND", "Can't use custom skin: Alex skin file not found!");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_UNABLE_LOAD", "Can't use custom skin: failed to read the Steve skin file!");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_UNABLE_LOAD", "Can't use custom skin: failed to read the Alex skin file!");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_NOT_FOUND", "Can't use custom skin: The skin file \"%s\" not found");
            en.put("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_FAILED_LOAD", "Can't use custom skin: Failed to load the skin file \"%s\"");
            en.put("UNABLE_GET_VERSION_INFORMATION", "Failed to read version information");
            en.put("UNABLE_SET_SKIN", "Failed to set skin");
            en.put("INSTALL_MODLOADER_FAILED_TO_GET_INSTALLABLE_VERSION", "Unable to install %s: Failed to get installable versions.");
            en.put("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", "Unable to install %1$s: There is no installable version of %1$s, probably because %1$s does not support this version of the game.");
            en.put("INSTALL_MODLOADER_FAILED_TO_GET_TARGET_JSON", "Unable to install %1$s: Failed to get target %1$s JSON.");
            en.put("INSTALL_MODLOADER_SELECT", "Please enter the version of %1$s you want to install: ");
            en.put("INSTALL_MODLOADER_SELECT_NOT_FOUND", "Version \"%1$s\" not found, please enter the version of %2$s you want to install: ");
            en.put("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", "Would you like to continue installing the original version (without %s)?");
            en.put("INSTALL_MODLOADER_FAILED_TO_PARSE_TARGET_JSON", "Unable to install %1$s: Failed to parse target %1$s JSON.");
            en.put("INSTALL_MODLOADER_ALREADY_INSTALL", "Unable to install %1$s: The target version is installed %1$s.");
            en.put("INSTALL_MODLOADER_EMPTY_MC_VERSION", "Unable to install %1$s: Could not get the target version of the game version.");
            en.put("INSTALL_MODLOADER_FAILED_WITH_REASON", "Failed to install %s: %s");
            en.put("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", "Unable to install %1$s: The target version already has %2$s installed, %2$s and %1$s cannot coexist.");
            en.put("INSTALL_MODLOADER_NO_FILE", "Unable to install %1$s: The version of %1$s you entered has no downloadable files. Please try another version of %1$s.");
            en.put("INSTALL_MODLOADER_FAILED_DOWNLOAD", "Unable to install %s: download file failed");
            en.put("INSTALL_MODLOADER_DOWNLOADING_FILE", "Downloading file...");
            en.put("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION_2", "Unable to install %1$s: There is no installable version of %1$s.");
            en.put("INSTALL_MODLOADER_FAILED_UNKNOWN_TYPE", "Unable to install %1$s: Unknown type of %1$s.");
            en.put("INSTALL_MODLOADER_FAILED_MC_VERSION_MISMATCH", "Unable to install %1$s: The game version of the target %1$s does not match the target game version.");
            en.put("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", "${NAME} version \"%s\" not found.");
            en.put("INSTALL_MODLOADER_SELECT_NOT_FOUND_GAME_OR_TARGET_EXTRA", "Not found target game version or ${NAME} version.");
            en.put("INSTALLED_MODLOADER", "%s installed successfully");
            en.put("VERSION_INFORMATION_GAME_VERSION", "   Game Version:             ");
            en.put("VERSION_INFORMATION_RELEASE_TIME", "   Version Release Time:     ");
            en.put("VERSION_INFORMATION_FABRIC_VERSION", "   Fabric Version:           ");
            en.put("VERSION_INFORMATION_FORGE_VERSION", "   Forge Version:            ");
            en.put("VERSION_INFORMATION_JAVA_COMPONENT", "   Java Component:           ");
            en.put("VERSION_INFORMATION_JAVA_VERSION", "   Java Version Requirement: ");
            en.put("VERSION_INFORMATION_ASSETS_VERSION", "   Resource Version:         ");
            en.put("VERSION_INFORMATION_LITELOADER_VERSION", "   LiteLoader Version:       ");
            en.put("VERSION_INFORMATION_OPTIFINE_VERSION", "   OptiFine Version:         ");
            en.put("VERSION_INFORMATION_QUILT_VERSION", "   Quilt Version:            ");
            en.put("VERSION_INFORMATION_VERSION_TYPE", "   Version Type:             ");
            en.put("VERSION_INFORMATION_VERSION_TYPE_RELEASE", "Release");
            en.put("VERSION_INFORMATION_VERSION_TYPE_SNAPSHOT", "Snapshot");
            en.put("VERSION_INFORMATION_VERSION_TYPE_OLD_BETA", "Old Beta");
            en.put("VERSION_INFORMATION_VERSION_TYPE_OLD_ALPHA", "Old Alpha");
            en.put("VERSION_INFORMATION_GAME_VERSION_FAILED_GET", "Failed to get");
            en.put("VERSION_INFORMATION_VERSION_PATH", "   File Location:            ");
            en.put("EXCEPTION_JAVA_NOT_FOUND", "Unable to launch game: the java file not found");
            en.put("EXCEPTION_READ_FILE", "Failed to read file");
            en.put("EXCEPTION_READ_FILE_WITH_PATH", "Failed to read the file \"%s\"");
            en.put("EXCEPTION_PARSE_FILE", "Failed to parse file");
            en.put("EXCEPTION_PARSE_FILE_WITH_PATH", "Failed to parse the file \"%s\"");
            en.put("EXCEPTION_WRITE_FILE", "Failed to write content to file");
            en.put("EXCEPTION_WRITE_FILE_WITH_PATH", "Failed to write content to the file \"%s\"");
            en.put("EXCEPTION_UNABLE_PARSE", "Failed to parse");
            en.put("EXCEPTION_INSTALL_MODPACK", "Failed to install modpack: %s");
            en.put("EXCEPTION_EXECUTE_COMMAND", "Failed to execute the command");
            en.put("EXCEPTION_INCOMPLETE_VERSION", "This version is incomplete, please use \"-version -b <Version Name>\" to complete the version before starting.");
            en.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK", "File download link not found.");
            en.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK_WITH_FILENAME", "The download url for the file \"%s\" could not be found.");
            en.put("CF_FAILED_TO_SHOW_SOMEONE", "Failed to display ${NAME} %d: %s");
            en.put("CF_AUTHOR_MORE", "and other %d authors");
            en.put("CF_SELECT_TARGET", "Please select the target ${NAME} (%d-%d): ");
            en.put("CF_SUPPORTED_GAME_VERSION", "%s supported game versions: ");
            en.put("CF_INPUT_GAME_VERSION", "Please enter the version you want to download: ");
            en.put("CF_INPUT_VERSION", "Please select the ${NAME} version you want to download (%d-%d, exit if the value is -1): ");
            en.put("CF_STORAGE_FILE_EXISTS", "The file \"%s\" already exists, please enter a directory to store the ${NAME} file: ");
            en.put("CF_NO_VERSION_FOR_GAME_VERSION", "There is no %s version available for this game version.");
            en.put("CF_INFORMATION_NOTHING", "There is no information about this %s");
            en.put("CF_INFORMATION_MOD_NAME", "   Mod Name:                      ");
            en.put("CF_INFORMATION_MOD_ID", "   Mod ID:                        ");
            en.put("CF_INFORMATION_MODPACK_NAME", "   Modpack Name:                  ");
            en.put("CF_INFORMATION_MODPACK_ID", "   Modpack ID:                    ");
            en.put("CF_INFORMATION_AUTHORS", "   Authors:                       ");
            en.put("CF_INFORMATION_AUTHOR", "   Author:                        ");
            en.put("CF_INFORMATION_SUMMARY", "   Introduction:                  ");
            en.put("CF_INFORMATION_LATEST_GAME_VERSION", "   Latest Supported Game Version: ");
            en.put("CF_INFORMATION_MOD_LOADERS", "   Mod Loaders:                   ");
            en.put("CF_INFORMATION_DATE_MODIFIED", "   Modified Date:                 ");
            en.put("CF_INFORMATION_DATE_CREATED", "   Created Date:                  ");
            en.put("CF_INFORMATION_DATE_RELEASED", "   Released Date:                 ");
            en.put("CF_INFORMATION_ISSUE_TRACKER_URL", "   Feedback:                      ");
            en.put("CF_INFORMATION_SOURCE_URL", "   Source Code Repository:        ");
            en.put("CF_INFORMATION_WEBSITE_URL", "   Webpage Introduction:          ");
            en.put("CF_INFORMATION_WIKI_URL", "   Wiki Website:                  ");
            en.put("CF_INFORMATION_DOWNLOADS", "   Downloads:                     ");
            en.put("CF_INFORMATION_CATEGORIES", "   Categories:       ");
            en.put("CF_INFORMATION_DISCORD_URL", "   Discord URL:      ");
            en.put("CF_INFORMATION_DONATION", "   Donation:         ");
            en.put("CF_INFORMATION_DONATION_URL", "         Url: ");
            en.put("CF_INFORMATION_AUTHOR_URL", "         Homepage: ");
            en.put("CF_GET_BY_ID_FAILED", "Unable to get target ${NAME}: %s\nPossible reasons for this error:\n1. The target ${NAME} does not exist\n2. Network exception\n3. There is a problem with the server");
            en.put("CF_GET_BY_ID_NOT_OF_MC", "The target add-on is not a Minecraft ${NAME}, the game ID of the add-on is %d.");
            en.put("CF_DEPENDENCIES_TIP", "This ${NAME} requires the following pre-${NAME}s to work properly, please install the following pre-${NAME}s after installing this ${NAME}.");
            en.put("CF_DEPENDENCY_INFORMATION_ID", "   ID:   %d");
            en.put("CF_DEPENDENCY_INFORMATION_ID_STRING", "   ID:   %s");
            en.put("CF_DEPENDENCY_INFORMATION_NAME", "   Name: %s");
            en.put("CF_BESEARCHED_MOD_ALC", "mod");
            en.put("CF_BESEARCHED_MOD_FUC", "Mod");
            en.put("CF_BESEARCHED_MODPACK_ALC", "modpack");
            en.put("CF_BESEARCHED_MODPACK_FUC", "Modpack");
            en.put("CF_GET_BY_ID_INCORRECT_CATEGORY", "The target game component is not a ${NAME}, the category ID of this component is %d.");
            en.put("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL", "The target game component is not a ${NAME}, the component is a ${TARGET}.");
            en.put("MOD_FAILED_TO_GET_ALL_FILES", "Failed to get list of ${NAME} files: %s");
            en.put("NO_SEARCH_RESULTS", "No search results.");
            en.put("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", "Failed to download the mod with projectId %d: %s");
            en.put("INSTALL_MODPACK_EACH_MOD_GET_URL", "Traversing to get the download links of each mod, please be patient");
            en.put("INSTALL_MODPACK_COMPLETE", "Install modpack complete");
            en.put("INSTALL_MODPACK_MODRINTH_UNKNOWN_MODLOADER", "Unknown modloader: %s");
            en.put("INSTALL_OPTIFINE_INCOMPATIBLE_WITH_FORGE_17", "Unable to install OptiFine: The current game version of Forge is not compatible with OptiFine versions lower than H1 Pre2, please try a newer version of OptiFine.");
            en.put("DOWNLOAD_SOURCE_OFFICIAL", "Official");
            en.put("DOWNLOAD_SOURCE_BMCLAPI", "BMCLAPI");
            en.put("DOWNLOAD_SOURCE_MCBBS", "MCBBS");
        }
        return en;
    }

    /*enUSUsage*/
    public static Map<String, String> getEnUsage() {
        if (enUsage.size() == 0) {
            enUsage.put("TITLE",
                    "Usage Manual:\n" +
                            "    Print usage manual:                    -u or -usage\n" +
                            "    Start the selected version:             Direct start game without parameters or -b or -start\n" +
                            "    Start a specific version:               Unique parameter: <Version Name> or -b <Version Name>\n" +
                            "    List all versions:                     -l or -list or -ls\n" +
                            "    List all versions in another game dir: -l <Target Game Directory>\n" +
                            "    Print the launch command:              -p <Version Name> or -print <Version Name>\n" +
                            "    Select version:                        -s <Version Name> or -select <Version Name>\n" +
                            "    Get about description:                 -a or -about\n" +
                            "    To enter immersive mode:               -i or -immersive\n" +
                            "    Check for Updates:                     -cfu");
            enUsage.put("ACCOUNT",
                    "Account Related:\n" +
                            "    Select a account:         -account <Order Number>\n" +
                            "    List all accounts:        -account -p\n" +
                            "    Delete a account:         -account -t <Order Number>\n" +
                            "    Offline Login:            -account -l -o <Offline Playername> -s(Optional, select this account after successful login)\n" +
                            "    Microsoft Account Login:  -account -l -m -s(Optional, select this account after successful login)\n" +
                            "    authlib-injector Login:   -account -l -a <Server Address> -s(Optional, select this account after successful login)\n" +
                            "    Refresh account:          -account -r\n" +
                            "    Download skin:            -account -s -d <Skin File Storage Path>\n" +
                            "    Set skin (Microsoft account not available):              -account -s -u <Skin file path (if it is an offline account, if you do not enter it, you will cancel the skin setting)>\n" +
                            "    Set the skin to Steve (Microsoft account not available): -account -s -e\n" +
                            "    Set the skin to Alex (Microsoft account not available):  -account -s -x\n" +
                            "    Set a cape (only for offline account):                   -account -c <Cape file path, if not entered it will unset the cape>");
            enUsage.put("CONFIG",
                    "Configuration Related:\n" +
                            "    Print a configuration:                           -config -p <Configuration Name>\n" +
                            "    Print all configurations:                        -config -a\n" +
                            "    Print the original content of the configuration: -config -o <The number of spaces to indent, can be empty, defaults to 2>\n" +
                            "    Clear all configurations:                        -config -c\n" +
                            "    Remove a configuration:                          -config -r <Configuration Name>\n" +
                            "    Set a configuration(regardless of type):         -config <Configuration Name> <Configuration Value>\n" +
                            "    Set a configuration: -config -s -t <Configuration type, such as\n" +
                            "                                            i Integer,\n" +
                            "                                            b Boolean,\n" +
                            "                                            s String and\n" +
                            "                                            f Fraction> -n <Configuration Name> -v <Configuration Value>");
            enUsage.put("VERSION",
                    "Version Related:\n" +
                            "    View Version Information: -version -i <Version Name>\n" +
                            "    Delete a version:         -version -d <Version Name>\n" +
                            "    Rename a version:         -version -r <Version Name> -t <New Version Name>\n" +
                            "    Re-download the native dependency library files:    -version -n <Version Name>\n" +
                            "    Find missing dependency library files and download: -version -l <Version Name>\n" +
                            "    Complete version:                                   -version -b <Version Name>\n" +
                            "    Install Fabric to local version:                    -version -f <Version Name> -fapi(optional, with Fabric Api)\n" +
                            "    Install Forge to local version:                     -version -o <Version Name>\n" +
                            "    Install LiteLoader to local version:                -version -e <Version Name>\n" +
                            "    Install OptiFine to local version:                  -version -p <Version Name>\n" +
                            "    Install Quilt to local version:                     -version -q <Version Name>\n" +
                            "  Note: You can specify the version by adding \"-v <Version>\" after the commands to install Fabric, Forge, LiteLoader, OptiFine and Quilt, so as to avoid entering the command and then selecting the version.");


            enUsage.put("VERSION_CONFIG",
                    "Version Config Related:\n" +
                            "    Set version working directory: -vcfg <Version Name> -workingDirectory <Target directory, default if not entered>");

            enUsage.put("JVM_ARGS",
                    "Custom JVM Virtual Machine Parameters Related:\n" +
                            "    Print all parameters: -jvmArgs -p <The number of spaces to indent, can be empty, defaults to 2>\n" +
                            "    Add a parameter:      -jvmArgs -a <Parameter Content>\n" +
                            "    Delete a parameter:   -jvmArgs -d <Order number, starting from 0>");

            enUsage.put("GAME_ARGS",
                    "Custom Game Parameters Related:\n" +
                            "    Print all parameters: -gameArgs -p <The number of spaces to indent, can be empty, defaults to 2>\n" +
                            "    Add a parameter:      -gameArgs -a -n <Parameter Name> -v <Parameter Value(optional, do not enter -v if this is empty)>\n" +
                            "    Delete a parameter:   -gameArgs -d <Parameter Name>");

            enUsage.put("INSTALL",
                    "Installation Version Related:\n" +
                            "    Direct install version: -install <Version Name (if there are spaces, add double quotes)> -n <Local Version Name (optional)>\n" +

                            "    Optional parameters:\n" +
                            "                -f Install Fabric -fapi(optional, with Fabric Api)\n" +
                            "                -o Install Forge\n" +
                            "                -e Install LiteLoader\n" +
                            "                -p Install OptiFine\n" +
                            "                -q Install Quilt\n" +
                            "                -t <Thread Count> Set the number of threads for downloading asset files (default 64)\n" +
                            "                -na Do not download asset files\n" +
                            "                -nl Do not download dependency library files\n" +
                            "                -nn Do not download native dependency library files\n" +
                            "         Note: Fabric and Forge, Fabric and LiteLoader, Fabric and OptiFine cannot be installed at the same time or coexist (Quilt is the same as Fabric, but they also cannot coexist)\n" +
                            "               You can specify the version after the parameters -f, -o, -e, -p, -q to avoid asking for the version during installation. For example: \"-f 0.14.8\" means to install Fabric with version 0.14.8.\n" +
                            "\n" +
                            "    Show installable versions (if no range is set, all versions of this type are showed by default): -install -s <Versions types: a All; r Releases; s Snapshots; oa Ancient Alpha; ob Ancient Beta>\n" +
                            "      Set time range (optional): -i <from year>-<from month>-<from day>/<to year>-<to month>-<to day>\n" +
                            "                        Example: -i 2020-05-09/2021-10-23");
            enUsage.put("MOD",
                    "Mod Related (Download Source: CurseForge):\n" +
                            "    Search for mods and install (by name):             -mod -i <Mod Name>\n" +
                            "    Search for mods and install (by ID):               -mod -i -c <Mod ID>\n" +
                            "    Search for mods and display information (by name): -mod -s <Mod Name>\n" +
                            "    Search for mods and display information (by ID):   -mod -s -c <Mod ID>");
            enUsage.put("MODPACK",
                    "Modpack Related (Download Source: CurseForge):\n" +
                            "   Optional parameters for installing the modpack:\n" +
                            "                  -t <Thread Count> Set the number of threads for downloading asset files (default 64)\n" +
                            "                  -na Do not download asset files\n" +
                            "                  -nl Do not download dependency library files\n" +
                            "                  -nn Do not download native dependency library files\n" +
                            "    Search for modpacks and install (by name):             -modpack -i <Modpack Name> -k(Optional, keep the file after installation)\n" +
                            "    Search for modpacks and install (by ID):               -modpack -i -c <Modpack ID> -k(Optional, keep the file after installation)\n" +
                            "    Search for modpacks and display information (by name): -modpack -s <Modpack Name>\n" +
                            "    Search for modpacks and display information (by ID):   -modpack -s -c <Modpack ID>\n" +
                            "    Install local modpack:                                 -modpack -l <Modpack Path>");
            enUsage.put("MOD2",
                    "Mod Related (Download Source: Modrinth):\n" +
                            "    Search for mods and install (by name):             -mod2 -i <Mod Name> -l <Optional, limit the number of results, default is 50>\n" +
                            "    Search for mods and install (by ID):               -mod2 -i -c <Mod ID>\n" +
                            "    Search for mods and display information (by name): -mod2 -s <Mod Name> -l <Optional, limit the number of results, default is 50>\n" +
                            "    Search for mods and display information (by ID):   -mod2 -s -c <Mod ID>");
            enUsage.put("MODPACK2",
                    "Modpack Related (Download Source: Modrinth):\n" +
                            "   Optional parameters for installing the modpack:\n" +
                            "                  -t <Thread Count> Set the number of threads for downloading asset files (default 64)\n" +
                            "                  -na Do not download asset files\n" +
                            "                  -nl Do not download dependency library files\n" +
                            "                  -nn Do not download native dependency library files\n" +
                            "    Search for modpacks and install (by name):             -modpack2 -i <Modpack Name> -k(Optional, keep the file after installation) -l <Optional, limit the number of results, default is 50>\n" +
                            "    Search for modpacks and install (by ID):               -modpack2 -i -c <Modpack ID> -k(Optional, keep the file after installation)\n" +
                            "    Search for modpacks and display information (by name): -modpack2 -s <Modpack Name> -l <Optional, limit the number of results, default is 50>\n" +
                            "    Search for modpacks and display information (by ID):   -modpack2 -s -c <Modpack ID>");

        }
        return enUsage;
    }

    /*zhCNUsage*/
    public static Map<String, String> getZhUsage() {
        if (zhUsage.size() == 0) {
            zhUsage.put("TITLE",
                    "使用手册：\n" +
                            "    获得使用手册：              -u 或 -usage\n" +
                            "    启动选择的版本：             无参数直接运行 或 -b 或 -start\n" +
                            "    启动特定的版本：             唯一参数：<版本名称> 或 -b <版本名称>\n" +
                            "    列出所有版本：              -l 或 -list 或 -ls\n" +
                            "    列出某个游戏目录的所有版本：-l <目标游戏目录>\n" +
                            "    打印启动命令：              -p <版本名称> 或 -print <版本名称>\n" +
                            "    选择版本：                  -s <版本名称> 或 -select <版本名称>\n" +
                            "    获得关于信息：              -a 或 -about\n" +
                            "    进入沉浸模式：              -i 或 -immersive\n" +
                            "    检查更新：                  -cfu");
            zhUsage.put("ACCOUNT",
                    "账号相关：\n" +
                            "    选择账号：       -account <序号>\n" +
                            "    列出账号：       -account -p\n" +
                            "    删除账号：       -account -t <序号>\n" +
                            "    离线登录：       -account -l -o <离线用户名> -s(可选，登录成功后选择此账号)\n" +
                            "    微软账户登录：   -account -l -m -s(可选，登录成功后选择此账号)\n" +
                            "    外置账号登录：   -account -l -a <服务器地址> -s(可选，登录成功后选择此账号)\n" +
                            "    刷新账号：       -account -r\n" +
                            "    下载皮肤：       -account -s -d <皮肤文件存储路径>\n" +
                            "    设置皮肤（微软账户不可用）：         -account -s -u <皮肤文件路径(如果是离线账号，不输入则为取消设置皮肤)>\n" +
                            "    设置皮肤为 Steve（微软账户不可用）： -account -s -e\n" +
                            "    设置皮肤为 Alex（微软账户不可用）：  -account -s -x\n" +
                            "    设置披风（仅离线账号）：             -account -c <披风文件路径，如果不输入则为取消设置披风>");
            zhUsage.put("CONFIG",
                    "配置相关：\n" +
                            "    打印某项配置：        -config -p <配置名>\n" +
                            "    打印全部配置：        -config -a\n" +
                            "    打印配置原内容：      -config -o <缩进的空格数，可为空，默认为2>\n" +
                            "    清空配置：            -config -c\n" +
                            "    删除某项配置：        -config -r <配置名称>\n" +
                            "    设置配置（不分类型）：-config <配置名称> <配置值>\n" +
                            "    设置某项配置：        -config -s -t <配置类型，如\n" +
                            "                                       i 整数、\n" +
                            "                                       b 布尔值、\n" +
                            "                                       s 字符串以及\n" +
                            "                                       f 小数> -n <配置名称> -v <配置值>");
            zhUsage.put("VERSION",
                    "版本相关：\n" +
                            "    查看版本信息：              -version -i <版本名称>\n" +
                            "    删除某个版本：              -version -d <版本名称>\n" +
                            "    重命名版本：                -version -r <版本名称> -t <新版本名称>\n" +
                            "    重新下载原生依赖库文件：    -version -n <版本名称>\n" +
                            "    查找缺少的依赖库文件并下载：-version -l <版本名称>\n" +
                            "    把版本补充完整：            -version -b <版本名称>\n" +
                            "    安装 Fabric 到本地版本：    -version -f <版本名称> -fapi(可选，安装 Fabric Api)\n" +
                            "    安装 Forge 到本地版本：     -version -o <版本名称>\n" +
                            "    安装 LiteLoader 到本地版本：-version -e <版本名称>\n" +
                            "    安装 OptiFine 到本地版本：  -version -p <版本名称>\n" +
                            "    安装 Quilt 到本地版本：     -version -q <版本名称>\n" +
                            "  注：可在安装 Fabric、Forge、LiteLoader、OptiFine 和 Quilt 的命令后添加上“-v <版本>”以指定版本，以免输入命令后再选择版本。");

            zhUsage.put("VERSION_CONFIG",
                    "版本配置相关：\n" +
                            "    设置版本隔离：-vcfg <版本名称> -workingDirectory <目标目录，不输入则设为默认>");

            zhUsage.put("JVM_ARGS",
                    "自定义JVM虚拟机参数相关：\n" +
                            "    输出所有参数：-jvmArgs -p <缩进的空格数，可为空，默认为2>\n" +
                            "    添加参数：    -jvmArgs -a <参数内容>\n" +
                            "    删除参数：    -jvmArgs -d <序号，从0开始>");

            zhUsage.put("GAME_ARGS",
                    "自定义游戏参数相关：\n" +
                            "    输出所有参数：-gameArgs -p <缩进的空格数，可为空，默认为2>\n" +
                            "    添加参数：    -gameArgs -a -n <参数名称> -v <参数值(可选，如果此项为空则不要输入-v)>\n" +
                            "    删除参数：    -gameArgs -d <参数名称>");


            zhUsage.put("INSTALL",
                    "安装版本相关：\n" +
                            "    直接安装版本：-install <版本名称（如果有空格要加双引号）> -n <存储的版本名称(可选)>\n" +
                            "      可选的参数：-f 安装 Fabric -fapi(可选，安装 Fabric Api)\n" +
                            "                  -o 安装 Forge\n" +
                            "                  -e 安装 LiteLoader\n" +
                            "                  -p 安装 OptiFine\n" +
                            "                  -q 安装 Quilt\n" +
                            "                  -t <线程数> 设置下载资源文件的线程数（默认为64）\n" +
                            "                  -na 不下载资源文件\n" +
                            "                  -nl 不下载依赖库文件\n" +
                            "                  -nn 不下载原生依赖库文件\n" +
                            "        注：Fabric 和 Forge、Fabric 和 LiteLoader、Fabric 和 OptiFine 不能同时安装或共存（Quilt 与 Fabric 相同，但它们也不能共存）\n" +
                            "          可以在参数-f、-o、-e、-p、-q后指定版本，以免安装时询问版本，例:“-f 0.14.8”则为安装版本为 0.14.8 的 Fabric。\n" +
                            "\n" +
                            "    显示可安装的版本（若没有设置范围，默认显示该类型的全部版本）：-install -s <版本类型：a 全部；r 正式版；s 快照版；oa 远古alpha版；ob 远古beta版>\n" +
                            "      设置时间范围（可选）：-i <从年>-<从月>-<从日>/<到年>-<到月>-<到日>\n" +
                            "                        例：-i 2020-05-09/2021-10-23");
            zhUsage.put("MOD",
                    "模组相关（下载源：CurseForge）：\n" +
                            "    搜索模组并安装（通过名称）：    -mod -i <模组名称>\n" +
                            "    搜索模组并安装（通过ID）：      -mod -i -c <模组ID>\n" +
                            "    搜索模组并显示信息（通过名称）：-mod -s <模组名称>\n" +
                            "    搜索模组并显示信息（通过ID）：  -mod -s -c <模组ID>");
            zhUsage.put("MODPACK",
                    "整合包相关（下载源：CurseForge）：\n" +
                            "   安装整合包的可选参数：\n" +
                            "                  -t <线程数> 设置下载资源文件的线程数（默认为64）\n" +
                            "                  -na 不下载资源文件\n" +
                            "                  -nl 不下载依赖库文件\n" +
                            "                  -nn 不下载原生依赖库文件\n" +
                            "    搜索整合包并安装（通过名称）：    -modpack -i <整合包名称> -k(可选，安装完成后保留文件)\n" +
                            "    搜索整合包并安装（通过ID）：      -modpack -i -c <整合包ID> -k(可选，安装完成后保留文件)\n" +
                            "    搜索整合包并显示信息（通过名称）：-modpack -s <整合包名称>\n" +
                            "    搜索整合包并显示信息（通过ID）：  -modpack -s -c <整合包ID>\n" +
                            "    安装本地整合包（多种格式）：      -modpack -l <整合包路径>");
            zhUsage.put("MOD2",
                    "模组相关（下载源：Modrinth）：\n" +
                            "    搜索模组并安装（通过名称）：    -mod2 -i <模组名称> -l <可选，限制结果数量，默认为50>\n" +
                            "    搜索模组并安装（通过ID）：      -mod2 -i -c <模组ID>\n" +
                            "    搜索模组并显示信息（通过名称）：-mod2 -s <模组名称> -l <可选，限制结果数量，默认为50>\n" +
                            "    搜索模组并显示信息（通过ID）：  -mod2 -s -c <模组ID>");
            zhUsage.put("MODPACK2",
                    "整合包相关（下载源：Modrinth）：\n" +
                            "   安装整合包的可选参数：\n" +
                            "                  -t <线程数> 设置下载资源文件的线程数（默认为64）\n" +
                            "                  -na 不下载资源文件\n" +
                            "                  -nl 不下载依赖库文件\n" +
                            "                  -nn 不下载原生依赖库文件\n" +
                            "    搜索整合包并安装（通过名称）：    -modpack2 -i <整合包名称> -k(可选，安装完成后保留文件) -l <可选，限制结果数量，默认为50>\n" +
                            "    搜索整合包并安装（通过ID）：      -modpack2 -i -c <整合包ID> -k(可选，安装完成后保留文件)\n" +
                            "    搜索整合包并显示信息（通过名称）：-modpack2 -s <整合包名称> -l <可选，限制结果数量，默认为50>\n" +
                            "    搜索整合包并显示信息（通过ID）：  -modpack2 -s -c <整合包ID>");
        }
        return zhUsage;
    }
}
