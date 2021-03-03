package sample;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Part extends Circle implements Folowable, Serializable { // część ogona
    Folowable next;
    double x;
    double y;
    public Part(double radius, Paint fill, Folowable next) {
        super(radius, fill);
        this.next=next;
    }
     public double[] vectorToNext()
     {
         double[] vec = {next.getX()-x,next.getY()-y};
         return vec;
     }
    @Override
    public void relocate(double x, double y) {
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
}
