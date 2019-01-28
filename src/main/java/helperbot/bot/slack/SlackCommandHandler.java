package helperbot.bot.slack;

import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.web.socket.WebSocketSession;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface SlackCommandHandler {

    /**
     * Handle commands
     * - args[0] is root command such as "!link"
     */
    Message handleCommand(WebSocketSession session, Event event, String[] args);
}