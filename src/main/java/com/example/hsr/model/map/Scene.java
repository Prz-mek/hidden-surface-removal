package com.example.hsr.model.map;

import javafx.scene.paint.Color;

public class Scene {
    public static final Cuboid[] DEFAULT_VIEW = new Cuboid[] {
            new Cuboid(500, 600, 3400, 900, -400, 3700, Color.WHEAT),
            new Cuboid(-1000, 650, 3100, -300, -500, 3800, Color.RED),
            new Cuboid(550, 550, 1900, 1300, -100, 2300, Color.BLUE),
            new Cuboid(-800, 700, 1800, -300, -600, 2700, Color.GREEN)
    };
}
