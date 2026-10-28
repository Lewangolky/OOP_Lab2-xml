package com.oop.knives.sort;

import com.oop.knives.model.Knife;

import java.util.Comparator;

/**
 * Ready-made orderings of knives.
 */
public enum KnifeSort implements Comparator<Knife> {

    BY_ID(Comparator.comparing(Knife::getId)),

    BY_TYPE(Comparator.comparing(Knife::getType)
            .thenComparing(Knife::getId)),

    BY_ORIGIN(Comparator.comparing(Knife::getOrigin)),

    BY_BLADE_LENGTH(Comparator.comparingInt((Knife knife) -> knife.getVisual().getBlade().getLength())
            .thenComparing(Knife::getId)),

    /** Collectible knives first, longer blades first within each group. */
    COLLECTIBLE_FIRST(Comparator.comparing(Knife::isCollectible).reversed()
            .thenComparing(Comparator.comparingInt((Knife knife) -> knife.getVisual().getBlade().getLength()).reversed())
            .thenComparing(Knife::getId));

    private final Comparator<Knife> comparator;

    KnifeSort(Comparator<Knife> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(Knife first, Knife second) {
        return comparator.compare(first, second);
    }
}
