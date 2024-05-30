package leets.enhance.controller;

import leets.enhance.dto.UserLoinDTO;
import leets.enhance.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserLoginController {

    private final UserLoginService userLoginService;

    @PostMapping("/users/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoinDTO dto) {
        try {
            String token = userLoginService.login(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok("Bearer "+token +"\n"+ dto.getEmail());
        } catch (AuthenticationException e) {
            String errorMessage;
            if (e instanceof UsernameNotFoundException) {
                errorMessage = "유저없음";
                log.error(errorMessage);
            } else if (e instanceof BadCredentialsException) {
                errorMessage = "비밀번호틀림";
                log.error(errorMessage);
            } else {
                errorMessage = "로그인 정보를 다시 입력해주세요.";
                log.error(errorMessage);
            }
        }
        return ResponseEntity.badRequest().build();
    }
//    토큰 인증 테스트용 API
//    @GetMapping("/")
//    public ResponseEntity<String> getCurrentUser() {
//        return ResponseEntity.ok("인증됨");
//    }
}
