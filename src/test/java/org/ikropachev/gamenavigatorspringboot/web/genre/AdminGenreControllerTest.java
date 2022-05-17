package org.ikropachev.gamenavigatorspringboot.web.genre;

import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.ikropachev.gamenavigatorspringboot.repository.GenreRepository;
import org.ikropachev.gamenavigatorspringboot.util.JsonUtil;
import org.ikropachev.gamenavigatorspringboot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.ikropachev.gamenavigatorspringboot.web.genre.GenreTestData.*;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.ADMIN_MAIL;
import static org.ikropachev.gamenavigatorspringboot.web.user.UserTestData.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminGenreControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminGenreController.REST_URL+ '/';

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Genre newGenre = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newGenre)))
                .andExpect(status().isCreated());

        Genre created = GENRE_MATCHER.readFromJson(action);
        int newId = created.id();
        newGenre.setId(newId);
        GENRE_MATCHER.assertMatch(created, newGenre);
        GENRE_MATCHER.assertMatch(genreRepository.getById(newId), newGenre);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + GENRE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GENRE_MATCHER.contentJson(genre1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + GENRE_ID))
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
                .andExpect(GENRE_MATCHER.contentJson(genre1, genre2, genre3, genre4));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Genre updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + GENRE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        GENRE_MATCHER.assertMatch(genreRepository.getById(GENRE_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + GENRE_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        //assertThrows(NotFoundException.class, () -> service.get(GENRE_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
