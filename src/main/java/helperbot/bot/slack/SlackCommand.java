package helperbot.bot.slack;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum SlackCommand {

    HELP,
    LINK,
    UNKNOWN;

    private static Set<SlackCommand> TYPES = EnumSet.allOf(SlackCommand.class);

    public static SlackCommand getType(String type) {
        if (StringUtils.hasText(type)) {
            for (SlackCommand command : TYPES) {
                if (type.equalsIgnoreCase(command.name())) {
                    return command;
                }
            }
        }
        return UNKNOWN;
    }

}
