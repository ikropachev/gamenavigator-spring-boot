package org.ikropachev.gamenavigatorspringboot.web.genre;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ikropachev.gamenavigatorspringboot.View;
import org.ikropachev.gamenavigatorspringboot.error.NotFoundException;
import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.ikropachev.gamenavigatorspringboot.repository.GameRepository;
import org.ikropachev.gamenavigatorspringboot.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.ikropachev.gamenavigatorspringboot.web.game.AbstractGameController.GAME_ID_STR;

@RestController
@RequestMapping(value = AdminGenreController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Admin genre controller", description = "Operations for genres from admin")
public class AdminGenreController {
    static final String REST_URL = "/api/admin/genres";
    public static final String GENRE_ID_STR = "1";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GameRepository gameRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a genre")
    public ResponseEntity<Genre> createWithLocation(@Validated(View.Web.class) @RequestBody Genre genre) {
        log.info("create genre {}", genre);
        Genre created = genreRepository.save(genre);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping("/games/{gameId}/genres")
    public ResponseEntity<Genre> addGenre(@PathVariable(value = "gameId")
                                          @Parameter(example = GAME_ID_STR, required = true) int gameId,
                                          @RequestBody Genre genreRequest) {
        int genreId = genreRequest.getId();
        Game game = gameRepository.findById(gameId).orElse(null);
        Genre genre = genreRepository.getById(genreId);
        game.addGenre(genre);
        gameRepository.save(game);
        return new ResponseEntity<>(genre, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "View a genre by id")
    public ResponseEntity<Genre> get(@PathVariable @Parameter(example = GENRE_ID_STR, required = true) int id) {
        log.info("get genre with id {}", id);
        return ResponseEntity.of(genreRepository.findById(id));
    }

    @GetMapping
    @Operation(summary = "View a list of all genres")
    public List<Genre> getAll() {
        log.info("get all genres");
        return genreRepository.findAll();
    }

    @GetMapping("/games/{gameId}/genres")
    public ResponseEntity<List<Genre>> getAllGenresByGameId(@PathVariable(value = "gameId")
                                                            @Parameter(example = GAME_ID_STR, required = true)
                                                            Integer gameId) {
        if (gameRepository.findById(gameId) == null) {
            throw new NotFoundException("Not found Game with id = " + gameId);
        }
        List<Genre> genres = genreRepository.findGenresByGameId(gameId);
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a genre by id")
    public void update(@Validated(View.Web.class) @RequestBody Genre genre,
                       @PathVariable @Parameter(example = GENRE_ID_STR, required = true) int id) {
        log.info("update genre {} with id {}", genre, id);
        genre.setId(id);
        genreRepository.save(genre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre by id")
    public void delete(@PathVariable @Parameter(example = GENRE_ID_STR, required = true) int id) {
        log.info("delete genre with id {}", id);
        genreRepository.delete(id);
    }
}
