package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

public class Eye extends Pane {
    private Double angle;
    private Circle eye;
    private Circle pupil;
    private Rotate rotateParent;
    private Rotate rotateThis;
    private Rotate initialRotation;

    public Eye(int size, Color eyesColor, Rotate rotateParent, Rotate initialRotate) {
    angle=0.0;
    this.rotateParent=rotateParent;
    this.initialRotation=initialRotate;

    rotateThis = new Rotate(0);
    rotateThis.setPivotY(0);
    rotateThis.setPivotX(0);
    getTransforms().add(rotateThis);

    eye = new Circle(size, eyesColor);
    getChildren().add(eye);
    eye.relocate(-7,-7);

    pupil = new Circle(4,Color.BLACK);
    getChildren().add(pupil);
    pupil.relocate(0,-4);


    }

    public void rotateToVector(double x, double y)
    {
        rotateThis.setAngle(Math.toDegrees(Math.atan2(y,x))-rotateParent.getAngle()-initialRotation.getAngle());
    }

}
