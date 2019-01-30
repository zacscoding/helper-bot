package helperbot.repository;

import helperbot.entity.TagEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String name);

    List<TagEntity> findByNameIn(List<String> names);
}
