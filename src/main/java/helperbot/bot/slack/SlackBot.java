package helperbot.bot.slack;

import helperbot.bot.slack.exception.SlackException;
import helperbot.configuration.SlackConfiguration;
import helperbot.configuration.properties.SlackProperties;
import helperbot.links.Link;
import helperbot.links.LinkRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import lombok.extern.slf4j.Slf4j;
import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@JBot
@Component
@ConditionalOnBean(SlackConfiguration.class)
public class SlackBot extends Bot implements SlackCommandHandler {

    private SlackProperties slackProperties;
    private Map<SlackCommand, SlackCommandHandler> handlers;
    private String helpMessage;

    @Autowired
    public SlackBot(SlackProperties slackProperties, LinkRepository linkRepository) {
        this.slackProperties = slackProperties;

        this.handlers = new HashMap<>();
        this.handlers.put(SlackCommand.HELP, this);
        this.handlers.put(SlackCommand.LINK, new SlackLinkCommandHandler(linkRepository));
        this.handlers.put(SlackCommand.UNKNOWN, this);

        this.helpMessage = new StringBuilder("Command usages\n")
            .append("!link args     :  crud links")
            .append("!help          : display help message")
            .toString();
    }

    @Override
    public String getSlackToken() {
        return slackProperties.getSlackBotToken();
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    /**
     * Receive !command such as !link and handle
     */
    @Controller(events = EventType.MESSAGE, pattern = "^![a-zA-Z0-9].*?")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        logger.info("Receive slack command : {}", event.getText());
        String[] args = event.getText().split("\\s+");

        SlackCommand command = SlackCommand.getType(args[0]);
        SlackCommandHandler handler = handlers.get(command);

        if (handler == null) {
            logger.warn("Cannot find command handler. command : " + args[0]);
            return;
        }

        try {
            Message message = handler.handleCommand(session, event, args);
            if (message != null) {
                reply(session, event, message);
            }
        } catch (SlackException e) {
            String message = StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "Exception occur";
            reply(session, event, message);
        }
    }

    @Override
    public Message handleCommand(WebSocketSession session, Event event, String[] args) {
        return new Message(helpMessage);
    }
}