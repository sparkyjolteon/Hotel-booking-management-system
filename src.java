/**
 * UseCase4RoomSearch
 * ------------------
 * Demonstrates read-only search functionality where guests
 * can view available room options without modifying system state.
 *
 * Reinforces separation of concerns and safe data access.
 *
 * @author Neeraj
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

/* =======================
   Domain Model (Rooms)
   ======================= */

// Abstract Room
abstract class Room {
    private int beds;
    private int size;
    private double pricePerNight;

    public Room(int beds, int size, double pricePerNight) {
        this.beds = beds;
        this.size = size;
        this.pricePerNight = pricePerNight;
    }

    public int getBeds() { return beds; }
    public int getSize() { return size; }
    public double getPricePerNight() { return pricePerNight; }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type       : " + getRoomType());
        System.out.println("Beds            : " + beds);
        System.out.println("Room Size (sqft): " + size);
        System.out.println("Price/Night     : ₹" + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super(1, 180, 2200); }
    public String getRoomType() { return "Single Room"; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 300, 3800); }
    public String getRoomType() { return "Double Room"; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 550, 7200); }
    public String getRoomType() { return "Suite Room"; }
}


/* =======================
   Inventory (State Holder)
   ======================= */

class RoomInventory {

    private HashMap<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 12);
        availabilityMap.put("Double Room", 0); // Unavailable
        availabilityMap.put("Suite Room", 3);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return availabilityMap; // exposed read-only by convention
    }
}


/* =======================
   Search Service (Read Only)
   ======================= */

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms() {
        System.out.println("---- Available Rooms ----\n");

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {

            String type = entry.getKey();
            int count = entry.getValue();

            // Validation: show only available rooms
            if (count > 0) {

                Room room = createRoomByType(type);
                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Rooms : " + count);
                    System.out.println();
                }
            }
        }
    }

    // Factory method (domain usage)
    private Room createRoomByType(String type) {
        switch (type) {
            case "Single Room": return new SingleRoom();
            case "Double Room": return new DoubleRoom();
            case "Suite Room":  return new SuiteRoom();
            default: return null;
        }
    }
}


/* =======================
   Application Entry Point
   ======================= */

public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App");
        System.out.println("     Hotel Booking System v4.0");
        System.out.println("======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Guest initiates search
        RoomSearchService searchService = new RoomSearchService(inventory);
        searchService.searchAvailableRooms();

        System.out.println("Search completed. System state unchanged.");
    }
}
