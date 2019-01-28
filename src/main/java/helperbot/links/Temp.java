package helperbot.links;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class Temp {

    private LinkRepository linkRepository;

    @Autowired
    public Temp(LinkRepository linkRepository) {
        // TEMP
        if (linkRepository.findAll().size() == 0) {
            List<Link> links = Arrays.asList(
                Link.builder()
                    .title("link01")
                    .tags(Arrays.asList("tag01", "tag02"))
                    .build()
            );
            linkRepository.saveAll(links);
            System.out.println("## Success to save :: " + linkRepository.findAll().size());
        } else {
            System.out.println("## Find " + linkRepository.findAll().size());
        }
    }
}
