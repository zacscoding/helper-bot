package helperbot.configuration;

import helperbot.configuration.condition.BotTypeEnabledCondition.SlackEnabledCondition;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);

        return new RestTemplate(httpRequestFactory);
    }

    @PostConstruct
    private void setUp() {
        logger.info("## Slack bot enabled");
    }
}