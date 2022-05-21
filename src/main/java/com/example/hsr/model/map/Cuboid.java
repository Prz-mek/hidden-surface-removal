package com.example.hsr.model.map;

import com.example.hsr.model.transformations.Point;
import javafx.scene.paint.Color;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.List;

public class Cuboid implements Solid {

    /**
     * Points names:
     *
     *     D'------------------C'
     *   / |                  /|
     *  /  |                 / |
     * A'-------------------B' |
     * |   |                |  |
     * |   D- - - - - - - - |- C
     * |  /                 | /
     * | /                  |/
     * A--------------------B
     */
    private final int A = 0;
    private final int B = 1;
    private final int C = 2;
    private final int D = 3;
    private final int A_PRIME = 4;
    private final int B_PRIME = 5;
    private final int C_PRIME = 6;
    private final int D_PRIME = 7;

    private final Point[] vertices;
    
    /**
     * Wall indexes:
     * 0: front
     * 1: right
     * 2: back
     * 3: left
     * 4: bottom
     * 5: top
     */
    private final Wall[] walls;

    private Color color;


    public Cuboid(double leftX, double downY, double frontZ, double rightX, double upY, double backZ, Color color) {
        vertices = new Point[8];
        vertices[A] = new Point(leftX, downY, frontZ);
        vertices[B] = new Point(rightX, downY, frontZ);
        vertices[C] = new Point(rightX, downY, backZ);
        vertices[D] = new Point(leftX, downY, backZ);
        vertices[A_PRIME] = new Point(leftX, upY, frontZ);
        vertices[B_PRIME] = new Point(rightX, upY, frontZ);
        vertices[C_PRIME] = new Point(rightX, upY, backZ);
        vertices[D_PRIME] = new Point(leftX, upY, backZ);

        this.color = color;

        walls = new Wall[6];
        walls[0] = new Wall(vertices[A], vertices[B], vertices[B_PRIME], vertices[A_PRIME], color);
        walls[1] = new Wall(vertices[B], vertices[C], vertices[C_PRIME], vertices[B_PRIME], color);
        walls[2] = new Wall(vertices[C], vertices[D], vertices[D_PRIME], vertices[C_PRIME], color);
        walls[3] = new Wall(vertices[A], vertices[D], vertices[D_PRIME], vertices[A_PRIME], color);
        walls[4] = new Wall(vertices[A], vertices[B], vertices[C], vertices[D], color);
        walls[5] = new Wall(vertices[A_PRIME], vertices[B_PRIME], vertices[C_PRIME], vertices[D_PRIME], color);
    }

    public Cuboid(Point[] vertices, Color color) {
        if (vertices.length != 8) {
            throw new IllegalArgumentException("Array has wrong size.");
        }
        this.vertices = Arrays.copyOf(vertices, 8);

        walls = new Wall[6];
        walls[0] = new Wall(vertices[A], vertices[B], vertices[B_PRIME], vertices[A_PRIME], color);
        walls[1] = new Wall(vertices[B], vertices[C], vertices[C_PRIME], vertices[B_PRIME], color);
        walls[2] = new Wall(vertices[C], vertices[D], vertices[D_PRIME], vertices[C_PRIME], color);
        walls[3] = new Wall(vertices[A], vertices[D], vertices[D_PRIME], vertices[A_PRIME], color);
        walls[4] = new Wall(vertices[A], vertices[B], vertices[C], vertices[D], color);
        walls[5] = new Wall(vertices[A_PRIME], vertices[B_PRIME], vertices[C_PRIME], vertices[D_PRIME], color);
    }

    public Cuboid copy() {
        Point[] vertices = new Point[8];
        for (int i = 0; i < 8; i++) {
            Point vertex = this.vertices[i];
            vertex.normalize();
            vertices[i] = new Point(vertex.getX(), vertex.getY(), vertex.getZ());
        }
        return new Cuboid(vertices, color);
    }

    @Override
    public List<Wall> getWalls() {
        return List.of(walls);
    }

    private Point getCommonPoint(Point first, Point second, double viewportDistance) {
        double t = (viewportDistance - first.getZ()) / (second.getZ() - first.getZ());
        double x = first.getX() + (second.getX() - first.getX()) * t;
        double y = first.getY() + (second.getY() - first.getY()) * t;
        return new Point(x, y, viewportDistance);
    }

//    private void drawEdge(int firstIndex, int secondIndex, GraphicsContext gc, ProjectionMatrix projectionMatrix) {
//        Point first = vertices[firstIndex];
//        Point second = vertices[secondIndex];
//        double viewportDistance = projectionMatrix.getViewportDistance();
//
//        if (first.getZ() >= viewportDistance && second.getZ() >= viewportDistance) {
//            first = first.transform(projectionMatrix);
//            second = second.transform(projectionMatrix);
//            gc.strokeLine(first.getX() + View.WIDTH / 2, first.getY() + View.HEIGHT / 2,
//                    second.getX() + View.WIDTH / 2, second.getY() + View.HEIGHT / 2);
//        }
//        else if (first.getZ() < viewportDistance && second.getZ() > viewportDistance) {
//            first = getCommonPoint(first, second, viewportDistance);
//            first = first.transform(projectionMatrix);
//            second = second.transform(projectionMatrix);
//            gc.strokeLine(first.getX() + View.WIDTH / 2, first.getY() + View.HEIGHT / 2,
//                    second.getX() + View.WIDTH / 2, second.getY() + View.HEIGHT / 2);
//        } else if (first.getZ() > viewportDistance && second.getZ() < viewportDistance) {
//            second = getCommonPoint(first, second, viewportDistance);
//            first = first.transform(projectionMatrix);
//            second = second.transform(projectionMatrix);
//            gc.strokeLine(first.getX() + View.WIDTH / 2, first.getY() + View.HEIGHT / 2,
//                    second.getX() + View.WIDTH / 2, second.getY() + View.HEIGHT / 2);
//        }
//    }

    public Cuboid transform(RealMatrix matrix) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].transform(matrix);
        }

        for (int i = 0; i < walls.length; i++) {
            walls[i] = walls[i].transform(matrix);
        }

        return this;
    }

//    public void draw(GraphicsContext gc, ProjectionMatrix projectionMatrix) {
//        drawEdge(A, B, gc, projectionMatrix);
//        drawEdge(B, C, gc, projectionMatrix);
//        drawEdge(C, D, gc, projectionMatrix);
//        drawEdge(A, D, gc, projectionMatrix);
//
//        drawEdge(A_PRIME, B_PRIME, gc, projectionMatrix);
//        drawEdge(B_PRIME, C_PRIME, gc, projectionMatrix);
//        drawEdge(C_PRIME, D_PRIME, gc, projectionMatrix);
//        drawEdge(A_PRIME, D_PRIME, gc, projectionMatrix);
//
//        drawEdge(A, A_PRIME, gc, projectionMatrix);
//        drawEdge(B, B_PRIME, gc, projectionMatrix);
//        drawEdge(C, C_PRIME, gc, projectionMatrix);
//        drawEdge(D, D_PRIME, gc, projectionMatrix);
//    }
}
