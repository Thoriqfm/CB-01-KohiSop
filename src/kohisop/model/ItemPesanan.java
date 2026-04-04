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

    public double getSubTotal() { // TODO: implement logic subtotal
        return 0;
    }

    public double getTotalPajak() { // TODO: implement logic pajak
        return 0;
    }

    public double getTotal() { // TODO: implement logic total
        return 0;
    }
}
