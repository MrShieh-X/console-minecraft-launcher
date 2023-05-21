/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2023  MrShiehX <3553413882@qq.com>
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
 *
 */
package com.mrshiehx.cmcl.constants.languages;

import com.mrshiehx.cmcl.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class English implements Language {
    @Override
    public Map<String, String> getTextMap() {
        Map<String, String> en = new HashMap<>();
        en.put("APPLICATION_NAME", "Console Minecraft Launcher");
        en.put("MESSAGE_ABOUT_DESCRIPTION_1", "Console Minecraft Launcher %1$s");
        en.put("MESSAGE_ABOUT_DESCRIPTION_2", "A Launcher for Minecraft Java Edition");
        en.put("MESSAGE_ABOUT_DESCRIPTION_4", "Source code repository: ");
        en.put("MESSAGE_ABOUT_DESCRIPTION_6", "Dependency Libraries: ");
        en.put("MESSAGE_ABOUT_DESCRIPTION_MAIN_DEVELOPERS", "Main Developers:");
        en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS", "Special Thanks: ");
        en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_AUTHLIB_INJECTOR", "authlib-injector support");
        en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_BMCLAPI", "BMCLAPI download source provider");
        en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS_NAME", "MCBBS.....................");
        en.put("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS", "MCBBS download source provider");
        en.put("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_TITLE", "Disclaimer");
        en.put("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_CONTENT_1", "The copyright of Minecraft belongs to Mojang Studios and Microsoft. The software producer is not responsible for any copyright issues arising from the use of CMCL. Please support the official game.");
        en.put("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_CONTENT_2", "All consequences arising from the use of CMCL by the user shall be borne by the user himself. Any legal disputes and conflicts involving CMCL have nothing to do with the developer, and CMCL and the developer will not bear any responsibility.");
        en.put("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE", "Failed to login the official account");
        en.put("MESSAGE_LOGINED_TITLE", "Login into the account successfully");
        en.put("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT", "You did not set a skin");
        en.put("MESSAGE_UNABLE_TO_LOGIN_MICROSOFT", "Unable to login temporarily, please try again later.");
        en.put("MESSAGE_OFFICIAL_LOGIN_FAILED_MESSAGE", "Login to your account failed, probably because your account does not have Minecraft.");
        en.put("MESSAGE_FAILED_REFRESH_TITLE", "Failed to refresh account");
        en.put("MESSAGE_STARTING_GAME", "Starting game...");
        en.put("MESSAGE_FINISHED_GAME", "Game finished");
        en.put("MESSAGE_FAILED_TO_CONNECT_TO_URL", "Failed to connect to %s, please check your network connection.");
        en.put("MESSAGE_VERSIONS_LIST_IS_EMPTY", "The game versions list is empty");
        en.put("MESSAGE_INSTALL_INPUT_NAME", "Please enter a name of the new version: ");
        en.put("MESSAGE_INSTALL_INPUT_NAME_EXISTS", "%s: The name already exists, please change a name.");
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
        en.put("MESSAGE_FAILED_DOWNLOAD_FILE", "%s: Failed to download the file");
        en.put("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", "Failed to download the file \"%s\": %s");
        en.put("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES", "Downloading the library files...");
        en.put("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES", "Download the library files complete");
        en.put("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", "Failed to download the library files: %s");
        en.put("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY", "The libraries list is empty");
        en.put("MESSAGE_FAILED_TO_DECOMPRESS_FILE", "Failed to decompress file \"%1$s\": %2$s");
        en.put("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES", "Decompressing the native library files...");
        en.put("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES", "Decompress the native library files complete");
        en.put("MESSAGE_FAILED_TO_COPY_FILE", "Failed to copy file \"%1$s\" to \"%2$s\": %3$s");
        en.put("MESSAGE_INSTALLED_NEW_VERSION", "Install the new version complete");
        en.put("MESSAGE_UUID_ACCESSTOKEN_EMPTY", "UUID or accessToken is empty, you can try to refresh your account or login again.");
        en.put("MESSAGE_DOWNLOADING_FILE", "Downloading %s");
        en.put("MESSAGE_DOWNLOADING_FILE_TO", "Downloading %s to %s");
        en.put("MESSAGE_COPYING_FILE", "Copying %s to %s");
        en.put("MESSAGE_UNZIPPING_FILE", "Decompressing %s");
        en.put("MESSAGE_GAME_CRASH_CAUSE_TIPS", "Game crash possible error: %s");
        en.put("MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER", "Older versions of Minecraft may have this error because the Java version is too high, Java 8 and below can be used to fix this.\nYou can use \"version <version> --config=javaPath <Java path>\" to set the Java path for the version separately.");
        en.put("MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD", "This error may occur because some of the native dependency library are missing or damaged, please re-download native dependency libraries via \"version <version> --complete=natives\" to fix this problem.");
        en.put("MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL", "The memory is not enough, you can try to adjust the memory to a larger number.");
        en.put("MESSAGE_REDOWNLOADED_NATIVES", "Download native dependency libraries complete");
        en.put("MESSAGE_FAILED_SEARCH", "Failed to search: %s");
        en.put("MESSAGE_FAILED_RENAME_VERSION", "Failed to rename the version: %s");
        en.put("MESSAGE_START_INSTALLING_FORGE", "Start installing Forge");
        en.put("MESSAGE_INSTALLED_FORGE", "Forge installed successfully");
        en.put("MESSAGE_NOT_FOUND_LIBRARY_DOWNLOAD_URL", "Could not find the download URL of the dependency library %s");
        en.put("MESSAGE_INSTALL_NATIVES_EMPTY_JAR", "No native dependency library files that need to be decompressed");
        en.put("MESSAGE_INSTALL_FORGE_FAILED_EXECUTE_PROCESSOR", "Failed to execute processor: %s");
        en.put("MESSAGE_YGGDRASIL_LOGIN_SELECT_PROFILE", "Please choose a character (%d-%d): ");
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
        en.put("MESSAGE_COMPLETE_VERSION_IS_COMPLETE", "This version is complete and does not need to be completed. If the version is indeed incomplete after checking, please reinstall the version.");
        en.put("MESSAGE_COMPLETED_VERSION", "Version completed successfully");
        en.put("MESSAGE_SELECT_DOWNLOAD_SOURCE", "Please select the download source for the first download (default is %d, stored as configuration \"downloadSource\"): ");
        en.put("MESSAGE_SELECT_ACCOUNT", "Please enter the order number of the account you want to select (%d-%d): ");
        en.put("MESSAGE_SELECT_ACCOUNT_TYPE", "No account is currently available, please select the account type of the new account (%d-%d): ");
        en.put("MESSAGE_FAILED_TO_CHECK_FOR_UPDATES", "Failed to check for updates");
        en.put("MESSAGE_NEW_VERSION", "New Version: %s\nUpdated Date: %s\nDownload urls:\n%sUpdated Content:\n%s");
        en.put("MESSAGE_CURRENT_IS_LATEST_VERSION", "The current version is the latest version");
        en.put("MESSAGE_BEFORE_LIST_VERSIONS", "Game versions in directory %s: ");
        en.put("MESSAGE_AUTHLIB_ACCOUNT_INCOMPLETE", "The authlib-injector account is incomplete, please delete it and login again.");
        en.put("MESSAGE_NIDE8AUTH_ACCOUNT_INCOMPLETE", "The nide8auth account is incomplete, please delete it and login again.");
        en.put("MESSAGE_ACCOUNT_FAILED_TO_VALIDATE", "Failed to validate account: %s");
        en.put("MESSAGE_ACCOUNT_INFO_EXPIRED_NEED_RELOGIN", "The information has expired, please login again.");
        en.put("MESSAGE_ACCOUNT_INFO_MISSING_NEED_RELOGIN", "The information has missing, please login again.");
        en.put("MESSAGE_AUTHLIB_ACCOUNT_REFRESH_NO_CHARACTERS", "Your character has been deleted, and there is no available character, please go to the authlib-injector login website to add a character and refresh or login again. Otherwise, the relevant functions in the game may not be available.");
        en.put("MESSAGE_NIDE8AUTH_ACCOUNT_REFRESH_NO_CHARACTERS", "Your character has been deleted, and there is no available character, please go to the nide8auth login website to add a character and refresh or login again. Otherwise, the relevant functions in the game may not be available.");
        en.put("MESSAGE_YGGDRASIL_ACCOUNT_REFRESH_OLD_CHARACTER_DELETED", "Your character has been deleted, please choose a new character.");
        en.put("MESSAGE_GAME_CRASH_CAUSE_JVM_UNRECOGNIZED_OPTION", "You added wrong JVM arguments. Get information about it through \"jvmArgs --help\".");
        en.put("MESSAGE_TELL_USER_CHECK_ACCOUNT_CAN_BE_OFF", "If you don't want to check whether the account is available before startup, you can use \"config checkAccountBeforeStart false\" or \"version <version> --config=checkAccountBeforeStart false\" to turn it off.");
        en.put("MESSAGE_STARTUP_INFO_MAIN", "Launching Version: ${VERSION_NAME} (${REAL_VERSION_NAME}) | Login Account: ${PLAYER_NAME} (${ACCOUNT_TYPE}) | Java Path: ${JAVA_PATH}\n" +
                "Game Exits with Launcher: ${EXIT_WITH_MC} | Fullscreen: ${FULLSCREEN} | Max Memory: ${MAX_MEMORY} | Window Width: ${WIDTH} | Window Height: ${HEIGHT} | Check account before startup: ${CHECK_ACCOUNT_BEFORE_START}\n" +
                "Game Directory: ${GAME_DIR}");
        en.put("MESSAGE_STARTUP_INFO_ASSETS_DIR", "Resource Packs Directory: ${ASSETS_DIR}");
        en.put("MESSAGE_STARTUP_INFO_RESOURCE_PACKS_DIR", "Assets Directory: ${RESOURCE_PACKS_DIR}");
        en.put("MESSAGE_STARTUP_INFO_ARGS", "Custom JVM Arguments:\n" +
                "${JVM_ARGS}\n" +
                "Custom Game Arguments:\n" +
                "${GAME_ARGS}");
        en.put("MESSAGE_TO_SELECT_VERSION", "Please use \"-s <version>\" to select a launch-able version or \"install <version>\" to install a new version and select it.");
        en.put("MESSAGE_PRINT_COMMAND_EXCEEDS_LENGTH_LIMIT", "Tip: The startup command is too long, you may not be able to run it directly in cmd or save it to a bat file and execute it. It is recommended that you use \"version [<version>] --export-script-ps=<script file>\" to export it as a PowerShell script file used later.");
        en.put("MESSAGE_EXPORT_COMMAND_EXCEEDS_LENGTH_LIMIT", "Unable to export the script file: Due to the length limit of the bat script file, the startup command is too long and cannot be exported as a bat file. You can only use \"version [<version>] --export-script-ps=<script file with .ps1 as the suffix>\" to export as a PowerShell script file.");
        en.put("MESSAGE_CONFIGURATIONS",
                " Note: For configurations whose type is Boolean, its value can be \"true\" which means \"yes\", or \"false\" which means \"no\".\n" +
                        "  accounts | JSON Array\n" +
                        "    Accounts (Non-direct modification, please use \"account -h\" to get the relevant tutorial for modification)\n\n" +
                        "  downloadSource | Integer\n" +
                        "    Download source, 0 is the default, 1 is BMCLAPI, 2 is MCBBS\n\n" +
                        "  language | String\n" +
                        "    Language, zh is Simplified Chinese, en is English and cantonese is Cantonese (Simplified)\n\n" +
                        "  selectedVersion | String\n" +
                        "    Selected start version\n\n" +
                        "  [Game related] | maxMemory | Integer\n" +
                        "    Maximum (Unit: MB)|\n\n" +
                        "  [Game related] | gameDir | String\n" +
                        "    Custom the path of the game directory (or set working directory), default is .minecraft\n\n" +
                        "  [Game related] | assetsDir | String\n" +
                        "    Custom assets resource directory path, if empty, it is the assets directory in the game directory\n\n" +
                        "  [Game related] | resourcesDir | String\n" +
                        "    Custom resource pack directory path, if empty, it is the resourcepacks directory in the game directory\n\n" +
                        "  [Game related] | javaPath | String\n" +
                        "    Java Path (It will get automatically if it is empty)\n\n" +
                        "  [Game related] | windowSizeWidth | Integer\n" +
                        "    The width of the game window\n\n" +
                        "  [Game related] | windowSizeHeight | Integer\n" +
                        "    The height of the game window\n\n" +
                        "  [Game related] | isFullscreen | Boolean\n" +
                        "    Whether the game window is fullscreen or not\n\n" +
                        "  [Game related] | exitWithMinecraft | Boolean\n" +
                        "    When running the game, whether or not you need to exit the launcher and exit the game by the way\n\n" +
                        "  [Game related] | printStartupInfo | Boolean\n" +
                        "    When starting the game, whether to output startup information (Java path, maximum memory, etc.)\n\n" +
                        "  [Game related] | checkAccountBeforeStart | Boolean\n" +
                        "    Check whether the account is available before starting the game\n\n" +
                        "  [Game related] | jvmArgs | JSON Array\n" +
                        "    Customize JVM arguments (Non-direct modification, please use \"jvmArgs -h\" to get the relevant tutorial for modification)\n\n" +
                        "  [Game related] | gameArgs | JSON Object\n" +
                        "    Customize game arguments (Non-direct modification, please use \"gameArgs -h\" to get the relevant tutorial for modification)\n\n" +
                        "  proxyEnabled | Boolean\n" +
                        "    Whether to enable network proxy\n\n" +
                        "  proxyHost | String\n" +
                        "    Proxy Host Address\n\n" +
                        "  proxyPort | Integer\n" +
                        "    Proxy Port\n\n" +
                        "  proxyUsername | String\n" +
                        "    Proxy authentication username(optional for proxy)\n\n" +
                        "  proxyPassword | String\n" +
                        "    Proxy authentication password(optional for proxy)\n\n" +
                        "  modDownloadSource | String\n" +
                        "    Mod download source, curseforge or modrinth\n\n" +
                        "  modpackDownloadSource | String\n" +
                        "    Modpack download source, curseforge or modrinth");
        en.put("ERROR_WITH_MESSAGE", "Error: %1$s\nError Message: %2$s");
        en.put("EXCEPTION_VERSION_JSON_NOT_FOUND", "The JSON file or JAR file of the target version does not exist, please use \"-s <version>\" to select a launch-able version or \"install <version>\" to install a new version and select it.");
        en.put("EXCEPTION_VERSION_NOT_FOUND", "%s: Version does not exist");
        en.put("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND", "Cannot find the native libraries directory or it is empty, you can re-download the native library files via \"version <version> --complete=natives\" to start game.");
        en.put("EXCEPTION_MAX_MEMORY_TOO_BIG", "The maximum memory is larger than the total physical memory size");
        en.put("EXCEPTION_MAX_MEMORY_MUST_BE_GREATER_THAN_ZERO", "Maximum memory must be greater than zero");
        en.put("EXCEPTION_JAVA_VERSION_TOO_LOW", "The minimum Java version required for this version of Minecraft is %d, the Java version you have selected is %d, please select a Java that meets the requirements and try again.");
        en.put("EXCEPTION_WINDOW_SIZE_MUST_BE_GREATER_THAN_ZERO", "The width and height of the game window must be greater than zero");
        en.put("EXCEPTION_JAVA_NOT_FOUND", "Unable to launch game: the java file not found");
        en.put("EXCEPTION_READ_FILE", "Failed to read file");
        en.put("EXCEPTION_READ_FILE_WITH_PATH", "%s: Failed to read the file");
        en.put("EXCEPTION_PARSE_FILE", "Failed to parse file");
        en.put("EXCEPTION_PARSE_FILE_WITH_PATH", "Failed to parse the file \"%s\"");
        en.put("EXCEPTION_WRITE_FILE", "Failed to write content to file");
        en.put("EXCEPTION_WRITE_FILE_WITH_PATH", "Failed to write content to the file \"%s\"");
        en.put("EXCEPTION_UNABLE_PARSE", "Failed to parse");
        en.put("EXCEPTION_INSTALL_MODPACK", "Failed to install modpack: %s");
        en.put("EXCEPTION_EXECUTE_COMMAND", "Failed to execute the command");
        en.put("EXCEPTION_INCOMPLETE_VERSION", "This version is incomplete, please use \"version <version> --complete\" to complete the version before starting.");
        en.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK", "File download link not found.");
        en.put("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK_WITH_FILENAME", "The download url for the file \"%s\" could not be found.");
        en.put("EXCEPTION_VERSION_JAR_NOT_FOUND", "The jar file of target version does not exist, please re-install this version.");
        en.put("EXCEPTION_CREATE_FILE", "Failed to create file");
        en.put("EXCEPTION_CREATE_FILE_WITH_PATH", "%s: Failed to create file");
        en.put("EXCEPTION_OF_NETWORK_WITH_URL", "Network error while accessing %1$s: %2$s");
        en.put("EXCEPTION_NIDE8AUTH_JAVA_VERSION_TOO_LOW", "Unable to use nide8auth because the Java version is less than 8u101, please replace it with a Java that meets the requirements and try again.");
        en.put("EXCEPTION_GET_USER_PROPERTIES", "Failed to get user profile: %s");
        en.put("EXCEPTION_SAVE_CONFIG", "Failed to save configuration: %s");
        en.put("EXCEPTION_READ_CONFIG_FILE", "Failed to read the configuration file, please make sure the configuration file (cmcl.json) is readable and the content is correct: %s");
        en.put("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY", "Network error: If it is not the problem of the target website, there may be a problem with your proxy, please check if your network proxy is available!");
        en.put("ON_AUTHENTICATED_PAGE_TEXT", "Microsoft account authorization has been completed. Please close this page and back to the launcher to complete login.");
        en.put("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE", "Login Microsoft Account - Console Minecraft Launcher");
        en.put("CONSOLE_UNSUPPORTED_VALUE", "Unsupported value: %s");
        en.put("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE", "Please login your Microsoft account in the browser,\nIf the login is successful, back to launcher and wait for the login to complete.\nIt will take some time to login, please be patient.");
        en.put("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE", "Server not responding");
        en.put("CONSOLE_FAILED_OPERATE", "Failed to operate: ");
        en.put("CONSOLE_FILE_EXISTS", "The file \"%s\" already exists");
        en.put("CONSOLE_INCORRECT_JAVA", "Please modify a correct Java path by \"config javaPath <Java Path>\" or \"version <version> --config=javaPath <Java path>\"");
        en.put("CONSOLE_FAILED_START", "Unable to start game");
        en.put("CONSOLE_START_COMMAND", "Launch Command: ");
        en.put("CONSOLE_NO_SELECTED_VERSION", "Please use \"-s <version>\" to select a version to start, or start via \"cmcl <version>\" with the version name.");
        en.put("CONSOLE_EMPTY_LIST", "The list is empty");
        en.put("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD", "You are missing the above necessary dependent libraries to start the game. Do you want to download them?");
        en.put("CONSOLE_FAILED_LIST_VERSIONS", "Failed to get the versions list: %s");
        en.put("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", "%s: Incorrect time format or the first time is bigger than the second time.");
        en.put("CONSOLE_REPLACE_LOGGED_ACCOUNT", "You have already logged in to this account (order number is %d). Do you want to overwrite the original account?");
        en.put("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_UUID_AND_URL_AND_TOKEN", "If it is an authlib-injector account, you must have the UUID, accessToken and the address of the authlib-injector server to perform this operation, you can try to refresh your account by \"account --refresh\" or login again.");
        en.put("CONSOLE_ACCOUNT_UN_OPERABLE_MISSING_INFO", "You must be logged in with an official account, an authlib-injector account or a nide8auth account and have a UUID to perform this operation. If it is an authlib-injector account, the target server address is also required. If it is a nide8auth account, the target server ID is also required. You can try to refresh your account by \"account --refresh\" or login again.");
        en.put("CONSOLE_INPUT_INT_WRONG", "Please enter a correct number within range. ");
        en.put("CONSOLE_INPUT_STRING_NOT_FOUND", "Not found \"%s\". ");
        en.put("CONSOLE_ONLY_HELP", "Please use the option -h or --help to get the help documentation.");
        en.put("CONSOLE_IMMERSIVE_WRONG", "Incorrect command: %s. Please type help to get help documentation and read the text related to immersive mode carefully.");
        en.put("CONSOLE_IMMERSIVE_NOT_FOUND", "%s: Command not found. Please type help to get the help documentation.");
        en.put("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", "%s: Unknown command or unknown meaning. Please use the option -h or --help to get the help documentation.");
        en.put("CONSOLE_IMMERSIVE_MISSING_PARAMETER", "Missing parameter. Type help for help documentation.");
        en.put("CONSOLE_NOT_FOUND_VERSION_OR_OPTION", "Could not find a start-able version or option with the name \"%s\". You can check the information you entered with the help documentation obtained by typing the option -h or --help.");
        en.put("CONSOLE_HELP_WRONG_WRITE", "The correct format is -h or --help, without parameter value, instead of \"%s\".");
        en.put("CONSOLE_VERSION_UNCLEAR_MEANING", "The meaning of \"%s\" is unknown, is it a version? Remember to add double quotes.");
        en.put("CONSOLE_UNKNOWN_USAGE", "Unknown usage: %s. Please use the option -h or --help to get the help documentation.");
        en.put("CONSOLE_ARG_CHECKING_ONE", "%s: Option usage is wrong or should not appear here. Please use the option -h or --help to get the help documentation.");
        en.put("CONSOLE_ARG_CHECKING_PLURAL", "The following options are used incorrectly or should not be appear here. Please use the option -h or --help to get the help documentation.\n%s");
        en.put("CONSOLE_ASK_EXIT_WITH_MC", "Do you need to exit the game when exiting the launcher (can be turned on or off through \"config exitWithMinecraft true/false\")?");
        en.put("CONSOLE_ASK_PRINT_STARTUP_INFO", "Do you need to print startup information when starting the game (such as Java path, maximum memory, login account, etc., which can be turned on or off through \"config printStartupInfo true/false\")?");
        en.put("CONSOLE_ASK_CHECK_ACCOUNT", "Do you need to check whether the account is available before starting the game (it will take time before starting, you can turn it on or off through \"config checkAccountBeforeStart true/false\")?");
        en.put("CONSOLE_CHOOSE_DOWNLOAD_SOURCE_CF_OR_MR", "Please choose a download source (%d by default, stored as configuration \"modDownloadSource\"): ");
        en.put("DATATYPE_STRING", "String");
        en.put("DATATYPE_INTEGER", "Integer");
        en.put("DATATYPE_BOOLEAN", "Boolean");
        en.put("DATATYPE_FRACTION", "Fraction");
        en.put("TIME_FORMAT", "EEE, MMM d, yyyy HH:mm:ss");
        en.put("ACCOUNT_TYPE_MICROSOFT", "Microsoft Account");
        en.put("ACCOUNT_TYPE_OFFLINE", "Offline Account");
        en.put("ACCOUNT_TYPE_OAS", "authlib-injector Account");
        en.put("ACCOUNT_TYPE_NIDE8AUTH", "Nide8Auth Account");
        en.put("ACCOUNT_TYPE_NIDE8AUTH_WITH_DETAIL", "Nide8Auth Account: %s %s");
        en.put("ACCOUNT_SELECTED", "Selected");
        en.put("ACCOUNT_NOT_EXISTS", "Account does not exist: %d");
        en.put("ACCOUNT_TYPE_OAS_WITH_DETAIL", "authlib-injector Account: %s %s");
        en.put("ACCOUNT_INVALID", "Invalid Account: %d");
        en.put("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME", "Please enter the offline login player name: ");
        en.put("ACCOUNT_TIP_LOGIN_OAS_ADDRESS", "Please enter the authlib-injector login server address: ");
        en.put("ACCOUNT_TIP_LOGIN_NIDE8AUTH_SERVER_ID", "Please enter the nide8auth login server ID: ");
        en.put("ACCOUNT_LOGIN_UNKNOWN_LOGIN_METHOD", "Unknown login method: %s. Please use the option -h or --help to get the help documentation.");
        en.put("ACCOUNT_LOGIN_NEED_NAME", "Please specify -n<player name> or --name=<player name>.");
        en.put("ACCOUNT_LOGIN_NEED_ADDRESS", "Please specify --address=<server address>.");
        en.put("ACCOUNT_LOGIN_NEED_SERVER_ID", "Please specify --serverId=<server ID>.");
        en.put("ACCOUNT_MICROSOFT_REFRESH_NOT_SAME", "It seems that the account you login on the website is not the account you login locally.");
        en.put("NOT_SELECTED_AN_ACCOUNT", "No account selected. Please log in to your account, use \"account -l\" to list the accounts, remember the order number of the account you want to select, and then use \"account -s<Order Number>\" to select the account; Or add \"-s\" option after login account command.");
        en.put("DATATYPE_JSON_ARRAY", "JSON Array");
        en.put("DATATYPE_JSON_OBJECT", "JSON Object");
        en.put("INPUT_ACCOUNT", "Account: ");
        en.put("INPUT_PASSWORD", "Password: ");
        en.put("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT", "Failed to login authlib-injector account: %s");
        en.put("FAILED_TO_LOGIN_YGGDRASIL_ACCOUNT_UNAVAILABLE_SERVER", "Target server access failed");
        en.put("FAILED_TO_LOGIN_OAA_NO_SELECTED_CHARACTER", "Login failed, please select an available character and try again.");
        en.put("FAILED_TO_LOGIN_NIDE8AUTH_ACCOUNT", "Failed to login nide8auth account: %s");
        en.put("WARNING_SHOWING_PASSWORD", "Warning: Do this from a non-console and your password will not be hidden!");
        en.put("WARNING_VCFG_JAVA_INCORRECT", "Warning: The Java path of the standalone version configuration does not exist or is invalid, the global configuration value will be used by default!");
        en.put("WARNING_VCFG_MAX_MEMORY_INCORRECT", "Warning: The maximum memory of the standalone version configuration is less than or equal to zero, the global configuration value will be used by default!");
        en.put("WARNING_VCFG_WINDOW_SIZE_WIDTH_INCORRECT", "Warning: The game window width of the standalone version configuration is less than or equal to zero, the global configuration value will be used by default!");
        en.put("WARNING_VCFG_WINDOW_SIZE_HEIGHT_INCORRECT", "Warning: The game window height of the standalone version configuration is less than or equal to zero, the global configuration value will be used by default!");
        en.put("FILE_NOT_FOUND_OR_IS_A_DIRECTORY", "Target file not found or target file is a directory");
        en.put("SUCCESSFULLY_SET_SKIN", "Set skin successfully");
        en.put("UNAVAILABLE_AUTHLIB_ACCOUNT_REASON", "authlib-injector is misconfigured: %s");
        en.put("UNAVAILABLE_NIDE8AUTH_ACCOUNT_REASON", "nide8auth is misconfigured: %s");
        en.put("UNAVAILABLE_AUTHLIB_ACCOUNT", "authlib-injector account is not available, the game will use offline account.");
        en.put("UNAVAILABLE_NIDE8AUTH_ACCOUNT", "nide8auth account is not available, the game will use offline account.");
        en.put("UNAVAILABLE_CUSTOM_SKIN", "Custom skin is not available");
        en.put("PRINT_COMMAND_NOT_SUPPORT_OFFLINE_CUSTOM_SKIN", "Note: If you are using an offline account and use the command to start the game, custom skin will not be available.");
        en.put("EMPTY_UUID", "UUID is empty");
        en.put("EMPTY_PLAYERNAME", "Player name is empty");
        en.put("ONLY_OFFLINE", "This feature only supports offline accounts");
        en.put("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE", "This function is not available for Microsoft account and nide8auth account.");
        en.put("SKIN_TYPE_DEFAULT_OR_SLIM", "Do you want to set the skin model to slim (Alex)?");
        en.put("SKIN_STEVE_UNABLE_READ", "Failed to set, failed to read the Steve skin file!");
        en.put("SKIN_ALEX_UNABLE_READ", "Failed to set, failed to read the Alex skin file!");
        en.put("SKIN_STEVE_NOT_FOUND", "Failed to set, Steve skin file not found!");
        en.put("SKIN_ALEX_NOT_FOUND", "Failed to set, Alex skin file not found!");
        en.put("SKIN_CANCEL_ONLY_FOR_OFFLINE", "Unsetting the skin is only valid for offline accounts.");
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
        en.put("INSTALL_MODLOADER_SELECT", "Please enter the version of %1$s you want to install (default is %2$s): ");
        en.put("INSTALL_MODLOADER_SELECT_NOT_FOUND", "Version \"%1$s\" not found, please enter the version of %2$s you want to install (default is %3$s): ");
        en.put("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", "Would you like to continue installing the original version (without %s)?");
        en.put("INSTALL_MODLOADER_FAILED_TO_PARSE_TARGET_JSON", "Unable to install %1$s: Failed to parse target %1$s JSON.");
        en.put("INSTALL_MODLOADER_ALREADY_INSTALL", "Unable to install %1$s: The target version is installed %1$s.");
        en.put("INSTALL_MODLOADER_EMPTY_MC_VERSION", "Unable to install %1$s: Could not get the target version of the game version.");
        en.put("INSTALL_MODLOADER_FAILED_WITH_REASON", "Failed to install %s: %s");
        en.put("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", "Unable to install %1$s: The target version already has %2$s installed, %2$s and %1$s cannot coexist.");
        en.put("INSTALL_MODLOADER_FAILED_DOWNLOAD", "Unable to install %s: download file failed");
        en.put("INSTALL_MODLOADER_DOWNLOADING_FILE", "Downloading file...");
        en.put("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION_2", "Unable to install %1$s: There is no installable version of %1$s.");
        en.put("INSTALL_MODLOADER_FAILED_UNKNOWN_TYPE", "Unable to install %1$s: Unknown type of %1$s.");
        en.put("INSTALL_MODLOADER_FAILED_MC_VERSION_MISMATCH", "Unable to install %1$s: The game version of the target %1$s does not match the target game version.");
        en.put("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", "${NAME} version \"%s\" not found.");
        en.put("INSTALL_MODLOADER_SELECT_NOT_FOUND_GAME_OR_TARGET_EXTRA", "Not found target game version or ${NAME} version.");
        en.put("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", "Failed to download the mod with projectId %d: %s");
        en.put("INSTALL_MODPACK_EACH_MOD_GET_URL", "Traversing to get the download links of each mod (file), please be patient");
        en.put("INSTALL_MODPACK_COMPLETE", "Install modpack complete");
        en.put("INSTALL_MODPACK_MODRINTH_UNKNOWN_MODLOADER", "Unknown modloader: %s");
        en.put("INSTALL_OPTIFINE_INCOMPATIBLE_WITH_FORGE_17", "Unable to install OptiFine: The current game version of Forge is not compatible with OptiFine versions lower than H1 Pre2, please try a newer version of OptiFine.");
        en.put("INSTALL_SHOW_UNKNOWN_TYPE", "%s: Unknown version type. Please use the option -h or --help to get the help documentation.");
        en.put("INSTALL_COEXIST", "%1$s and %2$s cannot coexist. Please use the option -h or --help to get the help documentation.");
        en.put("INSTALL_FABRIC_API_WITHOUT_FABRIC", "How to install Fabric API without installing Fabric?");
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
        en.put("VERSION_UNKNOWN_COMPLETING", "Unknown completion: %s. Only assets, libraries, natives are supported.");
        en.put("VERSION_COMPLETE_LIBRARIES_NO_NEED_TO", "No missing libraries need to be completed.");
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
        en.put("CF_DEPENDENCIES_TIP", "This ${NAME} requires the following pre-${NAME}s to work properly, will install the following pre-${NAME}s first.");
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
        en.put("MOD_UNKNOWN_SOURCE", "%s: Unknown download source. Use option -h or --help for more information.");
        en.put("MOD_CONTAINS_BOTH_NAME_AND_ID", "-n or --name and --id cannot exist at the same time.");
        en.put("MOD_CONTAINS_BOTH_NOT_NAME_AND_ID", "Must specify -n or --name or --id. Use option -h or --help for more information.");
        en.put("MOD_SEARCH_LIMIT_GREATER_THAN_FIFTY", "If the download source is CurseForge, the maximum limit is 50.");
        en.put("MOD_ID_LIMIT_COEXIST", "You don't need the search function of -n or --name, how can you use --limit to limit the search results?");
        en.put("MOD_CONTAINS_NOTHING", "Must specify --install, --info or --url.");
        en.put("MOD_CONTAINS_TWO_OR_MORE", "Only one of --install, --info or --url can exist.");
        en.put("NO_SEARCH_RESULTS", "No search results.");
        en.put("DOWNLOAD_SOURCE_OFFICIAL", "Official");
        en.put("DOWNLOAD_SOURCE_BMCLAPI", "BMCLAPI");
        en.put("DOWNLOAD_SOURCE_MCBBS", "MCBBS");
        en.put("MODPACK_CONTAINS_TWO_OR_MORE", "Only one of --install, --info, --file or --url can exist.");
        en.put("MODPACK_CONTAINS_NOTHING", "Must specify --install, --info, --file or --url.");
        return en;
    }

    @Override
    public Map<String, String> getHelpMap() {
        Map<String, String> enHelp = new HashMap<>();
        enHelp.put("ROOT",
                "Console Minecraft Launcher " + Constants.CMCL_VERSION_NAME + ": A Launcher for Minecraft Java Edition Running On The Console\n" +
                        "\n" +
                        "Note:\n" +
                        "  Content in square brackets is optional.\n" +
                        "  A comma in an option means that both options can do the same thing.\n" +
                        "  For specifying content for options,\n" +
                        "  you can only add content directly after the abbreviated option (a hyphen) (no spaces), such as cmcl -lD:\\.minecraft,\n" +
                        "  you can only add an equal sign after the complete option (two hyphens) and then enter the content, such as cmcl --list=D:\\.minecraft,\n" +
                        "  or add a space after the two and then enter the content, such as cmcl -l D:\\.minecraft; cmcl --list D:\\.minecraft,\n" +
                        "  for details, please refer to the example after the option description.\n" +
                        "\n" +
                        "Usage:\n" +
                        "cmcl [<version>]               Start the selected version or the version specified by <version>.\n" +
                        "                                  e.g cmcl 1.19: to start 1.19;\n" +
                        "                                  If the currently selected version is 1.18.2,\n" +
                        "                                  run it directly (without parameters), it will start 1.18.2.\n" +
                        "     -h, --help                Get help documentation\n" +
                        "     -l, --list[=<game dir>]   List all game versions in current game directory or <game dir>.\n" +
                        "                                  e.g. cmcl -l; cmcl --list=D:\\.minecraft\n" +
                        "     -p, --print[=<version>]   Print the startup command for selected version or <version>.\n" +
                        "                                  e.g. cmcl -p; cmcl --print=1.19\n" +
                        "     -s, --select=<version>    Select version.\n" +
                        "     -a, --about               Show about information.\n" +
                        //"     -i, --immersive           Enter immersive mode*\n" +
                        "     -c, --check-for-updates   Check for updates\n" +
                        "     <function> <option>...    Put the function name in \"All functions\" into <function> (without hyphen), and\n" +
                        "                               put the option after the function name to perform the corresponding operation.\n" +
                        /*"\n" +
                        " * Immersive mode: In this mode, only complete options can be used to execute the above command, such as help,\n" +
                        "   list, print; execute the command below without entering \"cmcl\", such as config maxMemory 2048;\n" +
                        "   starting the game should use start [<version>].\n" +*/
                        "\n" +
                        "All functions:\n" +
                        "     function name     effect\n" +
                        "     install           Install versions\n" +
                        "     version           Version operation\n" +
                        "     account           Account operation\n" +
                        "     config            Modify launcher configuration\n" +
                        "     jvmArgs           Custom JVM arguments\n" +
                        "     gameArgs          Custom game arguments\n" +
                        "     mod               Mods searching and installation\n" +
                        "     modpack           Modpacks searching and installation\n" +
                        "    \n" +
                        "Each function can use -h or --help to get help documentation for related options.\n" +
                        "In the function help documentation, the content following the description is a usage example.");
        enHelp.put("install",
                "Install Version\n" +
                        "  Function Name: install\n" +
                        "  First usage: \n" +
                        "     install <version to be installed> <option>...  Install a version   cmcl install 1.18.2\n" +
                        "  Options:\n" +
                        "   -n, --name=<version storage name>    Indicates the name of the version store locally.\n" +
                        "                                           cmcl install 1.18.2 -n my1.18.2\n" +
                        "   -s, --select                         Select this version after installing.\n" +
                        "                                           cmcl install 1.18.2 -s\n" +
                        "   --fabric[=<Fabric version>]          Install Fabric, you can choose whether to install Fabric API and specify\n" +
                        "      [--api[=<Fabric API version>]]    its version. Not compatible with Forge, LiteLoader, OptiFine, Quilt.\n" +
                        "                                           cmcl install 1.19 --fabric=0.14.12 --api\n" +
                        "   --forge[=<Forge version>]            Install Forge\n" +
                        "                                           cmcl install 1.19 --forge\n" +
                        "   --liteloader[=<LiteLoader version>]  Install LiteLoader\n" +
                        "                                           cmcl install 1.12 --liteloader=1.12-SNAPSHOT\n" +
                        "   --optifine[=<OptiFine version>]      Install OptiFine\n" +
                        "   --quilt[=<Quilt version>]            Install Quilt. Not compatible with Forge, LiteLoader, OptiFine, Fabric.\n" +
                        "   -t, --thread=<thread count>          Set the number of threads for downloading assets (64 by default)\n" +
                        "                                           cmcl install 1.19 --thread=96\n" +
                        "   --no-assets                          Do not download assets\n" +
                        "   --no-libraries                       Do not download libraries\n" +
                        "   --no-natives                         Do not download native libraries\n" +
                        "  Second Usage: \n" +
                        "     install --show=<version type> [-t, --time=<time range>]\n" +
                        "        Show all installable versions.\n" +
                        "        Version type: All: a/all; Release: r/release; Snapshot: s/snapshot;\n" +
                        "                      Old Alpha: oa/oldAlpha; Old Beta: ob/oldBeta.\n" +
                        "        Time range format: <from year>-<from month>-<from day>/<to year>-<to month>-<to day>\n" +
                        "           cmcl install --show=all                            Show all installable versions\n" +
                        "           cmcl install --show=r                              Show all release versions\n" +
                        "           cmcl install --show=s --time=2020-05-09/2021-10-23 Show snapshot versions from May 9, 2020 to October 23, 2021");
        enHelp.put("version",
                "Version\n" +
                        "  Function Name: version\n" +
                        "  Basic usage: version [<target version>] <option>... Operate target version or selected version\n" +
                        "  Options:\n" +
                        "   --info                                View version information. cmcl version 1.19 --info\n" +
                        "   -d, --delete                          Delete the version.       cmcl version -d\n" +
                        "   --rename=<new name>                   Rename the version\n" +
                        "   --complete[=assets|libraries|natives] Complete assets, libraries or native libraries,\n" +
                        "    [-t, --thread=<thread count>]        If you don't specify which content to complete,\n" +
                        "                                         the version will be completed. When completing assets, you can also\n" +
                        "                                         specify the number of threads by specifying -t, --thread=<thread count>.\n" +
                        "                                            cmcl version 1.19 --complete\n" +
                        "                                            cmcl version --complete=assets\n" +
                        "   --config=<config name> [<content>]    Set the configuration separately for the version, use \"config --view\" to\n" +
                        "                                         view the content with \"[Game related]\" is the configuration that can be set.\n" +
                        "                                         If no <content> is entered, the global configuration will be used.\n" +
                        "   --fabric[=<Fabric version>]           Install Fabric for the version, you can specify the version.\n" +
                        "      [--api[=<Fabric API version>]]     Add --api to install Fabric API, you can also specify the version.\n" +
                        "                                            cmcl version 1.19 --fabric --api=0.58.0\n" +
                        "   --forge[=<Forge version>]             Install Forge for the version, you can specify the version.\n" +
                        "                                            cmcl version 1.19 --forge=42.0.0\n" +
                        "   --liteloader[=<LiteLoader version>]   Install LiteLoader for the version, you can specify the version.\n" +
                        "   --optifine[=<OptiFine version>]       Install OptiFine for the version, you can specify the version.\n" +
                        "   --quilt[=<Quilt version>]             Install Quilt for the version, you can specify the version.\n" +
                        "   --isolate                             Set version working directory isolation(override gameDir configuration)\n" +
                        "   --unset-isolate                       Unset version working directory isolation\n" +
                        "   -p, --print-command                   Print the startup command.\n" +
                        "   --export-script=<script file>         Export launch script (bat format under Windows, otherwise sh format)\n" +
                        "   --export-script-ps=<script file>      Export PowerShell launch script (.ps1)");
        enHelp.put("account",
                "Account\n" +
                        "  Function Name: account\n" +
                        "  Note: The order number of the account can be obtained through -l or --list.\n" +
                        "  Options:\n" +
                        "   -s, --select=<order>                     Select an account.                      cmcl account -s 3\n" +
                        "   -l, --list                               List all accounts.                      cmcl account --list\n" +
                        "   -d, --delete=<order>                     Delete an account.                      cmcl account --delete=4\n" +
                        "   -r, --refresh                            Refresh the currently selected account. cmcl account --refresh\n" +
                        "   --cape[=<cape file path>]                Custom cape, if not entered path, the cape will be unset.\n" +
                        "                                            This feature is only available for offline accounts*.\n" +
                        "   --download-skin=<skin file storage path> Download skin file.      cmcl account --download-skin=D:\\mySkin.png\n" +
                        "   --skin[=steve|alex|<skin file path>]     Set the skin to Steve, Alex, or a custom skin. If it is an offline\n" +
                        "                                            account and if you do not enter path, the skin will be unset.\n" +
                        "                                            This feature is not available for Microsoft accounts and nide8auth*.\n" +
                        "                                               cmcl account --skin\n" +
                        "                                               cmcl account --skin=steve\n" +
                        "                                               cmcl account --skin=D:\\handsome.png\n" +
                        "   --login=offline|microsoft|authlib|nide8auth [-s, --select]\n" +
                        "       Login your account (and select).\n" +
                        "         offline: To login an offline account, need to specify: -n, --name=<player name>,\n" +
                        "            cmcl account --login=offline --name=Alexander\n" +
                        "         microsoft: To login a Microsoft account, no content need to be specified,\n" +
                        "            cmcl account --login=microsoft\n" +
                        "         authlib: To login an authlib-injector account, need to specify: --address=<server address>,\n" +
                        "            cmcl account --login=authlib --address=127.0.0.1\n" +
                        "         nide8auth: To login an nide8auth account, need to specify: --serverId=<server ID>,\n" +
                        "            cmcl account --login=nide8auth --serverId=1234567890abcdef1234567890abcdef\n" +
                        "   * Some accounts do not support some functions, because there are no available APIs, please go to the corresponding website to do this by yourself.");
        enHelp.put("config",
                "Configuration\n" +
                        "  Function Name: config\n" +
                        "  Options:\n" +
                        "   <config name> [<content>]        If <content> is not empty, the configuration will be set,\n" +
                        "                                    you can use -v to view the settable configuration;\n" +
                        "                                    otherwise, output the configuration value corresponding to <config name>.\n" +
                        "                                       cmcl config javaPath        Output Java path\n" +
                        "                                       cmcl config maxMemory 2048  Modify maximum memory\n" +
                        "   -a, --all                        Output all configuration content\n" +
                        "                                       cmcl config -a\n" +
                        "   --getRaw[=<indent number>]       Output the original content of the configuration, <indent number> defaults to 2.\n" +
                        "                                       cmcl config --getRaw\n" +
                        "   -d, --delete=<config name>       Delete the configuration corresponding to <config name>.\n" +
                        "                                       cmcl config -d javaPath\n" +
                        "                                       cmcl config --delete=javaPath\n" +
                        "   -c, --clear                      Clear configuration.\n" +
                        "                                       cmcl config --clear\n" +
                        "   -v, --view                       View all settable configurations.\n" +
                        "                                       cmcl config -v");
        enHelp.put("jvmArgs",
                "Custom JVM Arguments\n" +
                        "  Function Name: jvmArgs\n" +
                        "  Options:\n" +
                        "   -p, --print[=<indent number>] [-v, --version=<version>]        Output all arguments, <indent number> is 2 by\n" +
                        "                                                                  default, and the version can be specified.\n" +
                        "                                                                     cmcl jvmArgs -p2 -v1.19\n" +
                        "   -a, --add=<content> [-v, --version=<version>]                  Add an argument, the version can be specified.\n" +
                        "                                                                  To prevent parsing errors, enclose content in double\n" +
                        "                                                                  quotes and use an equals sign to specify the content.\n" +
                        "                                                                     cmcl jvmArgs --add=\"-Dfile.encoding=UTF-8\"\n" +
                        "   -d, --delete=<order, starts from 0> [-v, --version=<version>]  Delete an argument, the version can be specified.\n" +
                        "                                                                     cmcl jvmArgs --delete=2 --version=1.19");
        enHelp.put("gameArgs",
                "Custom Game Arguments\n" +
                        "  Function Name: gameArgs\n" +
                        "  Options:\n" +
                        "   -p, --print[=<indent number>] [-v, --version=<version>]        Output all arguments, <indent number> is 2 by\n" +
                        "                                                                  default, and the version can be specified.\n" +
                        "                                                                     cmcl gameArgs --print --version=1.19\n" +
                        "   -a, --add=<config name> [<content>] [-v, --version=<version>]  Add an argument, the version can be specified.\n" +
                        "                                                                     cmcl gameArgs -a width 256\n" +
                        "   -d, --delete=<config name> [-v, --version=<version>]           Delete an argument, the version can be specified.\n" +
                        "                                                                     cmcl gameArgs --delete=demo");
        enHelp.put("mod",
                "Mod\n" +
                        "  Function Name: mod\n" +
                        "  Note: <source> can input cf or curseforge (CurseForge), mr or modrinth (Modrinth).\n" +
                        "  Options:\n" +
                        "   --url=<mod url> Download mod from Internet\n" +
                        "   --install\n" +
                        "      [--source=<source>]\n" +
                        "      -n, --name=<mod name>|--id=<mod ID>\n" +
                        "      [--limit=<limit the number of search results>]\n" +
                        "      [--game-version=<game version>]\n" +
                        "      [-v, --version=<mod version>]\n" +
                        "     Search and install mod by mod name or ID. When searching by mod name, the number\n" +
                        "     of results defaults to 50, it can be restricted. If <source> is CurseForge, the limit is 50 at most.\n" +
                        "        cmcl mod --install -nMinis --limit=30\n" +
                        "        cmcl mod --install --source=curseforge --id=297344\n" +
                        "        cmcl mod --install --name=Sodium --limit=30\n" +
                        "        cmcl mod --install --source=mr --id=GBeCx05I\n" +
                        "   --info\n" +
                        "      [--source=<source>]\n" +
                        "      -n, --name=<mod name>|--id=<mod ID>\n" +
                        "      [--limit=<limit the number of search results>]\n" +
                        "     Search and display mod information by mod name or ID. When searching by mod name, the number\n" +
                        "     of results defaults to 50, it can be restricted. If <source> is CurseForge, the limit is 50 at most.");
        enHelp.put("modpack",
                "Modpack\n" +
                        "  Function Name: modpack\n" +
                        "  Note: <source> can input cf or curseforge (CurseForge), mr or modrinth (Modrinth).\n" +
                        "        -t, --thread=<thread count>, --no-assets, --no-libraries and --no-natives are also supported when installing the modpack.\n" +
                        "        For their functions, please refer to the help documentation of \"Install Version\"(install).\n" +
                        "  Options:\n" +
                        "   --url=<modpack url> [--storage=<version storage name>]   Download and install modpack from Internet\n" +
                        "   --file=<modpack path> [--storage=<version storage name>] Install local modpack\n" +
                        "   --install\n" +
                        "      [--source=<source>]\n" +
                        "      -n, --name=<modpack name>|--id=<modpack ID>\n" +
                        "      [--limit=<limit the number of search results>]\n" +
                        "      [--storage=<version storage name>]\n" +
                        "      [-k, --keep-file]\n" +
                        "      [--game-version=<game version>]\n" +
                        "      [-v, --version=<modpack version>]\n" +
                        "     Search and install modpack by modpack name or ID. When searching by modpack name, the number\n" +
                        "     of results defaults to 50, it can be restricted. If <source> is CurseForge, the limit is 50 at most.\n" +
                        "     Adding -k or --keep-file means to keep the file (in directory .cmcl/modpacks) after installation.\n" +
                        "        cmcl modpack --install -nRLCraft --limit=30 --storage=\"New Game\"\n" +
                        "        cmcl modpack --install --source=curseforge --id=285109\n" +
                        "        cmcl modpack --install --name=\"Sugar Optimization\" --limit=30 --storage NewModpack\n" +
                        "        cmcl modpack --install --source=mr --id=BYN9yKrV\n" +
                        "   --info\n" +
                        "      [--source=<source>]\n" +
                        "      -n, --name=<modpack name>|--id=<modpack ID>\n" +
                        "      [--limit=<limit the number of search results>]\n" +
                        "     Search and display modpack information by modpack name or ID. When searching by modpack name, the number\n" +
                        "     of results defaults to 50, it can be restricted. If <source> is CurseForge, the limit is 50 at most.");
        return enHelp;
    }
}
