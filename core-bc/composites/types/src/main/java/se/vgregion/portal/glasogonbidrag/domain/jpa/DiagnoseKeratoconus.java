package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Embeddable
public class DiagnoseKeratoconus {
    @Column(name = "keratoconus_bilateral")
    private boolean bilateral = false;
    @Column(name = "keratoconus_left")
    private boolean left = false;
    @Column(name = "keratoconus_left_adjustment")
    private float leftSightAdjustment = 0;
    @Column(name = "keratoconus_right")
    private boolean right = false;
    @Column(name = "keratoconus_right_adjustment")
    private float rightSightAdjustment = 0;
    private boolean noGlass = false;

    public boolean isBilateral() {
        return bilateral;
    }

    public void setBilateral(boolean bilateral) {
        this.bilateral = bilateral;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public float getLeftSightAdjustment() {
        return leftSightAdjustment;
    }

    public void setLeftSightAdjustment(float leftSightAdjustment) {
        this.leftSightAdjustment = leftSightAdjustment;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public float getRightSightAdjustment() {
        return rightSightAdjustment;
    }

    public void setRightSightAdjustment(float rightSightAdjustment) {
        this.rightSightAdjustment = rightSightAdjustment;
    }

    public boolean isNoGlass() {
        return noGlass;
    }

    public void setNoGlass(boolean noGlass) {
        this.noGlass = noGlass;
    }
}
