package org.ikropachev.gamenavigatorspringboot.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Tag(name = "Game Controller")
public interface GameRepository extends BaseRepository<Game> {

    @EntityGraph(attributePaths = {"genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT g FROM Game g ORDER BY g.name")
    List<Game> findAll();

    @EntityGraph(attributePaths = {"genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT g FROM Game g WHERE g.id=:id")
    Optional<Game> findById(@Param("id") int id);

    //https://www.codejava.net/frameworks/spring/jpa-join-query-for-like-search-examples
    @Query("SELECT g FROM Game g JOIN g.genres ge WHERE ge.id =:genreId")
    List<Game> findAllByGenreId(@Param("genreId") Integer genreId);
}
