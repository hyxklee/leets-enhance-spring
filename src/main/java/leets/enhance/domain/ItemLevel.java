package leets.enhance.domain;

public enum ItemLevel {
    DESTROYED(0, "Item is destroyed"),
    LEVEL_1(1, "Level 1"),
    LEVEL_2(2, "Level 2"),
    LEVEL_3(3, "Level 3"),
    LEVEL_4(4, "Level 4"),
    LEVEL_5(5, "Level 5"),
    LEVEL_6(6, "Level 6"),
    LEVEL_7(7, "Level 7"),
    LEVEL_8(8, "Level 8"),
    LEVEL_9(9, "Level 9"),
    LEVEL_10(10, "Level 10");

    private final int level;
    private final String description;

    ItemLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public static ItemLevel fromLevel(int level) {
        for (ItemLevel itemLevel : values()) {
            if (itemLevel.level == level) {
                return itemLevel;
            }
        }
        throw new IllegalArgumentException("Invalid level: " + level);
    }
}
