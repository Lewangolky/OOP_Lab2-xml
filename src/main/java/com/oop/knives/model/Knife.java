package com.oop.knives.model;

import java.util.Objects;

/**
 * Cold weapon described by the {@code knife} element of {@code knives.xsd}.
 */
public class Knife {

    private String id;
    private WeaponType type;
    private Handy handy;
    private String origin;
    private Visual visual;
    private boolean collectible;

    public Knife() {
    }

    public Knife(String id, WeaponType type, Handy handy, String origin, Visual visual, boolean collectible) {
        this.id = id;
        this.type = type;
        this.handy = handy;
        this.origin = origin;
        this.visual = visual;
        this.collectible = collectible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WeaponType getType() {
        return type;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public Handy getHandy() {
        return handy;
    }

    public void setHandy(Handy handy) {
        this.handy = handy;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Visual getVisual() {
        return visual;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public void setCollectible(boolean collectible) {
        this.collectible = collectible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Knife other)) {
            return false;
        }
        return collectible == other.collectible
                && type == other.type
                && handy == other.handy
                && Objects.equals(id, other.id)
                && Objects.equals(origin, other.origin)
                && Objects.equals(visual, other.visual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, handy, origin, visual, collectible);
    }

    @Override
    public String toString() {
        return "Knife{id='" + id + "', type=" + type + ", handy=" + handy + ", origin='" + origin
                + "', visual=" + visual + ", collectible=" + collectible + "}";
    }
}
