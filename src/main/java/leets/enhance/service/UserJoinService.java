package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import leets.enhance.domain.User;
import leets.enhance.dto.UserJoinDTO;
import leets.enhance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserJoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public void join(UserJoinDTO dto) {
        //기존에 존재하는 회원인지 검증
        validDuplicate(dto.getEmail());
        //비밀번호와 2차 비밀번호 검증
        validPasswordInput(dto);
        //유저 저장
        userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .boostCount(3)
                .build());
        log.info("User {} joined", dto.getEmail());
    }

    //중복 회원가입 검증
    public void validDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            log.info("User {} already exists", email);
            throw new IllegalStateException("User " + email + " already exists");
        }
    }

//    //중복 검증 고치기
//    public void validDuplicateEmail(String token, String email) {
//        String userEmail = jwtUtil.getUserEmail(token);
//        User user = userRepository.findByEmail(email);
//        if (user != null) {
//            log.info("User {} already exists", email);
//            throw new IllegalStateException("User " + email + " already exists");
//        }
//    }

    //비밀번호 2차 검증
    public void validPasswordInput(UserJoinDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            log.info("Passwords do not match");
            throw new IllegalStateException("Passwords do not match");
        }
    }
}
