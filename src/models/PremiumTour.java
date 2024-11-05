package models;

public class PremiumTour extends Tour {
    private String hotel;
    private String guide;

    public PremiumTour(String name, double price, String city, String hotel, String guide) {
        super(name, price, city);
        this.hotel = hotel;
        this.guide = guide;
    }

    @Override
    public void displayDetails() {
        System.out.println("Premium Tour - " + getName() + " in " + getCity() + " costs $" + getPrice() + " with hotel: " + hotel + " and guide: " + guide);
    }

    public String getHotel() {
        return hotel;
    }

    public String getGuide() {
        return guide;
    }
}