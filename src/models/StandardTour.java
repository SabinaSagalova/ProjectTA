package models;

public class StandardTour extends Tour {
    private String hotel;

    public StandardTour(String name, double price, String city, String hotel) {
        super(name, price, city);
        this.hotel = hotel;
    }

    @Override
    public void displayDetails() {
        System.out.println("Standard Tour - " + getName() + " in " + getCity() + " costs $" + getPrice() + " with hotel: " + hotel);
    }

    public String getHotel() {
        return hotel;
    }
}
