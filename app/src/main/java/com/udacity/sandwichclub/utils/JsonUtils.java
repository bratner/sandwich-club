package com.udacity.sandwichclub.utils;

import android.text.TextUtils;
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

    private final static String name_object = "name";
    private final static String mainName_field = "mainName";
    private final static String aka_array = "alsoKnownAs";
    private final static String origin_field = "placeOfOrigin";
    private final static String desc_field = "description";
    private final static String image_field = "image";
    private final static String ingredients_array = "ingredients";

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

            /* mandatory fields must be present and will trigger exception if are not there */
            JSONObject nameHash = sandwichJson.getJSONObject(name_object);
            mainName = nameHash.getString(mainName_field);
            image = sandwichJson.getString(image_field);
            description = sandwichJson.getString(desc_field);
            JSONArray ingredientsJson = sandwichJson.getJSONArray(ingredients_array);
            ingredients = toStringArrayList(ingredientsJson);


            /* missing non-mandatory fields default to be safely emtpy */
            if (nameHash.has(aka_array)) {
                JSONArray alsoKnownAsJson = nameHash.getJSONArray("alsoKnownAs");
                alsoKnownAs = toStringArrayList(alsoKnownAsJson);
            } else {
                alsoKnownAs = new ArrayList<>();
            }
            placeOfOrigin = sandwichJson.optString(origin_field, "");

            /* validation */
            if (mainName.isEmpty() || image.isEmpty() || description.isEmpty()
                    || ingredients.size() < 2)
            {
                Log.e(TAG, "Some of the mandatory JSON fields are empty.");
                return null;
            }

            retSandwitch = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description,
                    image, ingredients);

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
