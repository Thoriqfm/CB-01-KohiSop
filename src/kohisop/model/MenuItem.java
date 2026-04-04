package kohisop.model;

public abstract class MenuItem {
    
    protected String kode;
    protected String nama;
    protected double harga;

    public MenuItem(String kode, String nama, double harga) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
    }

    public String getKode() {
        return this.kode;
    }

    public String getNama() {
        return this.nama;
    }

    public double getHarga() {
        return this.harga;
    }
    
    public abstract double hitungPajak();
    public abstract String getKategori();
    @Override
    public abstract String toString();
}
