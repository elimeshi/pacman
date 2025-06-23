package com.example.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import com.example.model.entity.enemy.GhostMode;
import com.example.utils.AssetLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GhostModeSchedule {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = AssetLoader.loadJSON("Ghost_mode_schedule_config").get("1");
    Queue<GhostMode> modes = new LinkedList<>();
    Queue<Integer> durations = new LinkedList<>();
    int frightenedDuration, inPenAfterEatenDuration;

    public GhostModeSchedule() {
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
