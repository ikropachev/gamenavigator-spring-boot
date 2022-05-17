package org.ikropachev.gamenavigatorspringboot.web.game;

import lombok.extern.slf4j.Slf4j;
import org.ikropachev.gamenavigatorspringboot.error.NotFoundException;
import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.repository.GameRepository;
import org.ikropachev.gamenavigatorspringboot.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class AbstractGameController {
    public static final String GAME_ID_STR = "1";

    @Autowired
    protected GameRepository gameRepository;

    @Autowired
    protected GenreRepository genreRepository;

    public Game get(int id) throws NotFoundException {
        log.info("get game with id {}", id);
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            throw new NotFoundException("Game not found");
        }
        return game;
    }

    public List<Game> getAll() {
        log.info("get all menus");
        return gameRepository.findAll();
    }

    public List<Game> getAllByGenreId(Integer genreId) {
        log.info("get all games by genre {}", genreId);
        return gameRepository.findAllByGenreId(genreId);
    }
}
