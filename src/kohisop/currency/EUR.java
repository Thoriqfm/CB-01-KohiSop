package kohisop.currency;

public class EUR extends MataUang{

    public EUR() {
        super("EUR", "Euro", 14);
    }
    
    @Override
    public double konversiDariIDR(double nominalIDR) {
        return nominalIDR / rate;
    }

    @Override
    public String format(double nominal) {
        return String.format("€%.2f", nominal);
    }
}
