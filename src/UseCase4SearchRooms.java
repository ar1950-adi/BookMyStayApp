import java.util.*;

abstract class Room {
    protected String type;
    protected int price;

    public Room(String type, int price) {
        this.type = type;
        this.price = price;
    }

    public String getType() { return type; }
    public int getPrice() { return price; }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single", 1000); }
    public void displayDetails() {
        System.out.println("Single Room - Price: " + price);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double", 2000); }
    public void displayDetails() {
        System.out.println("Double Room - Price: " + price);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 5000); }
    public void displayDetails() {
        System.out.println("Suite Room - Price: " + price);
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 0);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class SearchService {
    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {

        System.out.println("Available Rooms:\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("--------------------");
            }
        }
    }
}

public class UseCase4SearchRooms {
    public static void main(String[] args) {

        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        RoomInventory inventory = new RoomInventory();

        SearchService search = new SearchService();
        search.searchAvailableRooms(rooms, inventory);
    }
}