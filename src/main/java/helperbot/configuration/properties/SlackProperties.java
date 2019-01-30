package helperbot.configuration.properties;

import helperbot.configuration.SlackConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Component
@ConditionalOnBean(SlackConfiguration.class)
public class SlackProperties {

    private String slackApi;
    private String slackBotToken;
    private String slackIncomingWebhookUrl;

    @Autowired
    public SlackProperties(@Value("${slackApi}") String slackApi,
        @Value("${bot.slack.bot-token}") String slackBotToken,
        @Value("${bot.slack.web-hook-url}") String slackIncomingWebhookUrl) {

        this.slackApi = slackApi;
        this.slackBotToken = slackBotToken;
        this.slackIncomingWebhookUrl = slackIncomingWebhookUrl;
    }
}
