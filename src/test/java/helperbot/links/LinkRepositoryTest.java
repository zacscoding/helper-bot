package helperbot.links;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LinkRepositoryTest {

    @Autowired
    LinkRepository repository;

    @Test
    public void findAllByTags() {
        System.out.println(repository);
        List<Link> links = Arrays.asList(
            Link.builder()
                .title("link01")
                .tags(Arrays.asList("tag01", "tag02"))
                .build(),
            Link.builder()
                .title("link02")
                .tags(Arrays.asList("tag02", "tag05"))
                .build(),
            Link.builder()
                .title("link03")
                .tags(Arrays.asList("tag07"))
                .build()
        );
        repository.saveAll(links);
        // Link saved = repository.save(links.get(0));
        System.out.println(repository.findByTags("tag02").size());
        // System.out.println(repository.findAll().get(0).getTags().size());
    }
}
