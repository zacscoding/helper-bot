package helperbot.dev;

import com.beust.jcommander.JCommander;
import helperbot.bot.command.link.LinkEntityCommand;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Ignore
public class JCommanderTest {

    @Test
    public void test_emptyArgs() {
        LinkEntityCommand linkEntityCommand = new LinkEntityCommand();
        JCommander.newBuilder()
            .addObject(linkEntityCommand)
            .build()
            .parse(new String[]{});

        System.out.println(linkEntityCommand);
    }

    @Test
    public void test_usage() {
        LinkEntityCommand linkEntityCommand = new LinkEntityCommand();
        JCommander jCommander = JCommander.newBuilder()
            .addObject(linkEntityCommand)
            .build();
        StringBuilder sb = new StringBuilder();
        jCommander.usage(sb);
        System.out.println(sb.toString());
        // output
//        Usage: <main class> [options]
//        Options:
//        -d
//        description
//            -h
//        link href
//        -i
//        unique id
//        -t
//        title
//            -tag
//        tags
    }
}
