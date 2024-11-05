package models;

public class WeekendRate extends Tour {
    public WeekendRate(String name, double price, String city) {
        super(name, price, city);
    }

    @Override
    public void displayDetails() {
        System.out.println("Weekend Tour - " + getName() + " in " + getCity() + " costs $" + getPrice());
    }
}
