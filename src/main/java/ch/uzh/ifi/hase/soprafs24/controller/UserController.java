package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }
    @PutMapping("/status/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void status(@PathVariable String username){userService.status(username);}

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userCreds = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User newUser = userService.createUser(userCreds);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(newUser);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getTheUser(@PathVariable("id") String id) {
        Long idLong = convertStringLong(id);
        assert idLong != null;
        User wantedUser = userService.getUser(idLong);
        System.out.println("Found the user");
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(wantedUser);
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editUser(@RequestBody UserPutDTO userPutDTO, @PathVariable("id") String id){
        Long idLong=convertStringLong(id);
        User user=DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userService.editUser(user, idLong);
    }

    @GetMapping("/logout/{userId}") //for setting user offline
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logOutUser(@PathVariable("userId") Long userid) {
        // this function is implemented in the UserService
        userService.logoutUser(userid);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO logInUser(@RequestBody UserPostDTO userPostDTO){
        User userCredentials = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User user = userService.logIn(userCredentials.getUsername(), userCredentials.getPassword());
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    }
    private Long convertStringLong(String id){
        System.out.println("ID received: " + id);
        Long idLong;
        try {
            idLong = Long.parseLong(id);
        }
        catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This id is not valid "+ id);

        }
        return idLong;
    }
}