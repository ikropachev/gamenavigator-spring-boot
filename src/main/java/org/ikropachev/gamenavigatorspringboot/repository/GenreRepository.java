package org.ikropachev.gamenavigatorspringboot.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Tag(name = "Genre Controller")
public interface GenreRepository extends BaseRepository<Genre> {

    @Query("SELECT g FROM Genre g WHERE g.name=:name")
    Genre findByName(@Param("name") String name);

    @Query("SELECT g FROM Genre g JOIN g.games ga WHERE ga.id =:gameId")
    List<Genre> findGenresByGameId(@Param("gameId") Integer gameId);
}
