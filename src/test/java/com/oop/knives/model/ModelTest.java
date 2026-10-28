package com.oop.knives.model;

import com.oop.knives.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {

    @Test
    void settersAndGettersFillKnife() {
        Blade blade = new Blade();
        blade.setLength(40);
        blade.setWidth(25);

        Handle handle = new Handle();
        handle.setMaterial(HandleMaterial.WOODEN);
        handle.setWood(Wood.BEECH);

        Visual visual = new Visual();
        visual.setBlade(blade);
        visual.setBladeMaterial(BladeMaterial.BRONZE);
        visual.setHandle(handle);
        visual.setFuller(true);

        Knife knife = new Knife();
        knife.setId("k9");
        knife.setType(WeaponType.STILETTO);
        knife.setHandy(Handy.ONE_HANDED);
        knife.setOrigin("Italy");
        knife.setVisual(visual);
        knife.setCollectible(true);

        assertEquals(40, knife.getVisual().getBlade().getLength());
        assertEquals(25, knife.getVisual().getBlade().getWidth());
        assertEquals(BladeMaterial.BRONZE, knife.getVisual().getBladeMaterial());
        assertEquals(HandleMaterial.WOODEN, knife.getVisual().getHandle().getMaterial());
        assertEquals(Wood.BEECH, knife.getVisual().getHandle().getWood());
        assertTrue(knife.getVisual().hasFuller());
        assertEquals("k9", knife.getId());
        assertEquals(WeaponType.STILETTO, knife.getType());
        assertEquals(Handy.ONE_HANDED, knife.getHandy());
        assertEquals("Italy", knife.getOrigin());
        assertTrue(knife.isCollectible());
    }

    @Test
    void equalKnivesHaveEqualHashCodes() {
        Knife first = TestData.ukrainianDagger();
        Knife second = TestData.ukrainianDagger();

        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.getVisual().hashCode(), second.getVisual().hashCode());
        assertEquals(first.getVisual().getBlade().hashCode(), second.getVisual().getBlade().hashCode());
        assertEquals(first.getVisual().getHandle().hashCode(), second.getVisual().getHandle().hashCode());
    }

    @Test
    void knivesWithDifferentFieldsAreNotEqual() {
        Knife knife = TestData.ukrainianDagger();

        assertNotEquals(knife, null);
        assertNotEquals(knife, "k1");
        assertNotEquals(knife, TestData.germanKnife());

        Knife otherId = TestData.ukrainianDagger();
        otherId.setId("k100");
        assertNotEquals(knife, otherId);

        Knife otherType = TestData.ukrainianDagger();
        otherType.setType(WeaponType.SABER);
        assertNotEquals(knife, otherType);

        Knife otherHandy = TestData.ukrainianDagger();
        otherHandy.setHandy(Handy.TWO_HANDED);
        assertNotEquals(knife, otherHandy);

        Knife otherOrigin = TestData.ukrainianDagger();
        otherOrigin.setOrigin("Poland");
        assertNotEquals(knife, otherOrigin);

        Knife otherVisual = TestData.ukrainianDagger();
        otherVisual.getVisual().setFuller(false);
        assertNotEquals(knife, otherVisual);

        Knife notCollectible = TestData.ukrainianDagger();
        notCollectible.setCollectible(false);
        assertNotEquals(knife, notCollectible);
    }

    @Test
    void nestedObjectsCompareAllFields() {
        Blade blade = new Blade(20, 30);
        assertEquals(blade, blade);
        assertNotEquals(blade, new Blade(20, 31));
        assertNotEquals(blade, new Blade(21, 30));
        assertNotEquals(blade, null);

        Handle handle = new Handle(HandleMaterial.WOODEN, Wood.ASH);
        assertEquals(handle, handle);
        assertNotEquals(handle, new Handle(HandleMaterial.WOODEN, Wood.OAK));
        assertNotEquals(handle, new Handle(HandleMaterial.METAL, Wood.ASH));
        assertNotEquals(handle, "handle");

        Visual visual = new Visual(blade, BladeMaterial.STEEL, handle, true);
        assertEquals(visual, visual);
        assertNotEquals(visual, new Visual(blade, BladeMaterial.STEEL, handle, false));
        assertNotEquals(visual, new Visual(blade, BladeMaterial.COPPER, handle, true));
        assertNotEquals(visual, new Visual(new Blade(1, 1), BladeMaterial.STEEL, handle, true));
        assertNotEquals(visual, new Visual(blade, BladeMaterial.STEEL, new Handle(HandleMaterial.METAL, null), true));
        assertNotEquals(visual, null);
    }

    @Test
    void toStringDescribesKnife() {
        String text = TestData.ukrainianDagger().toString();

        assertTrue(text.contains("k1"));
        assertTrue(text.contains("DAGGER"));
        assertTrue(text.contains("Ukraine"));
        assertTrue(text.contains("length=25cm"));
        assertTrue(text.contains("wood=OAK"));
    }

    @Test
    void toStringOmitsWoodOfNonWoodenHandle() {
        Handle handle = new Handle(HandleMaterial.PLASTIC, null);

        assertNull(handle.getWood());
        assertFalse(handle.toString().contains("wood"));
    }
}
