package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Embeddable
public class DiagnoseWeakEyesight {
    @Column(name = "weak_bilateral")
    private boolean bilateral = false;
    @Column(name = "weak_left")
    private boolean left = false;
    @Column(name = "weak_right")
    private boolean right = false;
    @Column(name = "weak_sight")
    private boolean weak = false;

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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isWeak() {
        return weak;
    }

    public void setWeak(boolean weak) {
        this.weak = weak;
    }
}