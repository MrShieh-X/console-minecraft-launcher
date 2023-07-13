package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.SingleArgument;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.CMCL.getString;

public class SimplifyCommandFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        //因为参数内容可能会被误判，并且好像没有检测的必要，所以不检测

        JSONObject config = Utils.getConfig();
        JSONObject simplifyCommands = config.optJSONObject("simplifyCommands");
        if (simplifyCommands == null) config.put("simplifyCommands", simplifyCommands = new JSONObject());

        Argument firstArg = arguments.optArgument(1);
        if (firstArg instanceof SingleArgument) {
            String firstKey = firstArg.key;
            if (firstKey.equals("p") || firstKey.equals("print")) {
                System.out.println(simplifyCommands.toString(2));
            } else {
                System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                return;
            }
        } else if (firstArg instanceof ValueArgument) {
            String firstKey = firstArg.key;
            String simplifiedCommand = ((ValueArgument) firstArg).value;
            switch (firstKey) {
                case "s":
                case "set":
                    Argument originCommandArg = arguments.optArgument(2);
                    if (originCommandArg == null) {
                        System.out.println(getString("CONSOLE_IMMERSIVE_MISSING_PARAMETER"));
                        return;
                    }
                    String originCommand = originCommandArg.originString;
                    simplifyCommands.put(simplifiedCommand, originCommand);
                    Utils.saveConfig(config);
                    break;
                case "d":
                case "delete":
                    simplifyCommands.remove(simplifiedCommand);
                    Utils.saveConfig(config);
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                    break;
            }

        } else {
            System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
        }
    }

    @Override
    public String getUsageName() {
        return "simplify";
    }
}
