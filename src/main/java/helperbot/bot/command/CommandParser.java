package helperbot.bot.command;

import lombok.extern.slf4j.Slf4j;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class CommandParser {

    /**
     * Parse "!command args[]"
     */
    public static CommandParsedResult parseCommandLine(String commandLine) {
        return parseCommandLine("!", commandLine);
    }

    public static CommandParsedResult parseCommandLine(String commandPrefix, String commandLine) {
        if (commandLine == null || commandPrefix == null
            || commandLine.length() < commandPrefix.length() + 1) {
            return null;
        }

        if (!commandLine.startsWith(commandPrefix)) {
            return null;
        }

        // remove command prefix
        commandLine = commandLine.substring(commandPrefix.length());

        String[] split = commandLine.split("\\s+");
        if (split.length == 1) {
            return CommandParsedResult.builder().rootCommand(split[0]).args(new String[0]).build();
        }

        CommandParsedResult parsedResult = CommandParsedResult.builder()
            .rootCommand(split[0])
            .build();

        // public static native void arraycopy(Object src,  int  srcPos, Object dest, int destPos, int length);
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, split.length - 1);
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                args[i] = args[i].trim().replace("__", " ");
            }
        }

        parsedResult.setArgs(args);
        return parsedResult;
    }
}
