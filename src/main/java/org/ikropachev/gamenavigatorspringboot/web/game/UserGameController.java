package org.ikropachev.gamenavigatorspringboot.web.game;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.ikropachev.gamenavigatorspringboot.web.game.AdminGameController.GENRE_NAME;

@RestController
@RequestMapping(value = UserGameController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "User game controller", description = "Operations for games from user")
public class UserGameController extends AbstractGameController {

    static final String REST_URL = "/api/user/games";

    @GetMapping
    @Operation(summary = "View a list of all games")
    public List<Game> getAll() {
        log.info("get all games");
        return super.getAll();
    }

    @GetMapping(value = "/by-genre-name")
    @Operation(summary = "View a list of all games by name of genre")
    public List<Game> getAllByGenreName(@Nullable @RequestParam(value = "genre-name")
                                        @Parameter(example = GENRE_NAME, required = false) String genreName) {
        log.info("get all games by name of genre {}", genreName);
        Genre genre = genreRepository.findByName(genreName);
        return super.getAllByGenreId(genre.getId());
    }
}
