package com.oop.knives.parser;

import com.oop.knives.model.Blade;
import com.oop.knives.model.BladeMaterial;
import com.oop.knives.model.Handle;
import com.oop.knives.model.HandleMaterial;
import com.oop.knives.model.Handy;
import com.oop.knives.model.Knife;
import com.oop.knives.model.Visual;
import com.oop.knives.model.WeaponType;
import com.oop.knives.model.Wood;
import com.oop.knives.model.XmlEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds {@link Knife} objects from a stream of start-element / characters / end-element
 * events. Every parser (DOM, SAX, StAX) only translates its own API into these events,
 * so the mapping from XML to objects lives in one place.
 */
final class KnifeAssembler {

    static final String ATTR_ID = "id";
    static final String ATTR_TYPE = "type";
    static final String ATTR_WOOD = "wood";

    private final List<Knife> knives = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();

    private Knife knife;
    private Visual visual;
    private Blade blade;
    private Handle handle;

    void startElement(String name, Map<String, String> attributes) {
        text.setLength(0);
        KnifeTag tag = KnifeTag.of(name);
        switch (tag) {
            case KNIFE -> {
                knife = new Knife();
                knife.setId(attributes.get(ATTR_ID));
                knife.setType(XmlEnum.fromValue(WeaponType.class, attributes.get(ATTR_TYPE)));
            }
            case VISUAL -> {
                visual = new Visual();
                require(knife, tag).setVisual(visual);
            }
            case BLADE -> {
                blade = new Blade();
                require(visual, tag).setBlade(blade);
            }
            case HANDLE -> {
                handle = new Handle();
                require(visual, tag).setHandle(handle);
            }
            case WOODEN, PLASTIC, METAL -> {
                require(handle, tag).setMaterial(XmlEnum.fromValue(HandleMaterial.class, tag.value()));
                String wood = attributes.get(ATTR_WOOD);
                if (wood != null) {
                    handle.setWood(XmlEnum.fromValue(Wood.class, wood));
                }
            }
            default -> {
                // value of a simple element is taken on its end tag
            }
        }
    }

    void characters(String chunk) {
        text.append(chunk);
    }

    void endElement(String name) {
        String value = text.toString().trim();
        text.setLength(0);
        KnifeTag tag = KnifeTag.of(name);
        switch (tag) {
            case HANDY -> require(knife, tag).setHandy(XmlEnum.fromValue(Handy.class, value));
            case ORIGIN -> require(knife, tag).setOrigin(value);
            case COLLECTIBLE -> require(knife, tag).setCollectible(parseBoolean(value));
            case LENGTH -> require(blade, tag).setLength(Integer.parseInt(value));
            case WIDTH -> require(blade, tag).setWidth(Integer.parseInt(value));
            case BLADE_MATERIAL -> require(visual, tag).setBladeMaterial(XmlEnum.fromValue(BladeMaterial.class, value));
            case FULLER -> require(visual, tag).setFuller(parseBoolean(value));
            case KNIFE -> {
                knives.add(knife);
                knife = null;
                visual = null;
                blade = null;
                handle = null;
            }
            default -> {
                // complex elements are completed by their children
            }
        }
    }

    List<Knife> getKnives() {
        return new ArrayList<>(knives);
    }

    private static <T> T require(T parent, KnifeTag tag) {
        if (parent == null) {
            throw new IllegalStateException("Element <" + tag.value() + "> is outside of its parent element");
        }
        return parent;
    }

    /**
     * Parses {@code xs:boolean}, which also allows {@code 1} and {@code 0}.
     */
    private static boolean parseBoolean(String value) {
        return switch (value) {
            case "true", "1" -> true;
            case "false", "0" -> false;
            default -> throw new IllegalArgumentException("Not a boolean value: '" + value + "'");
        };
    }
}
