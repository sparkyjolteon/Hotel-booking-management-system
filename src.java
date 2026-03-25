
import java.util.*;

/* =======================
   Reservation Model
   ======================= */

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}


/* =======================
   Booking Request Queue (FIFO)
   ======================= */

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}


/* =======================
   Inventory Service
   ======================= */

class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single Room", 2);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}


/* =======================
   Booking Service (Allocation Logic)
   ======================= */

class BookingService {

    private InventoryService inventory;

    // Track ALL allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track room IDs grouped by room type
    private Map<String, Set<String>> roomTypeToIds = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processRequest(Reservation r) {

        String type = r.getRoomType();
        System.out.println("\nProcessing booking for " + r.getGuestName());

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("❌ No rooms available for " + type);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(type);

        // Record allocation
        allocatedRoomIds.add(roomId);
        roomTypeToIds.putIfAbsent(type, new HashSet<>());
        roomTypeToIds.get(type).add(roomId);

        // Inventory synchronization
        inventory.decrement(type);

        // Confirmation
        System.out.println("✅ Booking Confirmed");
        System.out.println("Guest  : " + r.getGuestName());
        System.out.println("Type   : " + type);
        System.out.println("RoomID : " + roomId);
    }

    // Ensure uniqueness using Set
    private String generateRoomId(String type) {
        String prefix = type.replace(" ", "").substring(0, 2).toUpperCase();
        int number = 1;
        String id;

        do {
            id = prefix + number++;
        } while (allocatedRoomIds.contains(id));

        return id;
    }
}


/* =======================
   Application Entry Point
   ======================= */

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App");
        System.out.println("     Hotel Booking System v6.0");
        System.out.println("======================================");

        // Initialize services
        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);
        BookingRequestQueue queue = new BookingRequestQueue();

        // Incoming booking requests
        queue.addRequest(new Reservation("Amit", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Suite Room"));
        queue.addRequest(new Reservation("Rahul", "Single Room"));
        queue.addRequest(new Reservation("Sneha", "Single Room")); // exceeds stock

        // Process queue FIFO
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processRequest(r);
        }

        // Final inventory state
        inventory.displayInventory();

        System.out.println("\nAll requests processed safely.");
    }
}
