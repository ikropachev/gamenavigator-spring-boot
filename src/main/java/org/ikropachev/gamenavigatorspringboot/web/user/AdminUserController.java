package org.ikropachev.gamenavigatorspringboot.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ikropachev.gamenavigatorspringboot.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.ikropachev.gamenavigatorspringboot.util.validation.ValidationUtil.assureIdConsistent;
import static org.ikropachev.gamenavigatorspringboot.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Admin user controller", description = "Operations for users from admin")
public class AdminUserController extends AbstractUserController {

    static final String REST_URL = "/api/admin/users";
    private static final String USER_ID_STR = "2";
    private static final String USER_FOR_DELETE_ID_STR = "3";
    private static final String USER_FOR_UPDATE_ID_STR = "4";

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "View the user by id")
    public ResponseEntity<User> get(@PathVariable @Parameter(example = USER_ID_STR, required = true) int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the user by id")
    public void delete(@PathVariable @Parameter(example = USER_FOR_DELETE_ID_STR, required = true) int id) {
        super.delete(id);
    }

    @GetMapping
    @Operation(summary = "View a list of all users")
    public List<User> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create the user")
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the user by id")
    public void update(@Valid @RequestBody User user,
                       @PathVariable @Parameter(example = USER_FOR_UPDATE_ID_STR, required = true) int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        prepareAndSave(user);
    }

    @GetMapping("/by-email")
    @Operation(summary = "View the user by e-mail")
    public ResponseEntity<User> getByEmail(@RequestParam @Parameter(example = "user@gmail.com", required = true) String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(repository.findByEmailIgnoreCase(email));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Set enable status for the user")
    public void enable(@PathVariable @Parameter(example = USER_FOR_UPDATE_ID_STR, required = true) int id,
                       @RequestParam @Parameter(example = "false", required = true) boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = repository.getById(id);
        user.setEnabled(enabled);
    }
}