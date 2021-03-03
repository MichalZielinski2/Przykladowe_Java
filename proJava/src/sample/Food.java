package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Food extends Circle implements Folowable, Serializable {

    double x;
    double y;
    public Food() {
        super(5, Color.rgb(0,255,0));
    }

    @Override
    public void relocate(double x, double y)
    {
        super.relocate(x,y);
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
}
