package kohisop.service;

import kohisop.model.entities.ItemPesanan;
import kohisop.model.entities.MenuItem;
import kohisop.model.entities.Pesanan;

import java.util.ArrayList;

/**
 * Service untuk menangani logika bisnis pesanan
 */
public class PesananService {
    
    /**
     * Hitung total belanja dari list item pesanan
     */
    public double hitungTotalBelanja(ArrayList<ItemPesanan> items) {
        double total = 0;
        for (ItemPesanan item : items) {
            total += item.getSubTotal();
        }
        return total;
    }
    
    /**
     * Validasi pesanan tidak kosong
     */
    public void validasiPesananTidakKosong(ArrayList<ItemPesanan> items) throws IllegalArgumentException {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Pesanan kosong!");
        }
    }
    
    /**
     * Buat item pesanan dari menu item dan quantity
     */
    public ItemPesanan buatItemPesanan(MenuItem menuItem, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity harus lebih dari 0!");
        }
        return new ItemPesanan(menuItem, quantity);
    }
    
    /**
     * Update quantity item pesanan
     */
    public void updateQuantity(ItemPesanan item, int newQuantity) throws IllegalArgumentException {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity tidak boleh negatif!");
        }
        item.setKuantitas(newQuantity);
    }
    
    /**
     * Hitung subtotal untuk satu item
     */
    public double hitungSubtotal(ItemPesanan item) {
        return item.getSubTotal();
    }
}
