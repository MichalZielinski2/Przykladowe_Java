package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {
    public double headLocationX;
    public double headLocationY;
    public double headRotation;

    public double foodLocationX;
    public double foodLocationY;

    ArrayList<Double> tailDataa;


    public SaveData(double headLocationX, double headLocationY, double headRotation, double foodLocationX, double foodLocationY, ArrayList<Part> parts) {
        this.headLocationX = headLocationX;
        this.headLocationY = headLocationY;
        this.headRotation = headRotation;
        this.foodLocationX = foodLocationX;
        this.foodLocationY = foodLocationY;
        this.tailDataa=new ArrayList<>();

        for(Part p: parts)
        {
            tailDataa.add(p.x);
            tailDataa.add(p.y);
        }
    }






}
