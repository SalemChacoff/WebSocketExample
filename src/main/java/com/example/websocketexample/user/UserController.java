package com.example.websocketexample.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Adds a user to the system and publishes the user to the "/user/topic" channel.
     *
     * @param  user  the user object to be added
     * @return       the added user object
     */
    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(@Payload User user) {
        userService.saveUser(user);
        return user;
    }

    /**
     * Disconnects a user by setting their status to OFFLINE and saving the updated user to the repository.
     *
     * @param  user  the user object to be disconnected
     * @return       the updated user object
     */
    @MessageMapping("/user.disconnect")
    @SendTo("/user/topic")
    public User disconnect(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    /**
     * Retrieves a list of connected users by calling the `findConnectedUsers` method of the `userService`
     * and returns it as a `ResponseEntity` object with a status of 200 (OK).
     *
     * @return          a `ResponseEntity` object containing a list of connected users
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
