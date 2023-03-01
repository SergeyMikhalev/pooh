package ru.job4j.pooh;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static ru.job4j.pooh.Req.GET;
import static ru.job4j.pooh.Req.POST;

public class TopicService implements Service {
    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String type = req.httpRequestType();
        String name = req.getSourceName();
        String param = req.getParam();

        String respText = null;
        String respStatus = "200";

        if (POST.equals(type)) {
            var map = topic.get(name);
            if (!Objects.isNull(map)) {
                map.forEach((key, value) -> value.add(param));
            }
        } else if (GET.equals(type)) {
            topic.putIfAbsent(name, new ConcurrentHashMap<>());
            topic.get(name).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            respText = topic.get(name).get(param).poll();
            if (Objects.isNull(respText)) {
                respStatus = "204";
            }
        }
        return new Resp(respText, respStatus);
    }
}
