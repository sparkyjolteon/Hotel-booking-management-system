/**
 * UseCase5BookingRequestQueue
 * ---------------------------
 * Demonstrates fair handling of booking requests using a Queue.
 * Requests are stored in arrival order (FIFO) without mutating inventory.
 *
 * @author Neeraj
 * @version 5.0
 */

import java.util.LinkedList;
import java.util.Queue;

/* =======================
   Reservation (Actor Model)
   ======================= */

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}


/* =======================
   Booking Request Queue
   ======================= */

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (intake)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View queue without processing
    public void displayQueue() {
        System.out.println("\n---- Booking Request Queue (FIFO Order) ----\n");

        if (requestQueue.isEmpty()) {
            System.out.println("No pending booking requests.");
            return;
        }

        int position = 1;
        for (Reservation r : requestQueue) {
            System.out.println("Position #" + position++);
            r.displayRequest();
            System.out.println();
        }
    }
}


/* =======================
   Application Entry Point
   ======================= */

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App");
        System.out.println("     Hotel Booking System v5.0");
        System.out.println("======================================\n");

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Amit", "Single Room");
        Reservation r2 = new Reservation("Priya", "Suite Room");
        Reservation r3 = new Reservation("Rahul", "Double Room");
        Reservation r4 = new Reservation("Sneha", "Single Room");

        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);
        queue.addRequest(r4);

        // Display queued requests (no allocation yet)
        queue.displayQueue();

        System.out.println("Requests queued successfully. Awaiting allocation.");
    }
}
