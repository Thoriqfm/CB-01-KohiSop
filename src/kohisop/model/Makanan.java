package kohisop.model;

public class Makanan extends MenuItem{

    public static final int MAX_KUANTITAS = 2;

    public Makanan(String kode, String nama, double harga) {
        super(kode, nama, harga);
    }

    @Override
    public double hitungPajak() { // TODO: logic for pajak
        if (this.harga > 50) {
            return this.harga * 0.08;
        } else {
            return this.harga * 0.11;
        }
    }

    @Override
    public String getKategori() {
        return "Makanan";
    }

    @Override
    public String toString() { // TODO: format string for information
        return String.format("%s\t%s\t%.0f", this.kode, this.nama, this.harga);
    }

}
