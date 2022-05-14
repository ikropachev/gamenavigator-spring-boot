package org.ikropachev.gamenavigatorspringboot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends NamedEntity {
    private static final String GENRE_LIST_STR = "[\n{\n\"id\": 1,\n\"name\": \"action\"\n},\n" +
            "    {\n\"id\": 3,\n\"name\": \"adventure\"\n}\n]";

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "developer", nullable = false)
    @Schema(example = "developer")
    protected String developer;

    //https://stackoverflow.com/questions/13370221/persistentobjectexception-detached-entity-passed-to-persist-thrown-by-jpa-and-h
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "game_x_genres",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    @Schema(example = GENRE_LIST_STR)
    private List<Genre> genres;

    public Game(Integer id, String name, String developer, List<Genre> genres) {
        super(id, name);
        this.developer = developer;
        this.genres = genres;
    }

    //Constructor for tests with ignoring fields
    public Game(Integer id, String name, String developer) {
        super(id, name);
        this.developer = developer;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getGames().add(this);
    }

    public void removeGenre(Integer genreId) {
        Genre genre = this.genres.stream().filter(g -> g.getId() == genreId).findFirst().orElse(null);
        if (genre != null) this.genres.remove(genre);
        genre.getGames().remove(this);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name=" + name +
                ", developer=" + developer +
                ", genres=" + genres +
                '}';
    }
}
