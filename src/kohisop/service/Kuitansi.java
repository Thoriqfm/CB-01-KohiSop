package kohisop.service;

import kohisop.currency.MataUang;
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
        // TODO: implement logic cetak kuitansi
    }

    // TODO: private method (parameter, logic, etc)
    private String formatHeader() {
        return null; // TODO: implement logic format header
    }

    private String formatBaris() {
        return null; // TODO: implement logic format baris
    }

    private String formatFooter() {
        return null; // TODO: implement logic format footer
    }

    private double hitungGrandTotal() {
        return 0; // TODO: implement logic hitung grand total
    }
}
