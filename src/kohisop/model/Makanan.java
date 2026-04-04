package kohisop.model;

public class Makanan extends MenuItem{

    public static final int MAX_KUANTITAS = 2;

    public Makanan(String kode, String nama, double harga) {
        super(kode, nama, harga);
    }

    @Override
    public double hitungPajak() { // TODO: logic for pajak
        return 0;
    }

    @Override
    public String getKategori() {
        return "Makanan";
    }

    @Override
    public String toString() { // TODO: format string for information
        return null;
    }

}
