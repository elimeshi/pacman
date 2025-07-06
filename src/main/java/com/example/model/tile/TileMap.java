package com.example.model.tile;

import java.io.Serializable;
import java.util.HashMap;

import com.example.utils.AssetLoader;

public class TileMap implements Serializable {
    public int size;
    public char[][] map;
    public HashMap<Character, Tile> tiles;

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
    }

    public void loadMap(int level) {
        String tileMap = AssetLoader.loadLevel(level);
        String[] rows = tileMap.split("\n");
        map = new char[rows.length][rows[0].split(" ").length];
        for (int i = 0; i < rows.length; i++) {
            String[] tilesInRow = rows[i].strip().split(" ");
            for (int j = 0; j < tilesInRow.length; j++) {
                map[i][j] = tilesInRow[j].charAt(0);
            }
        }
    }

    public Tile getTileAt(int row, int col) {
        Tile t = tiles.get(map[row][col]);
        if (t == null) System.out.println("no tile found for " + map[row][col] + ", " + row+ " " + col);
        return t;
    }

    public void setTileAt(int row, int col, char tile) {
        map[row][col] = tile;
    }

    public int mapWidth() { return map[0].length; }

    public int mapHeight() { return map.length; }
}
