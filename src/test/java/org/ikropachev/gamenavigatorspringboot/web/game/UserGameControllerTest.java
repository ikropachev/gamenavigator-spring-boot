package org.ikropachev.gamenavigatorspringboot.web.game;

import org.ikropachev.gamenavigatorspringboot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.ikropachev.gamenavigatorspringboot.web.game.AdminGameController.GENRE_NAME;
import static org.ikropachev.gamenavigatorspringboot.web.game.GameTestData.*;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.ADMIN_MAIL;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserGameControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserGameController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1, game2, game4, game3));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByGenreName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-genre-name?genre-name=" + GENRE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GAME_MATCHER.contentJson(game1, game2));
    }
}
