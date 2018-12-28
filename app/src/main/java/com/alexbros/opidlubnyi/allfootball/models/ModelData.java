package com.alexbros.opidlubnyi.allfootball.models;

public class ModelData {
    public boolean onlyLiveGamesFilterEnabled = false;
    public float displayDensity = 1;
    public String oddFormat = "";

    // Singleton
    private static ModelData instance;

    public static ModelData getInstance() {
        if (ModelData.instance == null) {
            ModelData.instance = new ModelData();
        }
        return ModelData.instance;
    }

    private ModelData() {
    }
}
