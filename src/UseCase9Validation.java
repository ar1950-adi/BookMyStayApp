import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request
class BookingInput {
    String guestName;
    String roomType;

    public BookingInput(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryValidatorService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryValidatorService() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void reduceRoom(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void showInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Validator
class BookingValidator {

    public static void validate(BookingInput input, InventoryValidatorService inventory)
            throws InvalidBookingException {

        if (input.guestName == null || input.guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(input.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + input.roomType);
        }

        if (!inventory.isAvailable(input.roomType)) {
            throw new InvalidBookingException("Room not available: " + input.roomType);
        }
    }
}

// Main Class
public class UseCase9Validation {
    public static void main(String[] args) {

        InventoryValidatorService inventory = new InventoryValidatorService();

        List<BookingInput> inputs = Arrays.asList(
                new BookingInput("Adi", "Deluxe"),
                new BookingInput("", "Suite"),              // invalid name
                new BookingInput("Rahul", "Penthouse"),     // invalid type
                new BookingInput("Priya", "Suite"),         // valid
                new BookingInput("Amit", "Suite")           // no availability
        );

        for (BookingInput input : inputs) {
            try {
                System.out.println("\nProcessing booking for: " + input.guestName);

                // Validate first (Fail-Fast)
                BookingValidator.validate(input, inventory);

                // If valid → proceed
                inventory.reduceRoom(input.roomType);

                System.out.println("Booking successful for " + input.guestName +
                        " (" + input.roomType + ")");

            } catch (InvalidBookingException e) {
                // Graceful failure
                System.out.println("Booking failed: " + e.getMessage());
            }
        }

        // Final inventory state
        System.out.println();
        inventory.showInventory();
    }
}