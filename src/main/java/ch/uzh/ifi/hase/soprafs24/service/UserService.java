package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(Long Id) {
        //
        User user = this.userRepository.findByid(Id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist");
        }
        return user;
    }

    public User createUser(User newUser) {

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        checkIfUserExists(newUser);
        newUser.setCreation_date(LocalDate.now());
        newUser.setStatus(UserStatus.ONLINE);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    public User logIn(String username, String password){
        //
        User userByUsername = userRepository.findByUsername(username);
        if (userByUsername==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sign up first!");
        }
        if (!userByUsername.getPassword().equals(password)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Wrong password.");
        }
        userByUsername.setStatus(UserStatus.ONLINE);
        userRepository.flush();
        return userByUsername;
    }

    public void editUser (User userToEdit, Long id){
        System.out.println(id);
        User wantedUser = getUser(id);


        if (!userToEdit.getUsername().equals(wantedUser.getUsername())){
            checkIfUserExists(userToEdit);
        }
        if (userToEdit.getUsername()!=""){
            wantedUser.setUsername(userToEdit.getUsername());
            wantedUser.setBirthday(userToEdit.getBirthday());
        }

    }

    public void logoutUser(Long UserId) {
        User userByID = null;

        List<User> usersByUsername = userRepository.findAll();

        for (User user : usersByUsername) {
            if (user.getId().equals(UserId)) {
                userByID = user;
            }
        }
        if (userByID == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userByID.setStatus(UserStatus.OFFLINE);
        userRepository.save(userByID);
        userRepository.flush();

    }



    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
    }



    public User checkLoginCredentials(String username, String password) {
        User userByUsername = userRepository.findByUsername(username);

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username does not exist.");

        } else if (!userByUsername.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Wrong password");
        }
        return userByUsername;
        }

}
