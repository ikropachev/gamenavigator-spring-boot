package org.ikropachev.gamenavigatorspringboot.web.genre;

import org.ikropachev.gamenavigatorspringboot.model.Genre;
import org.ikropachev.gamenavigatorspringboot.repository.GenreRepository;
import org.ikropachev.gamenavigatorspringboot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.ikropachev.gamenavigatorspringboot.web.genre.GenreTestData.getNew;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminGenreControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminGenreController.REST_URL+ '/';

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void createWithLocation() throws Exception {
        Genre newGenre = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newGenre)));

        Genre created = GENRE_MATCHER.readFromJson(action);
        int newId = created.id();
        newGenre.setId(newId);
        GENRE_MATCHER.assertMatch(created, newGenre);
        GENRE_MATCHER.assertMatch(service.get(newId), newGenre);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + GENRE_ID)
                .with(userHttpBasic(admin)))
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
    void getNotFound() {
        try {
            perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                    .with(userHttpBasic(admin)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
        }
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(GENRE_MATCHER.contentJson(genre1, genre3, genre4, genre2));
    }

    @Test
    void update() throws Exception {
        Genre updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + GENRE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        GENRE_MATCHER.assertMatch(service.get(GENRE_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + GENRE_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(GENRE_ID));
    }

    @Test
    void deleteNotFound() {
        try {
            perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND)
                    .with(userHttpBasic(admin)))
                    .andExpect(status().isUnprocessableEntity());
        } catch (Exception e) {
            assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
        }
    }
}
