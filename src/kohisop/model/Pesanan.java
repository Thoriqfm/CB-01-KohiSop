package kohisop.model;

import java.util.ArrayList;

public class Pesanan {
    private ArrayList<ItemPesanan> daftarItem;

    public Pesanan() {
        // TODO: implementasi constructor
    }

    public void tambahItem(ItemPesanan item) {
        // TODO implementasi tambah item ke pesanan
    }

    public void hapusItem(String kode) {
        // TODO: implementasi hapus item dari pesanan
    }

    public ItemPesanan getItemByKode(String kode) {
        return null; // TODO: implement logic for get item by code
    }

    public ArrayList<ItemPesanan> getAllItem() {
        return null; // TODO: implement logic for get all items in order
    }

    public ArrayList<ItemPesanan> getItemMinuman() {
        return null; // TODO: implement logic for get all drink items in order
    }

    public ArrayList<ItemPesanan> getItemMakanan() {
        return null; // TODO: implement logic for get all food items in order
    }

    public double getTotalTanpaPajak() {
        return 0; // TODO: implement logic for get total price without tax
    }

    public double getTotalDenganPajak() {
        return 0; // TODO: implement logic for get total price with tax
    }

    public boolean isEmpty() {
        return false; // TODO: implement logic for check if order is empty
    }

    public void batalkan() {
        // TODO: implementasi logika untuk membatalkan pesanan
    }
}
