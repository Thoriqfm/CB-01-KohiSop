package kohisop.app;

import java.util.Scanner;
import kohisop.currency.EUR;
import kohisop.currency.IDR;
import kohisop.currency.JPY;
import kohisop.currency.MYR;
import kohisop.currency.MataUang;
import kohisop.currency.USD;
import kohisop.model.*;
import kohisop.payment.Emoney;
import kohisop.payment.MetodePembayaran;
import kohisop.payment.Qris;
import kohisop.payment.Tunai;
import kohisop.service.Kuitansi;
import kohisop.service.Menu;

public class KohiApp {

    private Menu menu;
    private Pesanan pesanan;
    private Scanner scanner = new Scanner(System.in);

    // ANSI escape codes for colored output (for warning messages)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";



    public KohiApp() {
        this.menu = new Menu();
        this.pesanan = new Pesanan();
    }

    public void jalankan() {
        System.out.println("--- Selamat datang di KohiSop Cafe! ---");
        menu.tampilkanMenu();

        inputPesanan();

        if (pesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan. Terima kasih!");
            return;
        }

        tampilkanTabelPesanan();

        MetodePembayaran metodePembayaran = pilihMetodePembayaran();
        MataUang mataUang = pilihMataUang();

        tampilkanKuitansi(metodePembayaran, mataUang);
    }

    private void inputPesanan() {
        while (true) { 
            System.out.print("\nMasukkan kode menu (atau 'selesai' untuk selesai): ");
            String kode = scanner.nextLine().trim();

            if (kode.equalsIgnoreCase("CC")) {
                System.out.println("Pesanan dibatalkan. Program dihentikan.");
                System.exit(0);
            }

            if (kode.equalsIgnoreCase("selesai")) break;

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

        System.out.print("Masukkan kuantitas (maks " + max + "): ");
        try {
            int kuantitas = Integer.parseInt(scanner.nextLine().trim());

            if (kuantitas < 1 || kuantitas > max) {
                System.out.println(ANSI_RED_BACKGROUND + "Kuantitas harus antara 1 - " + max + ANSI_RESET);
                return;
            }

            pesanan.tambahItem(new ItemPesanan(item, kuantitas));
            System.out.println(item.getNama() + " x" + kuantitas + " ditambahkan ke pesanan.");

        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED_BACKGROUND + "Input kuantitas tidak valid. Harap masukkan angka." + ANSI_RESET);
        }
    }

    private void tampilkanTabelPesanan() {
        System.out.println("\n--- PESANAN ANDA ---");
        for (ItemPesanan item : pesanan.getAllItem()) {
            System.out.printf("%-30s x%d%n",
                item.getMenuItem().getNama(),
                item.getKuantitas()
            );
        }
    }

    private MetodePembayaran pilihMetodePembayaran() {
        System.out.println("\nPilih metode pembayaran:");
        System.out.println("1. Tunai");
        System.out.println("2. QRIS (diskon 5%)");
        System.out.println("3. E-Money (diskon 7%)");
        System.out.print("Pilihan: ");

        String pilihan = scanner.nextLine().trim();

        switch (pilihan) {
            case "1":
                return new Tunai();
            case "2":
                System.out.print("Masukkan saldo QRIS: ");
                double saldoQRIS = Double.parseDouble(scanner.nextLine().trim());
                return new Qris(saldoQRIS);
            case "3":
                System.out.print("Masukkan saldo E-Money: ");
                double saldoEMoney = Double.parseDouble(scanner.nextLine().trim());
                return new Emoney(saldoEMoney);
            default:
                System.out.println("Pilihan tidak valid. Default ke Tunai.");
                return new Tunai();
        }
    }

    private MataUang pilihMataUang() {
        System.out.println("\nPilih mata uang:");
        System.out.println("1. IDR - Indonesian Rupiah");
        System.out.println("2. USD - US Dollar");
        System.out.println("3. EUR - Euro");
        System.out.println("4. JPY - Japanese Yen");
        System.out.println("5. MYR - Malaysian Ringgit");
        System.out.print("Pilihan: ");

        String pilihan = scanner.nextLine().trim();

        switch (pilihan) {
            case "1": return new IDR();
            case "2": return new USD();
            case "3": return new EUR();
            case "4": return new JPY();
            case "5": return new MYR();
            default:
                System.out.println("Pilihan tidak valid. Default ke IDR.");
                return new IDR();
        }
    }

    private void tampilkanKuitansi(MetodePembayaran metodePembayaran, MataUang mataUang) {
        Kuitansi kuitansi = new Kuitansi(pesanan, metodePembayaran, mataUang);
        kuitansi.cetak();
    }
}
