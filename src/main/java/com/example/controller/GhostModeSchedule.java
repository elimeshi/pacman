package com.example.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import com.example.model.entity.enemy.GhostMode;
import com.example.utils.AssetLoader;
import com.fasterxml.jackson.databind.JsonNode;

public class GhostModeSchedule {
    private static GhostModeSchedule instance = null;

    private JsonNode cfg;
    public Queue<GhostMode> modes = new LinkedList<>();
    public Queue<Integer> durations = new LinkedList<>();
    public int frightenedDuration, inPenAfterEatenDuration;

    private GhostModeSchedule() { cfg = AssetLoader.loadJSON("Ghost_mode_schedule_config"); }

    public static GhostModeSchedule getInstance() {
        if (instance == null) instance = new GhostModeSchedule();
        return instance;
    }

    public void loadModeSchedule(int level) {
        modes.clear();
        durations.clear();
        JsonNode node = cfg.get("level" + level);
        for (JsonNode item : node.get("modeSchedule")) {
            Iterator<Entry<String, JsonNode>> fields = item.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> entry = fields.next();
                modes.add(GhostMode.valueOf(entry.getKey()));
                durations.add(entry.getValue().asInt());
            }
        }
        frightenedDuration = node.get("frightenedDuration").asInt();
        inPenAfterEatenDuration = node.get("inPenAfterEatenDuration").asInt();
    }
}
