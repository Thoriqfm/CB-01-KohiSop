package kohisop.payment;

public class Qris implements MetodePembayaran {

    private double saldo;

    public Qris(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String getNama() {
        return "Qris";
    }

    @Override
    public double getDiskon() {
        // Diskon 5%
        return 0.05;
    }

    @Override
    public double getBiayaAdmin() {
        // Tidak ada biaya admin
        return 0;
    }

    @Override
    public boolean cekSaldoCukup(double totalIDR) {
        return saldo >= totalIDR - (totalIDR * getDiskon()) + getBiayaAdmin();
    }

    @Override
    public double hitungTotalSetelahDiskon(double totalIDR) {
        return totalIDR - (totalIDR * getDiskon()) + getBiayaAdmin();
    }

    public double getSaldo() {
        return saldo;
    }

    public void kurangiSaldo(double jumlah) {
        this.saldo -= jumlah;
    }

}
