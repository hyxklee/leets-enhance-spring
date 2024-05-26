package leets.enhance.repository;

import leets.enhance.domain.Item;
import leets.enhance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemByUserAndItemName(User user, String itemName);
}
