import java.util.*;

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notifyAll();
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Inventory Manager with synchronized critical section
class InventoryManager {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Critical Section
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate processing delay (to expose race conditions if unsynchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Worker Thread (Concurrent Booking Processor)
class BookingProcessor implements Runnable {
    private BookingQueue queue;
    private InventoryManager inventory;

    public BookingProcessor(BookingQueue queue, InventoryManager inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        // Each thread processes multiple requests
        for (int i = 0; i < 2; i++) {
            BookingRequest request = queue.getRequest();

            boolean success = inventory.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: Room booked for " + request.getGuestName() +
                        " (" + request.getRoomType() + ")");
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No room available for " + request.getGuestName() +
                        " (" + request.getRoomType() + ")");
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        InventoryManager inventory = new InventoryManager();

        // Simulate multiple booking requests
        queue.addRequest(new BookingRequest("Alice", "Standard"));
        queue.addRequest(new BookingRequest("Bob", "Standard"));
        queue.addRequest(new BookingRequest("Charlie", "Standard"));
        queue.addRequest(new BookingRequest("David", "Deluxe"));

        // Create multiple threads (guests booking concurrently)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final inventory state
        inventory.displayInventory();
    }
}
