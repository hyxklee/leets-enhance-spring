package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLoginService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public String login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken); //로그인 실패시 AuthenticationException 반환
        log.info("login success");

        String accessToken = jwtUtil.createAccessToken(email);
        return accessToken;
    }
}
