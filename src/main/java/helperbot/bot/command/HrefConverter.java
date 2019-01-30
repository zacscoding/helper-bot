package helperbot.bot.command;

import com.beust.jcommander.IStringConverter;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class HrefConverter implements IStringConverter<String> {

    @Override
    public String convert(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }

        int beginIndex = value.charAt(0) == '<' ? 1 : 0;
        int endIndex = value.charAt(value.length() - 1) == '>' ? value.length() - 1 : value.length();

        return value.substring(beginIndex, endIndex);
    }
}
