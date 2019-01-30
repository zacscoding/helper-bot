package helperbot.bot.command.link;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
public enum LinkCommandType {

    ADD,
    UPDATE,
    DELETE,
    QUERY,
    HELP,
    BACKUP,
    UNKNOWN;

    private static Set<LinkCommandType> TYPES = EnumSet.allOf(LinkCommandType.class);

    public static LinkCommandType getType(String type) {
        if (StringUtils.hasText(type)) {
            for (LinkCommandType command : TYPES) {
                if (type.equalsIgnoreCase(command.name())) {
                    return command;
                }
            }
        }
        return UNKNOWN;
    }
}
