package org.ikropachev.gamenavigatorspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre extends NamedEntity {

    //https://stackoverflow.com/questions/13370221/persistentobjectexception-detached-entity-passed-to-persist-thrown-by-jpa-and-h
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            //CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "genres")
    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    private List<Game> games;

    public Genre(Integer id, String name, List<Game> games) {
        super(id, name);
        this.games = games;
    }

    //Constructor for tests with ignoring fields
    public Genre(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
