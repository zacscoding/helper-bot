package helperbot.bot.slack;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum SlackRootCommand {

    HELP,
    LINK,
    UNKNOWN;

    private static Set<SlackRootCommand> TYPES = EnumSet.allOf(SlackRootCommand.class);

    public static SlackRootCommand getType(String type) {
        if (StringUtils.hasText(type)) {
            for (SlackRootCommand command : TYPES) {
                if (type.equalsIgnoreCase(command.name())) {
                    return command;
                }
            }
        }
        return UNKNOWN;
    }
}
