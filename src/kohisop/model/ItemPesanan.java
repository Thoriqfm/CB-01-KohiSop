package kohisop.model;

public class ItemPesanan {
    private MenuItem menuItem;
    private int kuantitas;

    public ItemPesanan(MenuItem menuItem, int kuantitas) {
        this.menuItem = menuItem;
        this.kuantitas = kuantitas;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public double getSubTotal() { 
        return menuItem.getHarga() * kuantitas;
    }

    public double getTotalPajak() { 
        return menuItem.hitungPajak() * kuantitas;
    }

    public double getTotal() { 
        return getSubTotal() + getTotalPajak();
    }
}
