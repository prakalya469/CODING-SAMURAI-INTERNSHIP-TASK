package com.example.todolist1;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
    private String text;
    private boolean isCompleted;

    public Task(String text, boolean isCompleted) {
        this.text = text;
        this.isCompleted = isCompleted;
    }

    public String getText() {
        return text;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void toggleCompleted() {
        this.isCompleted = !this.isCompleted;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", text);
            jsonObject.put("isCompleted", isCompleted);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Task fromJson(JSONObject jsonObject) throws JSONException {
        return new Task(jsonObject.getString("text"), jsonObject.getBoolean("isCompleted"));
    }
}
