package org.ikropachev.gamenavigatorspringboot.web.game;

import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.ikropachev.gamenavigatorspringboot.web.MatcherFactory;

import java.util.Arrays;
import java.util.List;

import static org.ikropachev.gamenavigatorspringboot.web.genre.GenreTestData.*;

public class GameTestData {
    public static final MatcherFactory.Matcher<Game> GAME_MATCHER = MatcherFactory.usingEqualsComparator(Game.class);

    public static final int GAME_ID = 1;

    public static final Game game1 = new Game(GAME_ID, "half-life", "valve", Arrays.asList(genre1, genre4));
    public static final Game game2 = new Game(GAME_ID + 1, "silent hill", "konami", Arrays.asList(genre1, genre3, genre4));
    public static final Game game3 = new Game(GAME_ID + 2, "warcraft", "blizzard", Arrays.asList(genre2));
    public static final Game game4 = new Game(GAME_ID + 3, "starcraft", "blizzard", Arrays.asList(genre2));

    //Games must be sorted by name
    public static final List<Game> games = List.of(game1, game2, game4, game3);

    public static Game getNew() {
        return new Game(null, "New_Game", "New_Game_Developer",
                Arrays.asList(new Genre(1, "action"), new Genre(3, "adventure")));
    }

    public static Game getUpdated() {
        return new Game(GAME_ID, "Updated_Game", "Updated_Game_Developer");
    }
}
