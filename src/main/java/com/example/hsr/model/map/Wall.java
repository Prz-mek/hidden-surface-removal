package com.example.hsr.model.map;

import com.example.hsr.model.transformations.Point;
import com.example.hsr.model.transformations.Polygon;
import com.example.hsr.view.View;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.linear.RealMatrix;

public class Wall implements Comparable<Wall> {

    private Polygon polygon;
    private Color color;

    public Wall(Point a, Point b, Point c, Point d, Color color) {
        this.polygon = new Polygon(a, b, c, d);
        this.color = color;
    }

    @Override
    public int compareTo(Wall wall) {
        return this.polygon.compareTo(wall.polygon);
    }

    public Wall transform(RealMatrix matrix) {
        polygon.transform(matrix);
        return this;
    }

    public void draw(GraphicsContext gc) {
        double[] x = polygon.getX();
        double[] y = polygon.getY();
        for  (int i = 0; i < 4; i++) {
            x[i] = x[i] + View.WIDTH / 2;
            y[i] = y[i] + View.HEIGHT / 2;
        }
        gc.setFill(color);
        gc.fillPolygon(x, y, 4);
        gc.strokeLine(x[0] ,  y[0], x[1] , y[1] );
        gc.strokeLine(x[1] ,  y[1], x[2] , y[2] );
        gc.strokeLine(x[2] ,  y[2] , x[3], y[3] );
        gc.strokeLine(x[3] ,  y[3], x[0], y[0]);
    }

    public void print() {
        double[] x = polygon.getX();
        double[] y = polygon.getY();
        double[] z = polygon.getZ();
        for (int i = 0; i < 4; i++) {
            System.out.println(x[i] + " " + y[i] + " " + z[i]);
        }
        System.out.println();
    }
}
