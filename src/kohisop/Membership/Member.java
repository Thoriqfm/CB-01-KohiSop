package kohisop.Membership;

import java.util.Random;

public class Member {
    private String kodeMember;
    private String nama;
    private int poin;

    public Member(String nama) {
        this.nama = nama;
        this.kodeMember = generateKode(); // Generate otomatis saat objek dibuat
        this.poin = 0;
    }

    // Peran 2: Generator 6 karakter alfanumerik acak (A-F, 0-9)
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

    // Peran 2: Mesin kalkulasi pajak (cek status "A")
    public boolean isBebasPajak() {
        return kodeMember.contains("A");
    }

    // Mengecek apakah berhak dapat poin ganda
    public boolean isPoinGanda() {
        return kodeMember.contains("A");
    }

    // Peran 2: Mengelola penambahan poin otomatis (1 poin tiap kelipatan 10)
    public void tambahPoin(double totalBelanjaIDR) {
        int tambahan = (int) (totalBelanjaIDR / 10);
        if (isPoinGanda()) {
            tambahan *= 2; 
        }
        this.poin += tambahan;
    }

    // Peran 2: Integrasi pemotongan (1 poin = 2 IDR)
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