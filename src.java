

import java.util.HashMap;
import java.util.Map;

// Inventory Component
class RoomInventory {

    // Single Source of Truth
    private HashMap<String, Integer> availabilityMap;

    // Constructor initializes inventory
    public RoomInventory() {
        availabilityMap = new HashMap<>();

        // Register room types with available counts
        availabilityMap.put("Single Room", 12);
        availabilityMap.put("Double Room", 7);
        availabilityMap.put("Suite Room", 3);
    }

    // Retrieve availability
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    // Update availability (controlled method)
    public void updateAvailability(String roomType, int newCount) {
        if (availabilityMap.containsKey(roomType)) {
            availabilityMap.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Display full inventory state
    public void displayInventory() {
        System.out.println("---- Current Room Inventory ----");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
        System.out.println();
    }
}

// Application Entry Point
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App");
        System.out.println("     Hotel Booking System v3.1");
        System.out.println("======================================\n");

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial state
        inventory.displayInventory();

        // Example update operation
        System.out.println("Updating availability for Double Room...\n");
        inventory.updateAvailability("Double Room", 5);

        // Display updated state
        inventory.displayInventory();

        System.out.println("Application terminated successfully.");
    }
}
