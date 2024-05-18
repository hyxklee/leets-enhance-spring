package leets.enhance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private String name;
}
