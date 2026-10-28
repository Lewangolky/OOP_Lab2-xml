package com.oop.knives.sort;

import com.oop.knives.TestData;
import com.oop.knives.model.Knife;
import com.oop.knives.model.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KnifeSortTest {

    private List<Knife> knives;

    @BeforeEach
    void setUp() {
        knives = new ArrayList<>(List.of(
                TestData.knife("k3", WeaponType.SWORD, "Japan", 70, true),
                TestData.knife("k1", WeaponType.DAGGER, "Ukraine", 25, false),
                TestData.knife("k4", WeaponType.KNIFE, "Finland", 10, false),
                TestData.knife("k2", WeaponType.DAGGER, "Austria", 25, true)));
    }

    private List<String> idsSortedBy(KnifeSort sort) {
        knives.sort(sort);
        return knives.stream().map(Knife::getId).toList();
    }

    @Test
    void sortsById() {
        assertEquals(List.of("k1", "k2", "k3", "k4"), idsSortedBy(KnifeSort.BY_ID));
    }

    @Test
    void sortsByTypeThenId() {
        assertEquals(List.of("k4", "k1", "k2", "k3"), idsSortedBy(KnifeSort.BY_TYPE));
    }

    @Test
    void sortsByOrigin() {
        assertEquals(List.of("k2", "k4", "k3", "k1"), idsSortedBy(KnifeSort.BY_ORIGIN));
    }

    @Test
    void sortsByBladeLengthThenId() {
        assertEquals(List.of("k4", "k1", "k2", "k3"), idsSortedBy(KnifeSort.BY_BLADE_LENGTH));
    }

    @Test
    void putsCollectibleKnivesFirstWithLongestBladeFirst() {
        assertEquals(List.of("k3", "k2", "k1", "k4"), idsSortedBy(KnifeSort.COLLECTIBLE_FIRST));
    }

    @Test
    void comparatorIsConsistentForEqualKnives() {
        Knife knife = TestData.ukrainianDagger();

        for (KnifeSort sort : KnifeSort.values()) {
            assertEquals(0, sort.compare(knife, TestData.ukrainianDagger()), sort.name());
        }
    }
}
