package com.example.hsr.model.map;

import com.example.hsr.model.transformations.ProjectionMatrix;
import com.example.hsr.model.transformations.Point;
import com.example.hsr.model.transformations.PositionMatrix;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Camera {
    private PositionMatrix positionMatrix;
    private ProjectionMatrix projectionMatrix;

    public Camera() {
        reset();
    }

    public void transform(Operations operation) {
        switch (operation) {
            case MOVE_FORWARD:
                positionMatrix.translate(0, 0, -PositionMatrix.DEFAULT_DISTANCE);
                break;
            case MOVE_BACKWARD:
                positionMatrix.translate(0, 0, PositionMatrix.DEFAULT_DISTANCE);
                break;
            case MOVE_DOWN:
                positionMatrix.translate(0, -PositionMatrix.DEFAULT_DISTANCE, 0);
                break;
            case MOVE_LEFT:
                positionMatrix.translate(PositionMatrix.DEFAULT_DISTANCE, 0, 0);
                break;
            case MOVE_RIGHT:
                positionMatrix.translate(-PositionMatrix.DEFAULT_DISTANCE, 0, 0);
                break;
            case MOVE_UP:
                positionMatrix.translate(0, PositionMatrix.DEFAULT_DISTANCE, 0);
                break;
            case ROTATE_LEFT:
                positionMatrix.rotateY(PositionMatrix.DEFAULT_ANGLE);
                break;
            case ROTATE_RIGHT:
                positionMatrix.rotateY(-PositionMatrix.DEFAULT_ANGLE);
                break;
            case ROTATE_DOWN:
                positionMatrix.rotateX(PositionMatrix.DEFAULT_ANGLE);
                break;
            case ROTATE_UP:
                positionMatrix.rotateX(-PositionMatrix.DEFAULT_ANGLE);
                break;
            case ROTATE_CLOCKWISE:
                positionMatrix.rotateZ(PositionMatrix.DEFAULT_ANGLE);
                break;
            case ROTATE_COUNTERCLOCKWISE:
                positionMatrix.rotateZ(-PositionMatrix.DEFAULT_ANGLE);
                break;
            case ZOOM_IN:
                projectionMatrix.zoomIn();
                break;
            case ZOOM_OUT:
                projectionMatrix.zoomOut();
                break;
            default:
                break;
        }
    }

    public void draw(GraphicsContext gc) {
        List<List<Wall>> walls = new ArrayList<>();
        for (Cuboid cuboid : Scene.DEFAULT_VIEW) {
            ArrayList<Wall> nextList = new ArrayList<>();
            nextList.addAll(cuboid.copy().transform(positionMatrix).getWalls());
            nextList.sort(Wall::compareTo);
            walls.add(nextList);
        }

        walls.sort(new Comparator<List<Wall>>() {

            @Override
            public int compare(List<Wall> arg0, List<Wall> arg1) {
                double avgZ0 = 0;
                for (Wall wall : arg0) {
                    for (Point point : wall.polygon.vertices) {
                        avgZ0 += point.getZ();
                    }
                }
                avgZ0 /= 24;
                double avgZ1 = 0;
                for (Wall wall : arg1) {
                    for (Point point : wall.polygon.vertices) {
                        avgZ1 += point.getZ();
                    }
                }
                avgZ1 /= 24;
                return avgZ0 - avgZ1 < 0 ? 1 : -1;
            }

        });

        for (List<Wall> list : walls) {
            for (Wall wall : list) {
                wall.transform(projectionMatrix).draw(gc);
            }
        }
    }

    public void reset() {
        positionMatrix = new PositionMatrix();
        projectionMatrix = new ProjectionMatrix();
    }
}
