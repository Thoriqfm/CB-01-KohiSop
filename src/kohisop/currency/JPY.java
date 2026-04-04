package kohisop.currency;

public class JPY extends MataUang{

    public JPY() {
        super("JPY", "Japanese Yen", 0.1);
    }

    @Override
    public double konversiDariIDR(double nominalIDR) {
        return nominalIDR / rate;
    }

    @Override
    public String format(double nominal) {
        return String.format("¥%.2f", nominal);
    }
}
