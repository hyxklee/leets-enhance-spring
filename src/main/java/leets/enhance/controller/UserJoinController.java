package leets.enhance.controller;

import leets.enhance.dto.UserJoinDTO;
import leets.enhance.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserJoinService userJoinService;

    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(UserJoinDTO dto) {
        userJoinService.join(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/users/check-duplicate-id")
    public ResponseEntity<String> checkDuplicateId(UserJoinDTO dto){
        try {
            userJoinService.validDuplicate(dto.getEmail());
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일 입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일 입니다.");
    }
}
