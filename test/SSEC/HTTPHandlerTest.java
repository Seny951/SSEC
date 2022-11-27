package SSEC;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

enum requestTypes {
    POST,
    GET,
    DELETE
}

class HTTPHandlerTest {

    @Test
    void PostInvalidDateTest() throws IOException {
        HttpURLConnection con = openConnection(requestTypes.POST.toString(), "http://localhost:8001/test");

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"NOT_A_TIMESTAMP\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        sendMessage(con, jsonInputString);
        readResponse(con);
        con.disconnect();
    }

    @Test
    void PostInvalidNameTest() throws IOException {
        HttpURLConnection con = openConnection(requestTypes.POST.toString(), "http://localhost:8001/test");

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"2022-26-11T23:46:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        sendMessage(con, jsonInputString);
        readResponse(con);
        con.disconnect();
    }

    @Test
    void PostValidDataTest() throws IOException {
        HttpURLConnection con = openConnection(requestTypes.POST.toString(), "http://localhost:8001/test");

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"2022-26-11T23:46:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"s__name\" : \"row_5.5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        sendMessage(con, jsonInputString);
        readResponse(con);
        con.disconnect();
    }

    @Test
    void PostEmptyRequestTest() throws IOException {
        HttpURLConnection con = openConnection(requestTypes.POST.toString(), "http://localhost:8001/test");
        String jsonInputString = "";
        sendMessage(con, jsonInputString);
        readResponse(con);
        con.disconnect();
    }

    @Test
    void getRequestTest() throws IOException {
        String URL = "http://localhost:8001/test?characterList=User";
        HttpURLConnection con = openConnection(requestTypes.POST.toString(), URL);
        con.setRequestMethod(requestTypes.GET.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        API.logger.log(Level.INFO, "Message Sent to Server: " + URL);
        //Skip sendMessage here as we don't send a body with a GET
        readResponse(con);
        con.disconnect();
    }

    static HttpURLConnection openConnection(String requestType, String URL) throws IOException {
        URL url = new URL (URL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(requestType);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con;
    }

    static void sendMessage(HttpURLConnection con, String request) throws IOException {
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = request.getBytes("utf-8");
            os.write(input, 0, input.length);
            API.logger.log(Level.INFO, "Message Sent to Server: " + request);
        }
    }

    static void readResponse(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            readResponseStream(br, responseCode);
        } catch (IOException e) {
            // Error response so read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                readResponseStream(br, responseCode);
            }
        }
    }

    static void readResponseStream(BufferedReader br, int responseCode) throws IOException {
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        API.logger.log(Level.INFO, "Message Received from Server: ResponseCode " + responseCode + " - " + response);
    }
}