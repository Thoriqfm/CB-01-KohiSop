package kohisop.model;

import java.util.ArrayList;
import kohisop.Membership.Member;

public class Pesanan {
    private ArrayList<ItemPesanan> daftarItem;
    private Member member; 

    public Pesanan() {
        this.daftarItem = new ArrayList<>();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public boolean isBebasPajak() {
        return member != null && member.isBebasPajak();
    }

    public boolean tambahItem(ItemPesanan item) {
        ItemPesanan existing = getItemByKode(item.getMenuItem().getKode());
        int maxKuantitas = (item.getMenuItem() instanceof Minuman) ? Minuman.MAX_KUANTITAS : Makanan.MAX_KUANTITAS;

        if (existing != null) {
            int totalBaru = existing.getKuantitas() + item.getKuantitas();
            if (totalBaru > maxKuantitas) return false;
            existing.setKuantitas(totalBaru);
        } else {
            String kategori = item.getMenuItem().getKategori();
            if (kategori.equalsIgnoreCase("Makanan") && getItemMakanan().size() >= 5) return false;
            else if (kategori.equalsIgnoreCase("Minuman") && getItemMinuman().size() >= 5) return false;
            
            if (item.getKuantitas() > maxKuantitas) return false;
            daftarItem.add(item);
        }
        return true;
    }

    public void hapusItem(String kode) {
        daftarItem.removeIf(item -> item.getMenuItem().getKode().equals(kode));
    }

    public ItemPesanan getItemByKode(String kode) {
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKode().equals(kode)) return item;
        }
        return null;
    }

    public ArrayList<ItemPesanan> getAllItem() { return daftarItem; }

    // Dikelompokkan jenis makanan lalu minuman, kemudian diurutkan berdasarkan harga
    public ArrayList<ItemPesanan> getSortedItems() {
        ArrayList<ItemPesanan> sorted = new ArrayList<>(daftarItem);
        sorted.sort((a, b) -> {
            int prioritasA = a.getMenuItem().getKategori().equals("Makanan") ? 0 : 1;
            int prioritasB = b.getMenuItem().getKategori().equals("Makanan") ? 0 : 1;
            
            if (prioritasA != prioritasB) {
                return Integer.compare(prioritasA, prioritasB);
            }
            return Double.compare(a.getMenuItem().getHarga(), b.getMenuItem().getHarga());
        });
        return sorted;
    }

    public ArrayList<ItemPesanan> getItemMinuman() {
        ArrayList<ItemPesanan> minuman = new ArrayList<>();
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKategori().equals("Minuman")) minuman.add(item);
        }
        return minuman;
    }

    public ArrayList<ItemPesanan> getItemMakanan() {
        ArrayList<ItemPesanan> makanan = new ArrayList<>();
        for (ItemPesanan item : daftarItem) {
            if (item.getMenuItem().getKategori().equals("Makanan")) makanan.add(item);
        }
        return makanan;
    }

    public double getTotalTanpaPajak() {
        double total = 0;
        for (ItemPesanan item : daftarItem) total += item.getSubTotal();
        return total;
    }

    public double getTotalDenganPajak() {
        double total = 0;
        boolean bebas = isBebasPajak();
        for (ItemPesanan item : daftarItem) total += item.getTotal(bebas);
        return total;
    }

    public boolean isEmpty() { return daftarItem.isEmpty(); }
    public void batalkan() { daftarItem.clear(); }
}