#include "stdafx.h"
#include "main.h"
#include "os.h"
#include "java.h"
#include "lang.h"
#include <cstdlib>
#include <direct.h>
#pragma warning(disable : 4996)

Version J8(TEXT("8"));

extern "C" {
_declspec(dllexport) DWORD NvOptimusEnablement = 0x00000001;
_declspec(dllexport) DWORD AmdPowerXpressRequestHighPerformance = 0x00000001;
}

char *wstring2chars(const std::wstring wStr) {
  const wchar_t *input = wStr.c_str();

  // Count required buffer size (plus one for null-terminator).
  size_t size = (wcslen(input) + 1) * sizeof(wchar_t);
  char *buffer = new char[size];

#ifdef __STDC_LIB_EXT1__
  // wcstombs_s is only guaranteed to be available if __STDC_LIB_EXT1__ is
  // defined
  size_t convertedSize;
  std::wcstombs_s(&convertedSize, buffer, size, input, size);
#else
  std::wcstombs(buffer, input, size);
#endif
  return buffer;
}

std::wstring chars2Wstring(const char *chars)  //返回值类型是wstring类型
{
  int unicodeLen =
      ::MultiByteToWideChar(CP_ACP, 0, chars, -1, NULL, 0);  //获取字符串长度

  wchar_t *pUnicode = new wchar_t[unicodeLen + 1];  //开辟宽字节内存
  memset(pUnicode, 0, (unicodeLen + 1) * sizeof(wchar_t));  //清空

  ::MultiByteToWideChar(CP_ACP, 0, chars, -1, (LPWSTR)pUnicode,
                        unicodeLen);           //转换
  std::wstring wString = (wchar_t *)pUnicode;  //强转后赋值给返回变量
  delete[] pUnicode;
  return wString;
}

std::wstring CurrentWorkingDirectory() {
  char buff[500];
  _getcwd(buff, 500);
  return chars2Wstring(buff);
}

LPCWSTR VENDOR_DIRS[] = {L"Java",  L"Microsoft",          L"BellSoft",
                         L"Zulu",  L"Eclipse Foundation", L"AdoptOpenJDK",
                         L"Semeru"};

void RawLaunchJVM(const std::wstring &javaPath, const std::wstring &workdir,
                  const std::wstring &jarPath, int argc, char *argv[]) {
  std::wstring command;

  std::wstring dqm = L"\"";
  std::wstring space = L" ";

  command = /*dqm+*/ dqm + javaPath + dqm + space + dqm + L"-jar" + dqm +
            space + dqm + jarPath + dqm;
  for (int i = 1; i < argc; i++) {
    command = command + space + dqm + chars2Wstring(argv[i]) + dqm;
  }
  // command = command + dqm;
  // std::cout << wstring2chars(command) << std::endl;
  PROCESS_INFORMATION pi;
  if (MyCreateProcess(command, workdir, &pi)) {
    WaitForSingleObject(pi.hProcess, INFINITE);
    exit(EXIT_SUCCESS);
  }
}

void LaunchJVM(const std::wstring &javaPath, const std::wstring &workdir,
               const std::wstring &jarPath, int argc, char *argv[]) {
  Version javaVersion(L"");
  if (!MyGetFileVersionInfo(javaPath, javaVersion)) return;

  if (J8 <= javaVersion) {
    RawLaunchJVM(javaPath, workdir, jarPath, argc, argv);
  }
}

void FindJavaInDirAndLaunchJVM(const std::wstring &baseDir,
                               const std::wstring &workdir,
                               const std::wstring &jarPath, int argc,
                               char *argv[]) {
  std::wstring pattern = baseDir + L"*";

  WIN32_FIND_DATA data;
  HANDLE hFind =
      FindFirstFile(pattern.c_str(), &data);  // Search all subdirectory

  if (hFind != INVALID_HANDLE_VALUE) {
    do {
      std::wstring java =
          baseDir + data.cFileName + std::wstring(L"\\bin\\java.exe");
      if (FindFirstFileExists(java.c_str(), 0)) {
        LaunchJVM(java, workdir, jarPath, argc, argv);
      }
    } while (FindNextFile(hFind, &data));
    FindClose(hFind);
  }
}

