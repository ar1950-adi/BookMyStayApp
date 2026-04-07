import java.util.*;

// Booking Request
class ConcurrentBookingRequest {
    String guestName;
    String roomType;

    public ConcurrentBookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Inventory (Thread-Safe)
class ConcurrentInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public ConcurrentInventory() {
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Critical Section
    public synchronized boolean bookRoom(String roomType, String guestName) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " booking for " + guestName);

            inventory.put(roomType, available - 1);

            System.out.println("SUCCESS: " + guestName + " got " + roomType);
            return true;
        } else {
            System.out.println("FAILED: No " + roomType + " for " + guestName);
            return false;
        }
    }

    public void showInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private Queue<ConcurrentBookingRequest> queue;
    private ConcurrentInventory inventory;

    public BookingProcessor(Queue<ConcurrentBookingRequest> queue,
                            ConcurrentInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            ConcurrentBookingRequest request;

            // Critical section for queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.poll();
            }

            if (request != null) {
                inventory.bookRoom(request.roomType, request.guestName);
            }
        }
    }
}

// Main Class
public class UseCase11Concurrency {
    public static void main(String[] args) throws InterruptedException {

        Queue<ConcurrentBookingRequest> queue = new LinkedList<>();

        // Simulate multiple users
        queue.add(new ConcurrentBookingRequest("Adi", "Deluxe"));
        queue.add(new ConcurrentBookingRequest("Rahul", "Deluxe"));
        queue.add(new ConcurrentBookingRequest("Priya", "Suite"));
        queue.add(new ConcurrentBookingRequest("Amit", "Suite"));

        ConcurrentInventory inventory = new ConcurrentInventory();

        // Multiple threads
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println();
        inventory.showInventory();
    }
}