package com.oop.knives.model;

import java.util.Objects;

/**
 * Handle of a weapon. {@link #getWood()} is set only for a wooden handle.
 */
public class Handle {

    private HandleMaterial material;
    private Wood wood;

    public Handle() {
    }

    public Handle(HandleMaterial material, Wood wood) {
        this.material = material;
        this.wood = wood;
    }

    public HandleMaterial getMaterial() {
        return material;
    }

    public void setMaterial(HandleMaterial material) {
        this.material = material;
    }

    public Wood getWood() {
        return wood;
    }

    public void setWood(Wood wood) {
        this.wood = wood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Handle other)) {
            return false;
        }
        return material == other.material && wood == other.wood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, wood);
    }

    @Override
    public String toString() {
        return "Handle{material=" + material + (wood == null ? "" : ", wood=" + wood) + "}";
    }
}
