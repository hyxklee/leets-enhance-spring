package leets.enhance.controller;

import leets.enhance.dto.UserJoinDTO;
import leets.enhance.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserJoinService userJoinService;

    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(@RequestBody UserJoinDTO dto) {
        userJoinService.join(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    //고치기
    @GetMapping("/users/check-duplicate-id")
    public ResponseEntity<String> checkDuplicateId(@RequestBody UserJoinDTO dto){
        try {
            userJoinService.validDuplicate(dto.getEmail());
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일 입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일 입니다.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
