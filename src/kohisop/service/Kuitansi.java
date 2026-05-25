package kohisop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import kohisop.currency.MataUang;
import kohisop.model.ItemPesanan;
import kohisop.model.Pesanan;
import kohisop.payment.MetodePembayaran;

public class Kuitansi {

    private Pesanan pesanan;
    private MetodePembayaran metodePembayaran;
    private MataUang mataUang;

    public Kuitansi(Pesanan pesanan, MetodePembayaran metodePembayaran, MataUang mataUang) {
        this.pesanan = pesanan;
        this.metodePembayaran = metodePembayaran;
        this.mataUang = mataUang;
    }

    public void cetak() {
        System.out.println(formatHeader());
        System.out.println(formatBaris());
        System.out.println(formatFooter());
    }

    private String formatHeader() {
        String garis = "=".repeat(45);
        String garisKecil = "-".repeat(45);
        StringBuilder sb = new StringBuilder();

        sb.append(garis).append("\n");
        sb.append(String.format("%s%s%n", " ".repeat(15), "KOHI SOP CAFE"));
        sb.append(String.format("%s%s%n", " ".repeat(15), "Jl. Jalan Jalan No. 1"));
        sb.append(garis).append("\n");
        sb.append(String.format("%-20s %s%n", "ITEM", "QTY SUBTOTAL"));
        sb.append(garisKecil).append("\n");

        return sb.toString();
    }

    private String formatBaris() {
        StringBuilder sb = new StringBuilder();
        String garisKecil = "-".repeat(45);

        ArrayList<ItemPesanan> allPesanan = pesanan.getAllItem();
        int n = allPesanan.size();

        // ** Bubble sort
        for (int i = 0; i < n -1; i++) {
            for (int j = 0; j < n - 1; j++) {
                ItemPesanan item1 = allPesanan.get(j);
                ItemPesanan item2 = allPesanan.get(j + 1);

                String kategori1 = item1.getMenuItem().getKategori();
                String kategori2 = item2.getMenuItem().getKategori();
                boolean harusTukar = false;

                // Prioritas: Makanan di atas minuman
                if (kategori1.equalsIgnoreCase("Minuman") && kategori2.equalsIgnoreCase("Makanan")) {
                    harusTukar = true;
                }
                // Jika kategori sama, urutkan berdasarkan harga termurah
                else if (kategori1.equalsIgnoreCase(kategori2)) { 
                    if (item1.getMenuItem().getHarga() > item2.getMenuItem().getHarga()) {
                        harusTukar = true;
                    }
                }

                if (harusTukar) {
                    allPesanan.set(j, item2);
                    allPesanan.set(j + 1, item1);
                }
            }
        }

        String currentKategori = "";
        for (ItemPesanan item : allPesanan) {
            String kategoriItem = item.getMenuItem().getKategori();

            if (!kategoriItem.equalsIgnoreCase(currentKategori)) {
                sb.append(String.format("%s%n", "---" + kategoriItem.toUpperCase() + "---"));
                currentKategori = kategoriItem;
            }

            double subtotalIDR = item.getSubTotal();
            double subtotalKonversi = mataUang.konversiDariIDR(subtotalIDR);

            sb.append(String.format("%-22s%n", item.getMenuItem().getNama()));
            sb.append(String.format(" %-18s x%-3d %s%n", 
                    mataUang.format(mataUang.konversiDariIDR(item.getMenuItem().getHarga())), 
                    item.getKuantitas(), 
                    mataUang.format(subtotalKonversi)));

            double pajak = item.getTotalPajak();
            if (pajak > 0) {
                sb.append(String.format(" Pajak (%.0f%%) %s%n", (pajak / subtotalIDR * 100), mataUang.format(mataUang.konversiDariIDR(pajak))));
            }
        }
        sb.append(garisKecil).append("\n");
        return sb.toString();
    }

    private String formatItemsByCategory(java.util.ArrayList<ItemPesanan> items) {
        StringBuilder sb = new StringBuilder();

        for (ItemPesanan item : items) {
            double subtotalIDR = item.getSubTotal();
            double subtotalKonversi = mataUang.konversiDariIDR(subtotalIDR);

            sb.append(String.format("%-22s%n", item.getMenuItem().getNama()));
            sb.append(String.format(" %-18s x%-3d %s%n", mataUang.format(mataUang.konversiDariIDR(item.getMenuItem().getHarga())), item.getKuantitas(), mataUang.format(subtotalKonversi)));

            double pajak = item.getTotalPajak();
            if (pajak > 0) {
                sb.append(String.format(" Pajak (%.0f%%)%s%n", (pajak / item.getSubTotal() * 100), mataUang.format(mataUang.konversiDariIDR(pajak))));
            }
        }

        return sb.toString();
    }

    private String formatFooter() {
        String garis = "=".repeat(45);
        StringBuilder sb = new StringBuilder();

        double totalTanpaPajak = pesanan.getTotalTanpaPajak();
        double totalDenganPajak = pesanan.getTotalDenganPajak();
        double grandTotal = hitungGrandTotal();
        double diskon = totalDenganPajak * metodePembayaran.getDiskon();
        double biayaAdmin = metodePembayaran.getBiayaAdmin();

        sb.append(String.format("%-25s %s%n", "Subtotal:", mataUang.format(mataUang.konversiDariIDR(totalTanpaPajak))));
        sb.append(String.format("%-25s %s%n", "Pajak:", mataUang.format(mataUang.konversiDariIDR(totalDenganPajak - totalTanpaPajak))));

        if (diskon > 0) {
            sb.append(String.format("%-25s -%s%n", "Diskon (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(diskon))));
        }
        if (biayaAdmin > 0) {
            sb.append(String.format("%-25s %s%n", "Biaya Admin (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(biayaAdmin))));
        }

        sb.append(garis).append("\n");
        sb.append(String.format("%-25s %s%n", "GRAND TOTAL (" + mataUang.getKode() + "):", mataUang.format(mataUang.konversiDariIDR(grandTotal))));
        sb.append(garis).append("\n");
        sb.append(String.format("%-25s %s%n", "Metode Pembayaran:", metodePembayaran.getNama()));
        sb.append(garis).append("\n");
        sb.append(String.format("%s%s%n", " ".repeat(10), "Terima kasih sudah berkunjung!"));
        sb.append(garis).append("\n");

        return sb.toString();
    }

    private double hitungGrandTotal() {
        double totalDenganPajak = pesanan.getTotalDenganPajak();
        return metodePembayaran.hitungTotalSetelahDiskon(totalDenganPajak);
    }
}
