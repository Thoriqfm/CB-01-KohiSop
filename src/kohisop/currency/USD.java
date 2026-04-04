package kohisop.currency;

public class USD extends MataUang{
    public USD() {
        super("USD", "US Dollar", 15);
    }

    @Override
    public double konversiDariIDR(double nominalIDR) {
        return nominalIDR / rate;
    }

    @Override
    public String format(double nominal) {
        return String.format("$%.2f", nominal);
    }
}
