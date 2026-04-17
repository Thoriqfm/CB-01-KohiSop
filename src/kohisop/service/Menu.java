package kohisop.service;

import java.util.ArrayList;
import kohisop.model.*;

public class Menu {
    private ArrayList<MenuItem> daftarMenu;

    public Menu() {
        this.daftarMenu = new ArrayList<>();
        InitMenu();
    }

    private void InitMenu() {
        daftarMenu.add(new Minuman("A1", "Caffe Latte", 46));
        daftarMenu.add(new Minuman("A2", "Cappuccino", 46));
        daftarMenu.add(new Minuman("E1", "Caffe Americano", 37));
        daftarMenu.add(new Minuman("E2", "Caffe Mocha", 55));
        daftarMenu.add(new Minuman("E3", "Caramel Macchiato", 59));
        daftarMenu.add(new Minuman("E4", "Asian Dolce Latte", 55));
        daftarMenu.add(new Minuman("E5", "Double Shots Iced Shaken Espresso", 50));
        daftarMenu.add(new Minuman("B1", "Freshly Brewed Coffee", 23));
        daftarMenu.add(new Minuman("B2", "Vanilla Sweet Cream Cold Brew", 50));
        daftarMenu.add(new Minuman("B3", "Cold Brew", 44));

        daftarMenu.add(new Makanan("M1", "Petemania Pizza", 112));
        daftarMenu.add(new Makanan("M2", "Mie Rebus Super Mario", 35));
        daftarMenu.add(new Makanan("M3", "Ayam Bakar Goreng Rebus Spesial", 72));
        daftarMenu.add(new Makanan("M4", "Soto Kambing Iga Guling", 124));
        daftarMenu.add(new Makanan("S1", "Singkong Bakar A La Carte", 37));
        daftarMenu.add(new Makanan("S2", "Ubi Cilembu Bakar Arang", 58));
        daftarMenu.add(new Makanan("S3", "Tempe Mendoan", 18));
        daftarMenu.add(new Makanan("S4", "Tahu Bakso Extra Telur", 28));
    }

    public MenuItem cariByKode(String kode) {
        for (MenuItem item : daftarMenu) {
            if (item.getKode().equalsIgnoreCase(kode)) {
                return item;
            }
        }
        return null; // Mengembalikan null jika kode tidak ditemukan // TODO: implement logic for get by code
    }

    public boolean isKodeValid(String kode) {
        return cariByKode(kode) != null; // TODO: implement logic for valid code
    }

    public void tampilkanMenu() {
        System.out.println("=== DAFTAR MENU MINUMAN ===");
        for (MenuItem minuman : getDaftarMinuman()) {
            System.out.println(minuman.toString());
        }

        System.out.println("\n=== DAFTAR MENU MAKANAN ===");
        for (MenuItem makanan : getDaftarMakanan()) {
            System.out.println(makanan.toString());
        }
    }

    public ArrayList<MenuItem> getDaftarMinuman() {
        ArrayList<MenuItem> daftarMinuman = new ArrayList<>();
        for (MenuItem item : daftarMenu) {
            if (item instanceof Minuman) {
                daftarMinuman.add((Minuman) item);
            }
        }
        return daftarMinuman;
    }

    public ArrayList<MenuItem> getDaftarMakanan() {
        ArrayList<MenuItem> daftarMakanan = new ArrayList<>();
        for (MenuItem item : daftarMenu) {
            if (item instanceof Makanan) {
                daftarMakanan.add((Makanan) item);
            }
        }
        return daftarMakanan;
    }
}
