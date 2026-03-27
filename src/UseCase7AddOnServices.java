import java.util.*;

// Add-On Service
class AddOnService {
    String serviceName;
    double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service " + service + " to Reservation " + reservationId);
    }

    // Show services for a reservation
    public void showServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services for Reservation " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println(s);
        }
    }

    // Calculate total cost
    public void calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        double total = 0;

        if (services != null) {
            for (AddOnService s : services) {
                total += s.price;
            }
        }

        System.out.println("Total Add-On Cost for " + reservationId + ": ₹" + total);
    }
}

// Main Class
public class UseCase7AddOnServices {
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example Reservation IDs (from UC6)
        String res1 = "DL-1234";
        String res2 = "SU-5678";

        // Add services
        manager.addService(res1, new AddOnService("Breakfast", 500));
        manager.addService(res1, new AddOnService("Spa", 1500));
        manager.addService(res2, new AddOnService("Airport Pickup", 800));

        // Show services
        manager.showServices(res1);
        manager.showServices(res2);

        // Calculate cost
        manager.calculateTotalCost(res1);
        manager.calculateTotalCost(res2);
    }
}