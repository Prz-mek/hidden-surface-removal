package com.example.hsr.model.transformations;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class Polygon implements Comparable<Polygon> {

    private Point[] vertices;

    public Polygon(Point a, Point b, Point c, Point d) {
        this.vertices = new Point[] {a, b, c, d};
    }

    @Override
    public int compareTo(Polygon p) {
        RealMatrix plane =  new Array2DRowRealMatrix(new double[][] {
                { -p.vertices[0].getX(), -p.vertices[0].getY(), -p.vertices[0].getZ() },
                { -p.vertices[1].getX(), -p.vertices[1].getY(), -p.vertices[1].getZ() },
                { -p.vertices[2].getX(), -p.vertices[2].getY(), -p.vertices[2].getZ() }
        });

        double detCamera = new LUDecomposition(plane).getDeterminant();
        double maxDet = 0.0;

        for (Point v : vertices) {
            RealMatrix help =  new Array2DRowRealMatrix(new double[][] {
                    { v.getX(), v.getY(), v.getZ() },
                    { v.getX(), v.getY(), v.getZ() },
                    { v.getX(), v.getY(), v.getZ() }
            });
            double detPolygon = new LUDecomposition(plane.add(help)).getDeterminant();
            if (Math.abs(detPolygon) > Math.abs(maxDet)) {
                maxDet = detPolygon;
            }
        }

        if (maxDet * detCamera < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public double[] getX() {
        return new double[]{vertices[0].getX(), vertices[1].getX(), vertices[2].getX(), vertices[3].getX()};
    }

    public double[] getY() {
        return new double[]{vertices[0].getY(), vertices[1].getY(), vertices[2].getY(), vertices[3].getY()};
    }

    public double[] getZ() {
        return new double[]{vertices[0].getZ(), vertices[1].getZ(), vertices[2].getZ(), vertices[3].getZ()};
    }

    public void transform(RealMatrix matrix) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].transform(matrix);
        }
    }
}
