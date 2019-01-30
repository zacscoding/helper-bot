package helperbot.bot.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class CommandParserTest {

    @Test
    public void test_parse() {
        // given
        String commandLine = "!cmd -t title -n name";

        // when
        CommandParsedResult result = CommandParser.parseCommandLine(commandLine);

        // then
        assertThat(result.getRootCommand()).isEqualTo("cmd");
        assertThat(result.getArgs().length).isEqualTo(4);
        assertThat(result.getArgs()[1]).isEqualTo("title");

        // given
        String commandLine2 = "!cmd ";

        // when
        CommandParsedResult result2 = CommandParser.parseCommandLine(commandLine2);

        // then
        assertThat(result2.getRootCommand()).isEqualTo("cmd");
        assertThat(result2.getArgs()).isNull();
    }

    @Test
    public void test_replaceSpaceArgs() {
        // given
        String commandLine = "!cmd -t title -d this__is__description  ";

        // when
        CommandParsedResult result = CommandParser.parseCommandLine(commandLine);

        // then
        assertThat(result.getArgs().length).isEqualTo(4);
        assertThat(result.getArgs()[0]).isEqualTo("-t");
        assertThat(result.getArgs()[1]).isEqualTo("title");
        assertThat(result.getArgs()[2]).isEqualTo("-d");
        assertThat(result.getArgs()[3]).isEqualTo("this is description");
    }

    @Test
    public void test_parse_invalidLine() {
        // given
        String invalidLine1 = null;
        // when then
        assertThat(CommandParser.parseCommandLine(invalidLine1)).isNull();

        // given
        String invalidLine2 = "@cmd -t title";
        assertThat(CommandParser.parseCommandLine(invalidLine2)).isNull();
    }
}