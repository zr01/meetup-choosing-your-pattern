package studio.camelcase.meetup.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMetrics {

    MeterRegistry registry;

    Counter ctrPostCreate;
    Counter ctrCreatedAsync;
    Counter ctrCreatedSync;
    Counter ctrCreated;

    public ApplicationMetrics(
        MeterRegistry registry
    ) {
        this.registry = registry;
        ctrPostCreate = Counter.builder("person_create_requested")
            .tags("method", "POST", "by", "sync")
            .register(registry);
        ctrCreated = Counter.builder("person_created")
            .tags("method", "SAVED", "by", "db")
            .register(registry);
        ctrCreatedSync = Counter.builder("person_created_synchronously")
            .tags("method", "DIRECT", "by", "sync")
            .register(registry);
        ctrCreatedAsync = Counter.builder("person_created_asynchronously")
            .tags("method", "FUTURE", "by", "async")
            .register(registry);
    }

    public void requestInc() {
        ctrPostCreate.increment();
    }

    public void syncInc() {
        ctrCreatedSync.increment();
    }

    public void asyncInc() {
        ctrCreatedAsync.increment();
    }

    public void createdInc() { ctrCreated.increment(); }
}
