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

package com.mrshiehx.cmcl.modules.extra.liteloader;

import com.mrshiehx.cmcl.api.download.DefaultApiProvider;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.SplitLibraryName;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.modules.extra.ExtraMerger;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class LiteloaderMerger implements ExtraMerger {
    private static final String MODLOADER_NAME = "LiteLoader";

    public static Pair<Boolean, List<JSONObject>> installInternal(String minecraftVersion, String liteloaderVersionString, JSONObject headJSONObject) throws DescriptionException {
        Map<String, LiteloaderVersion> versionsOfLiteLoader;
        versionsOfLiteLoader = getVersionList(minecraftVersion);
        LiteloaderVersion liteloaderVersion = versionsOfLiteLoader.get(liteloaderVersionString);

        if (liteloaderVersion == null) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", liteloaderVersionString).replace("${NAME}", MODLOADER_NAME));
        }

        return installInternal(liteloaderVersion, headJSONObject);
    }

    /**
     * 将 LiteLoader 的JSON合并到原版JSON
     *
     * @return key: 如果无法安装 LiteLoader，是否继续安装 value:如果成功合并，则为需要安装的依赖库集合，否则为空
     **/
    @Override
    public Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File jarFile, boolean askContinue, @Nullable String extraVersion) {
        Map<String, LiteloaderVersion> versionsOfLiteLoader;

        try {
            versionsOfLiteLoader = getVersionList(minecraftVersion);
        } catch (Exception e) {
            if (Constants.isDebug()) e.printStackTrace();
            System.out.println(e.getMessage());
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
        LiteloaderVersion liteloaderVersion;

        if (isEmpty(extraVersion)) {
            if (versionsOfLiteLoader.size() == 0) {
                System.out.println(getString("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", MODLOADER_NAME));
                return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
            }

            System.out.print('[');

            List<String> liteloaderVersions = new ArrayList<>(versionsOfLiteLoader.keySet());
            for (int i = liteloaderVersions.size() - 1; i >= 0; i--) {
                String versionName = liteloaderVersions.get(i);
                if (versionName.contains(" ")) versionName = "\"" + versionName + "\"";
                System.out.print(versionName);//legal
                if (i > 0) {
                    System.out.print(", ");
                }
            }
            System.out.println(']');

            String inputLLVersion = selectLiteloaderVersion(getString("INSTALL_MODLOADER_SELECT", MODLOADER_NAME, liteloaderVersions.get(0)), versionsOfLiteLoader, liteloaderVersions.get(0));
            if (inputLLVersion == null)
                return new Pair<>(false, null);

            liteloaderVersion = versionsOfLiteLoader.get(inputLLVersion);
            if (liteloaderVersion == null)
                return new Pair<>(false, null);
        } else {
            liteloaderVersion = versionsOfLiteLoader.get(extraVersion);
            if (liteloaderVersion == null) {
                System.out.println(getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", extraVersion).replace("${NAME}", "LiteLoader"));
                return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
            }
        }

        try {
            return installInternal(liteloaderVersion, headJSONObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
    }

    private static Map<String, LiteloaderVersion> getVersionList(String minecraftVersion) throws DescriptionException {
        JSONObject versions;
        try {
            versions = new JSONObject(NetworkUtils.get(DownloadSource.getProvider().liteLoaderVersion())).optJSONObject("versions");
        } catch (Exception e) {
            //e.printStackTrace();
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("INSTALL_MODLOADER_FAILED_TO_GET_INSTALLABLE_VERSION", MODLOADER_NAME));
        }
        JSONObject version = versions.optJSONObject(minecraftVersion);
        if (version == null) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", MODLOADER_NAME));
        }
        JSONObject repository = version.optJSONObject("repo");
        String repoUrl = null;
        if (repository != null) {
            repoUrl = repository.optString("url");
        }
        if (isEmpty(repoUrl)) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, "'url' in 'repo' is empty"));
        }
        Map<String, LiteloaderVersion> versionsOfLiteLoader = new HashMap<>();
        JSONObject artefacts = version.optJSONObject("artefacts");
        if (artefacts != null) {
            JSONObject liteloader = artefacts.optJSONObject("com.mumfrey:liteloader");
            executeBranch(minecraftVersion, repoUrl, liteloader, versionsOfLiteLoader, false);
        }
        JSONObject snapshots = version.optJSONObject("snapshots");
        if (snapshots != null) {
            JSONObject liteloader = snapshots.optJSONObject("com.mumfrey:liteloader");
            executeBranch(minecraftVersion, repoUrl, liteloader, versionsOfLiteLoader, true);
        }
        return versionsOfLiteLoader;
    }

    private static Pair<Boolean, List<JSONObject>> installInternal(LiteloaderVersion liteloaderVersion, JSONObject headJSONObject) throws DescriptionException {

        String libraryName = "com.mumfrey:liteloader:" + liteloaderVersion.version;
        File libraryFile = new SplitLibraryName(/*libraryName,*/"com.mumfrey", "liteloader", liteloaderVersion.version).getPhysicalFile();
        JSONObject library = new JSONObject().put("name", libraryName).put("url", "http://dl.liteloader.com/versions/").put("downloads", new JSONObject().put("artifact", new JSONObject().put("url", liteloaderVersion.url)));
        if (libraryFile.length() <= 0) {
            try {
                System.out.print(getString("MESSAGE_DOWNLOADING_FILE", libraryFile.getName()));
                DownloadUtils.downloadFile(liteloaderVersion.url, libraryFile, new PercentageTextProgress());
            } catch (Exception e) {
                throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("INSTALL_MODLOADER_FAILED_DOWNLOAD", MODLOADER_NAME));
                //return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
            }
        }

        String minecraftArguments = headJSONObject.optString("minecraftArguments");
        headJSONObject.put("minecraftArguments", "--tweakClass " + liteloaderVersion.tweakClass + " " + minecraftArguments);


        headJSONObject.put("mainClass", "net.minecraft.launchwrapper.Launch");
        JSONObject liteloader = new JSONObject();
        liteloader.put("version", liteloaderVersion.version);
        liteloader.put("jarUrl", liteloaderVersion.url);
        headJSONObject.put("liteloader", liteloader);

        List<JSONObject> libraries = liteloaderVersion.libraries;
        libraries.add(library);

        JSONArray headLibraries = headJSONObject.optJSONArray("libraries");
        if (headLibraries == null) {
            headJSONObject.put("libraries", headLibraries = new JSONArray());
        }
        for (JSONObject library2 : libraries) {
            headLibraries.put(library2);
        }

        return new Pair<>(true, libraries);
    }

    private static void executeBranch(String gameVersion, String repoUrl, JSONObject liteloader, Map<String, LiteloaderVersion> versions, boolean snapshotBool) {
        for (Map.Entry<String, Object> entry : liteloader.toMap().entrySet()) {
            String branchName = entry.getKey();
            Object versionO = entry.getValue();
            if (!(versionO instanceof Map) || "latest".equals(branchName)) continue;
            JSONObject versionJO = new JSONObject((Map<?, ?>) versionO);
            String tweakClass = versionJO.optString("tweakClass");
            String version = versionJO.optString("version");
            String file = versionJO.optString("file");
            String url = (DownloadSource.getProvider() instanceof DefaultApiProvider) ? (repoUrl + "com/mumfrey/liteloader/" + gameVersion + "/" + file) : (DownloadSource.getProvider().thirdPartyLiteLoaderDownload() + "?version=" + version);
            if (snapshotBool) {
                try {
                    Element snapshot = (Element) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((NetworkUtils.addSlashIfMissing(repoUrl) + "com/mumfrey/liteloader/" + versionJO.optString("version") + "/") + "maven-metadata.xml").getDocumentElement().getElementsByTagName("snapshot").item(0);
                    version = version.replace("SNAPSHOT", snapshot.getElementsByTagName("timestamp").item(0).getTextContent() + "-" + snapshot.getElementsByTagName("buildNumber").item(0).getTextContent());
                    url = repoUrl + "com/mumfrey/liteloader/" + versionJO.optString("version") + "/liteloader-" + version + "-release.jar";
                } catch (Exception ignore) {
                }
            }
            List<JSONObject> libraries = JSONUtils.jsonArrayToJSONObjectList(versionJO.optJSONArray("libraries"));
            versions.put(version, new LiteloaderVersion(tweakClass, file, version, libraries, url));
        }
    }

    private static class LiteloaderVersion {
        public final String tweakClass;
        public final String file;
        public final String version;
        public final List<JSONObject> libraries;
        public final String url;

        private LiteloaderVersion(String tweakClass, String file, String version, List<JSONObject> libraries, String url) {
            this.tweakClass = tweakClass;
            this.file = file;
            this.version = version;
            this.libraries = libraries;
            this.url = url;
        }
    }

    private static String selectLiteloaderVersion(String text, Map<String, LiteloaderVersion> liteloaders, String defaulx) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        try {
            String s = scanner.nextLine();
            if (!isEmpty(s)) {
                LiteloaderVersion jsonObject = liteloaders.get(s);
                if (jsonObject != null) return s;
                return selectLiteloaderVersion(getString("INSTALL_MODLOADER_SELECT_NOT_FOUND", s, MODLOADER_NAME, defaulx), liteloaders, defaulx);
            } else {
                return /*selectLiteloaderVersion(text, liteloaders)*/defaulx;
            }
        } catch (NoSuchElementException ignore) {
            return null;
        }
    }
}
