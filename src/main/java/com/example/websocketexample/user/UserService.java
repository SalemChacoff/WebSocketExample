package com.example.websocketexample.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Saves the given user by setting their status to ONLINE and saving them to the repository.
     *
     * @param  user  the user object to be saved
     */
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }

    /**
     * Disconnects a user by setting their status to OFFLINE and saving the updated user to the repository.
     *
     * @param  user  the user object to be disconnected
     */
    public void disconnect(User user) {
        // disconnect user
        var storedUser = userRepository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    /**
     * Finds all connected users by retrieving users with the status of ONLINE from the user repository.
     *
     * @return a list of User objects representing the connected users
     */
    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
