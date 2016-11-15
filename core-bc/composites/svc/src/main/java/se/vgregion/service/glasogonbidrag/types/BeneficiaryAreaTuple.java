package se.vgregion.service.glasogonbidrag.types;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class BeneficiaryAreaTuple {
    private String county;
    private String municipality;

    public BeneficiaryAreaTuple() {
    }

    public BeneficiaryAreaTuple(String municipality, String county) {
        this.municipality = municipality;
        this.county = county;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeneficiaryAreaTuple that = (BeneficiaryAreaTuple) o;

        if (county != null ? !county.equals(that.county) : that.county != null)
            return false;
        if (municipality != null ? !municipality.equals(that.municipality) : that.municipality != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = county != null ? county.hashCode() : 0;
        result = 31 * result + (municipality != null ? municipality.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BeneficiaryAreaTransport{" +
                "county='" + county + '\'' +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
