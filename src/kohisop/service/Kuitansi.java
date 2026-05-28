package kohisop.service;

import kohisop.Membership.Member;
import kohisop.currency.MataUang;
import kohisop.model.ItemPesanan;
import kohisop.model.Pesanan;
import kohisop.payment.MetodePembayaran;

public class Kuitansi {

    private Pesanan pesanan;
    private MetodePembayaran metodePembayaran;
    private MataUang mataUang;
    private double poinDipakaiIDR;
    private int poinSebelum;
    private int poinDidapat;

    public Kuitansi(Pesanan pesanan, MetodePembayaran metodePembayaran, MataUang mataUang, double poinDipakaiIDR, int poinSebelum, int poinDidapat) {
        this.pesanan = pesanan;
        this.metodePembayaran = metodePembayaran;
        this.mataUang = mataUang;
        this.poinDipakaiIDR = poinDipakaiIDR;
        this.poinSebelum = poinSebelum;
        this.poinDidapat = poinDidapat;
    }

    public void cetak() {
        System.out.println("=============================================");
        System.out.println("               KOHI SOP CAFE                 ");
        System.out.println("=============================================");

        boolean bebas = pesanan.isBebasPajak();
        String currentKategori = "";
        
        for (ItemPesanan item : pesanan.getSortedItems()) {
            String kategoriItem = item.getMenuItem().getKategori();

            if (!kategoriItem.equalsIgnoreCase(currentKategori)) {
                System.out.println("--- " + kategoriItem.toUpperCase() + " ---");
                currentKategori = kategoriItem;
            }

            double subtotalIDR = item.getSubTotal();
            double subtotalKonversi = mataUang.konversiDariIDR(subtotalIDR);

            System.out.printf("%-22s%n", item.getMenuItem().getNama());
            System.out.printf(" %-18s x%-3d %s%n", 
                    mataUang.format(mataUang.konversiDariIDR(item.getMenuItem().getHarga())), 
                    item.getKuantitas(), 
                    mataUang.format(subtotalKonversi));

            double pajak = item.getTotalPajak(bebas);
            if (pajak > 0) {
                System.out.printf(" Pajak (%.0f%%) %s%n", (pajak / subtotalIDR * 100), mataUang.format(mataUang.konversiDariIDR(pajak)));
            }
        }
        
        System.out.println("---------------------------------------------");
        
        double totalTanpaPajak = pesanan.getTotalTanpaPajak();
        double totalDenganPajak = pesanan.getTotalDenganPajak();
        double diskon = totalDenganPajak * metodePembayaran.getDiskon();
        double biayaAdmin = metodePembayaran.getBiayaAdmin();
        double totalSetelahChannel = metodePembayaran.hitungTotalSetelahDiskon(totalDenganPajak);

        System.out.printf("%-25s %s%n", "Subtotal:", mataUang.format(mataUang.konversiDariIDR(totalTanpaPajak)));
        System.out.printf("%-25s %s%n", "Pajak:", mataUang.format(mataUang.konversiDariIDR(totalDenganPajak - totalTanpaPajak)));

        if (diskon > 0) {
            System.out.printf("%-25s -%s%n", "Diskon (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(diskon)));
        }
        if (biayaAdmin > 0) {
            System.out.printf("%-25s %s%n", "Biaya Admin (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(biayaAdmin)));
        }
        if (poinDipakaiIDR > 0) {
            System.out.printf("%-25s -%s%n", "Diskon Poin:", mataUang.format(poinDipakaiIDR));
        }

        double grandTotal = mataUang.konversiDariIDR(totalSetelahChannel - poinDipakaiIDR);
        System.out.println("=============================================");
        System.out.printf("%-25s %s%n", "GRAND TOTAL (" + mataUang.getKode() + "):", mataUang.format(grandTotal));
        System.out.println("=============================================");
        System.out.printf("%-25s %s%n", "Metode Pembayaran:", metodePembayaran.getNama());
        System.out.println("=============================================");
        
        Member m = pesanan.getMember();
        if (m != null) {
            System.out.println("Member: " + m.getNama() + " (" + m.getKodeMember() + ")");
            System.out.println("Poin Awal       : " + poinSebelum);
            System.out.println("Poin Terpakai   : " + (int)(poinDipakaiIDR / 2.0));
            System.out.println("Poin Didapat    : " + poinDidapat);
            System.out.println("Total Poin Kini : " + m.getPoin());
            System.out.println("=============================================");
        }
        System.out.printf("%s%s%n", " ".repeat(10), "Terima kasih sudah berkunjung!");
    }
}