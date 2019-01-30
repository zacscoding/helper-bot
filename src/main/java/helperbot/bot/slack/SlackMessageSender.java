package helperbot.bot.slack;

/**
 * @GitHub : https://github.com/zacscoding
 */
@FunctionalInterface
public interface SlackMessageSender {

    void send(String message);
}
