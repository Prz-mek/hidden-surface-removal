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

    private class IndexedWalls {
        public List<Wall> walls;
        public int index;
    }

    public void draw(GraphicsContext gc) {
        List<IndexedWalls> walls = new ArrayList<>();
        int index = 0;
        for (Cuboid cuboid : Scene.DEFAULT_VIEW) {
            ArrayList<Wall> nextList = new ArrayList<>();
            nextList.addAll(cuboid.copy().transform(positionMatrix).getWalls());
            IndexedWalls iwalls = new IndexedWalls();
            iwalls.walls = nextList;
            iwalls.index = index++;
            walls.add(iwalls);
        }

        walls.sort(new Comparator<IndexedWalls>() {

            @Override
            public int compare(IndexedWalls arg0, IndexedWalls arg1) {
                List<Wall> walls = new ArrayList<>();
                if (arg0.index + arg1.index == 1 || arg0.index + arg1.index == 5) {
                    walls.add(arg0.walls.get(1));
                    walls.add(arg0.walls.get(3));
                    walls.add(arg1.walls.get(1));
                    walls.add(arg1.walls.get(3));
                } else {
                    walls.add(arg0.walls.get(0));
                    walls.add(arg0.walls.get(2));
                    walls.add(arg1.walls.get(0));
                    walls.add(arg1.walls.get(2));
                }
                for (int i = 0; i < 2; i++) {
                    Wall wall = walls.get(i);
                    boolean inFront = true;
                    for (int j = 2; j < 4; j++) {
                        if (walls.get(j).compareTo(wall) > 0) {
                            inFront = false;
                            break;
                        }
                    }
                    if (inFront) {
                        return 1;
                    }
                }
                return -1;
            }

        });

        for (IndexedWalls list : walls) {
            list.walls.sort(Wall::compareTo);
            for (Wall wall : list.walls) {
                wall.transform(projectionMatrix).draw(gc);
            }
        }
    }

    public void reset() {
        positionMatrix = new PositionMatrix();
        projectionMatrix = new ProjectionMatrix();
    }
}
