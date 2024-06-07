package leets.enhance.service;

import leets.enhance.config.jwt.JWTUtil;
import leets.enhance.domain.*;
import leets.enhance.dto.ItemDTO;
import leets.enhance.dto.RequestBoost;
import leets.enhance.dto.ResponseItemDTO;
import leets.enhance.exception.ItemLevelZeroException;
import leets.enhance.repository.ItemRepository;
import leets.enhance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        itemRepository.save(Item.builder()
                .user(user)
                .level(ItemLevel.LEVEL_1)
                .itemName(dto.getItemName())
                .build());
        log.info("Registered Item: {}", dto);
    }

    //아이템 강화
    public ResponseItemDTO enhanceItem(String token, RequestBoost dto) {
        User user = getUserByToken(token);

        Optional<Item> optionalItem = itemRepository.findByUser(user);
        Item item;
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else {
            throw new UsernameNotFoundException("user not found");
        }

        ResponseItemDTO dtoResponse = ResponseItemDTO.builder().build();
        ItemLevel level = item.getLevel();

        double generateProbability = random.nextDouble();
        EnhancementProbability probability = EnhancementProbability.valueOf("LEVEL_" + level.getLevel());
        if (dto.isUseBoost()) {
            if (user.getBoostCount() > 0) {
                user.setBoostCount(user.getBoostCount() - 1);
                generateProbability += generateProbability + 0.1;
                dtoResponse.setProbability(probability);
            } else {
                throw new IllegalStateException("Boost count is zero");
            }
        }
        boolean isSuccess = generateProbability < probability.getProbability(); //난수 발생
        try {
            if (isSuccess) {
                item.updateItemLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() + 1)));
                dtoResponse.updateItemStatus(ItemStatus.SUCCESS);
            } else {
                ItemStatus status = destroyItem(user);
                dtoResponse.updateItemStatus(status);
            }
        } catch (ItemLevelZeroException e) {
            dtoResponse.updateItemStatus(ItemStatus.DESTROY);
        }
        dtoResponse.setItemName(item.getItemName());
        dtoResponse.setLevel(item.getLevel());
        itemRepository.save(item);
        log.info("Enhanced Item: {}", item);
        return dtoResponse;
    }

    public ItemStatus destroyItem(User user) {
        Optional<Item> optionalItem = itemRepository.findByUser(user);
        Item item;
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else {
            throw new UsernameNotFoundException("user not found");
        }

        ItemLevel level = item.getLevel();
        DestroyProbability probability = DestroyProbability.valueOf("LEVEL_" + level.getLevel());

        //파괴시도
        boolean isDestroyed = random.nextDouble() < probability.getProbability();

        if (isDestroyed) {
            item.updateItemLevel(ItemLevel.valueOf("LEVEL_" + 0));
            item.updateItemStatus(ItemStatus.DESTROY);
            log.info("Destroyed Item: {}", item);
            throw new ItemLevelZeroException("Item level has dropped to zero and the item is destroyed.");
        } else {
            if(level.getLevel() > 0) {
                item.updateItemLevel(ItemLevel.valueOf("LEVEL_" + (level.getLevel() - 1)));
                item.updateItemStatus(ItemStatus.FAIL);
                log.info("Fail to enhance Item: {}", item);
            }
        }
        return item.getItemStatus();
    }

    public ResponseItemDTO findItem(String token) {
        User user = getUserByToken(token);
        Optional<Item> optionalItem = itemRepository.findByUser(user);
        Item item;
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else {
            throw new UsernameNotFoundException("user not found");
        }

        ResponseItemDTO dtoResponse = ResponseItemDTO.builder()
                .itemName(item.getItemName())
                .level(item.getLevel())
                .itemStatus(ItemStatus.SUCCESS)
                .build();
        return dtoResponse;
    }

    private User getUserByToken(String token) {
        String userEmail = jwtUtil.getUserEmail(token);
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<ResponseItemDTO> getTop10Items() {
        List<Item> top10ByOrderByLevelDesc = itemRepository.findTop10ByOrderByLevelDesc();
        List<ResponseItemDTO> responseItemDTOS = new ArrayList<>();
        for (Item item : top10ByOrderByLevelDesc) {
            ResponseItemDTO responseItemDTO = ResponseItemDTO.builder()
                    .itemName(item.getItemName())
                    .itemStatus(ItemStatus.SUCCESS)
                    .level(item.getLevel())
                    .build();
            responseItemDTOS.add(responseItemDTO);
        }
        return responseItemDTOS;
    }
}
