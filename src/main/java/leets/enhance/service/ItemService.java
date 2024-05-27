package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import leets.enhance.domain.*;
import leets.enhance.dto.ItemDTO;
import leets.enhance.dto.ResponseItemDTO;
import leets.enhance.repository.ItemRepository;
import leets.enhance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();
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
    public ResponseItemDTO enhanceItem(String token, ItemDTO dto) {
        String userEmail = jwtUtil.getUserEmail(token);
        User user = userRepository.findByEmail(userEmail);
        ResponseItemDTO dtoResponse = new ResponseItemDTO();
        Item item = itemRepository.findItemByUserAndItemName(user, dto.getItemName());
        ItemLevel level = item.getLevel();
        EnhancementProbability probability = EnhancementProbability.valueOf("LEVEL_" + level.getLevel());

        boolean isSuccess = random.nextDouble() < probability.getProbability();
        if (isSuccess) {
            item.setLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() + 1)));
            dtoResponse.setItemStatus(ItemStatus.SUCCESS);
        } else {
            ItemStatus status = destroyItem(user, dto.getItemName());
            dtoResponse.setItemStatus(status);
        }
        dtoResponse.setItemName(item.getItemName());
        dtoResponse.setLevel(item.getLevel());
        itemRepository.save(item);
        return dtoResponse;
    }

    public ItemStatus destroyItem(User user, String itemName) {
        Item item = itemRepository.findItemByUserAndItemName(user,itemName);
        ItemLevel level = item.getLevel();
        DestroyProbability probability = DestroyProbability.valueOf("LEVEL_" + level.getLevel());

        //파괴시도
        boolean isDestroyed = random.nextDouble() < probability.getProbability();

        if (isDestroyed) {
            item.setLevel(ItemLevel.valueOf("LEVEL_" + 0));
            item.setItemStatus(ItemStatus.DESTROY);
        } else {
            if(level.getLevel() > 0) {
                item.setLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() - 1)));
                item.setItemStatus(ItemStatus.FAIL);
            }
        }
        return item.getItemStatus();
    }
}
