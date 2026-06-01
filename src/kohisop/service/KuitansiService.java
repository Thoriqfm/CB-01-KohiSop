package kohisop.service;

import kohisop.model.membership.Member;
import kohisop.model.currency.MataUang;
import kohisop.model.entities.ItemPesanan;
import kohisop.model.entities.Pesanan;
import kohisop.model.payment.MetodePembayaran;

public class KuitansiService {

    private Pesanan pesanan;
    private MetodePembayaran metodePembayaran;
    private MataUang mataUang;
    private double poinDipakaiIDR;
    private int poinSebelum;
    private int poinDidapat;

    public KuitansiService(Pesanan pesanan, MetodePembayaran metodePembayaran, MataUang mataUang, double poinDipakaiIDR, int poinSebelum, int poinDidapat) {
        this.pesanan = pesanan;
        this.metodePembayaran = metodePembayaran;
        this.mataUang = mataUang;
        this.poinDipakaiIDR = poinDipakaiIDR;
        this.poinSebelum = poinSebelum;
        this.poinDidapat = poinDidapat;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================\n");
        sb.append("               KOHI SOP CAFE                 \n");
        sb.append("=============================================\n");

        boolean bebas = pesanan.isBebasPajak();
        String currentKategori = "";

        for (ItemPesanan item : pesanan.getSortedItems()) {
            String kategoriItem = item.getMenuItem().getKategori();

            if (!kategoriItem.equalsIgnoreCase(currentKategori)) {
                sb.append("--- ").append(kategoriItem.toUpperCase()).append(" ---\n");
                currentKategori = kategoriItem;
            }

            double subtotalIDR = item.getSubTotal();
            double subtotalKonversi = mataUang.konversiDariIDR(subtotalIDR);

            sb.append(String.format("%-22s%n", item.getMenuItem().getNama()));
            sb.append(String.format(" %-18s x%-3d %s%n",
                    mataUang.format(mataUang.konversiDariIDR(item.getMenuItem().getHarga())),
                    item.getKuantitas(),
                    mataUang.format(subtotalKonversi)));

            double pajak = item.getTotalPajak(bebas);
            if (pajak > 0) {
                sb.append(String.format(" Pajak (%.0f%%) %s%n", (pajak / subtotalIDR * 100), mataUang.format(mataUang.konversiDariIDR(pajak))));
            }
        }

        sb.append("---------------------------------------------\n");

        double totalTanpaPajak = pesanan.getTotalTanpaPajak();
        double totalDenganPajak = pesanan.getTotalDenganPajak();
        double diskon = totalDenganPajak * metodePembayaran.getDiskon();
        double biayaAdmin = metodePembayaran.getBiayaAdmin();
        double totalSetelahChannel = metodePembayaran.hitungTotalSetelahDiskon(totalDenganPajak);

        sb.append(String.format("%-25s %s%n", "Subtotal:", mataUang.format(mataUang.konversiDariIDR(totalTanpaPajak))));
        sb.append(String.format("%-25s %s%n", "Pajak:", mataUang.format(mataUang.konversiDariIDR(totalDenganPajak - totalTanpaPajak))));

        if (diskon > 0) {
            sb.append(String.format("%-25s -%s%n", "Diskon (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(diskon))));
        }
        if (biayaAdmin > 0) {
            sb.append(String.format("%-25s %s%n", "Biaya Admin (" + metodePembayaran.getNama() + "):", mataUang.format(mataUang.konversiDariIDR(biayaAdmin))));
        }
        if (poinDipakaiIDR > 0) {
            sb.append(String.format("%-25s -%s%n", "Diskon Poin:", mataUang.format(poinDipakaiIDR)));
        }

        double grandTotal = mataUang.konversiDariIDR(totalSetelahChannel - poinDipakaiIDR);
        sb.append("=============================================\n");
        sb.append(String.format("%-25s %s%n", "GRAND TOTAL (" + mataUang.getKode() + "):", mataUang.format(grandTotal)));
        sb.append("=============================================\n");
        sb.append(String.format("%-25s %s%n", "Metode Pembayaran:", metodePembayaran.getNama()));
        sb.append("=============================================\n");

        Member m = pesanan.getMember();
        if (m != null) {
            sb.append("Member: ").append(m.getNama()).append(" (").append(m.getKodeMember()).append(")\n");
            sb.append("Poin Awal       : ").append(poinSebelum).append("\n");
            sb.append("Poin Terpakai   : ").append((int) (poinDipakaiIDR / 2.0)).append("\n");
            sb.append("Poin Didapat    : ").append(poinDidapat).append("\n");
            sb.append("Total Poin Kini : ").append(m.getPoin()).append("\n");
            sb.append("=============================================\n");
        }
        sb.append(String.format("%s%s%n", " ".repeat(10), "Terima kasih sudah berkunjung!"));

        return sb.toString();
    }

    public void cetak() {
        System.out.print(generate());
    }
}
