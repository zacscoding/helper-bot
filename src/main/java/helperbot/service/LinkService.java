package helperbot.service;

import helperbot.bot.command.link.LinkEntityCommand;
import helperbot.entity.LinkEntity;
import helperbot.entity.TagEntity;
import helperbot.repository.LinkRepository;
import helperbot.repository.TagRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class LinkService {

    private final String defaultTagName = "unknown";
    private LinkRepository linkRepository;
    private TagRepository tagRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, TagRepository tagRepository) {
        this.linkRepository = linkRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Save link from link entity command
     */
    public LinkEntity saveLink(LinkEntityCommand command) {
        LinkEntity link = convertToLink(command);

        // convert tags
        List<String> tagValues = command.getTagValues();
        if (tagValues == null) {
            tagValues = new ArrayList<>(1);
        }

        if (tagValues.size() == 0) {
            tagValues.add(defaultTagName);
        }

        for (String tagValue : tagValues) {
            link.addTag(getOrSaveTag(tagValue));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("try to save link : {}", link);
        }

        return linkRepository.save(link);
    }

    /**
     * Update link
     *
     * @param command : must have id value
     */
    public LinkEntity updateLink(LinkEntityCommand command) {
        if (command == null || command.getId() == null) {
            return null;
        }

        Optional<LinkEntity> entityOptional = linkRepository.findById(command.getId());
        if (!entityOptional.isPresent()) {
            logger.debug("Skip update link because not exist id : {}", command.getId());
            return null;
        }

        LinkEntity saved = entityOptional.get();
        updateLink(saved, command);
        if (logger.isDebugEnabled()) {
            logger.debug("Try to update link : {}", saved);
        }

        return linkRepository.save(saved);
    }

    /**
     * Delete link by id
     */
    public void deleteLink(Long id) {
        if (id == null) {
            logger.debug("Skip delete link because id is null");
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Try to delete link. id : {}", id);
        }

        linkRepository.deleteById(id);
    }

    /**
     * Query by tag name
     */
    public List<LinkEntity> queryByTagName(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            logger.debug("Try to query with all");
            return linkRepository.findAll();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Try to query with tag name : {}", tagName);
        }

        return linkRepository.findByTags_name(tagName);
    }

    /**
     * Convert LinkEntityCommand to LinkEntity except for tags
     */
    private LinkEntity convertToLink(LinkEntityCommand dto) {
        if (dto == null) {
            return null;
        }

        return LinkEntity.builder()
            .title(dto.getTitle())
            .href(dto.getHref())
            .description(dto.getDescription())
            .build();
    }

    /**
     * Update link entity about valid fields in LinkEntityCommand
     */
    private void updateLink(LinkEntity entity, LinkEntityCommand command) {
        if (StringUtils.hasText(command.getTitle())) {
            entity.setTitle(command.getTitle());
        }

        if (StringUtils.hasText(command.getDescription())) {
            entity.setDescription(command.getDescription());
        }

        if (StringUtils.hasText(command.getHref())) {
            entity.setHref(command.getHref());
        }

        if (!CollectionUtils.isEmpty(command.getTagValues())) {
            entity.setTags(new HashSet<>());
            for (String tagValue : command.getTagValues()) {
                entity.addTag(getOrSaveTag(tagValue));
            }
        }
    }

    /**
     * Get or save tag by name
     */
    public TagEntity getOrSaveTag(String tagName) {
        if (!StringUtils.hasText(tagName)) {
            throw new IllegalArgumentException("tagName must not be empty");
        }

        Optional<TagEntity> tagOptional = tagRepository.findByName(tagName);
        TagEntity tag = null;
        if (tagOptional.isPresent()) {
            tag = tagOptional.get();
        } else {
            tag = tagRepository.save(TagEntity.builder().name(tagName).build());
        }

        return tag;
    }
}
