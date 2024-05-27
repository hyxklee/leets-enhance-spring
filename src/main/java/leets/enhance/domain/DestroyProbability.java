package leets.enhance.domain;

import lombok.Getter;

@Getter
public enum DestroyProbability {

    LEVEL_0(0.05),
    LEVEL_1(0.05),
    LEVEL_2(0.05),
    LEVEL_3(0.05),
    LEVEL_4(0.1),
    LEVEL_5(0.15),
    LEVEL_6(0.2),
    LEVEL_7(0.25),
    LEVEL_8(0.3),
    LEVEL_9(0.35),
    LEVEL_10(0.4),
    LEVEL_11(0.45),
    LEVEL_12(0.5),
    LEVEL_13(0.5);
    private final double probability;

    DestroyProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}