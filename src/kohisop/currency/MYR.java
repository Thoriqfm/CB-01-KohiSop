package kohisop.currency;

public class MYR extends MataUang{

    public MYR() {
        super("MYR", "Malaysian Ringgit", 4);
    }
    
    @Override
    public double konversiDariIDR(double nominalIDR) {
        return nominalIDR / rate;
    }

    @Override
    public String format(double nominal) {
        return String.format("RM%.2f", nominal);
    }

}
