package se.vgregion.portal.glasogonbidrag.value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class PrescriptionValueObject {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PrescriptionValueObject.class);

    private DiagnoseType type;
    private VisualLaterality laterality;
    private String comment;
    private String prescriber;
    private Date date;
    private boolean noGlasses;
    private float visualAcuityLeft;
    private float visualAcuityRight;
    private boolean weakEyeSight;

    public PrescriptionValueObject() {
        type = DiagnoseType.NONE;
    }

    public DiagnoseType getType() {
        return type;
    }

    public void setType(DiagnoseType type) {
        this.type = type;
    }

    public VisualLaterality getLaterality() {
        return laterality;
    }

    public void setLaterality(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isNoGlasses() {
        return noGlasses;
    }

    public void setNoGlasses(boolean noGlasses) {
        this.noGlasses = noGlasses;
    }

    public float getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    public void setVisualAcuityLeft(float visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    public float getVisualAcuityRight() {
        return visualAcuityRight;
    }

    public void setVisualAcuityRight(float visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    public boolean isWeakEyeSight() {
        return weakEyeSight;
    }

    public void setWeakEyeSight(boolean weakEyeSight) {
        this.weakEyeSight = weakEyeSight;
    }

    public boolean isDiagnoseAphakia() {
        return type == DiagnoseType.APHAKIA;
    }

    // TODO: Fix spelling, keratoconus with c instead of k.
    public boolean isDiagnoseKeratokonus() {
        return type == DiagnoseType.KERATOCONUS;
    }

    public boolean isDiagnoseNone() {
        return type == DiagnoseType.NONE;
    }

    public boolean isDiagnoseSpecial() {
        return type == DiagnoseType.SPECIAL;
    }

    public Diagnose getDiagnose() {
        Diagnose result;

        Constructor<? extends Diagnose> constructor;
        Class<? extends Diagnose> reference = type.getDiagnoseClass();
        try {
            constructor = reference.getConstructor(PrescriptionValueObject.class);
        } catch (NoSuchMethodException e) {
            LOGGER.info("Diagnose of class {} don't contain a constructor" +
                            "to build from value object.",
                    reference.getCanonicalName());

            return fallbackDiagnose();
        }

        if (constructor.isAccessible()) {
            LOGGER.info("Class {}'s constructor that takes a diagnose value " +
                            "object is not accessible.",
                    reference.getCanonicalName());

            return fallbackDiagnose();
        }

        try {
            result = constructor.newInstance(this);
        } catch (InstantiationException
                |IllegalAccessException
                |InvocationTargetException e) {
            LOGGER.info("Class {}'s constructor that takes a diagnose value " +
                            "threw exception, got  {}.",
                    reference.getCanonicalName(),
                    e.getMessage());

            return fallbackDiagnose();
        }

        return result;
    }

    private Diagnose fallbackDiagnose() {
        Class<? extends Diagnose> reference = type.getDiagnoseClass();

        try {
            return reference.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(
                    "Could not create an instance of class " +
                            reference.getCanonicalName() + ".", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Illegal access when creating instance of class " +
                            reference.getCanonicalName() + ".", e);
        }
    }
}
