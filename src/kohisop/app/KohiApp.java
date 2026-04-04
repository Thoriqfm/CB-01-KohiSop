package kohisop.app;

import java.awt.Menu;
import java.util.Scanner;
import kohisop.currency.MataUang;
import kohisop.model.Pesanan;
import kohisop.payment.MetodePembayaran;

public class KohiApp {

    private Menu menu;
    private Pesanan pesanan;
    private Scanner scanner = new Scanner(System.in);

    public KohiApp() {
        // TODO: implementasi constructor
    }

    public void jalankan() {
        // TODO: implementasi logika untuk menjalankan aplikasi
    }

    // TODO: private method (parameter, logic, etc)
    private void inputPesanan() {
        // TODO: implementasi logika untuk input pesanan
    }

    private void inputKuantitas() {
        // TODO: implementasi logika untuk input kuantitas
    }

    private void tampilkanTabelPesanan() {
        // TODO: implement format
    }

    private MetodePembayaran pilihMetodePembayaran() {
        return null; // TODO: implementasi logika untuk memilih metode pembayaran
    }

    private MataUang pilihMataUang() {
        return null; // TODO: implementasi logika untuk memilih mata uang
    }

    private void tampilkanKuitansi(MetodePembayaran metodePembayaran, MataUang mataUang) {
        //TODO: implementasi logika untuk menampilkan kuitansi
    }
}
