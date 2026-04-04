package kohisop.currency;

public abstract class MataUang {
    protected String kode;
    protected String nama;
    protected double rate;

    public MataUang(String kode, String nama, double rate) {
        this.kode = kode;
        this.nama = nama;
        this.rate = rate;
    }

    public String getKode() {
        return kode;
    }
    
    public String getNama() {
        return nama;
    }

    public double getRate() {
        return rate;
    }

    public abstract double konversiDariIDR(double nominalIDR);

    public abstract String format(double nominal);
}
