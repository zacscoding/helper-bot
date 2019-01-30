package helperbot.bot.slack;

import helperbot.bot.command.CommandParsedResult;
import helperbot.bot.command.CommandParser;
import helperbot.configuration.SlackConfiguration;
import helperbot.configuration.properties.SlackProperties;
import helperbot.service.LinkService;
import java.util.function.Consumer;
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
import org.springframework.web.socket.WebSocketSession;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@JBot
@Component
@ConditionalOnBean(SlackConfiguration.class)
public class SlackBot extends Bot {

    private SlackProperties slackProperties;
    private String helpMessage;
    // handles
    private SlackLinkCommandHandler slackLinkCommandHandler;

    @Autowired
    public SlackBot(SlackProperties slackProperties, LinkService linkService) {
        this.slackProperties = slackProperties;
        this.slackLinkCommandHandler = new SlackLinkCommandHandler(linkService);
        this.helpMessage = new StringBuilder("Command usages\n")
            .append("!link args     :  crud links")
            .append("!help          :  display help message")
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
        CommandParsedResult parsedResult = CommandParser.parseCommandLine(event.getText());
        if (parsedResult == null) {
            reply(session, event, "Cannot handle " + event.getText());
            return;
        }

        SlackRootCommand rootCommand = SlackRootCommand.getType(parsedResult.getRootCommand());
        SlackMessageSender messageSender = (message) -> reply(session, event, new Message(message));
        switch (rootCommand) {
            case LINK:
                slackLinkCommandHandler.handleCommand(messageSender, parsedResult);
                break;
            case HELP:
                messageSender.send(helpMessage);
                break;
            case UNKNOWN:
            default:
                messageSender.send("Cannot handle " + event.getText());
                break;
        }
    }
}
