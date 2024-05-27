package leets.enhance.dto;

import leets.enhance.domain.ItemLevel;
import leets.enhance.domain.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseItemDTO {
    private String itemName;

    private ItemLevel level;

    private ItemStatus itemStatus;
}
