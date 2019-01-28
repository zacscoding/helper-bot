package helperbot.configuration.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition about bot types
 *
 * @GitHub : https://github.com/zacscoding
 */
public class BotTypeEnabledCondition {

    public static class SlackEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return "slack".equals(context.getEnvironment().getProperty("bot.type"));
        }
    }

}
