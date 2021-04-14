package metrics.play.infrastructure;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import metrics.play.domain.ReservationMade;
import metrics.play.domain.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReservationsCountMetric {

    private static final String GAUGE_NAME = "business.reservationsGauge";
    private static final String COUNTER_NAME = "business.reservationsCounter";
    private final MeterRegistry meterRegistry;
    private final ReservationRepository reservationRepository;
    private final Logger logger = LoggerFactory.getLogger(ReservationsCountMetric.class);

    private final AtomicLong reservationsCount = new AtomicLong();
    private final AtomicLong pastReservationsCount = new AtomicLong();
    private final AtomicLong futureReservationsCount = new AtomicLong();
    private final AtomicLong currentReservationsCount = new AtomicLong();
    private Counter reservationsCounter;

    public ReservationsCountMetric(MeterRegistry meterRegistry, ReservationRepository reservationRepository) {
        this.meterRegistry = meterRegistry;
        this.reservationRepository = reservationRepository;
    }

    @EventListener({ApplicationReadyEvent.class})
    public void init() {
        updateGauge();
        this.reservationsCounter = Counter.builder(COUNTER_NAME).tag("name", COUNTER_NAME).register(meterRegistry);
        meterRegistry.gauge(GAUGE_NAME, List.of(Tag.of("name", GAUGE_NAME), Tag.of("mettricattribute", "all")), reservationsCount);
        meterRegistry.gauge(GAUGE_NAME, List.of(Tag.of("name", GAUGE_NAME), Tag.of("mettricattribute", "future")), futureReservationsCount);
        meterRegistry.gauge(GAUGE_NAME, List.of(Tag.of("name", GAUGE_NAME), Tag.of("mettricattribute", "past")), pastReservationsCount);
        meterRegistry.gauge(GAUGE_NAME, List.of(Tag.of("name", GAUGE_NAME), Tag.of("mettricattribute", "current")), currentReservationsCount);
    }

    @TransactionalEventListener({ReservationMade.class})
    public void updateGauge() {
        long all = reservationRepository.count();
        LocalDate now = LocalDate.now();
        long future = reservationRepository.countByFromInclusiveGreaterThan(now);
        long past = reservationRepository.countByUntilExclusiveLessThanEqual(now);
        reservationsCount.set(all);
        pastReservationsCount.set(past);
        futureReservationsCount.set(future);
        currentReservationsCount.set(all - past - future);
    }

    @TransactionalEventListener({ReservationMade.class})
    public void updateCounter() {
        reservationsCounter.increment();
    }
}
