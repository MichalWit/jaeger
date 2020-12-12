package org.example.actuator;

import com.google.common.collect.EvictingQueue;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomTraceRepository implements HttpTraceRepository {

    private EvictingQueue<HttpTrace> lastTraces = EvictingQueue.create(10);

    @Override
    public List<HttpTrace> findAll() {
        return new ArrayList<>(lastTraces);
    }

    @Override
    public void add(HttpTrace trace) {
        lastTraces.add(trace);
    }
}
