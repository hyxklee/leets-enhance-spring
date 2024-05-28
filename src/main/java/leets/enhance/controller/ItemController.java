package leets.enhance.controller;

import io.jsonwebtoken.MalformedJwtException;
import leets.enhance.dto.ItemDTO;
import leets.enhance.dto.RequestBoost;
import leets.enhance.dto.ResponseItemDTO;
import leets.enhance.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseItemDTO> enhanceItem(@RequestHeader("Authorization") String token, @RequestBody RequestBoost dto) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            ResponseItemDTO responseItemDTO = itemService.enhanceItem(jwtToken, dto);
            return ResponseEntity.ok().body(responseItemDTO);
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/items")
    public ResponseEntity<ResponseItemDTO> getEnhancedItem(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            ResponseItemDTO responseItemDTO = itemService.findItem(jwtToken);
            return ResponseEntity.ok().body(responseItemDTO);
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/items/top10")
    public List<ResponseItemDTO> getTop10Items() {
        return itemService.getTop10Items();
    }
}
