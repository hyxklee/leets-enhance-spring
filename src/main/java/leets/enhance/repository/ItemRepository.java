package leets.enhance.repository;

import leets.enhance.domain.Item;
import leets.enhance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByUser(User user);

    @Query("SELECT i FROM Item i ORDER BY i.level DESC")
    List<Item> findTop10ByOrderByLevelDesc();
}
