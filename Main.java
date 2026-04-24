import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    static String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    static String REG_NO = "2024CS101";
    static boolean SUBMIT = false;

    public static void main(String[] args) throws Exception {

        List<JSONObject> events = fetchData();
        List<JSONObject> uniqueEvents = deduplicate(events);
        Map<String, Integer> scores = aggregate(uniqueEvents);
        List<JSONObject> leaderboard = createLeaderboard(scores);

        System.out.println("\nLeaderboard:");
        for (JSONObject obj : leaderboard) {
            System.out.println(obj.toString());
        }

        if (SUBMIT) {
            submit(leaderboard);
        }
    }

    static List<JSONObject> fetchData() throws Exception {
        List<JSONObject> allEvents = new ArrayList<>();

        for (int poll = 0; poll < 10; poll++) {
            String urlStr = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + poll;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray arr = json.getJSONArray("events");

            for (int i = 0; i < arr.length(); i++) {
                allEvents.add(arr.getJSONObject(i));
            }

            System.out.println("Fetched poll " + poll);
            Thread.sleep(5000);
        }

        return allEvents;
    }

    static List<JSONObject> deduplicate(List<JSONObject> events) {
        Set<String> seen = new HashSet<>();
        List<JSONObject> unique = new ArrayList<>();

        for (JSONObject e : events) {
            String key = e.getString("roundId") + "-" + e.getString("participant");

            if (!seen.contains(key)) {
                seen.add(key);
                unique.add(e);
            }
        }

        return unique;
    }

    static Map<String, Integer> aggregate(List<JSONObject> events) {
        Map<String, Integer> scores = new HashMap<>();

        for (JSONObject e : events) {
            String participant = e.getString("participant");
            int score = e.getInt("score");

            scores.put(participant, scores.getOrDefault(participant, 0) + score);
        }

        return scores;
    }

    static List<JSONObject> createLeaderboard(Map<String, Integer> scores) {
        List<JSONObject> leaderboard = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("participant", entry.getKey());
            obj.put("totalScore", entry.getValue());
            leaderboard.add(obj);
        }

        leaderboard.sort((a, b) -> b.getInt("totalScore") - a.getInt("totalScore"));

        return leaderboard;
    }

    static void submit(List<JSONObject> leaderboard) throws Exception {
        URL url = new URL(BASE_URL + "/quiz/submit");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject payload = new JSONObject();
        payload.put("regNo", REG_NO);
        payload.put("leaderboard", leaderboard);

        conn.getOutputStream().write(payload.toString().getBytes());

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }
}