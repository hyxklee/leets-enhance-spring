package leets.enhance.config.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leets.enhance.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        //헤더 토큰 유무 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JWT Token: {}", authorizationHeader);
        String token = authorizationHeader.split(" ")[1];

        //토큰 소멸 검증
        try {
            jwtUtil.isExpired(token);
        } catch (JwtException e){
            log.error("JWT Token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = jwtUtil.getUserEmail(token);

        User user = User.builder()
                .email(userEmail)
                .build();
        //토큰이 유효하다면 토큰에서 정보를 빼서 그 정보로 UsernamePasswordAuthenticationToken을 생성함
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        //해당 정보로 로그인 세션 형성
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}