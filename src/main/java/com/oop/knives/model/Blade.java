package com.oop.knives.model;

/**
 * Blade dimensions: length in centimetres, width in millimetres.
 */
public class Blade {

    private int length;
    private int width;

    public Blade() {
    }

    public Blade(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blade other)) {
            return false;
        }
        return length == other.length && width == other.width;
    }

    @Override
    public int hashCode() {
        return 31 * length + width;
    }

    @Override
    public String toString() {
        return "Blade{length=" + length + "cm, width=" + width + "mm}";
    }
}
