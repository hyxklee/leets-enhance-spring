package leets.enhance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String itemName;

    private ItemLevel level;

    @Builder
    public Item(User user, String itemName, ItemLevel level) {
        this.user = user;
        this.itemName = itemName;
        this.level = level;
    }
}
