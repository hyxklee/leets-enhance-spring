package leets.enhance.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String itemName;

    private ItemLevel level;

    private ItemStatus itemStatus;

    @Builder
    public Item(User user, String itemName, ItemLevel level, ItemStatus itemStatus) {
        this.user = user;
        this.itemName = itemName;
        this.level = level;
        this.itemStatus = itemStatus;
    }
}
