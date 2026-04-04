package kohisop.payment;

public class Tunai implements MetodePembayaran {

    public Tunai() {
    }

    @Override
    public String getNama() {
        return "Tunai";
    }

    @Override
    public double getDiskon() {
        // Tidak ada diskon
        return 0;
    }

    @Override
    public double getBiayaAdmin() {
        // Tidak ada biaya admin
        return 0;
    }

    @Override
    public boolean cekSaldoCukup(double totalIDR) {
        // Tunai selalu cukup
        return true;
    }

    @Override
    public double hitungTotalSetelahDiskon(double totalIDR) {
        return totalIDR - (totalIDR * getDiskon()) + getBiayaAdmin();
    }

}
