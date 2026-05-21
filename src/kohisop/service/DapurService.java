package kohisop.service;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import kohisop.model.ItemPesanan;
import kohisop.model.Pesanan;

public class DapurService {
    
    private PriorityQueue<ItemPesanan> antreanMakanan;
    private Stack<ItemPesanan> antreanMinuman;

    public DapurService() {
        Comparator<ItemPesanan> pembandingHarga = (a, b) -> Double.compare(b.getMenuItem().getHarga(), a.getMenuItem().getHarga());
        this.antreanMakanan = new PriorityQueue<>(pembandingHarga);
        this.antreanMinuman = new Stack<>();
    }

    public void tambahPesanan(Pesanan pesanan) {
        for (ItemPesanan item : pesanan.getAllItem()) {
            if (item.getMenuItem().getKategori().equalsIgnoreCase("Makanan")) {
                antreanMakanan.add(item);
            } else if (item.getMenuItem().getKategori().equalsIgnoreCase("Minuman")) {
                antreanMinuman.push(item);
            }
        }
    }

    public void prosesAntreanDapur() {
        System.out.println("\n===========================================");
        System.out.println("       PEMROSESAN ANTREAN DAPUR CAFE       ");
        System.out.println("===========================================");

        System.out.println("\n--- Membuat Makanan (Prioritas Harga Termahal) ---");
        if (antreanMakanan.isEmpty()) {
            System.out.println("[Dapur] tidak ada antrean makanan.");
        } else {
            while (!antreanMakanan.isEmpty()) {
                ItemPesanan makanan = antreanMakanan.poll();
                System.out.printf("[Dapur] Memasak: %s x%d (Harga Satuan: Rp%.0f)%n", 
                    makanan.getMenuItem().getNama(), 
                    makanan.getKuantitas(),
                    makanan.getMenuItem().getHarga()
                );
            }
        }

        System.out.println("\n--- MEMBUAT MINUMAN (Last-Ordered-First-Served) ---");
        if (antreanMakanan.isEmpty()) {
            System.out.println("[Dapur] Tidak ada antrean minuman.");
        } else {
            while (!antreanMakanan.isEmpty()) {
                ItemPesanan minuman = antreanMinuman.pop();
                System.out.printf("[Dapur] Meracik: %s x%d%n", 
                    minuman.getMenuItem().getNama(), 
                    minuman.getKuantitas()
                );
            }
        }
        System.out.println("===========================================\n");
    }
}
