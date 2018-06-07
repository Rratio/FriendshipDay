package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.Sandwich;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtils {

    public static String main, also_known, desc, place_of, image,increments;

    public static Sandwich parseSandwichJson(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);

        main = jsonObject.getString("mainName ");
        also_known = jsonObject.getString("alsoKnownAs");
        desc = jsonObject.getString("placeOfOrigin");
        place_of = jsonObject.getString("description");
        increments = jsonObject.getString("increments");
        image = jsonObject.getString("image");


        return null;
    }
}
