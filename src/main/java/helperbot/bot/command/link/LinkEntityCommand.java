package helperbot.bot.command.link;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IParameterSplitter;
import helperbot.bot.command.HrefConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LinkEntityCommand implements IParameterSplitter {

    @Parameter(names = "-i", description = "unique id")
    private Long id;
    @Parameter(names = "-t", description = "title")
    private String title;
    @Parameter(names = "-d", description = "description")
    private String description;
    @Parameter(names = "-h", description = "link href", converter = HrefConverter.class)
    private String href;
    @Parameter(names = "-tag", description = "tags", splitter = LinkEntityCommand.class)
    private List<String> tagValues;

    @Override
    public List<String> split(String value) {
        if (!StringUtils.hasText(value)) {
            return Collections.emptyList();
        }

        StringTokenizer tokenizer = new StringTokenizer(value, "#");
        List<String> tags = new ArrayList<>(tokenizer.countTokens());

        while (tokenizer.hasMoreTokens()) {
            // avoid duplicate
            String tag = tokenizer.nextToken();
            if (tags.contains(tag)) {
                continue;
            }

            tags.add(tag);
        }

        return tags;
    }
}
