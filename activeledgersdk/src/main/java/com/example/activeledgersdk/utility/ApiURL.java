package com.example.activeledgersdk.utility;

public class ApiURL {

    private static String subscribe = "/api/activity/subscribe";
    private static String event = "/api/events";

    public static String subscribeURL() {
        return subscribe;
    }

    public static String subscribeURL(String stream) {
        return subscribe + "/" + stream;
    }

    public static String eventsURL() {
        return event;
    }

    public static String eventsURL(String cotract) {
        return event + "/" + cotract;
    }

    public static String eventsURL(String cotract, String event) {
        return event + "/" + cotract + "/" + event;
    }

}
