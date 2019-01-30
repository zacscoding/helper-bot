package helperbot.service;

import static org.assertj.core.api.Assertions.assertThat;

import helperbot.bot.command.link.LinkEntityCommand;
import helperbot.entity.LinkEntity;
import helperbot.entity.TagEntity;
import helperbot.repository.LinkRepository;
import helperbot.repository.TagRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LinkServiceTest {

    @Autowired
    LinkRepository linkRepository;
    @Autowired
    TagRepository tagRepository;

    private LinkService linkService;

    @Before
    public void setUp() {
        linkService = new LinkService(linkRepository, tagRepository);
    }

    @Test
    public void test_saveLink() {
        // given
        LinkEntityCommand command = new LinkEntityCommand();
        command.setId(2L);
        command.setTitle("zaccoding`s github");
        command.setDescription("Sample description");
        command.setHref("http://github.com/zacscoding");
        command.setTagValues(Arrays.asList("git", "github"));

        // when
        LinkEntity saved = linkService.saveLink(command);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotEqualTo(command.getId());
        assertThat(saved.getTitle()).isEqualTo(command.getTitle());
        assertThat(saved.getTitle()).isEqualTo(command.getTitle());
        assertThat(saved.getTags().size()).isEqualTo(2);
        assertThat(containsTag(saved, "git")).isTrue();
        assertThat(containsTag(saved, "github")).isTrue();
    }

    @Test
    public void test_updateLink() {
        // given
        LinkEntityCommand saveCommand = LinkEntityCommand.builder()
            .id(2L)
            .title("zaccoding`s github")
            .description("Sample description")
            .href("http://github.com/zacscoding")
            .tagValues(Arrays.asList("git", "github"))
            .build();
        LinkEntity saved = linkService.saveLink(saveCommand);

        // when
        LinkEntityCommand updateCommand = new LinkEntityCommand();
        updateCommand.setId(saved.getId());
        updateCommand.setHref("new href");
        updateCommand.setTagValues(Arrays.asList("git", "spring"));
        LinkEntity updated = linkService.updateLink(updateCommand);

        // then
        assertThat(updated).isNotNull();
        assertThat(updated.getHref()).isEqualTo(updateCommand.getHref());
        assertThat(updated.getTags().size()).isEqualTo(2);
        assertThat(containsTag(updated, "github")).isFalse();
        assertThat(containsTag(updated, "git")).isTrue();
        assertThat(containsTag(updated, "spring")).isTrue();
    }

    @Test
    public void test_queryByTagName() {
        // given
        linkService.saveLink(
            LinkEntityCommand.builder().title("title01").tagValues(Arrays.asList("tag1", "tag2")).build()
        );
        linkService.saveLink(
            LinkEntityCommand.builder().title("title02").tagValues(Arrays.asList("tag2", "tag3")).build()
        );
        linkService.saveLink(
            LinkEntityCommand.builder().title("title03").tagValues(Arrays.asList("tag3")).build()
        );

        // when then
        assertThat(linkService.queryByTagName("tag1").size()).isEqualTo(1);

        // when then
        assertThat(linkService.queryByTagName("tag2").size()).isEqualTo(2);

        // when then
        assertThat(linkService.queryByTagName("tag3").size()).isEqualTo(2);

        // when then
        assertThat(linkService.queryByTagName("tag4").size()).isEqualTo(0);
    }

    private boolean containsTag(LinkEntity link, String tagName) {
        if (link == null || CollectionUtils.isEmpty(link.getTags())) {
            return false;
        }

        TagEntity tag = TagEntity.builder().name(tagName).build();

        return link.getTags().contains(tag);
    }

    private Set<TagEntity> toTagSet(List<String> tagNames) {
        if (CollectionUtils.isEmpty(tagNames)) {
            return Collections.emptySet();
        }

        return tagNames.stream().map(tagName -> TagEntity.builder().name(tagName).build()).collect(Collectors.toSet());
    }
}
