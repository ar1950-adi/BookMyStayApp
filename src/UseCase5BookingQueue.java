import java.io.*;
import java.util.*;

// Serializable Reservation
class PersistedReservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;

    public PersistedReservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Wrapper for full system state
class SystemState implements Serializable {
    Map<String, Integer> inventory;
    List<PersistedReservation> bookings;

    public SystemState(Map<String, Integer> inventory,
                       List<PersistedReservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // SAVE
    public static void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // LOAD
    public static SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting safe default.");
        }

        return new SystemState(new HashMap<>(), new ArrayList<>());
    }
}

// Main Class
public class UseCase12Persistence {
    public static void main(String[] args) {

        // STEP 1: Load existing state (simulate restart)
        SystemState state = PersistenceService.load();

        Map<String, Integer> inventory = state.inventory;
        List<PersistedReservation> bookings = state.bookings;

        // If first run → initialize
        if (inventory.isEmpty()) {
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
        }

        // STEP 2: Simulate new booking
        PersistedReservation newBooking =
                new PersistedReservation("R" + (bookings.size() + 1),
                        "Adi",
                        "Deluxe");

        bookings.add(newBooking);

        // Update inventory safely
        inventory.put("Deluxe", inventory.get("Deluxe") - 1);

        System.out.println("New Booking Added: " + newBooking);

        // STEP 3: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, bookings));

        // STEP 4: Show current state
        System.out.println("\nCurrent Inventory: " + inventory);

        System.out.println("\nBooking History:");
        for (PersistedReservation b : bookings) {
            System.out.println(b);
        }
    }
}