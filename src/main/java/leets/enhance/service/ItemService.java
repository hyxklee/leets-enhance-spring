package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import leets.enhance.domain.*;
import leets.enhance.dto.ItemDTO;
import leets.enhance.dto.ResponseItemDTO;
import leets.enhance.exception.ItemLevelZeroException;
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
    public ResponseItemDTO enhanceItem(String token) {
        User user = getUserByToken(token);
        ResponseItemDTO dtoResponse = new ResponseItemDTO();

        Item item = itemRepository.findByUser(user);
        ItemLevel level = item.getLevel();

        EnhancementProbability probability = EnhancementProbability.valueOf("LEVEL_" + level.getLevel());
        boolean isSuccess = random.nextDouble() < probability.getProbability(); //난수 발생
        try {
            if (isSuccess) {
                item.setLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() + 1)));
                dtoResponse.setItemStatus(ItemStatus.SUCCESS);
            } else {
                ItemStatus status = destroyItem(user);
                dtoResponse.setItemStatus(status);
            }
        } catch (ItemLevelZeroException e){
            dtoResponse.setItemStatus(ItemStatus.DESTROY);
        }
        dtoResponse.setItemName(item.getItemName());
        dtoResponse.setLevel(item.getLevel());
        itemRepository.save(item);
        log.info("Enhanced Item: {}", item);
        return dtoResponse;
    }

    public ItemStatus destroyItem(User user) {
        Item item = itemRepository.findByUser(user);
        ItemLevel level = item.getLevel();
        DestroyProbability probability = DestroyProbability.valueOf("LEVEL_" + level.getLevel());

        //파괴시도
        boolean isDestroyed = random.nextDouble() < probability.getProbability();

        if (isDestroyed) {
            item.setLevel(ItemLevel.valueOf("LEVEL_" + 0));
            item.setItemStatus(ItemStatus.DESTROY);
            log.info("Destroyed Item: {}", item);
            throw new ItemLevelZeroException("Item level has dropped to zero and the item is destroyed.");
        } else {
            if(level.getLevel() > 0) {
                item.setLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() - 1)));
                item.setItemStatus(ItemStatus.FAIL);
                log.info("Fail to enhance Item: {}", item);
            }
        }
        return item.getItemStatus();
    }

    private User getUserByToken(String token) {
        String userEmail = jwtUtil.getUserEmail(token);
        return userRepository.findByEmail(userEmail);
    }
}
