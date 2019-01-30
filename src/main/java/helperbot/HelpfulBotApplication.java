package helperbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * add args
 * --spring.config.location=classpath:/application.yaml,classpath:/secret.yaml
 */
@SpringBootApplication
public class HelpfulBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpfulBotApplication.class, args);
    }
}

