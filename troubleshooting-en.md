# Troubleshooting
### Why crash?
It's not crashing, it's your operation method is wrong. Open cmd, enter `cd /d`, then enter a space, then enter the location where cmcl.exe is stored (directory path, no need to enter cmcl.exe), and then enter the command in the video to perform the corresponding operation.

---

### Executing cmcl.exe says that the Java version is inappropriate?
Download cmcl.jar and start it with `java -jar cmcl.jar`.

---

### How to use CMCL in any path?
You need to add the directory path where cmcl.exe is located in the environment variable (there is no "cmcl.exe" at the end), the specific method:
1. Right-click "This PC" (or "Computer") on the desktop
2. Click Properties
3. Locate and click "Advanced system settings"
4. Click the "Environment Variables" button
5. Find and select "Path" in user variables or system variables, click Edit
6. Win10 users click "New", enter the directory path where cmcl.exe is located, and then press "OK" all the way
7. Win7 users enter a semicolon at the end of the "Variable value" item, then enter the directory path where cmcl.exe is located, and press "OK" all the way

---

### How to set version isolation?
```
cmcl version <version> --isolate
```
So how to set the working directory of the version to another directory?
Set the game directory of version "1.19.3" to `D:\wokingdirs\1.19.3`:
```
cmcl version 1.19.3 --config=gameDir D:\wokingdirs\1.19.3
```
Among them, the usage related to the version can be obtained through `cmcl version -h`, and then find the item `--config=<config name>`, I believe everyone can understand it here. Then `gameDir` can be obtained by `cmcl config -v` or found on [Configuration](README-en.md#-configurations). Essentially modifying configurations individually for versions.

---

### How to set up network proxy?
First execute the following command to configure proxy information:
```
cmcl config proxyHost <proxy host address>
cmcl config proxyPort <proxy port>
```
If you need to set username and password, you can execute the following command, but it is not necessary:
```
cmcl config proxyUsername <username for proxy authentication>
cmcl config proxyPassword <password for proxy authentication>
```
Finally, the proxy can be enabled by `cmcl config proxyEnabled true`, and disabled by `cmcl config proxyEnabled false`. </br>
Note: If you have enabled the proxy and an error occurs when performing operations that require networking, it may be that your network proxy is not available. You can check whether there is a problem with your network proxy.

---

### Can I make a "shortcut" for a command I type often?
For some commands that may be entered frequently, such as switching the download source `cmcl config downloadSource <download source>`, 
switching the proxy `cmcl config proxyEnabled true/false`, you can set the simplified command through 
`cmcl simplify -s <Simplified Command> "<Original Command>"`, for example: after entering the command 
`cmcl simplify -s pon "config proxyEnabled true"`, enter the command `cmcl pon` to quickly start the proxy. 
But be careful not to conflict with existing options and local version names. Get additional usage via `cmcl simplify -h`. 
Combined with the actual usage of users, it is recommended to set the following simplified commands:

| Simplified command (can be set freely) | Original command          | Meaning                             |
|----------------------------------------|---------------------------|-------------------------------------|
| ds0                                    | config downloadSource 0   | Set the download source to official |
| ds1                                    | config downloadSource 1   | Set the download source to BMCLAPI  |
| pon                                    | config proxyEnabled true  | Enable proxy                        |
| poff                                   | config proxyEnabled false | Disable proxy                       |
| als                                    | account --list            | List all accounts                   |
| sr                                     | install --show=r          | List all available release versions |
| ar                                     | account -r                | Refresh the current login account   |

---

### Does it support playing with others online and server opening?
Sorry, not at the moment.

---

### Is there a mobile version available?
No, because that is another category. Isn't Java cross-platform? Yes, but some game-related things in Minecraft cannot run on platforms such as Android, iOS, etc. Otherwise, why use Boardwalk and Pojav?

---

### Can I call CMCL as the core and make my own launcher?
Yes, but you must follow the [Open Source License](LICENSE), and your launcher indicates that you use CMCL, and all legal disputes and conflicts arising from this have nothing to do with me (the CMCL developer).