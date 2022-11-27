package SSEC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

class DataObject {
    private String s__uuid;
    private String s__name;
    private Date d__timestamp;

    public String getS__uuid() { return s__uuid; }
    public void setS__uuid(String s__uuid) { this.s__uuid = s__uuid; }

    public String getS__name() {
        return s__name;
    }

    public void setS__name(String s__name) {
        this.s__name = s__name;
    }

    public Date getD__timestamp() {
        return d__timestamp;
    }

    public void setD__timestamp(Date d__timestamp) {
        this.d__timestamp = d__timestamp;
    }
}

public class HTTPHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(httpExchange);
                break;
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "DELETE":
                break;
        }
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        // configure the mapper, so we can ignore unknown and missing parameters and validate these manually and
        // allow a single string as an array object in order to deserialize a JSON string into an array.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // Read the JSON data from the request body and replace new line delimiters with commas for deserialization with Jackson.
        // Jackson also validates date formats on deserialization, so we can reject requests with invalid dates based on Json parsing exceptions
        byte[] test = httpExchange.getRequestBody().readAllBytes();
        String s = new String(test, StandardCharsets.UTF_8);
        s = s.replace("\n", ",");

        // Try to Deserialize the JSON into an object list & in case of any errors respond with an appropriate http response code
        List<DataObject> objList;
        try {
            objList = objectMapper.readValue(s, typeFactory.constructCollectionType(List.class, DataObject.class));
        } catch (JsonProcessingException e) {
            API.logger.log(Level.SEVERE, e.getMessage());

            // send error response
            sendResponse(httpExchange, e.getMessage(), 400);
            return;
        }

        // Validate uuids
        if(objList.size() > 0) {
            for(int i = 0; i < objList.size(); i++) {
                DataObject t = objList.get(i);
                if(t.getS__uuid() == null || t.getS__uuid().isEmpty()) {
                    API.logger.log(Level.WARNING, "Object " + objList.indexOf(t) + " in request has no uuid, generating a default");
                    t.setS__uuid(generateuuid(6));
                }

                if(t.getS__name() == null || t.getS__name().isEmpty()) {
                    sendResponse(httpExchange, "Object " + objList.indexOf(t) + " has no name", 400);
                }
            }
        }

        // Communicate with Database here.

        // return outcome of the database update, in this case we aren't actually performing any updates on data so
        // assume successful.
        sendResponse(httpExchange, "Data successfully stored in the database", 200);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private void sendResponse(HttpExchange httpExchange, String message, int responseCode) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(responseCode, message.length());
        outputStream.write(message.getBytes());
        outputStream.flush();
        outputStream.close();
    }


    /**
     * Generates a random uuid by picking characters at random from a predetermined string
     * up to a given length
     * @param length - Length of the uuid to generate
     */
    private String generateuuid(int length) {
        final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
