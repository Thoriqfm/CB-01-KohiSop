package kohisop.app;

import java.util.LinkedList;
import java.util.Scanner;
import kohisop.Membership.Member;
import kohisop.currency.EUR;
import kohisop.currency.IDR;
import kohisop.currency.JPY;
import kohisop.currency.MYR;
import kohisop.currency.MataUang;
import kohisop.currency.USD;
import kohisop.model.ItemPesanan;
import kohisop.model.Makanan;
import kohisop.model.MenuItem;
import kohisop.model.Minuman;
import kohisop.model.Pesanan;
import kohisop.payment.Emoney;
import kohisop.payment.MetodePembayaran;
import kohisop.payment.Qris;
import kohisop.payment.Tunai;
import kohisop.service.DapurService;
import kohisop.service.Kuitansi;
import kohisop.service.Menu;

public class KohiApp {

    private Menu menu;
    private Pesanan pesanan;
    private LinkedList<Member> databaseMember; // Tambahan untuk menyimpan data member
    private Scanner scanner = new Scanner(System.in);

    // ANSI escape codes for colored output (for warning messages)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public KohiApp() {
        this.menu = new Menu();
        this.pesanan = new Pesanan();
        this.databaseMember = new LinkedList<>();

        // Initialize dengan beberapa member testing
        databaseMember.add(new Member("John Doe", "A12345"));  // Bebas pajak + Poin ganda
        databaseMember.add(new Member("Jane Smith", "B54321")); // Regular member
        databaseMember.add(new Member("Alex Brown", "ABCDEF")); // Bebas pajak + Poin ganda
    }

    public void jalankan() {
        int jumlahPelanggan = 0;
        DapurService dapurService = new DapurService();

        while (true) {
            this.pesanan = new Pesanan();

            System.out.println("=======================================");
            System.out.println("--- Selamat datang di KohiSop Cafe! ---");
            System.out.println("=======================================");

            // --- BAGIAN MEMBERSHIP ---
            System.out.print("Apakah Anda member? (Y/N/Daftar): ");
            String isMember = scanner.nextLine().trim().toUpperCase();

            if (isMember.equals("Y")) {
                System.out.print("Masukkan kode member: ");
                String kodeInput = scanner.nextLine().trim();
                Member memberFound = cariMember(kodeInput);
                if (memberFound != null) {
                    pesanan.setMember(memberFound);
                    System.out.println("Selamat datang kembali, " + memberFound.getNama() + "! Poin Anda: " + memberFound.getPoin());
                } else {
                    System.out.println(ANSI_RED_BACKGROUND + "Member tidak ditemukan. Melanjutkan sebagai Non-Member." + ANSI_RESET);
                }
            } else if (isMember.equals("DAFTAR")) {
                System.out.print("Masukkan nama Anda: ");
                String namaBaru = scanner.nextLine().trim();
                Member newMember = new Member(namaBaru);
                databaseMember.add(newMember);
                pesanan.setMember(newMember);
                System.out.println("Berhasil mendaftar! Kode Member Anda: " + newMember.getKodeMember());
            }

            // --- TAMPILKAN MENU & INPUT PESANAN ---
            menu.tampilkanMenu();
            inputPesanan();

            if (pesanan.isEmpty()) {
                System.out.println("Tidak ada pesanan. Terima kasih!");
                continue; // Lanjut ke pelanggan berikutnya
            }

            tampilkanTabelPesanan();

            MataUang matauang = pilihMataUang();
            boolean pembayaranBerhasil = false;

            while (true) {
                MetodePembayaran metodePembayaran = pilihMetodePembayaran();

                if (prosesPembayaran(metodePembayaran, matauang)) {
                    pembayaranBerhasil = true;
                    break;
                } else {
                    System.out.print("\nIngin mencoba metode pembayaran lain? (Y/N): ");
                    String jawab = scanner.nextLine().trim().toUpperCase();
                    if (!jawab.equals("Y")) {
                        System.out.println("Pembayaran dibatalkan.");
                        break;
                    }
                }
            }

            if (pembayaranBerhasil) {
                dapurService.tambahPesanan(pesanan);

                jumlahPelanggan++;
                System.out.println("\n[Sistem] Pelanggan ke-" + jumlahPelanggan + " berhasil dilayani.");

                if (jumlahPelanggan % 3 == 0) {
                    dapurService.prosesAntreanDapur();
                }
            }
        }
    }

    private Member cariMember(String kode) {
        for (Member m : databaseMember) {
            if (m.getKodeMember().equalsIgnoreCase(kode)) {
                return m;
            }
        }
        return null;
    }

    private void inputPesanan() {
        while (true) {
            System.out.print("\nMasukkan kode menu (atau 'selesai' untuk selesai): ");
            String kode = scanner.nextLine().trim();

            if (kode.equalsIgnoreCase("CC")) {
                System.out.println("Pesanan dibatalkan. Program dihentikan.");
                System.exit(0);
            }

            if (kode.equalsIgnoreCase("selesai")) {
                break;
            }

            if (!menu.isKodeValid(kode)) {
                System.out.println(ANSI_RED_BACKGROUND + "Kode menu tidak valid. Silakan coba lagi." + ANSI_RESET);
                continue;
            }

            inputKuantitas(kode);
        }
    }

    private void inputKuantitas(String kode) {
        MenuItem item = menu.cariByKode(kode);
        int max = (item instanceof Minuman) ? Minuman.MAX_KUANTITAS : Makanan.MAX_KUANTITAS;

        boolean isValid = false;

        while (!isValid) {
            System.out.print("Masukkan kuantitas (maks " + max + ", ketik 'S' atau '0' untuk skip): ");
            String input = scanner.nextLine().trim();

            // Memenuhi syarat "S" (skip) atau 0 dari studi kasus KohiSop II
            if (input.equalsIgnoreCase("S") || input.equals("0")) {
                System.out.println("Pesanan untuk item " + item.getNama() + " dibatalkan.");
                return;
            }

            try {
                // Memenuhi syarat "Secara default adalah satu porsi" jika input kosong
                int kuantitas = input.isEmpty() ? 1 : Integer.parseInt(input);

                if (kuantitas < 1 || kuantitas > max) {
                    System.out.println(ANSI_RED_BACKGROUND + "Kuantitas harus antara 1 - " + max + ANSI_RESET);
                    continue;
                }

                if (pesanan.tambahItem(new ItemPesanan(item, kuantitas))) {
                    System.out.println(item.getNama() + " x" + kuantitas + " ditambahkan ke pesanan.");
                    isValid = true;
                } else {
                    String kategori = item.getKategori();
                    int jumlahJenis = kategori.equalsIgnoreCase("Makanan")
                            ? pesanan.getItemMakanan().size()
                            : pesanan.getItemMinuman().size();

                    if (jumlahJenis >= 5 && pesanan.getItemByKode(item.getKode()) == null) {
                        System.out.println(ANSI_RED_BACKGROUND + "Tidak bisa menambah! Keranjang untuk kategori "
                                + kategori + " sudah mencapai batas maksimal 5 jenis." + ANSI_RESET);
                    } else {
                        System.out.println(ANSI_RED_BACKGROUND + "Tidak bisa menambah! Total akan melebihi maksimal "
                                + max + " untuk item ini." + ANSI_RESET);
                    }
                    isValid = true; // Keluar dari loop agar tidak terperangkap
                }

            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED_BACKGROUND + "Input kuantitas tidak valid. Harap masukkan angka." + ANSI_RESET);
            }
        }
    }

    private void tampilkanTabelPesanan() {
        System.out.println("\n--- PESANAN ANDA ---");
        for (ItemPesanan item : pesanan.getAllItem()) {
            System.out.printf("%-30s x%d%n", item.getMenuItem().getNama(), item.getKuantitas());
        }
    }

    private MetodePembayaran pilihMetodePembayaran() {
        while (true) {
            System.out.println("\nPilih metode pembayaran:");
            System.out.println("1. Tunai");
            System.out.println("2. QRIS (diskon 5%)");
            System.out.println("3. E-Money (diskon 7%)");
            System.out.print("Pilihan (1 - 3): ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    return new Tunai();
                case "2":
                    try {
                        System.out.print("Masukkan saldo QRIS: ");
                        double saldoQRIS = Double.parseDouble(scanner.nextLine().trim());
                        return new Qris(saldoQRIS);
                    } catch (NumberFormatException e) {
                        System.out.println(ANSI_RED_BACKGROUND + "Input saldo QRIS tidak valid. Harap masukkan angka." + ANSI_RESET);
                    }
                    break;
                case "3":
                    try {
                        System.out.print("Masukkan saldo E-Money: ");
                        double saldoEMoney = Double.parseDouble(scanner.nextLine().trim());
                        return new Emoney(saldoEMoney);
                    } catch (NumberFormatException e) {
                        System.out.println(ANSI_RED_BACKGROUND + "Input saldo E-Money tidak valid. Harap masukkan angka." + ANSI_RESET);
                    }
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silahkan coba lagi.");
                    break;
            }
        }
    }

    private MataUang pilihMataUang() {
        while (true) {
            System.out.println("\nPilih mata uang:");
            System.out.println("1. IDR - Indonesian Rupiah");
            System.out.println("2. USD - US Dollar");
            System.out.println("3. EUR - Euro");
            System.out.println("4. JPY - Japanese Yen");
            System.out.println("5. MYR - Malaysian Ringgit");
            System.out.print("Pilihan (1 - 5): ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    return new IDR();
                case "2":
                    return new USD();
                case "3":
                    return new EUR();
                case "4":
                    return new JPY();
                case "5":
                    return new MYR();
                default:
                    System.out.println(ANSI_RED_BACKGROUND + "Pilihan tidak valid. Silahkan coba lagi." + ANSI_RESET);
            }
        }
    }

    private boolean prosesPembayaran(MetodePembayaran metodePembayaran, MataUang mataUang) {
        double grandTotal = pesanan.getTotalDenganPajak();
        double totalSetelahDiskon = metodePembayaran.hitungTotalSetelahDiskon(grandTotal);

        Member member = pesanan.getMember();
        double poinDipakaiIDR = 0;
        int poinSebelum = 0;
        int poinDidapat = 0;

        // PERAN 2: Integrasi Pembayaran Poin & Kondisi Pengunci IDR
        if (member != null) {
            poinSebelum = member.getPoin();
            // Pemotongan poin HANYA valid jika pelanggan memilih mata uang IDR
            if (mataUang.getKode().equals("IDR") && member.getPoin() > 0) {
                // Kalkulasi dan pemotongan poin dilakukan di sini
                double nilaiPoin = member.getPoin() * 2.0;
                poinDipakaiIDR = Math.min(nilaiPoin, totalSetelahDiskon);
                poinDipakaiIDR = Math.ceil(poinDipakaiIDR / 2.0) * 2.0;
            }
        }

        double finalTagihanIDR = totalSetelahDiskon - poinDipakaiIDR;

        // Validasi saldo
        if (metodePembayaran instanceof Qris) {
            Qris qris = (Qris) metodePembayaran;
            if (!qris.cekSaldoCukup(finalTagihanIDR)) {
                System.out.println(ANSI_RED_BACKGROUND + "Saldo QRIS tidak cukup!" + ANSI_RESET);
                return false;
            }
            qris.kurangiSaldo(finalTagihanIDR);
        } else if (metodePembayaran instanceof Emoney) {
            Emoney emoney = (Emoney) metodePembayaran;
            if (!emoney.cekSaldoCukup(finalTagihanIDR)) {
                System.out.println(ANSI_RED_BACKGROUND + "Saldo E-Money tidak cukup!" + ANSI_RESET);
                return false;
            }
            emoney.kurangiSaldo(finalTagihanIDR);
        }

        // Eksekusi potong/tambah poin sesungguhnya jika transaksi berhasil
        if (member != null) {
            if (poinDipakaiIDR > 0) {
                member.gunakanPoin(poinDipakaiIDR);
            }
            member.tambahPoin(finalTagihanIDR);
            poinDidapat = member.getPoin() - (poinSebelum - (int) (poinDipakaiIDR / 2.0));
        }

        Kuitansi kuitansi = new Kuitansi(pesanan, metodePembayaran, mataUang, poinDipakaiIDR, poinSebelum, poinDidapat);
        kuitansi.cetak();
        return true;
    }
}
