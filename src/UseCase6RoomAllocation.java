import java.util.*;

// BookingRequest (renamed to avoid duplicate class issue)
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void reduceRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void showInventory() {
        System.out.println("\nRemaining Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {
    private Queue<BookingRequest> queue;
    private InventoryService inventoryService;

    // Track allocated rooms
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(Queue<BookingRequest> queue, InventoryService inventoryService) {
        this.queue = queue;
        this.inventoryService = inventoryService;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 4);
    }

    public void processBookings() {
        while (!queue.isEmpty()) {
            BookingRequest request = queue.poll();

            System.out.println("\nProcessing: " + request);

            if (inventoryService.isAvailable(request.roomType)) {

                String roomId;

                // Ensure uniqueness
                do {
                    roomId = generateRoomId(request.roomType);
                } while (allocatedRoomIds.contains(roomId));

                // Store allocation
                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(request.roomType, k -> new HashSet<>())
                        .add(roomId);

                // Reduce inventory
                inventoryService.reduceRoom(request.roomType);

                System.out.println("Booking Confirmed!");
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed (No rooms available)");
            }
        }
    }

    public void showAllocations() {
        System.out.println("\nFinal Room Allocations:");
        System.out.println(roomAllocations);
    }
}

// Main Class
public class UseCase6RoomAllocation {
    public static void main(String[] args) {

        // Step 1: Create queue
        Queue<BookingRequest> queue = new LinkedList<>();

        queue.add(new BookingRequest("Adi", "Deluxe"));
        queue.add(new BookingRequest("Rahul", "Suite"));
        queue.add(new BookingRequest("Priya", "Deluxe"));
        queue.add(new BookingRequest("Amit", "Deluxe")); // may fail

        // Step 2: Inventory
        InventoryService inventory = new InventoryService();

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(queue, inventory);

        // Step 4: Process bookings
        bookingService.processBookings();

        // Step 5: Show results
        bookingService.showAllocations();
        inventory.showInventory();
    }
}