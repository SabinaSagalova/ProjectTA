package models;

public class Transportation {
    private String mode;
    private double price;
    private int capacity;

    public Transportation(String mode, double price, int capacity) {
        this.mode = mode;
        this.price = price;
        this.capacity = capacity;
    }

    public String getMode() {
        return mode;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }
}
