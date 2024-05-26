package leets.enhance.controller;

import io.jsonwebtoken.MalformedJwtException;
import leets.enhance.dto.ItemDTO;
import leets.enhance.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<String> registerItem(@RequestHeader("Authorization") String token, @RequestBody ItemDTO dto) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            itemService.registerItem(jwtToken, dto);
            return ResponseEntity.ok().build();
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/enhance")
    public ResponseEntity<String> enhanceItem(@RequestHeader("Authorization") String token, @RequestBody ItemDTO dto) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            itemService.enhanceItem(jwtToken, dto);
            return ResponseEntity.ok().build();
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
