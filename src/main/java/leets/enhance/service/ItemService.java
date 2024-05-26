package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import leets.enhance.domain.Item;
import leets.enhance.domain.ItemLevel;
import leets.enhance.domain.User;
import leets.enhance.dto.ItemDTO;
import leets.enhance.repository.ItemRepository;
import leets.enhance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    //아이템 등록
    public void registerItem(String token, ItemDTO dto) {
        String userEmail = jwtUtil.getUserEmail(token);
        User user = userRepository.findByEmail(userEmail);

        itemRepository.save(Item.builder()
                .user(user)
                .level(ItemLevel.LEVEL_1)
                .itemName(dto.getItemName())
                .build());
        log.info("Registered Item: {}", dto);
    }

    //아이템 강화
    public void enhanceItem(String token, ItemDTO dto) {
        String userEmail = jwtUtil.getUserEmail(token);
        User user = userRepository.findByEmail(userEmail);

        Item item = itemRepository.findItemByUserAndItemName(user, dto.getItemName());
        ItemLevel level = item.getLevel();
    }
}
