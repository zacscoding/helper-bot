package helperbot.bot.slack;

import helperbot.links.LinkRepository;
import java.util.Objects;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.web.socket.WebSocketSession;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SlackLinkCommandHandler implements SlackCommandHandler {

    private LinkRepository linkRepository;
    private String helpMessage;

    public SlackLinkCommandHandler(LinkRepository linkRepository) {
        Objects.requireNonNull(linkRepository, "linkRepository must be not null");
        this.linkRepository = linkRepository;
        this.helpMessage = new StringBuilder("!link command usages\n")
            .append("!link add {title} {description} {href} [tags]")
            .append("\te.g) !link add spring spring docs https://... #spring#docs\n")
            .append("!link update {id} {key:value}")
            .append("\te.g) !link update 1 tags:#spring#doc\n")
            .append("!link search {searchType} {keyword}")
            .append("\te.g) !link search tags #spring#docs\n")
            .append("!link delete {id}")
            .append("\te.g) !link delete 1")
            .toString();
    }

    @Override
    public Message handleCommand(WebSocketSession session, Event event, String[] args) {
        if (args.length < 2) {
            return new Message(helpMessage);
        }

        return null;
    }
}
