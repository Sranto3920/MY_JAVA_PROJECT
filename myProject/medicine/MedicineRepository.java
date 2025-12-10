package myProject.medicine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MedicineRepository {
    private final ObservableList<Medicine> all = FXCollections.observableArrayList();
    private static final String CSV_FILE = "myProject/medicine/medicines.csv";

    public MedicineRepository() {
        loadData();
    }

    public ObservableList<Medicine> all() { return all; }

    public List<String> distinctNames() {
        return all.stream().map(Medicine::getName)
                .distinct().sorted().collect(Collectors.toList());
    }

    public Set<String> distinctLocations() {
        return new TreeSet<>(all.stream().map(Medicine::getLocation).collect(Collectors.toSet()));
    }

    public Set<String> distinctShops() {
        return new TreeSet<>(all.stream().map(Medicine::getShop).collect(Collectors.toSet()));
    }

    public Set<String> distinctBrands() {
        return new TreeSet<>(all.stream().map(Medicine::getBrand).collect(Collectors.toSet()));
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0].trim();
                    String brand = parts[1].trim();
                    String shop = parts[2].trim();
                    String location = parts[3].trim();
                    double price = Double.parseDouble(parts[4].trim());
                    all.add(new Medicine(name, brand, shop, location, price));
                }
            }
            System.out.println("Loaded " + all.size() + " medicines from CSV.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Warning: Could not load CSV (" + e.getMessage() + "). Using in-memory data.");
            seedInMemory();
        }
    }

    private void seedInMemory() {
        String[] locations = {"Dhaka", "Dinajpur", "Chankharpul"};
        String[] shops = {"Shop X", "Shop Y", "Shop Z"};
        String[] brands = {"BrandA", "BrandB", "BrandC"};
        String[] meds = {"Paracetamol", "Ibuprofen", "Cetirizine", "Omeprazole", "Azithromycin"};

        double base = 5.0;
        for (String med : meds) {
            for (String loc : locations) {
                for (int i = 0; i < shops.length; i++) {
                    String shop = shops[i];
                    String brand = brands[(i + med.length()) % brands.length];
                    double price = base + (i * 1.25) + (med.length() % 3);
                    all.add(new Medicine(med, brand, shop, loc, price));
                }
            }
        }
    }
}
