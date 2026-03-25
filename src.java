import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages mapping between Reservation ID and Add-On Services
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).addAll(services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation ID: " + reservationId);
        for (AddOnService service : services) {
            System.out.println(" - " + service);
        }
    }
}

// Main Class
class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample reservation ID (assumed already created in previous use case)
        String reservationId = "RES123";

        // Predefined services
        List<AddOnService> availableServices = Arrays.asList(
                new AddOnService("Breakfast", 500),
                new AddOnService("Airport Pickup", 1200),
                new AddOnService("Extra Bed", 800),
                new AddOnService("Spa Access", 1500)
        );

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Available Add-On Services:");

        for (int i = 0; i < availableServices.size(); i++) {
            System.out.println((i + 1) + ". " + availableServices.get(i));
        }

        System.out.println("\nSelect services (enter numbers separated by space): ");
        String input = scanner.nextLine();
        String[] selections = input.split(" ");

        List<AddOnService> selectedServices = new ArrayList<>();

        for (String sel : selections) {
            try {
                int index = Integer.parseInt(sel) - 1;
                if (index >= 0 && index < availableServices.size()) {
                    selectedServices.add(availableServices.get(index));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + sel);
            }
        }

        // Add selected services to reservation
        manager.addServices(reservationId, selectedServices);

        // Display selected services
        System.out.println();
        manager.displayServices(reservationId);

        // Display total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        scanner.close();
    }
