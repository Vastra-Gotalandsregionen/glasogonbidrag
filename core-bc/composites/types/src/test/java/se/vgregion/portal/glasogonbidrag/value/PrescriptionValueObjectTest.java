package se.vgregion.portal.glasogonbidrag.value;

import org.junit.Assert;
import org.junit.Test;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class PrescriptionValueObjectTest {
    @Test
    public void testAphakiaInstance() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.APHAKIA);
        vo.setLaterality(VisualLaterality.LEFT);

        Diagnose diagnose = vo.getDiagnose();

        Aphakia expectedDiagnose = new Aphakia(
                VisualLaterality.LEFT);

        Assert.assertEquals(expectedDiagnose, diagnose);
    }

    @Test
    public void testKeratoconusInstance() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.KERATOCONUS);
        vo.setLaterality(VisualLaterality.BILATERAL);
        vo.setVisualAcuityRight(0.4f);
        vo.setVisualAcuityLeft(0.3f);
        vo.setNoGlasses(true);

        Diagnose diagnose = vo.getDiagnose();

        Keratoconus expectedDiagnose = new Keratoconus(
                VisualLaterality.BILATERAL, 0.4f, 0.3f, true);

        Assert.assertEquals(expectedDiagnose, diagnose);
    }

    @Test
    public void testSpecialInstance() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.SPECIAL);
        vo.setLaterality(VisualLaterality.RIGHT);
        vo.setWeakEyeSight(false);

        Diagnose diagnose = vo.getDiagnose();

        Special expectedDiagnose = new Special(
                VisualLaterality.RIGHT, false);

        Assert.assertEquals(expectedDiagnose, diagnose);
    }

    @Test
    public void testNoneInstance() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.NONE);

        Diagnose diagnose = vo.getDiagnose();

        None expectedDiagnose = new None();

        Assert.assertEquals(expectedDiagnose, diagnose);
    }
}
