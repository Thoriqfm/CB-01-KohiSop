package kohisop.service;

import kohisop.model.membership.Member;
import kohisop.model.currency.MataUang;
import kohisop.model.entities.ItemPesanan;
import kohisop.model.entities.Pesanan;
import kohisop.model.payment.Emoney;
import kohisop.model.payment.MetodePembayaran;
import kohisop.model.payment.Qris;

import java.util.ArrayList;

/**
 * Service untuk menangani logika bisnis pembayaran
 * Memisahkan business logic dari UI (View)
 */
public class PembayaranService {
    
    /**
     * Data class untuk menyimpan hasil proses pembayaran
     */
    public static class HasilPembayaran {
        public boolean berhasil;
        public String pesan;
        public String kuitansi;
        public double saldoAkhir;
        public int poinTotal;
        
        public HasilPembayaran(boolean berhasil, String pesan, String kuitansi, double saldoAkhir, int poinTotal) {
            this.berhasil = berhasil;
            this.pesan = pesan;
            this.kuitansi = kuitansi;
            this.saldoAkhir = saldoAkhir;
            this.poinTotal = poinTotal;
        }
    }
    
    /**
     * Validasi input saldo
     */
    public void validasiSaldo(String saldoText) throws IllegalArgumentException {
        try {
            double saldo = Double.parseDouble(saldoText);
            if (saldo < 0) {
                throw new IllegalArgumentException("Saldo tidak boleh negatif!");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Saldo harus berupa angka!");
        }
    }
    
    /**
     * Validasi metode pembayaran dan mata uang sudah dipilih
     */
    public void validasiPilihan(MetodePembayaran metodePembayaran, MataUang mataUang) 
            throws IllegalArgumentException {
        if (metodePembayaran == null || mataUang == null) {
            throw new IllegalArgumentException("Pilih metode pembayaran dan mata uang!");
        }
    }
    
    /**
     * Validasi saldo cukup untuk pembayaran
     */
    public void validasiSaldoCukup(double saldo, double totalBelanja, 
                                     MetodePembayaran metodePembayaran) throws IllegalArgumentException {
        // Hitung total yang sebenarnya harus dibayarkan
        double totalYangHarusDibayar = Math.round(metodePembayaran.hitungTotalSetelahDiskon(totalBelanja) * 100.0) / 100.0;
        double saldoAwalRounded = Math.round(saldo * 100.0) / 100.0;
        
        if (saldoAwalRounded < totalYangHarusDibayar) {
            String error = String.format(
                "Saldo tidak cukup!\n\n" +
                "Total Belanja: Rp%.2f\n" +
                "Total Bayar: Rp%.2f\n" +
                "Saldo Anda: Rp%.2f\n" +
                "Kurang: Rp%.2f",
                totalBelanja, totalYangHarusDibayar, saldoAwalRounded, 
                totalYangHarusDibayar - saldoAwalRounded
            );
            throw new IllegalArgumentException(error);
        }
    }
    
    /**
     * Proses pembayaran dan generate hasil transaksi
     */
    public HasilPembayaran prosesPembayaran(Member currentMember, 
                                             ArrayList<ItemPesanan> pesananItems,
                                             double totalBelanja,
                                             double saldo,
                                             MetodePembayaran metodePembayaran,
                                             MataUang mataUang) {
        try {
            // Buat pesanan object
            Pesanan pesanan = new Pesanan();
            if (currentMember != null) {
                pesanan.setMember(currentMember);
            }
            
            for (ItemPesanan item : pesananItems) {
                pesanan.tambahItem(item);
            }
            
            // Hitung poin
            int poinSebelum = currentMember != null ? currentMember.getPoin() : 0;
            int poinDidapat = 0;
            
            if (currentMember != null) {
                poinDidapat = (int) (totalBelanja / 10);
                if (currentMember.isPoinGanda()) {
                    poinDidapat *= 2;
                }
                currentMember.tambahPoin(totalBelanja);
            }
            
            // Generate kuitansi
            KuitansiService kuitansi = new KuitansiService(pesanan, metodePembayaran, mataUang, 0, poinSebelum, poinDidapat);
            String kuitansiText = kuitansi.generate();
            
            // Hitung total dan saldo akhir
            double totalSetelahDiskon = Math.round(metodePembayaran.hitungTotalSetelahDiskon(totalBelanja) * 100.0) / 100.0;
            double saldoAwalRounded = Math.round(saldo * 100.0) / 100.0;
            double saldoAkhir = (metodePembayaran.getDiskon() == 0 && metodePembayaran.getBiayaAdmin() == 0) 
                ? saldoAwalRounded - totalBelanja 
                : saldoAwalRounded - totalSetelahDiskon;
            
            // Buat pesan sukses
            String pesan = buatPesanSukses(totalBelanja, totalSetelahDiskon, 
                                           saldoAwalRounded, saldoAkhir, 
                                           poinDidapat, currentMember, metodePembayaran);
            
            return new HasilPembayaran(true, pesan, kuitansiText, saldoAkhir, currentMember != null ? currentMember.getPoin() : 0);
            
        } catch (Exception e) {
            return new HasilPembayaran(false, "Error: " + e.getMessage(), "", 0, 0);
        }
    }
    
    /**
     * Buat pesan detail transaksi berhasil
     */
    private String buatPesanSukses(double totalBelanja, double totalSetelahDiskon,
                                    double saldoAwal, double saldoAkhir,
                                    int poinDidapat, Member member, 
                                    MetodePembayaran metode) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Pembayaran berhasil diproses!\n\n");
        sb.append("═══════════════════════════════════\n");
        sb.append("DETAIL TRANSAKSI\n");
        sb.append("═══════════════════════════════════\n");
        sb.append(String.format("Total Belanja: Rp%.2f\n", totalBelanja));
        
        if (metode.getDiskon() > 0) {
            sb.append(String.format("Diskon (%s): Rp%.2f\n", metode.getNama(), 
                                   totalBelanja - totalSetelahDiskon));
        }
        if (metode.getBiayaAdmin() > 0) {
            sb.append(String.format("Biaya Admin (%s): Rp%.2f\n", metode.getNama(), 
                                   metode.getBiayaAdmin()));
        }
        
        sb.append(String.format("Total Bayar: Rp%.2f\n", totalSetelahDiskon));
        sb.append("───────────────────────────────────\n");
        sb.append("SALDO\n");
        sb.append("───────────────────────────────────\n");
        sb.append(String.format("Saldo Awal: Rp%.2f\n", saldoAwal));
        sb.append(String.format("Saldo Akhir: Rp%.2f\n", Math.max(0, saldoAkhir)));
        sb.append(String.format("Pengurangan: Rp%.2f\n", totalSetelahDiskon));
        
        if (member != null) {
            sb.append("───────────────────────────────────\n");
            sb.append("POIN\n");
            sb.append("───────────────────────────────────\n");
            sb.append(String.format("Poin Didapat: %d\n", poinDidapat));
            sb.append(String.format("Total Poin: %d\n", member.getPoin()));
        }
        
        sb.append("═══════════════════════════════════");
        
        return sb.toString();
    }
}
