package leets.enhance.domain;

import lombok.Getter;

@Getter
public enum EnhancementProbability {
    LEVEL_0(1),
    LEVEL_1(0.9),
    LEVEL_2(0.8),
    LEVEL_3(0.7),
    LEVEL_4(0.5),
    LEVEL_5(0.4),
    LEVEL_6(0.1),
    LEVEL_7(0.03),
    LEVEL_8(0.03),
    LEVEL_9(0.03),
    LEVEL_10(0.03),
    LEVEL_11(0.03),
    LEVEL_12(0.03),
    LEVEL_13(0.03);

    private final double probability;

    EnhancementProbability(double probability) {
        this.probability = probability;
    }

}
