import java.util.*;

// Custom Exception for Booking Errors
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Reservation Entity
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
               ", Guest: " + guestName +
               ", Room Type: " + roomType +
               ", Room ID: " + roomId +
               ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory;
    private Map<String, Stack<String>> roomPool;

    public InventoryManager() {
        inventory = new HashMap<>();
        roomPool = new HashMap<>();

        // Initialize room types and IDs
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);

        roomPool.put("Standard", new Stack<>());
        roomPool.put("Deluxe", new Stack<>());

        roomPool.get("Standard").push("S1");
        roomPool.get("Standard").push("S2");
        roomPool.get("Deluxe").push("D1");
    }

    public String allocateRoom(String roomType) throws BookingException {
        if (!inventory.containsKey(roomType)) {
            throw new BookingException("Invalid room type.");
        }

        if (inventory.get(roomType) <= 0) {
            throw new BookingException("No rooms available.");
        }

        String roomId = roomPool.get(roomType).pop();
        inventory.put(roomType, inventory.get(roomType) - 1);
        return roomId;
    }

    public void releaseRoom(String roomType, String roomId) {
        roomPool.get(roomType).push(roomId); // LIFO rollback
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        System.out.println("\n--- Booking Records ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service
class CancellationService {

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              InventoryManager inventory) throws BookingException {

        Reservation reservation = history.getReservation(reservationId);

        // Validation
        if (reservation == null) {
            throw new BookingException("Reservation not found.");
        }

        if (reservation.isCancelled()) {
            throw new BookingException("Booking already cancelled.");
        }

        // Rollback (Controlled Mutation)
        inventory.releaseRoom(reservation.getRoomType(), reservation.getRoomId());

        // Update state
        reservation.cancel();
    }
}

// Main Class
public class UseCase10BookingCancellation {
    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        try {
            // Simulate booking
            String roomId1 = inventory.allocateRoom("Standard");
            Reservation r1 = new Reservation("RES201", "Alice", "Standard", roomId1);
            history.addReservation(r1);

            String roomId2 = inventory.allocateRoom("Deluxe");
            Reservation r2 = new Reservation("RES202", "Bob", "Deluxe", roomId2);
            history.addReservation(r2);

            System.out.println("Bookings Created Successfully!");

            // Display before cancellation
            history.displayAll();
            inventory.displayInventory();

            // Perform cancellation
            System.out.println("\nCancelling RES201...");
            cancellationService.cancelBooking("RES201", history, inventory);

            // Display after cancellation
            history.displayAll();
            inventory.displayInventory();

            // Attempt invalid cancellation
            System.out.println("\nAttempting duplicate cancellation...");
            cancellationService.cancelBooking("RES201", history, inventory);

        } catch (BookingException e) {
            System.out.println("\nOperation Failed: " + e.getMessage());
        }
    }
}
