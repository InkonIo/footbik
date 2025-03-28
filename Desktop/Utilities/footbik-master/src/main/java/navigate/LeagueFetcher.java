import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

private String fetchClubLogoURL(String clubName) {
    String apiUrl = "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=" + clubName.replace(" ", "%20");

    try {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder jsonResponse = new StringBuilder();
        while (scanner.hasNext()) {
            jsonResponse.append(scanner.nextLine());
        }
        scanner.close();
        conn.disconnect();

        // Вывод JSON-ответа в консоль
        System.out.println("API Response: " + jsonResponse);

        // Парсим JSON
        JSONObject jsonObject = new JSONObject(jsonResponse.toString());
        JSONArray teams = jsonObject.optJSONArray("teams");

        if (teams != null && teams.length() > 0) {
            return teams.getJSONObject(0).optString("strTeamBadge", null);
        } else {
            System.out.println("Логотип не найден.");
            return null;
        }

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

public void main() {
}
