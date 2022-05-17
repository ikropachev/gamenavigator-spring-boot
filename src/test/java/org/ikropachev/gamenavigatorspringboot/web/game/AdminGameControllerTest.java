package org.ikropachev.gamenavigatorspringboot.web.game;

import org.ikropachev.gamenavigatorspringboot.model.Game;
import org.ikropachev.gamenavigatorspringboot.repository.GameRepository;
import org.ikropachev.gamenavigatorspringboot.util.JsonUtil;
import org.ikropachev.gamenavigatorspringboot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.ikropachev.gamenavigatorspringboot.web.game.AdminGameController.GENRE_NAME;
import static org.ikropachev.gamenavigatorspringboot.web.game.GameTestData.*;
import static org.ikropachev.gamenavigatorspringboot.web.genre.GenreTestData.GENRE_ID;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.ADMIN_MAIL;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminGameControllerTest extends AbstractControllerTest {
    private static final Logger log = getLogger(AdminGameControllerTest.class);

    private static final String REST_URL = AdminGameController.REST_URL + "/";

    @Autowired
    private GameRepository gameRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Game newGame = getNew();
        log.info("get new test game {}", newGame);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newGame)))
                .andExpect(status().isCreated());

        Game created = GAME_MATCHER.readFromJson(action);
        int newId = created.id();
        newGame.setId(newId);
        GAME_MATCHER.assertMatch(created, newGame);
        GAME_MATCHER.assertMatch(gameRepository.findById(newId).orElse(null), newGame);


    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + GAME_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + GAME_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1, game2, game4, game3));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByGenreId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-genre-id?genreId=" + GENRE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1, game2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByGenreName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-genre-name?genre-name=" + GENRE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1, game2));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Game updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        GAME_MATCHER.assertMatch(gameRepository.findById(GAME_ID).orElse(null), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + GAME_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        //assertThrows(NotFoundException.class, () -> service.get(GAME_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
