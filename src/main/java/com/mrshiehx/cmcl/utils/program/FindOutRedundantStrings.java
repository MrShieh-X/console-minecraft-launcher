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
 *
 */

package com.mrshiehx.cmcl.utils.program;

import com.mrshiehx.cmcl.constants.Languages;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindOutRedundantStrings {
    public static void main2(String[] args) throws Exception {
        System.out.println("en:");
        main(Languages.en);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("zh:");
        main(Languages.zh);


        //clear();
    }

    /*private static void clear() {
        List<String>a= Arrays.asList(("MENU_LAUNCHER\n" +
                "MENU_GAME\n" +
                "SETTINGS_TEXTFIELD_OFFICIAL_MOJANG_PLAYERNAME_TIP_TEXT\n" +
                "SETTINGS_MENU_LANGUAGE\n" +
                "DIALOG_OFFICIAL_LOGIN_TIP_ENTER_PASSWORD_TEXT\n" +
                "DIALOG_CHANGE_SKIN_FILE_TITLE\n" +
                "MENU_COPY_COMMAND\n" +
                "DIALOG_CHOOSE_ASSETS_DIR_TITLE\n" +
                "LOGIN_METHOD_MESSAGE\n" +
                "SETTINGS_BUTTON_REFRESH_OFFICIAL_ACCOUNT_TEXT\n" +
                "MESSAGE_MANAGE_INPUT_NAME\n" +
                "APPLICATION_NAME\n" +
                "DIALOG_TITLE_FAILED_RESET_SKIN\n" +
                "DIALOG_CHANGE_SKIN_SELECT_MODEL_TITLE\n" +
                "MENU_KILL_MINECRAFT\n" +
                "DIALOG_CHOOSE_JAVA_FILE_TITLE\n" +
                "DIALOG_TEXT_RESET_SKIN\n" +
                "DIALOG_OFFICIAL_LOGIN_TIP_ENTER_ACCOUNT_TEXT\n" +
                "DIALOG_DOWNLOAD_SKIN_FILE_TYPE_TEXT\n" +
                "DIALOG_CHOOSE_TYPE_RESOURCE_PACK_DIR_TEXT\n" +
                "DIALOG_DOWNLOAD_SKIN_FILE_TITLE\n" +
                "SETTINGS_MENU_LANGUAGE_ENGLISH\n" +
                "SETTINGS_MENU_OFFICIAL_ACCOUNT_NAME\n" +
                "MESSAGE_FAILED_TO_PARSE_VERSIONS_FILE\n" +
                "SETTINGS_CHECKBOX_FULLSCREEN_TEXT\n" +
                "SETTINGS_TEXTFIELD_CUSTOM_RESOURCE_PACK_DIR_TIP_TEXT\n" +
                "SETTINGS_TEXTFIELD_OFFICIAL_MICROSOFT_PLAYERNAME_TIP_TEXT\n" +
                "SETTINGS_TEXTFIELD_GAME_WINDOW_SIZE_TIP_TEXT\n" +
                "DIALOG_DOWNLOADED_SKIN_FILE_TEXT\n" +
                "DIALOG_NOT_FOUND_NATIVES_MESSAGE\n" +
                "SETTINGS_TEXTFIELD_MAX_MEMORY_TIP_TEXT\n" +
                "MANAGE_VERSION_OPERATION_RENAME\n" +
                "APPLICATION_SHORT_NAME\n" +
                "SETTINGS_MENU_CHANGE_SKIN\n" +
                "MANAGE_VERSION_REDOWNLOAD_NATIVES\n" +
                "DIALOG_BUTTON_OK_TEXT\n" +
                "DIALOG_CHOOSE_GAME_DIR_TITLE\n" +
                "DIALOG_BUTTON_YES_TEXT\n" +
                "DIALOG_SOME_LIBRARIES_NOT_FOUND_SEE_TITLE\n" +
                "SETTINGS_BUTTON_OFFICIAL_LOGOUT_TEXT\n" +
                "MESSAGE_INSTALL_INPUT_NAME_IS_EMPTY\n" +
                "DIALOG_CHOOSE_TYPE_ASSETS_DIR_TEXT\n" +
                "MESSAGE_CHOOSE_A_OPERATION\n" +
                "SETTINGS_MENU_DOWNLOAD_SKIN\n" +
                "SETTINGS_CHECKBOX_CUSTOM_WORK_PATHS_TEXT\n" +
                "DIALOG_SOME_LIBRARIES_NOT_FOUND_MESSAGE\n" +
                "DIALOG_BUTTON_EXIT_TEXT\n" +
                "MENU_INSTALL_NEW_VERSION\n" +
                "MESSAGE_CHOOSE_A_VERSION\n" +
                "MENU_MANAGE\n" +
                "MENU_ABOUT_NAME\n" +
                "DIALOG_SOME_LIBRARIES_NOT_FOUND_DOWNLOAD\n" +
                "SETTINGS_TEXTFIELD_CUSTOM_ASSETS_DIR_TIP_TEXT\n" +
                "SETTINGS_BUTTON_OFFICIAL_LOGIN_TEXT\n" +
                "DIALOG_OFFICIAL_REFRESHED_TITLE\n" +
                "DIALOG_SOME_LIBRARIES_NOT_FOUND_SEE\n" +
                "SETTINGS_BUTTON_BROWSE_TEXT\n" +
                "DIALOG_NO_VERSIONS_MESSAGE\n" +
                "MENU_VERSION\n" +
                "SETTINGS_TEXTFIELD_OFFLINE_PLAYERNAME_TIP_TEXT\n" +
                "SETTINGS_MENU_RESET_SKIN\n" +
                "DIALOG_CHOOSE_RESOURCE_PACK_DIR_TITLE\n" +
                "SETTINGS_TEXTFIELD_JAVA_PATH_TIP_TEXT\n" +
                "DIALOG_CHOOSE_TYPE_GAME_DIR_TEXT\n" +
                "MANAGE_VERSION_OPERATION_DELETE\n" +
                "DIALOG_DOWNLOAD_LIBRARIES\n" +
                "SETTINGS_TEXTFIELD_CUSTOM_GAME_DIR_TIP_TEXT\n" +
                "SETTINGS_BROSE_FILE_TYPE_TEXT\n" +
                "DIALOG_CHOOSE_JAVA_EXE_FILE_TITLE\n" +
                "BUTTON_START_NAME\n" +
                "LOGIN_METHOD_MOJANG\n" +
                "DIALOG_CHANGED_SKIN_FILE_TEXT\n" +
                "SETTINGS_TEXTFIELD_OFFICIAL_PLAYERNAME_TIP_TEXT\n" +
                "SETTINGS_TEXTFIELD_OS_MAX_MEMORY_TIP_TEXT\n" +
                "SETTINGS_MENU_LANGUAGE_SIMPLIFIED_CHINESE\n" +
                "DIALOG_LOGOUT_MESSAGE\n" +
                "DIALOG_OFFICIAL_LOGIN_FAILED_NORESPONSE_TEXT\n" +
                "LOGIN_METHOD_MICROSOFT\n" +
                "SETTINGS_BUTTON_SAVE_TEXT\n" +
                "DIALOG_BUTTON_CANCEL_TEXT\n" +
                "DIALOG_OFFICIAL_LOGIN_ERROR_INVALID_AOP_TEXT\n" +
                "MENU_SETTINGS_NAME\n" +
                "MESSAGE_NOT_FOUND_JAVA").split("\n"));

        List<String>b=Arrays.asList(("enUS.put(\"APPLICATION_NAME\", \"Console Minecraft Launcher\");\n" +
                "            enUS.put(\"APPLICATION_SHORT_NAME\", \"CMCL\");\n" +
                "            enUS.put(\"BUTTON_START_NAME\", \"Start Game\");\n" +
                "            enUS.put(\"MENU_SETTINGS_NAME\", \"Settings\");\n" +
                "            enUS.put(\"MENU_ABOUT_NAME\", \"About\");\n" +
                "            enUS.put(\"MENU_KILL_MINECRAFT\", \"Terminate Minecraft Process\");\n" +
                "            enUS.put(\"MENU_INSTALL_NEW_VERSION\", \"Install a New Version\");\n" +
                "            enUS.put(\"MENU_COPY_COMMAND\", \"Copy Launch Command\");\n" +
                "            enUS.put(\"MENU_MANAGE\", \"Manage Versions\");\n" +
                "            enUS.put(\"MENU_LAUNCHER\", \"Launcher\");\n" +
                "            enUS.put(\"MENU_GAME\", \"Game\");\n" +
                "            enUS.put(\"MENU_VERSION\", \"Version\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_MAX_MEMORY_TIP_TEXT\", \"Maximum Memory\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_JAVA_PATH_TIP_TEXT\", \"Java Path\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_GAME_WINDOW_SIZE_TIP_TEXT\", \"Game Window Size\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_CUSTOM_GAME_DIR_TIP_TEXT\", \"Game Directory\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_CUSTOM_ASSETS_DIR_TIP_TEXT\", \"Assets Directory\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_CUSTOM_RESOURCE_PACK_DIR_TIP_TEXT\", \"Resource Pack Directory\");\n" +
                "            enUS.put(\"SETTINGS_CHECKBOX_FULLSCREEN_TEXT\", \"Fullscreen\");\n" +
                "            enUS.put(\"SETTINGS_CHECKBOX_CUSTOM_WORK_PATHS_TEXT\", \"Custom Working Directories\");\n" +
                "            enUS.put(\"SETTINGS_BUTTON_BROWSE_TEXT\", \"Browse\");\n" +
                "            enUS.put(\"SETTINGS_BUTTON_SAVE_TEXT\", \"Save\");\n" +
                "            enUS.put(\"SETTINGS_BROSE_FILE_TYPE_TEXT\", \"%s File\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_OS_MAX_MEMORY_TIP_TEXT\", \"Max: %sMB\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_OFFLINE_PLAYERNAME_TIP_TEXT\", \"Player Name\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_OFFICIAL_PLAYERNAME_TIP_TEXT\", \"Official Account\");\n" +
                "            enUS.put(\"SETTINGS_BUTTON_OFFICIAL_LOGOUT_TEXT\", \"Logout\");\n" +
                "            enUS.put(\"SETTINGS_BUTTON_OFFICIAL_LOGIN_TEXT\", \"Login\");\n" +
                "            enUS.put(\"SETTINGS_BUTTON_REFRESH_OFFICIAL_ACCOUNT_TEXT\", \"Refresh Official Account\");\n" +
                "            enUS.put(\"SETTINGS_MENU_DOWNLOAD_SKIN\", \"Download Skin\");\n" +
                "            enUS.put(\"SETTINGS_MENU_CHANGE_SKIN\", \"Change Skin\");\n" +
                "            enUS.put(\"SETTINGS_MENU_OFFICIAL_ACCOUNT_NAME\", \"Official Account\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_OFFICIAL_MOJANG_PLAYERNAME_TIP_TEXT\", \"Mojang Account\");\n" +
                "            enUS.put(\"SETTINGS_TEXTFIELD_OFFICIAL_MICROSOFT_PLAYERNAME_TIP_TEXT\", \"Microsoft Account\");\n" +
                "            enUS.put(\"SETTINGS_MENU_RESET_SKIN\", \"Reset Skin\");\n" +
                "            enUS.put(\"SETTINGS_MENU_LANGUAGE\", \"Language(need restart)\");\n" +
                "            enUS.put(\"SETTINGS_MENU_LANGUAGE_ENGLISH\", \"English\");\n" +
                "            enUS.put(\"SETTINGS_MENU_LANGUAGE_SIMPLIFIED_CHINESE\", \"\\u7b80\\u4f53\\u4e2d\\u6587\");\n" +
                "            enUS.put(\"DIALOG_DOWNLOAD_SKIN_FILE_TYPE_TEXT\", \"Skin File\");\n" +
                "            enUS.put(\"DIALOG_DOWNLOAD_SKIN_FILE_TITLE\", \"Please select a file to store the skin file\");\n" +
                "            enUS.put(\"DIALOG_BUTTON_CANCEL_TEXT\", \"Cancel\");\n" +
                "            enUS.put(\"DIALOG_BUTTON_OK_TEXT\", \"OK\");\n" +
                "            enUS.put(\"DIALOG_BUTTON_YES_TEXT\", \"Yes\");\n" +
                "            enUS.put(\"DIALOG_BUTTON_EXIT_TEXT\", \"Exit\");\n" +
                "            enUS.put(\"DIALOG_ABOUT_DESCRIPTION\", \"Console Minecraft Launcher %1$s\\nA Launcher of Minecraft Java Edition\\nSource code repository: https://www.github.com/MrShieh-X/console-minecraft-launcher\\n%2$s\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_JAVA_FILE_TITLE\", \"Please select the java file\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_JAVA_EXE_FILE_TITLE\", \"Please select the java.exe file\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_GAME_DIR_TITLE\", \"Please select a custom game directory\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_ASSETS_DIR_TITLE\", \"Please select a custom assets directory\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_RESOURCE_PACK_DIR_TITLE\", \"Please select a custom resource pack directory\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_TYPE_GAME_DIR_TEXT\", \"Game Directory (.minecraft)\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_TYPE_ASSETS_DIR_TEXT\", \"Assets Directory (assets)\");\n" +
                "            enUS.put(\"DIALOG_CHOOSE_TYPE_RESOURCE_PACK_DIR_TEXT\", \"Resource Packs Directory (resourcepacks)\");\n" +
                "            enUS.put(\"DIALOG_TITLE_NOTICE\", \"Notice\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_TIP_ENTER_ACCOUNT_TEXT\", \"Please enter an account (email address):\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_TIP_ENTER_PASSWORD_TEXT\", \"Please enter the password of the account:\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_FAILED_TITLE\", \"Failed to login the official account\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_FAILED_TEXT\", \"Error: %1$s\\nError Message: %2$s\");\n" +
                "            enUS.put(\"DIALOG_LOGOUT_MESSAGE\", \"Are you sure you want to logout?\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGINED_TITLE\", \"Login into the official account successfully\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_FAILED_NORESPONSE_TEXT\", \"The server is not responding.\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_REFRESHED_TITLE\", \"Refresh the official account successfully\");\n" +
                "            enUS.put(\"DIALOG_DOWNLOADED_SKIN_FILE_TEXT\", \"Download the skin file successfully\");\n" +
                "            enUS.put(\"DIALOG_CHANGE_SKIN_FILE_TITLE\", \"Please select a skin file to upload\");\n" +
                "            enUS.put(\"DIALOG_CHANGE_SKIN_SELECT_MODEL_TITLE\", \"Please select a character model:\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_ERROR_INVALID_AOP_TEXT\", \"Incorrect account or password\");\n" +
                "            enUS.put(\"DIALOG_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT\", \"You did not set a skin\");\n" +
                "            enUS.put(\"DIALOG_CHANGED_SKIN_FILE_TEXT\", \"Change the skin file successfully\");\n" +
                "            enUS.put(\"DIALOG_UNABLE_TO_LOGIN_MICROSOFT\", \"Unable to login, please try again later.\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_LOGIN_FAILED_MESSAGE\", \"Login to your account failed, probably because your account does not have Minecraft.\");\n" +
                "            enUS.put(\"DIALOG_OFFICIAL_FAILED_REFRESH_TITLE\", \"Failed to refresh official account\");\n" +
                "            enUS.put(\"DIALOG_TITLE_FAILED_RESET_SKIN\", \"Failed to reset\");\n" +
                "            enUS.put(\"DIALOG_TEXT_RESET_SKIN\", \"Successfully reset\");\n" +
                "            enUS.put(\"DIALOG_NO_VERSIONS_MESSAGE\", \"No versions\");\n" +
                "            enUS.put(\"DIALOG_SOME_LIBRARIES_NOT_FOUND_MESSAGE\", \"Some Minecraft dependency libraries could not be found. If you want to continue starting Minecraft, please download these dependent libraries.\");\n" +
                "            enUS.put(\"DIALOG_SOME_LIBRARIES_NOT_FOUND_SEE\", \"View them\");\n" +
                "            enUS.put(\"DIALOG_SOME_LIBRARIES_NOT_FOUND_DOWNLOAD\", \"Download them\");\n" +
                "            enUS.put(\"DIALOG_SOME_LIBRARIES_NOT_FOUND_SEE_TITLE\", \"Not Found Dependency Libraries\");\n" +
                "            enUS.put(\"DIALOG_DOWNLOAD_LIBRARIES\", \"Downloading dependency libraries\");\n" +
                "            enUS.put(\"DIALOG_NOT_FOUND_NATIVES_MESSAGE\", \"Unable to start Minecraft because the native dependency libraries directory doesn't exist or it's empty.\\nWould you like to download native dependency libraries?\");\n" +
                "            enUS.put(\"MESSAGE_NOT_FOUND_GAME_DIR\", \"Game directory not found\");\n" +
                "            enUS.put(\"MESSAGE_NOT_FOUND_JAVA\", \"Please enter the correct Java path in the settings\");\n" +
                "            enUS.put(\"MESSAGE_STARTING_GAME\", \"Starting game...\");\n" +
                "            enUS.put(\"MESSAGE_FINISHED_GAME\", \"Game finished\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_CONNECT_TO_LAUNCHERMETA\", \"Failed to connect to https://launchermeta.mojang.com, please check your network connection.\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_DOWNLOAD_VERSIONS_FILE\", \"Failed to download the versions file, please check your network connection.\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_PARSE_VERSIONS_FILE\", \"Failed to parse the versions file: %s\");\n" +
                "            enUS.put(\"MESSAGE_VERSIONS_LIST_IS_EMPTY\", \"The versions list is empty\");\n" +
                "            enUS.put(\"MESSAGE_CHOOSE_A_VERSION\", \"Please choose a version:\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_INPUT_NAME\", \"Please enter a name of the new version:\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_INPUT_NAME_EXISTS\", \"The name \\\"%s\\\" already exists, please change a name:\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_INPUT_NAME_IS_EMPTY\", \"The name entered is empty, please re-enter:\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE\", \"Failed to download or parse the target version of the JSON file: %s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO\", \"The download information of the client file in the JSON file of the target version not found.\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY\", \"The download url of the client file is empty.\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_INSTALL_NEW_VERSION\", \"Failed to install the new version: %s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADING_JAR_FILE\", \"Downloading the client file...\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADED_JAR_FILE\", \"Download the client file complete\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADING_ASSETS\", \"Downloading the asset files...\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADED_ASSETS\", \"Download the asset files complete\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX\", \"Failed to download the asset files, the asset files index not found.\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS\", \"Failed to download the asset files: %s\");\n" +
                "            enUS.put(\"MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL\", \"Cannot find download url\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_DOWNLOAD_FILE\", \"Failed to download the file \\\"%s\\\"\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADING_LIBRARIES\", \"Downloading the library files...\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DOWNLOADED_LIBRARIES\", \"Download the library files complete\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES\", \"Failed to download the library files: %s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY\", \"The libraries list is empty\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY\", \"Failed to download the library file \\\"%1$s\\\": %2$s\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_DECOMPRESS_FILE\", \"Failed to decompress file \\\"%1$s\\\": %2$s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES\", \"Decompressing the native library files...\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES\", \"Decompress the native library files complete\");\n" +
                "            enUS.put(\"MESSAGE_FAILED_TO_COPY_FILE\", \"Failed to copy file \\\"%1$s\\\" to \\\"%2$s\\\": %3$s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALLED_NEW_VERSION\", \"Install the new version complete\");\n" +
                "            enUS.put(\"MESSAGE_UUID_ACCESSTOKEN_EMPTY\", \"UUID or accessToken is empty\");\n" +
                "            enUS.put(\"MESSAGE_CHOOSE_A_OPERATION\", \"Please choose an operation:\");\n" +
                "            enUS.put(\"MESSAGE_MANAGE_INPUT_NAME\", \"Please input a new name:\");\n" +
                "            enUS.put(\"MESSAGE_DOWNLOADING_FILE\", \"Downloading %s\");\n" +
                "            enUS.put(\"MESSAGE_COPYING_FILE\", \"Copying %s to %s\");\n" +
                "            enUS.put(\"MESSAGE_UNZIPPING_FILE\", \"Decompressing %s\");\n" +
                "            enUS.put(\"MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES\", \"Copy the native dependency libraries complete\");\n" +
                "            enUS.put(\"MESSAGE_GAME_CRASH_CAUSE_TIPS\", \"Game crash possible error: %s\");\n" +
                "            enUS.put(\"MESSAGE_GAME_CRASH_CAUSE_URLCLASSLOADER\", \"Older versions of Minecraft may have this error because the Java version is too high, Java 8 and below can be used to fix this.\");\n" +
                "            enUS.put(\"MESSAGE_GAME_CRASH_CAUSE_LWJGL_FAILED_LOAD\", \"This error may occur because some of the native dependency library are missing or damaged, please re-download native dependency libraries via \\\"-version -n <version name>\\\" to fix this problem.\");\n" +
                "            enUS.put(\"MESSAGE_GAME_CRASH_CAUSE_MEMORY_TOO_SMALL\", \"The memory is too small, you can try to go to the settings to adjust the memory to a larger number.\");\n" +
                "            enUS.put(\"MESSAGE_REDOWNLOADED_NATIVES\", \"Download native dependency libraries complete\");\n" +
                "            enUS.put(\"EXCEPTION_VERSION_JSON_NOT_FOUND\", \"The JSON file of the target startup version does not exist\");\n" +
                "            enUS.put(\"EXCEPTION_VERSION_NOT_FOUND\", \"The target startup version does not exist\");\n" +
                "            enUS.put(\"EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND\", \"Cannot find the native libraries directory or it is empty, you can re-download the native library files via \\\"-version -n <version name>\\\" to start game.\");\n" +
                "            enUS.put(\"EXCEPTION_MAX_MEMORY_TOO_BIG\", \"The maximum memory is larger than the total physical memory size\");\n" +
                "            enUS.put(\"EXCEPTION_MAX_MEMORY_IS_ZERO\", \"The maximum memory is 0\");\n" +
                "            enUS.put(\"EXCEPTION_JAVA_VERSION_TOO_LOW\", \"The minimum Java version required for this version of Minecraft is %d, the Java version you have selected is %d, please go to settings to select a Java that meets the requirements and try again.\");\n" +
                "            enUS.put(\"LOGIN_METHOD_MOJANG\", \"Mojang Account\");\n" +
                "            enUS.put(\"LOGIN_METHOD_MICROSOFT\", \"Microsoft Account\");\n" +
                "            enUS.put(\"LOGIN_METHOD_MESSAGE\", \"Please choose an account to be logged in:\");\n" +
                "            enUS.put(\"LOGIN_MICROSOFT_WAIT_FOR_RESPONSE\", \"Please login your Microsoft account in the browser,\\nIf the login is successful, back to launcher and wait for the login to complete.\\nIt will take some time to login, please be patient.\\nPress the button below to cancel login.\");\n" +
                "            enUS.put(\"ON_AUTHENTICATED_PAGE_TEXT\", \"Microsoft account authorization has been completed. Please close this page and back to the launcher to complete login.\");\n" +
                "            enUS.put(\"WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE\", \"Login Microsoft Account - Console Minecraft Launcher\");\n" +
                "            enUS.put(\"MANAGE_VERSION_OPERATION_DELETE\", \"Delete\");\n" +
                "            enUS.put(\"MANAGE_VERSION_OPERATION_RENAME\", \"Rename\");\n" +
                "            enUS.put(\"MANAGE_VERSION_REDOWNLOAD_NATIVES\", \"Re-download native dependency libraries\");\n" +
                "            enUS.put(\"CONSOLE_GET_USAGE\", \"Use the option -usage or -help to get the usage manual.\");\n" +
                "            enUS.put(\"CONSOLE_UNKNOWN_OPTION\", \"Unknown option: %s\\nUse the option -usage or -help to get the usage manual.\");\n" +
                "            enUS.put(\"CONSOLE_INCORRECT_USAGE\", \"Incorrect usage.\\nUse the option -usage or -help to get the usage manual.\");\n" +
                "            enUS.put(\"CONSOLE_UNSUPPORTED_VALUE\", \"Unsupported value: %s\");\n" +
                "            enUS.put(\"CONSOLE_REPLACE_ACCOUNT\", \"Overwrite the original account?\");\n" +
                "            enUS.put(\"CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE\", \"Please login your Microsoft account in the browser,\\nIf the login is successful, back to launcher and wait for the login to complete.\\nIt will take some time to login, please be patient.\");\n" +
                "            enUS.put(\"CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN\", \"You must be logged in with a official account and have accessToken to perform this operation.\");\n" +
                "            enUS.put(\"CONSOLE_ACCOUNT_UN_OPERABLE_UUID\", \"You must be logged in with a official account and have UUID to perform this operation.\");\n" +
                "            enUS.put(\"CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE\", \"Server not responding\");\n" +
                "            enUS.put(\"CONSOLE_FAILED_OPERATE\", \"Failed to operate: \");\n" +
                "            enUS.put(\"CONSOLE_FILE_EXISTS\", \"The file \\\"%s\\\" already exists\");\n" +
                "            enUS.put(\"CONSOLE_INCORRECT_JAVA\", \"Please modify a correct Java path by \\\"-config -s -t s -n javaPath -v <Java Path>\\\"\");\n" +
                "            enUS.put(\"CONSOLE_FAILED_START\", \"Unable to start game\");\n" +
                "            enUS.put(\"CONSOLE_START_COMMAND\", \"Launch Command: \");\n" +
                "            enUS.put(\"CONSOLE_NO_SELECTED_VERSION\",\"Please select a version by \\\"-s <Version Name>\\\"\");\n" +
                "            enUS.put(\"CONSOLE_EMPTY_LIST\",\"The list is empty\");\n" +
                "            enUS.put(\"CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD\",\"You are missing the above necessary dependent libraries to start the game. Do you want to download them?\");").split(";\n"));
        List<String>ne=new LinkedList<>();
        for(String s:b){
            boolean allow=true;
            for(String s1:a){
                if(s.startsWith("            enUS.put(\""+s1)){
                    allow=false;
                    break;
                }
            }
            if(allow){
                ne.add(s+";");
            }
        }
        for(String s:ne){
            System.out.println(s);
        }


    }*/

    public static void main(Map<String, String> map) throws Exception {
        File dir = new File("src/main/java");
        List<File> files = new LinkedList<>();
        tj(files, dir);
        List<String> contents = new LinkedList<>();
        for (File file : files) {
            if (!file.getName().equals("Languages.java") && !file.getName().equals("ClearRedundantStrings.java"))
                contents.add(readFileContent(file, false));
        }

        List<String> finall = new LinkedList<>();
        for (String name : map.keySet()) {
            boolean contains = false;
            for (String content : contents) {
                if (content.contains(name)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) finall.add(name);
        }
        for (String s : finall) {
            System.out.println(s);
        }
    }

    public static void modifyFile(File file, String content, boolean append) throws IOException {
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, append);
        //BufferedWriter writer = new BufferedWriter(fileWriter);
        fileWriter.append(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static String readFileContent(File file, boolean huanhang) throws IOException {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
            if (huanhang) sbf.append('\n');
        }
        reader.close();
        return sbf.toString();
    }


    static void tj(List<File> list, File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File file1 : files) {
                        if (file1.isFile()) {
                            if (!file1.getName().equals("strings.xml") && !file1.getName().endsWith(".png"))
                                list.add(file1);
                        } else {
                            tj(list, file1);
                        }
                    }
                }
            }
        }
    }
}
