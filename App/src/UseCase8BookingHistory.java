import java.util.*;

// Booking Record (represents confirmed booking)
class BookingRecord {
    String reservationId;
    String guestName;
    String roomType;

    public BookingRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType;
    }
}

// Booking History (stores records)
class BookingHistory {

    private List<BookingRecord> history = new ArrayList<>();

    // Add confirmed booking
    public void addBooking(BookingRecord record) {
        history.add(record);
        System.out.println("Added to history: " + record);
    }

    // Get all bookings
    public List<BookingRecord> getAllBookings() {
        return history;
    }
}

// Report Service
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void showAllBookings() {
        System.out.println("\nBooking History:");
        for (BookingRecord record : history.getAllBookings()) {
            System.out.println(record);
        }
    }

    // Generate summary report
    public void generateSummary() {

        Map<String, Integer> countByRoomType = new HashMap<>();

        for (BookingRecord record : history.getAllBookings()) {
            countByRoomType.put(
                    record.roomType,
                    countByRoomType.getOrDefault(record.roomType, 0) + 1
            );
        }

        System.out.println("\nBooking Summary Report:");
        for (String type : countByRoomType.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByRoomType.get(type));
        }
    }
}

// Main Class
public class UseCase8BookingHistory {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings (from UC6)
        history.addBooking(new BookingRecord("DL-1111", "Adi", "Deluxe"));
        history.addBooking(new BookingRecord("SU-2222", "Rahul", "Suite"));
        history.addBooking(new BookingRecord("DL-3333", "Priya", "Deluxe"));

        // Reporting
        BookingReportService reportService = new BookingReportService(history);

        reportService.showAllBookings();
        reportService.generateSummary();
    }
}