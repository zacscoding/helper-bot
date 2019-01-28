package helperbot.links;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByTags(String tag);
}