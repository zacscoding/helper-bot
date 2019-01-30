package helperbot.repository;

import helperbot.entity.LinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

    List<LinkEntity> findByTags_name(String tagName);
}
