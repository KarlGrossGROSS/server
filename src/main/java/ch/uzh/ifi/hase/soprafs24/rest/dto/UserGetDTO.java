package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import java.time.LocalDate;
import java.util.Date;

public class UserGetDTO {

  private Long id;
  private String username;
  private UserStatus status;
  private String password;
  private LocalDate creation_date;
  private LocalDate birthday;

  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {this.birthday=birthday;}

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getPassword() {return password; }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
      this.password = password; }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public LocalDate getCreation_date() { return creation_date; }
  public void setCreation_date(LocalDate creation_date) {  this.creation_date = creation_date; }
}
