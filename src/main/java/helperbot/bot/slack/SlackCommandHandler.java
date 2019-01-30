package helperbot.bot.slack;

import helperbot.bot.command.CommandParsedResult;
import java.util.function.Consumer;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface SlackCommandHandler {

    /**
     * Handle commands
     */
    void handleCommand(SlackMessageSender messageSender, CommandParsedResult parsedResult);
}
