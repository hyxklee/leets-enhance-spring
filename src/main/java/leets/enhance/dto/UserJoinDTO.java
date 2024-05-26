package leets.enhance.dto;

import lombok.Getter;

@Getter
public class UserJoinDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String name;
}
