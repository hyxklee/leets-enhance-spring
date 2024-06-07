package leets.enhance.dto;

import leets.enhance.domain.EnhancementProbability;
import leets.enhance.domain.ItemLevel;
import leets.enhance.domain.ItemStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ResponseItemDTO {
    private String itemName;

    private ItemLevel level;

    private ItemStatus itemStatus;

    private EnhancementProbability probability;

    public void updateItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
}
