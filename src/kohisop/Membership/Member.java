package kohisop.Membership;

import java.util.Random;

public class Member {
    private String kodeMember;
    private String nama;
    private int poin;

    public Member(String nama) {
        this.nama = nama;
        this.kodeMember = generateKode();
        this.poin = 0;
    }

    // Generate 6 karakter alfanumerik acak (A-F, 0-9)
    private String generateKode() {
        String chars = "ABCDEF0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String getKodeMember() {
        return kodeMember;
    }

    public String getNama() {
        return nama;
    }

    public int getPoin() {
        return poin;
    }

    // Bebas pajak jika ada huruf "A"
    public boolean isBebasPajak() {
        return kodeMember.contains("A");
    }

    // Poin ganda jika ada huruf "A"
    public boolean isPoinGanda() {
        return kodeMember.contains("A");
    }

    // 1 poin untuk setiap kelipatan 10 IDR
    public void tambahPoin(double totalBelanjaIDR) {
        int tambahan = (int) (totalBelanjaIDR / 10);
        if (isPoinGanda()) {
            tambahan *= 2; 
        }
        this.poin += tambahan;
    }

    // 1 poin = 2 IDR. Mengembalikan nominal diskon IDR
    public double gunakanPoin(double totalTagihanIDR) {
        double nilaiPoinIDR = this.poin * 2.0;

        if (nilaiPoinIDR <= totalTagihanIDR) {
            double diskon = nilaiPoinIDR;
            this.poin = 0;
            return diskon;
        } else {
            int poinDibutuhkan = (int) Math.ceil(totalTagihanIDR / 2.0);
            this.poin -= poinDibutuhkan;
            return poinDibutuhkan * 2.0;
        }
    }
}