package com.example.hsr.model.map;

import com.example.hsr.model.transformations.ProjectionMatrix;
import com.example.hsr.model.transformations.PositionMatrix;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
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
        List<Wall> walls = new ArrayList<>();
        for (Cuboid cuboid : Scene.DEFAULT_VIEW) {
            walls.addAll(cuboid.copy().transform(positionMatrix).getWalls());
        }

        walls.sort(Wall::compareTo);

        for (int i = 0; i < 12; i++) {
            walls.get(i).print();
        }
        System.out.println();
        System.out.println("------------------------------------------------");


        for (Wall wall : walls) {
            wall.transform(projectionMatrix).draw(gc);
        }
    }

    public void reset() {
        positionMatrix = new PositionMatrix();
        projectionMatrix = new ProjectionMatrix();
    }
}
