package ru.job4j.pooh;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static ru.job4j.pooh.Req.GET;
import static ru.job4j.pooh.Req.POST;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String respText = null;
        String respCode = "200";

        String type = req.httpRequestType();
        String name = req.getSourceName();
        String param = req.getParam();

        if (GET.equals(type)) {
            respText = queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll();
            if (Objects.isNull(respText)) {
                respCode = "204";
            }
        } else if (POST.equals(req.httpRequestType())) {
            queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
            queue.get(name).add(param);
        }

        return new Resp(respText, respCode);
    }
}
