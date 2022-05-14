package org.ikropachev.gamenavigatorspringboot.web.game;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ikropachev.gamenavigatorspringboot.View;
import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.ikropachev.gamenavigatorspringboot.util.validation.ValidationUtil.checkNew;
import static org.ikropachev.gamenavigatorspringboot.web.genre.AdminGenreController.GENRE_ID_STR;

@RestController
@RequestMapping(value = AdminGameController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Admin game controller", description = "Operations for games from admin")
public class AdminGameController extends AbstractGameController {
    static final String REST_URL = "/api/admin/games";
    public static final String GENRE_NAME = "action";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a game")
    public ResponseEntity<Game> createWithLocation(@Validated(View.Web.class) @RequestBody Game game) {
        log.info("create {}", game);
        checkNew(game);
        Game created = gameRepository.save(game);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "View a game by id")
    public Game get(@PathVariable @Parameter(example = GAME_ID_STR, required = true) int id) {
        log.info("get game with id {}", id);
        return super.get(id);
    }

    @GetMapping
    @Operation(summary = "View a list of all games")
    public List<Game> getAll() {
        log.info("get all games");
        return super.getAll();
    }

    //test controller
    @GetMapping(value = "/by-genre-id")
    @Operation(summary = "View a list of all games by genre")
    public List<Game> getAllByGenreId(@Nullable @RequestParam(value = "genreId")
                                      @Parameter(example = GENRE_ID_STR, required = false) Integer genreId) {
        log.info("get all games by genre {}", genreId);
        return super.getAllByGenreId(genreId);
    }

    @GetMapping(value = "/by-genre-name")
    @Operation(summary = "View a list of all games by name of genre")
    public List<Game> getAllByGenreName(@Nullable @RequestParam(value = "genre-name")
                                        @Parameter(example = GENRE_NAME, required = false) String genreName) {
        log.info("get all games by name of genre {}", genreName);
        Genre genre = genreRepository.findByName(genreName);
        return super.getAllByGenreId(genre.getId());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a game by id")
    public void update(@Validated(View.Web.class) @RequestBody Game game,
                       @PathVariable @Parameter(example = GAME_ID_STR, required = true) int id) {
        log.info("update game {} with id {}", game, id);
        game.setId(id);
        gameRepository.save(game);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a game by id")
    public void delete(@PathVariable @Parameter(example = GAME_ID_STR, required = true) int id) {
        log.info("delete game with id {}", id);
        gameRepository.delete(id);
    }
}
