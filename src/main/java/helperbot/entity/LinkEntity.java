package helperbot.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "tags")
@Entity(name = "TBL_LINK")
public class LinkEntity {

    @Id
    @GeneratedValue
    @Column(name = "LINK_ID")
    private Long id;
    private String title;
    private String description;
    private String href;
    @CreationTimestamp
    private LocalDateTime registerDateTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "TBL_LINK_TAG",
        joinColumns = @JoinColumn(name = "LINK_ID"),
        inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private Set<TagEntity> tags = new HashSet<>();

    public boolean addTag(TagEntity tagEntity) {
        if (tags == null) {
            tags = new HashSet<>();
        }

        return tags.add(tagEntity);
    }
}
