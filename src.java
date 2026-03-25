import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Manages Room Inventory
class InventoryManager {
    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 0); // intentionally 0 to test validation
    }

    public boolean isValidRoomType(String roomType) {
        return roomInventory.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return roomInventory.getOrDefault(roomType, 0);
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        int available = getAvailableRooms(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        roomInventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Validates booking input
class InvalidBookingValidator {

    public void validate(String guestName, String roomType, InventoryManager inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new InvalidBookingException("Selected room type is fully booked.");
        }
    }
}

// Main Class
class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        InventoryManager inventory = new InventoryManager();
        InvalidBookingValidator validator = new InvalidBookingValidator();

        System.out.println("=== Book My Stay App ===");

        try {
            // Input
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Type (Standard/Deluxe/Suite): ");
            String roomType = scanner.nextLine();

            // Validation (Fail-Fast)
            validator.validate(guestName, roomType, inventory);

            // Allocation
            inventory.allocateRoom(roomType);

            // Create reservation
            String reservationId = "RES" + new Random().nextInt(1000);
            Reservation reservation = new Reservation(reservationId, guestName, roomType);

            System.out.println("\nBooking Confirmed!");
            System.out.println(reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("\nBooking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("\nUnexpected error occurred: " + e.getMessage());
        }

        // System continues safely
        inventory.displayInventory();

        scanner.close();
    }
}
