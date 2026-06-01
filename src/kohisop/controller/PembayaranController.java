package kohisop.controller;

import kohisop.model.membership.Member;
import kohisop.model.currency.MataUang;
import kohisop.model.entities.ItemPesanan;
import kohisop.model.payment.MetodePembayaran;
import kohisop.service.PembayaranService;
import kohisop.service.PembayaranService.HasilPembayaran;

import java.util.ArrayList;

/**
 * Controller untuk menangani event pembayaran dari View
 * Menghubungkan View dengan Service Layer
 */
public class PembayaranController {
    
    private PembayaranService pembayaranService;
    
    // Dependency untuk data
    private Member currentMember;
    private ArrayList<ItemPesanan> pesananItems;
    private double totalBelanja;
    
    public PembayaranController() {
        this.pembayaranService = new PembayaranService();
    }
    
    /**
     * Set data pembayaran
     */
    public void setDataPembayaran(Member member, ArrayList<ItemPesanan> items, double total) {
        this.currentMember = member;
        this.pesananItems = items;
        this.totalBelanja = total;
    }
    
    /**
     * Handle button proses pembayaran
     * Dipanggil dari View ketika user click tombol "Proses Pembayaran"
     */
    public HasilPembayaran prosesPembayaran(String saldoText, MetodePembayaran metode, 
                                             MataUang mataUang) {
        try {
            // Validasi input
            pembayaranService.validasiSaldo(saldoText);
            
            // Validasi pilihan
            pembayaranService.validasiPilihan(metode, mataUang);
            
            // Konversi saldo
            double saldo = Double.parseDouble(saldoText);
            
            // Validasi saldo cukup
            pembayaranService.validasiSaldoCukup(saldo, totalBelanja, metode);
            
            // Proses pembayaran
            HasilPembayaran hasil = pembayaranService.prosesPembayaran(
                currentMember, pesananItems, totalBelanja, saldo, metode, mataUang
            );
            
            return hasil;
            
        } catch (IllegalArgumentException e) {
            return new HasilPembayaran(false, e.getMessage(), "", 0, 0);
        } catch (Exception e) {
            return new HasilPembayaran(false, "Terjadi error: " + e.getMessage(), "", 0, 0);
        }
    }
    
    /**
     * Get current member
     */
    public Member getCurrentMember() {
        return currentMember;
    }
    
    /**
     * Get total belanja
     */
    public double getTotalBelanja() {
        return totalBelanja;
    }
}
