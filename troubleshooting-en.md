# Troubleshooting
### Why crash?
It's not crashing, it's your operation method is wrong. Open cmd, enter `cd /d`, then enter a space, then enter the location where cmcl.exe is stored (directory path, no need to enter cmcl.exe), and then enter the command in the video to perform the corresponding operation.

---

### Executing cmcl.exe says that the Java version is inappropriate?
Use `cmcl2.exe` instead of `cmcl.exe`; you can also download cmcl.jar and start it with `java -jar cmcl.jar`.

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
Among them, the usage related to the version can be obtained through `cmcl version -h`, and then find the item `--config=<config name>`, I believe everyone can understand it here. Then `gameDir` can be obtained by `cmcl config -v` or found on [Configuration](README-en.md#configurations). Essentially modifying configurations individually for versions.

---

### Does it support playing with others online and server opening?
Sorry, not at the moment.

---

### Is there a mobile version available?
No, because that is another category. Isn't Java cross-platform? Yes, but some game-related things in Minecraft cannot run on platforms such as Android, iOS, etc. Otherwise, why use Boardwalk and Pojav?

---

### Can I call CMCL as the core and make my own launcher?
Yes, but you must follow the [Open Source License](LICENSE), and your launcher indicates that you use CMCL, and all legal disputes and conflicts arising from this have nothing to do with me (the CMCL developer).