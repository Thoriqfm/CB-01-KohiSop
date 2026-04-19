package kohisop.model;

import java.util.ArrayList;

public class Pesanan {

    private ArrayList<ItemPesanan> daftarItem;

    public Pesanan() {
        daftarItem = new ArrayList<>();
    }

    public boolean tambahItem(ItemPesanan item) {
        ItemPesanan existing = getItemByKode(item.getMenuItem().getKode());
        int maxKuantitas = getMaxKuantitas(item.getMenuItem());

        if (existing != null) {
            int totalBaru = existing.getKuantitas() + item.getKuantitas();

            if (totalBaru > maxKuantitas) {
                return false;
            }
            existing.setKuantitas(totalBaru);
        } else {
            if (item.getKuantitas() > maxKuantitas) {
                return false;
            }
            daftarItem.add(item);
        }
        return true;
    }

    private int getMaxKuantitas(MenuItem item) {
        return (item instanceof Minuman) ? Minuman.MAX_KUANTITAS : Makanan.MAX_KUANTITAS;
    }

    public void hapusItem(String kode) {
        daftarItem.removeIf(item -> item.getMenuItem().getKode().equals(kode));
    }

    public ItemPesanan getItemByKode(String kode) {
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKode().equals(kode)) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<ItemPesanan> getAllItem() {
        return daftarItem;
    }

    public ArrayList<ItemPesanan> getItemMinuman() {
        ArrayList<ItemPesanan> minuman = new ArrayList<>();
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKategori().equals("Minuman")) {
                minuman.add(item);
            }
        }
        return minuman;
    }

    public ArrayList<ItemPesanan> getItemMakanan() {
        ArrayList<ItemPesanan> makanan = new ArrayList<>();
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKategori().equals("Makanan")) {
                makanan.add(item);
            }
        }
        return makanan;
    }

    public double getTotalTanpaPajak() {
        double total = 0;
        for (ItemPesanan item : daftarItem) {
            total += item.getSubTotal();
        }
        return total;
    }

    public double getTotalDenganPajak() {
        double total = 0;
        for (ItemPesanan item : daftarItem) {
            total += item.getTotal();
        }
        return total;
    }

    public boolean isEmpty() {
        return daftarItem.isEmpty();
    }

    public void batalkan() {
        daftarItem.clear();
    }
}
