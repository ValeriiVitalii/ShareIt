package ru.practicum.features.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postUser(User user) {
        return post("", user);
    }

    public ResponseEntity<Object> patchUser(Long userId, User user) {
        return patch("/" + userId, user);
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAllUser() {
        return get("");
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        return delete("/" + userId);
    }
}
