package ch.appquest.nico.memory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LogBook {
    private Intent logbookIntent = new Intent("ch.appquest.intent.LOG");

    // Checks if logbook app is installed on the phone
    public boolean checkIfLogbookInstalled(Context context) {
        if (context.getPackageManager().queryIntentActivities(logbookIntent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(context, "Logbook App not Installed", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    // Passes JSON object with solution info to logbook app
    public void passDataToLogbook(Context context, List<String[]> solution) {
        JSONObject solutionJSON = new JSONObject();
        JSONArray solutionJSONArray = new JSONArray(solution);
        try {
            solutionJSON.put("task", "Memory");
            solutionJSON.put("solution", solutionJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logbookIntent.putExtra("ch.appquest.logmessage", solutionJSON.toString());
        context.startActivity(logbookIntent);
    }
}
