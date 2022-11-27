package SSEC;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

class HTTPHandlerTest {

    @Test
    void PostInvalidDateTest() throws IOException {
        HttpURLConnection con = openConnection();

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"NOT_A_TIMESTAMP\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            API.logger.log(Level.INFO, "Message Sent to Server: " + jsonInputString);
        }

        // Read the response
        int responseCode = con.getResponseCode();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            readResponseStream(br, responseCode);
        } catch (IOException e) {
            // Error response so read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                readResponseStream(br, responseCode);

            }
        }

        con.disconnect();
    }

    @Test
    void PostInvalidNameTest() throws IOException {
        HttpURLConnection con = openConnection();

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"2022-26-11T23:46:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            API.logger.log(Level.INFO, "Message Sent to Server: " + jsonInputString);

        }

        // Read the response
        int responseCode = con.getResponseCode();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            readResponseStream(br, responseCode);
        } catch (IOException e) {
            // Error response so read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                readResponseStream(br, responseCode);

            }
        }

        con.disconnect();
    }

    @Test
    void PostValidDataTest() throws IOException {
        HttpURLConnection con = openConnection();

        String jsonInputString =
                "[{ \"s__uuid\" : \"ABC123\", \"s__name\" : \"row_1\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"DEF234\", \"s__name\" : \"row_2\", \"d__timestamp\" : \"2022-23-11T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"GHI345\", \"s__name\" : \"row_3\", \"d__timestamp\" : \"2022-26-11T23:46:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"JKL345\", \"s__name\" : \"row_4\"}\n" +
                        "{ \"s__name\" : \"row_5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"MNO456\", \"s__name\" : \"row_5.5\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}\n" +
                        "{ \"s__uuid\" : \"PQR567\", \"s__name\" : \"row_6\", \"d__timestamp\" : \"2022-11-23T15:21:50.123456Z\"}]";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            API.logger.log(Level.INFO, "Message Sent to Server: " + jsonInputString);

        }

        // Read the response
        int responseCode = con.getResponseCode();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            readResponseStream(br, responseCode);
        } catch (IOException e) {
            // Error response so read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                readResponseStream(br, responseCode);

            }
        }

        con.disconnect();
    }

    @Test
    void PostEmptyRequestTest() throws IOException {
        HttpURLConnection con = openConnection();

        String jsonInputString = "";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            API.logger.log(Level.INFO, "Message Sent to Server: " + jsonInputString);
        }

        // Read the response
        int responseCode = con.getResponseCode();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            readResponseStream(br, responseCode);
        } catch (IOException e) {
            // Error response so read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                readResponseStream(br, responseCode);

            }
        }

        con.disconnect();
    }

    static HttpURLConnection openConnection() throws IOException {
        URL url = new URL ("http://localhost:8001/test");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con;
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