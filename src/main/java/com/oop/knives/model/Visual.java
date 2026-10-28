package com.oop.knives.model;

import java.util.Objects;

/**
 * Visual characteristics of a weapon.
 */
public class Visual {

    private Blade blade;
    private BladeMaterial bladeMaterial;
    private Handle handle;
    private boolean fuller;

    public Visual() {
    }

    public Visual(Blade blade, BladeMaterial bladeMaterial, Handle handle, boolean fuller) {
        this.blade = blade;
        this.bladeMaterial = bladeMaterial;
        this.handle = handle;
        this.fuller = fuller;
    }

    public Blade getBlade() {
        return blade;
    }

    public void setBlade(Blade blade) {
        this.blade = blade;
    }

    public BladeMaterial getBladeMaterial() {
        return bladeMaterial;
    }

    public void setBladeMaterial(BladeMaterial bladeMaterial) {
        this.bladeMaterial = bladeMaterial;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }

    public boolean hasFuller() {
        return fuller;
    }

    public void setFuller(boolean fuller) {
        this.fuller = fuller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visual other)) {
            return false;
        }
        return fuller == other.fuller
                && bladeMaterial == other.bladeMaterial
                && Objects.equals(blade, other.blade)
                && Objects.equals(handle, other.handle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blade, bladeMaterial, handle, fuller);
    }

    @Override
    public String toString() {
        return "Visual{blade=" + blade + ", bladeMaterial=" + bladeMaterial
                + ", handle=" + handle + ", fuller=" + fuller + "}";
    }
}
