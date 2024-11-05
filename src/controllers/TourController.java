package controllers;

import factories.TourFactory;
import models.Tour;
import models.User;
import models.Transportation;
import services.TourService;
import services.PaymentService;
import services.UserService;
import payment.HalkBankPayment;
import payment.KaspiGoldPayment;
import payment.PaypalPayment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TourController {
    private TourService tourService = new TourService();
    private UserService userService = new UserService();
    private static final double TRANSPORTATION_COST = 100.0;

    // Sample hotels for premium tours
    private List<String> premiumHotels = List.of("The Grand Hotel", "Luxury Suites", "5-Star Resort");

    public void initializeTours() {
        tourService.addTour(TourFactory.createTour("standard", "City Break", 500, "Astana", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Luxury Escape", 1200, "Almaty", "5-star", ""));
        tourService.addTour(TourFactory.createTour("weekend", "Weekend Getaway", 300, "Shymkent", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Cultural Journey", 600, "Taldykorgan", "3-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Adventure in the Mountains", 1500, "Kokshetau", "5-star", ""));
        tourService.addTour(TourFactory.createTour("weekend", "Beach Relaxation", 400, "Aktau", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Historical Sites Tour", 700, "Taraz", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Gourmet Food Experience", 1300, "Almaty", "5-star", ""));
        tourService.addTour(TourFactory.createTour("weekend", "Nature Exploration", 350, "Semey", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Wildlife Safari", 800, "Atyrau", "3-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Luxury Cruise", 2000, "Astana", "5-star", ""));
        tourService.addTour(TourFactory.createTour("weekend", "Family Fun Trip", 500, "Shymkent", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Spa Retreat", 900, "Kokshetau", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Winter Wonderland", 1600, "Almaty", "5-star", ""));
        tourService.addTour(TourFactory.createTour("weekend", "City Adventure", 450, "Nur-Sultan", "", ""));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Tour Agency!");

        while (true) {
            System.out.println("Choose an option: 1. Register 2. Login 3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                userService.registerUser(username, password);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                User user = userService.authenticateUser(username, password);
                if (user != null) {
                    System.out.println("Login successful! Welcome, " + user.getUsername() + ".");
                    bookTour(scanner, user);
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void bookTour(Scanner scanner, User user) {
        System.out.print("Choose tour type (Standard/Premium/Weekend): ");
        String tourType = scanner.nextLine().toLowerCase();

        System.out.print("Enter your budget: ");
        double budget = scanner.nextDouble();
        scanner.nextLine();

        List<Tour> tours = tourService.findToursWithinBudget(budget);
        if (tours.isEmpty()) {
            System.out.println("No tours available within your budget.");
            return;
        }

        System.out.println("Available tours:");
        for (int i = 0; i < tours.size(); i++) {
            System.out.println((i + 1) + ". " + tours.get(i).getName() + " - " + tours.get(i).getCity() + " - $" + tours.get(i).getPrice());
        }

        System.out.print("Choose a tour by number: ");
        int tourChoice = scanner.nextInt();
        scanner.nextLine();
        Tour selectedTour = tours.get(tourChoice - 1);

        double initialBudget = budget;
        double totalCost = selectedTour.getPrice();
        String selectedHotel = null;

        // Check if the selected tour is premium to allow hotel selection
        if ("premium".equals(tourType)) {
            System.out.println("Available hotels for this premium tour:");
            for (int i = 0; i < premiumHotels.size(); i++) {
                System.out.println((i + 1) + ". " + premiumHotels.get(i));
            }
            System.out.print("Choose a hotel by number: ");
            int hotelChoice = scanner.nextInt();
            scanner.nextLine();
            selectedHotel = premiumHotels.get(hotelChoice - 1);
            System.out.println("You have selected the hotel: " + selectedHotel);
        }

        System.out.print("Would you like to include transportation for an additional $" + TRANSPORTATION_COST + "? (yes/no): ");
        String transportationChoice = scanner.nextLine().toLowerCase();
        Transportation selectedTransportation = null;

        if ("yes".equals(transportationChoice)) {
            totalCost += TRANSPORTATION_COST;
            selectedTransportation = new Transportation("Flight", TRANSPORTATION_COST, 2);
            selectedTour.addTransportation(selectedTransportation);
        }

        System.out.print("Choose payment method (HalkBank/KaspiGold/PayPal): ");
        String paymentMethod = scanner.nextLine();
        PaymentService paymentService;

        switch (paymentMethod.toLowerCase()) {
            case "halkbank":
                paymentService = new PaymentService(new HalkBankPayment());
                break;
            case "kaspigold":
                paymentService = new PaymentService(new KaspiGoldPayment());
                break;
            case "paypal":
                paymentService = new PaymentService(new PaypalPayment());
                break;
            default:
                System.out.println("Invalid payment method.");
                return;
        }

        boolean success = paymentService.processPayment(totalCost);
        if (success) {
            System.out.println("Your tour is successfully booked. Enjoy your trip!");

            // Save user data in a readable format in user.dat
            double remainingBudget = initialBudget - totalCost;
            saveUserData(user.getUsername(), selectedTour.getName(), initialBudget, remainingBudget, selectedTransportation, paymentMethod, selectedHotel);

        } else {
            System.out.println("Sorry, something went wrong. Please try again.");
        }
    }

    private void saveUserData(String username, String tourName, double initialBudget, double remainingBudget, Transportation transportation, String paymentMethod, String selectedHotel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.dat", true))) {
            writer.write("User: " + username);
            writer.newLine();
            writer.write("Tour Purchased: " + tourName);
            writer.newLine();
            writer.write("Initial Budget: $" + initialBudget);
            writer.newLine();
            writer.write("Remaining Budget: $" + remainingBudget);
            writer.newLine();
            writer.write("Selected Transportation: " + (transportation != null ? transportation.getMode() + " for $" + transportation.getPrice() : "No transportation selected"));
            writer.newLine();
            writer.write("Payment Method: " + paymentMethod);
            writer.newLine();
            if (selectedHotel != null) {
                writer.write("Selected Hotel: " + selectedHotel);
                writer.newLine();
            }
            writer.write("----------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
}
