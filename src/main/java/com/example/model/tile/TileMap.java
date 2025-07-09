package com.example.model.tile;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;

import com.example.config.GameConfig;
import com.example.utils.AssetLoader;

public class TileMap implements Serializable {

    private static TileMap instance = null;
    
    public char[][] map;
    public HashMap<Character, Tile> tiles;
    public int initialDots;
    private Point[] ghostPenCorners = new Point[4];
    private Point2D.Double ghostPenCenter;

    private TileMap() {}

    public static TileMap getInstance() {
        if (instance == null) instance = new TileMap();
        return instance;
    }

    public void loadLevel(int level) {
        loadTiles(level);
        loadMap(level);
    }
    public static java.util.Map<Character, Tile> tileMap;

    public void loadTiles(int level) {
        tiles = new HashMap<>();
        tiles.put('0', new Tile(AssetLoader.loadSprite("tiles/earth.png"), TileType.Empty));
        tiles.put('1', new Tile(AssetLoader.loadSprite("tiles/wall.png"), TileType.Wall));
        tiles.put('■', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/■.png"), TileType.Energizer));
        tiles.put('·', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/·.png"), TileType.Dot));
        tiles.put('┌', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┌.png"), TileType.Wall));
        tiles.put('└', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/└.png"), TileType.Wall));
        tiles.put('┐', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┐.png"), TileType.Wall));
        tiles.put('┘', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┘.png"), TileType.Wall));
        tiles.put('│', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/│.png"), TileType.Wall));
        tiles.put('─', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/─.png"), TileType.Wall));
        tiles.put('≡', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/≡.png"), TileType.Empty));
        tiles.put('┅', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┅.png"), TileType.Wall));
        tiles.put('╸', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╸.png"), TileType.Wall));
        tiles.put('╺', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╺.png"), TileType.Wall));
        tiles.put('┏', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┏.png"), TileType.Wall));
        tiles.put('╔', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╔.png"), TileType.Wall));
        tiles.put('┓', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┓.png"), TileType.Wall));
        tiles.put('╗', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╗.png"), TileType.Wall));
        tiles.put('┗', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┗.png"), TileType.Wall));
        tiles.put('╚', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╚.png"), TileType.Wall));
        tiles.put('┛', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┛.png"), TileType.Wall));
        tiles.put('╝', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/╝.png"), TileType.Wall));
        tiles.put('┡', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┡.png"), TileType.Wall));
        tiles.put('┢', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┢.png"), TileType.Wall));
        tiles.put('┩', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┩.png"), TileType.Wall));
        tiles.put('┪', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┪.png"), TileType.Wall));
        tiles.put('┱', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┱.png"), TileType.Wall));
        tiles.put('┲', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┲.png"), TileType.Wall));
        tiles.put('┹', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┹.png"), TileType.Wall));
        tiles.put('┺', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/┺.png"), TileType.Wall));
        tiles.put('_', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/_.png"), TileType.Wall));
        tiles.put('[', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/[.png"), TileType.Wall));
        tiles.put('⎻', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/⎻.png"), TileType.Wall));
        tiles.put(']', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/].png"), TileType.Wall));
        if (level > 0) return;
        tiles.put('a', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/a.png"), TileType.Wall));
        tiles.put('b', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/b.png"), TileType.Wall));
        tiles.put('c', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/c.png"), TileType.Wall));
        tiles.put('d', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/d.png"), TileType.Wall));
        tiles.put('e', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/e.png"), TileType.Wall));
        tiles.put('f', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/f.png"), TileType.Wall));
        tiles.put('g', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/g.png"), TileType.Wall));
        tiles.put('h', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/h.png"), TileType.Wall));
        tiles.put('i', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/i.png"), TileType.Wall));
        tiles.put('j', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/j.png"), TileType.Wall));
        tiles.put('k', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/k.png"), TileType.Wall));
        tiles.put('l', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/l.png"), TileType.Wall));
        tiles.put('x', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/x.png"), TileType.Wall));
        tiles.put('y', new Tile(AssetLoader.loadSprite("tiles/walls/level" + level + "/y.png"), TileType.Wall));
            }

    public void loadMap(int level) {
        initialDots = 0;
        String tileMap = AssetLoader.loadLevel(level);
        String[] rows = tileMap.split("\n");
        map = new char[rows.length][rows[0].split(" ").length];

        for (int i = 0; i < rows.length; i++) {

            String[] tilesInRow = rows[i].strip().split(" ");

            for (int j = 0; j < tilesInRow.length; j++) {
                char c = tilesInRow[j].charAt(0);
                if (c == '·') initialDots++;
                else if (c == '╸') setGhostPenCorners(j, i);
                map[i][j] = c;
            }
        }
    }

    public void setGhostPenCorners(int x, int y) {
        ghostPenCorners[0] = new Point(x - 2, y);
        ghostPenCorners[1] = new Point(x + 5, y);
        ghostPenCorners[2] = new Point(x - 2, y + 4);
        ghostPenCorners[3] = new Point(x + 5, y + 4);
        ghostPenCenter = new Point2D.Double(x + 1.5, y + 2);
    } 

    public Tile getTileAt(int row, int col) {
        Tile t = tiles.get(map[row][col]);
        if (t == null) System.out.println("no tile found for " + map[row][col] + ", " + row+ " " + col);
        return t;
    }

    public void setTileAt(int row, int col, char tile) {
        map[row][col] = tile;
    }

    public Point ghostPenTopLeftCorner() { return ghostPenCorners[0]; }
    public Point ghostPenTopRightCorner() { return ghostPenCorners[1]; }
    public Point ghostPenBottomLeftCorner() { return ghostPenCorners[2]; }
    public Point ghostPenBottomRightCorner() { return ghostPenCorners[3]; }
    public Point2D.Double ghostPenCenter() { return ghostPenCenter; }

    public int mapWidth() { return map[0].length; }
    public int mapPixelWidth() { return map[0].length * GameConfig.tileSize; }
    public int mapHeight() { return map.length; }
    public int mapPixelHeight() { return map.length * GameConfig.tileSize; }
}
