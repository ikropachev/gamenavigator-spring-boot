package org.ikropachev.gamenavigatorspringboot.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ikropachev.gamenavigatorspringboot.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Tag(name = "User Controller")
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);
}
