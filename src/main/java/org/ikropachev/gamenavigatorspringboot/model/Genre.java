package org.ikropachev.gamenavigatorspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genres")
public class Genre extends NamedEntity {

    //https://stackoverflow.com/questions/13370221/persistentobjectexception-detached-entity-passed-to-persist-thrown-by-jpa-and-h
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            //CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "genres")
    @JsonIgnore
    //@ApiModelProperty(hidden = true, readOnly = true)
    private List<Game> games;

    public Genre() {
    }

    public Genre(Integer id, String name, List<Game> games) {
        super(id, name);
        this.games = games;
    }

    //Constructor for tests with ignoring fields
    public Genre(Integer id, String name) {
        super(id, name);
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
