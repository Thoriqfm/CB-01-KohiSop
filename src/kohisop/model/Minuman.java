package kohisop.model;

public class Minuman extends MenuItem{

    public static final int MAX_KUANTITAS = 3;

    public Minuman(String kode, String nama, double harga) { // TODO: Constructor untuk Minuman
        super(kode, nama, harga);
    }

    @Override
    public double hitungPajak() { // TODO: logic for pajak
        return 0;
    }

    @Override
    public String getKategori() {
        return "Minuman";
    }

    @Override
    public String toString() { // TODO: format string for information
        return null;
    }
    
}