int main(int argc, char *argv[]) {
  std::wstring path, exePath;

  // Since Jar file is appended to this executable, we should first get the
  // location of JAR file.
  if (ERROR_SUCCESS != MyGetModuleFileName(NULL, exePath)) return 1;

  std::wstring workdir = CurrentWorkingDirectory();
  /*size_t last_slash = exePath.find_last_of(L"/\\");
  if (last_slash != std::wstring::npos && last_slash + 1 < exePath.size()) {
    workdir = exePath.substr(0, last_slash);
    // exePath = exePath.substr(last_slash + 1);
  }*/

  // Try java in PATH
  RawLaunchJVM(L"java", workdir, exePath, argc, argv);

  /*OSVERSIONINFOEX osvi;
  DWORDLONG dwlConditionMask = 0;
  int op = VER_GREATER_EQUAL;

  // Initialize the OSVERSIONINFOEX structure.
  ZeroMemory(&osvi, sizeof(OSVERSIONINFOEX));
  osvi.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);
  osvi.dwMajorVersion = 6;
  osvi.dwMinorVersion = 1;

  // Initialize the condition mask.
  VER_SET_CONDITION(dwlConditionMask, VER_MAJORVERSION, op);
  VER_SET_CONDITION(dwlConditionMask, VER_MINORVERSION, op);

  // Try downloading Java on Windows 7 or later
  bool isWin7OrLater = VerifyVersionInfo(&osvi, VER_MAJORVERSION |
  VER_MINORVERSION, dwlConditionMask);*/

  // Find Java in environment variables and Registry
  if (FindJava(path))
    LaunchJVM(path + L"\\bin\\java.exe", workdir, exePath, argc, argv);

  std::wstring programFiles;

  // Or we try to search Java in C:\Program Files
  if (!SUCCEEDED(MySHGetFolderPath(CSIDL_PROGRAM_FILES, programFiles)))
    programFiles = L"C:\\Program Files\\";
  for (LPCWSTR vendorDir : VENDOR_DIRS) {
    std::wstring dir = programFiles;
    MyPathAppend(dir, vendorDir);
    MyPathAddBackslash(dir);

    FindJavaInDirAndLaunchJVM(dir, workdir, exePath, argc, argv);
  }

  // Consider C:\Program Files (x86)
  if (!SUCCEEDED(MySHGetFolderPath(CSIDL_PROGRAM_FILESX86, programFiles)))
    programFiles = L"C:\\Program Files (x86)\\";
  for (LPCWSTR vendorDir : VENDOR_DIRS) {
    std::wstring dir = programFiles;
    MyPathAppend(dir, vendorDir);
    MyPathAddBackslash(dir);

    FindJavaInDirAndLaunchJVM(dir, workdir, exePath, argc, argv);
  }

  SYSTEM_INFO systemInfo;
  GetNativeSystemInfo(&systemInfo);
  // TODO: check whether the bundled JRE is valid.
  // Try the Java packaged together.
  bool isX64 =
      (systemInfo.wProcessorArchitecture == PROCESSOR_ARCHITECTURE_AMD64);
  bool isARM64 =
      (systemInfo.wProcessorArchitecture == PROCESSOR_ARCHITECTURE_ARM64);

  /*if (isARM64) {
    RawLaunchJVM(L"jre-arm64\\bin\\java.exe", workdir, exePath, argc ,argv);
  }
  if (isX64) {
    RawLaunchJVM(L"jre-x64\\bin\\java.exe", workdir, exePath, argc, argv);
  }
  RawLaunchJVM(L"jre-x86\\bin\\java.exe", workdir, exePath, argc, argv);*/

  std::wstring cmclJavaDir;
  {
    std::wstring buffer;
    if (SUCCEEDED(MySHGetFolderPath(CSIDL_APPDATA, buffer)) ||
        SUCCEEDED(MySHGetFolderPath(CSIDL_PROFILE, buffer))) {
      MyPathAppend(buffer, L".cmcl");
      MyPathAppend(buffer, L"java");
      if (isARM64) {
        MyPathAppend(buffer, L"windows-arm64");
      } else if (isX64) {
        MyPathAppend(buffer, L"windows-x86_64");
      } else {
        MyPathAppend(buffer, L"windows-x86");
      }
      MyPathAddBackslash(buffer);
      cmclJavaDir = buffer;
    }
  }

  if (!cmclJavaDir.empty()) {
    FindJavaInDirAndLaunchJVM(cmclJavaDir, workdir, exePath, argc, argv);
  }

error:
  LPCWSTR downloadLink;

  downloadLink = L"https://www.java.com/download/";

  bool useChinese = GetUserDefaultUILanguage() == 2052;  // zh-CN
  std::cout << (useChinese ? ERROR_PROMPT_ZH : ERROR_PROMPT) << std::endl;
  ShellExecute(0, 0, downloadLink, 0, 0, SW_SHOW);

  return 1;
}
