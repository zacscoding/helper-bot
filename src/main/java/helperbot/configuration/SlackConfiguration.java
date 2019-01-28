package helperbot.configuration;

import helperbot.configuration.condition.BotTypeEnabledCondition.SlackEnabledCondition;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Enable jbot package`s beans
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
@Conditional(value = SlackEnabledCondition.class)
@ComponentScan(basePackages = {"me.ramswaroop.jbot"})
public class SlackConfiguration {

    @PostConstruct
    private void setUp() {
        logger.info("## Slack bot enabled");
    }
}