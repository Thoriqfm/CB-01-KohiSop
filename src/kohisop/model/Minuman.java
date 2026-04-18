package kohisop.model;

public class Minuman extends MenuItem {

    public static final int MAX_KUANTITAS = 3;

    public Minuman(String kode, String nama, double harga) { // TODO: Constructor untuk Minuman
        super(kode, nama, harga);
    }

    @Override
    public double hitungPajak() { // TODO: logic for pajak
        if (this.harga < 50) {
            return 0; // Tidak dikenakan pajak
        } else if (this.harga >= 50 && this.harga <= 55) {
            return this.harga * 0.08; // Dikenakan pajak 8%
        } else {
            return this.harga * 0.11; // Dikenakan pajak 11%
        }
    }

    @Override
    public String getKategori() {
        return "Minuman";
    }

    @Override
    public String toString() { // TODO: format string for information
        return String.format("%-3s %-35s %7s", this.kode, this.nama, "Rp" + String.format("%.0f", this.harga));
    }

}
