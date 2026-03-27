import java.util.LinkedList;
import java.util.Queue;

// Reservation class (represents a booking request)
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

// Booking Queue Manager
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all requests
    public void showQueue() {
        System.out.println("\nCurrent Booking Queue:");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }
}

public class UseCase5BookingQueue {
    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        // Simulating booking requests
        bookingQueue.addRequest(new Reservation("Adi", "Deluxe"));
        bookingQueue.addRequest(new Reservation("Rahul", "Suite"));
        bookingQueue.addRequest(new Reservation("Priya", "Standard"));

        // Display queue
        bookingQueue.showQueue();
    }
}