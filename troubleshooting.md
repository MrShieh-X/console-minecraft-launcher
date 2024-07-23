# 疑难解答
### 为啥闪退？
不是闪退，是你的操作方式有误。打开cmd，输入`cd /d`，然后输入一个空格，接着输入cmcl.exe存放的位置（目录路径，不用输入cmcl.exe），之后就输入视频里的命令，以执行相应的操作。

---

### 执行cmcl.exe说 Java 版本不合适？
下载cmcl.jar，通过`java -jar cmcl.jar`来启动。

---

### 怎么做到在任何路径下都能用CMCL？ 
需要在环境变量中加入cmcl.exe所在的目录路径（最后没有“cmcl.exe”），具体做法：
1. 右键桌面上的“此电脑”（或“计算机”）
2. 点击属性
3. 找到并点击“高级系统设置”
4. 点击“环境变量”按钮
5. 在用户变量或系统变量中找到“Path”并选择，点击编辑
6. Win10 用户点击“新建”，把cmcl.exe所在的目录路径输入，然后一路按“确定”
7. Win7 用户在“变量值”一项的最后面输入一个分号，然后输入cmcl.exe所在的目录路径，然后一路按“确定”

---

### 如何设置版本隔离？
```
cmcl version <版本> --isolate
```
那怎么样把版本的工作目录设置到其他目录？
将版本“1.19.3”的游戏目录设为`D:\wokingdirs\1.19.3`：
```
cmcl version 1.19.3 --config=gameDir D:\wokingdirs\1.19.3
```
其中，与版本有关的用法可以通过`cmcl version -h`获取，然后找到`--config=<配置名称>`一项，相信大家这里看得懂。然后`gameDir`可通过`cmcl config -v`获取或者在[配置](README.md#-配置)上找得到。本质上是为版本单独修改配置。

---

### 如何设置网络代理？
先执行以下命令，配置代理信息：
```
cmcl config proxyHost <代理主机地址>
cmcl config proxyPort <代理端口>
```
如果有设置账户与密码的需要，可执行以下命令，但不是必须的：
```
cmcl config proxyUsername <代理验证的账户>
cmcl config proxyPassword <代理验证的密码>
```
最后可通过`cmcl config proxyEnabled true`来开启代理，通过`cmcl config proxyEnabled false`关闭代理。</br>
注意：如果您开启了代理，进行需要联网的操作时出错，则有可能是您的网络代理不可用，您可以检查您的网络代理是否出现了问题。

---

### 能为经常输入的命令搞个“快捷方式”吗？
对于某些可能会经常输入的命令，例如切换下载源`cmcl config downloadSource <下载源>`、开关代理`cmcl config proxyEnabled true/false`，
可以通过`cmcl simplify -s <简化命令> "<原命令>"`设置简化命令，例如：输入命令`cmcl simplify -s pon "config proxyEnabled true"`后，
输入命令`cmcl pon`即可快速开启代理。但是要注意，不要与已有的选项和本地版本名冲突。通过`cmcl simplify -h`获取其他用法。结合用户实际使用情况，建议设置以下简化命令：

| 简化命令（可随意设置） | 原命令                       | 含义            | 
|-------------|---------------------------|---------------|
| ds0         | config downloadSource 0   | 设置下载源为官方      |
| ds1         | config downloadSource 1   | 设置下载源为BMCLAPI |
| pon         | config proxyEnabled true  | 开启代理          |
| poff        | config proxyEnabled false | 关闭代理          |
| als         | account --list            | 列出所有账号        |
| sr          | install --show=r          | 列出所有可安装的正式版   |
| ar          | account -r                | 刷新当前登录账号      |

---

### 支持联机、开服吗？
抱歉，暂时不行。

---

### 能推出手机版吗？
不行，因为这属于另一个范畴了。那 Java 不是跨平台的吗？是的，但是 Minecraft 有些与游戏相关的东西不能在 Android、iOS 等平台上运行，不然的话还要 Boardwalk、Pojav 干嘛？

---

### 能套壳CMCL自己弄个启动器或调用CMCL做核心吗？
行，但是必须遵循[开源协议](LICENSE)，并且你的启动器注明用的是CMCL，由此产生的一切法律纠纷与冲突与本人（CMCL开发者）无关。
