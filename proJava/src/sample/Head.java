package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.Serializable;

import static java.lang.Math.atan2;

public class Head extends Pane implements Folowable, Serializable {
    private Circle head;
    private Eye lEye;
    private Eye rEye;
    private Rotate rotate;
    private Rotate initialRotate;
    private double x;
    private double y;


    public double getRotation() {
        return rotate.getAngle();
    }

    Head(Color mainColor, Color EyesColor)
    {
        head = new Circle(25,mainColor);
        getChildren().add(head);
        head.relocate(-head.getRadius(),-head.getRadius());

        rotate = new Rotate(0);
        rotate.setPivotX(0);
        rotate.setPivotY(0);
        getTransforms().add(rotate);

        initialRotate = new Rotate(-40);
        initialRotate.setPivotX(0);
        initialRotate.setPivotY(0);
        getTransforms().add(initialRotate);


        lEye = new Eye(7,EyesColor,rotate,initialRotate);
        getChildren().add(lEye);
        lEye.relocate(8,-6);

        rEye = new Eye(7,EyesColor,rotate,initialRotate);
        getChildren().add(rEye);
        rEye.relocate(-6,8);



    }

    public void rotateToVector(double x, double y)
    {
        double difrence = Math.toDegrees(atan2(y,x)) - rotate.getAngle();




        //https://gamedev.stackexchange.com/questions/4467/comparing-angles-and-working-out-the-difference
        //nie użyłem kodu ale pomysł jest stamtąd :)

        try {
            if (difrence < 0 && difrence > -180) {
                rotate.setAngle(rotate.getAngle() - 2.0);
            }
            if (difrence < -180) {
                rotate.setAngle(rotate.getAngle() + 2.0);
            }
            if (difrence > 0 && difrence < 180) {
                rotate.setAngle(rotate.getAngle() + 2.0);
            }
            if (difrence > 180) {
                rotate.setAngle(rotate.getAngle() - 2.0);
            }


            if (rotate.getAngle() > 180) {
                rotate.setAngle(rotate.getAngle() - 360);
            }
            if (rotate.getAngle() < -180) {
                rotate.setAngle(rotate.getAngle() + 360);
            }


            lEye.rotateToVector(x, y);
            rEye.rotateToVector(x, y);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }
    @Override
    public void relocate(double x, double y)
    {
        try {
            super.relocate(x, y);
        }catch (Exception e)
        {
            System.out.println(e);
        }
        this.x=x;
        this.y=y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
    public void setRotaation(Rotate rotate)
    {
        this.rotate.setAngle(rotate.getAngle());
    }
}
