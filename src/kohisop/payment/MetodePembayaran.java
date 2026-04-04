package kohisop.payment;

public interface MetodePembayaran {
    public String getNama();
    public double getDiskon();
    public double getBiayaAdmin();
    public boolean cekSaldoCukup(double totalIDR);
    public double hitungTotalSetelahDiskon(double totalIDR);
}
