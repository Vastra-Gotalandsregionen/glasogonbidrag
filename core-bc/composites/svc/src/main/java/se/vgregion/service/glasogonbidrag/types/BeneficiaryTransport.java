package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryTransport {
    private BeneficiaryNameTransport name;
    private BeneficiaryAreaTransport area;

    public BeneficiaryTransport() {
        this(new BeneficiaryNameTransport(), new BeneficiaryAreaTransport());
    }

    public BeneficiaryTransport(BeneficiaryNameTransport name,
                                BeneficiaryAreaTransport area) {
        this.name = name;
        this.area = area;
    }

    public BeneficiaryNameTransport getName() {
        return name;
    }

    public void setName(BeneficiaryNameTransport name) {
        if (area == null) {
            throw new NullPointerException("Name may not be null!");
        }

        this.name = name;
    }

    public BeneficiaryAreaTransport getArea() {
        return area;
    }

    public void setArea(BeneficiaryAreaTransport area) {
        if (area == null) {
            throw new NullPointerException("Area may not be null!");
        }

        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeneficiaryTransport that = (BeneficiaryTransport) o;

        if (!area.equals(that.area)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + area.hashCode();
        return result;
    }
}
