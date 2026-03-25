/**
 * UseCase2RoomInitialization
 * --------------------------
 * Demonstrates object modeling using abstraction, inheritance,
 * polymorphism, and encapsulation in a Hotel Booking domain.
 *
 * This version initializes predefined room types and displays
 * their static availability.
 *
 * @author Neeraj
 * @version 2.1
 */

// Abstract class representing a generalized Room
abstract class Room {
    private int beds;
    private int size;
    private double pricePerNight;

    public Room(int beds, int size, double pricePerNight) {
        this.beds = beds;
        this.size = size;
        this.pricePerNight = pricePerNight;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getRoomType();

    // Common behavior
    public void displayRoomDetails() {
        System.out.println("Room Type       : " + getRoomType());
        System.out.println("Beds            : " + beds);
        System.out.println("Room Size (sqft): " + size);
        System.out.println("Price/Night     : ₹" + pricePerNight);
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 180, 2200);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 300, 3800);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 550, 7200);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// Application Entry Point
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App");
        System.out.println("     Hotel Booking System v2.1");
        System.out.println("======================================\n");

        // Polymorphic references
        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailable = 12;
        int doubleAvailable = 7;
        int suiteAvailable  = 3;

        // Display details
        System.out.println("---- Room Inventory ----\n");

        single.displayRoomDetails();
        System.out.println("Availability    : " + singleAvailable + " rooms\n");

        doub.displayRoomDetails();
        System.out.println("Availability    : " + doubleAvailable + " rooms\n");

        suite.displayRoomDetails();
        System.out.println("Availability    : " + suiteAvailable + " rooms\n");

        System.out.println("Application terminated successfully.");
    }
}
