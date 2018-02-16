package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    /* Here is the JSON we are parsing:
    {
    "name": 
            {
                "mainName"  :   "name text",
                "alsoKnownAs":
                        [
                            "more",
                            "names",
                            "here"
                         ]
            },
    "placeOfOrigin" :   "origin string",
    "description"   :   "description text",
    "image"         :   "url to d/l the image",
    "ingredients"   :
            [
                "List",
                "of",
                "Stuff"
            ]
    }
    */
    public static Sandwich parseSandwichJson(String json) {
        Sandwich retSandwitch = null;

        String mainName; /* Must be present */
        List<String> alsoKnownAs = new ArrayList<>(); /* Can be empty */
        String placeOfOrigin; /* can be empty */
        String description; /* must have some text */
        String image;  /* must have a url */
        List<String> ingredients; /* A sandwitch must have at least 2 */

        if (json == null)
            return null;

        try {
            JSONObject sandwichJson = new JSONObject(json);
            
            JSONObject nameHash = sandwichJson.getJSONObject("name");
            mainName = nameHash.getString("mainName");
            JSONArray alsoKnownAsJson = nameHash.getJSONArray("alsoKnownAs");
            alsoKnownAs = toStringArrayList(alsoKnownAsJson);


            sandwichJson.getString("name");

        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse JSON:\n"+e.getMessage());
            Log.d(TAG, Log.getStackTraceString(e));
            //e.printStackTrace();
            return null;
        }

        return retSandwitch;
    }

    /**
     * Convert JSONArray to ArrayList<String>.
     *
     * based on: https://stackoverflow.com/questions/15871309/convert-jsonarray-to-string-array
     *
     * @param jsonArray JSON array.
     * @return String array.
     */
    private static ArrayList<String> toStringArrayList(JSONArray jsonArray) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        int arrayIndex;
        String jsonArrayItem;

        for (arrayIndex = 0; arrayIndex < jsonArray.length(); arrayIndex++) {
            jsonArrayItem = jsonArray.getString(arrayIndex);
            /* Being lenient with weird but legible input */
            if (jsonArrayItem != null && !jsonArrayItem.isEmpty())
                stringArray.add(jsonArrayItem);
        }

        return stringArray;
    }
}
