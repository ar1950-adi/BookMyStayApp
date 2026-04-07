import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation Model
class Reservation {
    String reservationId;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return reservationId + " -> " + roomType + " (" + roomId + ")";
    }
}

// Inventory Service
class CancellationInventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public CancellationInventoryService() {
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void increaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void showInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Cancellation Service
class CancellationService {

    private Map<String, Reservation> bookings;
    private Stack<String> rollbackStack;
    private CancellationInventoryService inventory;

    public CancellationService(Map<String, Reservation> bookings,
                               CancellationInventoryService inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) throws CancellationException {

        // Validate existence
        if (!bookings.containsKey(reservationId)) {
            throw new CancellationException("Reservation not found: " + reservationId);
        }

        Reservation res = bookings.get(reservationId);

        // Push roomId to stack (for rollback tracking)
        rollbackStack.push(res.roomId);

        // Restore inventory
        inventory.increaseRoom(res.roomType);

        // Remove booking
        bookings.remove(reservationId);

        System.out.println("Cancelled: " + res);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + rollbackStack);
    }
}

// Main Class
public class UseCase10Cancellation {
    public static void main(String[] args) {

        // Existing bookings (simulating confirmed ones)
        Map<String, Reservation> bookings = new HashMap<>();

        bookings.put("R1", new Reservation("R1", "Deluxe", "D101"));
        bookings.put("R2", new Reservation("R2", "Suite", "S201"));

        CancellationInventoryService inventory = new CancellationInventoryService();

        CancellationService service = new CancellationService(bookings, inventory);

        try {
            service.cancelBooking("R1");  // valid
            service.cancelBooking("R3");  // invalid
        } catch (CancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }

        System.out.println();

        inventory.showInventory();
        service.showRollbackStack();
    }
}