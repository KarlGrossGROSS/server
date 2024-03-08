package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import java.time.LocalDate;
import java.util.Date;

public class UserPutDTO {

    private String username;
    private Date birthday;
    private String token;

    public String getToken() { return token; }

    public void setToken(String token){ this.token = token; }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {this.birthday=birthday;}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
