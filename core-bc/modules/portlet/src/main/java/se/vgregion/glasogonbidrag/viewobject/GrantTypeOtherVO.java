package se.vgregion.glasogonbidrag.viewobject;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class GrantTypeOtherVO {

    private boolean aphakia = false;
    private boolean keratoconus = false;
    private boolean special = false;

    private boolean aphakiaBilateral = false;
    private boolean aphakiaRight = false;
    private boolean aphakiaLeft = false;

    private boolean keratoconusBilateral = false;
    private boolean keratoconusRight = false;
    private boolean keratoconusLeft = false;
    private double keratoconusRightAdjustment = 0.0;
    private double keratoconusLeftAdjustment = 0.0;
    private boolean keratoconusNoGlass = false;

    private boolean specialWeakSight = false;
    private boolean specialBilateral = false;
    private boolean specialRight = false;
    private boolean specialLeft = false;

    public boolean isAphakia() {
        return aphakia;
    }

    public void setAphakia(boolean aphakia) {
        this.aphakia = aphakia;
    }

    public boolean isKeratoconus() {
        return keratoconus;
    }

    public void setKeratoconus(boolean keratoconus) {
        this.keratoconus = keratoconus;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isAphakiaBilateral() {
        return aphakiaBilateral;
    }

    public void setAphakiaBilateral(boolean aphakiaBilateral) {
        this.aphakiaBilateral = aphakiaBilateral;
    }

    public boolean isAphakiaRight() {
        return aphakiaRight;
    }

    public void setAphakiaRight(boolean aphakiaRight) {
        this.aphakiaRight = aphakiaRight;
    }

    public boolean isAphakiaLeft() {
        return aphakiaLeft;
    }

    public void setAphakiaLeft(boolean aphakiaLeft) {
        this.aphakiaLeft = aphakiaLeft;
    }

    public boolean isKeratoconusBilateral() {
        return keratoconusBilateral;
    }

    public void setKeratoconusBilateral(boolean keratoconusBilateral) {
        this.keratoconusBilateral = keratoconusBilateral;
    }

    public boolean isKeratoconusRight() {
        return keratoconusRight;
    }

    public void setKeratoconusRight(boolean keratoconusRight) {
        this.keratoconusRight = keratoconusRight;
    }

    public boolean isKeratoconusLeft() {
        return keratoconusLeft;
    }

    public void setKeratoconusLeft(boolean keratoconusLeft) {
        this.keratoconusLeft = keratoconusLeft;
    }

    public double getKeratoconusRightAdjustment() {
        return keratoconusRightAdjustment;
    }

    public void setKeratoconusRightAdjustment(double keratoconusRightAdjustment) {
        this.keratoconusRightAdjustment = keratoconusRightAdjustment;
    }

    public double getKeratoconusLeftAdjustment() {
        return keratoconusLeftAdjustment;
    }

    public void setKeratoconusLeftAdjustment(double keratoconusLeftAdjustment) {
        this.keratoconusLeftAdjustment = keratoconusLeftAdjustment;
    }

    public boolean isKeratoconusNoGlass() {
        return keratoconusNoGlass;
    }

    public void setKeratoconusNoGlass(boolean keratoconusNoGlass) {
        this.keratoconusNoGlass = keratoconusNoGlass;
    }

    public boolean isSpecialWeakSight() {
        return specialWeakSight;
    }

    public void setSpecialWeakSight(boolean specialWeakSight) {
        this.specialWeakSight = specialWeakSight;
    }

    public boolean isSpecialBilateral() {
        return specialBilateral;
    }

    public void setSpecialBilateral(boolean specialBilateral) {
        this.specialBilateral = specialBilateral;
    }

    public boolean isSpecialRight() {
        return specialRight;
    }

    public void setSpecialRight(boolean specialRight) {
        this.specialRight = specialRight;
    }

    public boolean isSpecialLeft() {
        return specialLeft;
    }

    public void setSpecialLeft(boolean specialLeft) {
        this.specialLeft = specialLeft;
    }
}
