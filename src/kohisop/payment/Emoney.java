package kohisop.payment;

public class Emoney implements MetodePembayaran {

    private double saldo;
    public static final double BIAYA_ADMIN = 20.0;

    public Emoney(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String getNama() {
        return "E-Money";
    }

    @Override
    public double getDiskon() {
        // Diskon 7%
        return 0.07;
    }

    @Override
    public double getBiayaAdmin() {
        return BIAYA_ADMIN;
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
