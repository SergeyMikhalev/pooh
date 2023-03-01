package ru.job4j.pooh;

import java.util.Set;

public class Req {

    public static final String QUEUE = "queue";
    public static final String TOPIC = "topic";

    public static final String POST = "POST";
    public static final String GET = "GET";

    private final static Set<String> MODES = Set.of(QUEUE, TOPIC);

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String httpRequestType;
        String poohMode;
        String sourceName;
        String param = "";

        httpRequestType = getHttpRequestType(content);
        String[] strings = content.split(System.lineSeparator());
        String subs = (strings[0].split(" "))[1];
        String[] modeAndName = subs.split("/");
        poohMode = modeAndName[1];
        if (!MODES.contains(poohMode)) {
            throw new IllegalArgumentException();
        }
        sourceName = modeAndName[2];
        if (POST.equals(httpRequestType)) {
            param = strings[7];
        } else if (GET.equals(httpRequestType)) {
            if (TOPIC.equals(poohMode)) {
                param = modeAndName[3];
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    private static String getHttpRequestType(String content) {
        String httpRequestType;
        if (content.startsWith(POST + " ")) {
            httpRequestType = POST;
        } else if (content.startsWith(GET + " ")) {
            httpRequestType = GET;
        } else {
            throw new IllegalArgumentException();
        }
        return httpRequestType;
    }
}
