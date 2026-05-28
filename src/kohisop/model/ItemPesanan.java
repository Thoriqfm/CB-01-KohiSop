package kohisop.model;

public class ItemPesanan {
    private MenuItem menuItem;
    private int kuantitas;

    public ItemPesanan(MenuItem menuItem, int kuantitas) {
        this.menuItem = menuItem;
        this.kuantitas = kuantitas;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getKuantitas() { return kuantitas; }
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }

    public double getSubTotal() { 
        return menuItem.getHarga() * kuantitas;
    }

    // Logika pajak disesuaikan dengan status member
    public double getTotalPajak(boolean bebasPajak) { 
        if (bebasPajak) {
            return 0;
        }
        return menuItem.hitungPajak() * kuantitas;
    }

    public double getTotal(boolean bebasPajak) { 
        return getSubTotal() + getTotalPajak(bebasPajak);
    }
}