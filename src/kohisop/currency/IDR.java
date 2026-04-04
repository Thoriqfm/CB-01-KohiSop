package kohisop.currency;

public class IDR extends MataUang{

    public IDR() {
        super("IDR", "Indonesian Rupiah", 1);
    }

    @Override
    public double konversiDariIDR(double nominalIDR) {
        return nominalIDR;
    }

    @Override
    public String format(double nominal) {
        return String.format("Rp%.0f", nominal);
    }
    
}
