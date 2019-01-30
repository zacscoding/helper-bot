package helperbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "name")
@Entity(name = "tbl_tag")
@JsonIgnoreProperties(value = "links")
public class TagEntity {

    @Id
    @GeneratedValue
    @Column(name = "TAG_ID")
    private Long id;
    @Column(unique = true)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
    private Set<LinkEntity> links = new HashSet<>();

    public String toString() {
        return "#" + name;
    }
}
