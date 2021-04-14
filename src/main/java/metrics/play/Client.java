package metrics.play;

import metrics.play.api.RoomsPage;
import metrics.play.api.SearchFreeRoomsQuery;
import metrics.play.domain.CreateReservationCommand;
import metrics.play.domain.CreateRoomCommand;
import metrics.play.domain.RoomDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Client {

    private final Api api = new Api();
    private final List<String> cities = List.of("Paris", "Berlin", "London", "Rome", "Madrid", "Warsaw", "Prague", "Budapest", "Amsterdam");
    private final static Integer MAX_DAYS = 60;
    private final static Integer MAX_LENGTH = 14;

    public static void main(String[] args) {
        new Client().run();
    }

    private void run() {
        createRooms();
        makeReservations();
    }

    private void createRooms() {
        Flux.interval(Duration.ofMillis(2000))
                .flatMap(this::createRoom)
                .subscribeOn(Schedulers.single())
                .subscribe();
    }

    private void makeReservations() {
        Flux.create(emitter())
                .map(this::searchQuery)
                .flatMap(this::fetchFreeRooms)
                .flatMap(this::makeReservation)
                .onErrorContinue((error, object) -> error.printStackTrace())
                .subscribeOn(Schedulers.parallel())
                .blockLast();
    }

    private Consumer<FluxSink<Long>> emitter() {
        long start = System.currentTimeMillis();
        return (sink) -> {
            long i = 0;
            while (true) {
                sink.next(i++);
                long secondsSinceStart = (System.currentTimeMillis() - start) / 1000;
                try {
                    Thread.sleep((long) (Math.abs(Math.sin(0.5 * secondsSinceStart) * 100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Tuple2<Long, SearchFreeRoomsQuery> searchQuery(Long number) {
        SearchFreeRoomsQuery query = new SearchFreeRoomsQuery();
        query.setCity(city(number));
        LocalDate from = LocalDate.now().plusDays(number % MAX_DAYS);
        query.setFromInclusive(from);
        query.setEndExclusive(from.plusDays(1 + number % MAX_LENGTH));
        query.setPageNumber(0);
        return Tuples.of(number, query);
    }

    private Mono<Void> makeReservation(Tuple2<SearchFreeRoomsQuery, Page<RoomDto>> params) {
        SearchFreeRoomsQuery query = params.getT1();
        Page<RoomDto> roomsPage = params.getT2();
        if(roomsPage.getTotalElements() == 0) {
            return Mono.empty();
        }
        CreateReservationCommand command = new CreateReservationCommand();
        command.setRoomId(randomRoom(roomsPage).getId());
        command.setFromInclusive(query.getFromInclusive());
        command.setUntilExclusive(query.getEndExclusive());
        return api.makeReservation(command);
    }

    private RoomDto randomRoom(Page<RoomDto> roomsPage) {
        return roomsPage.getContent().get((int) Math.floor(Math.random() * roomsPage.getTotalElements()));
    }

    private Mono<Tuple2<SearchFreeRoomsQuery, Page<RoomDto>>> fetchFreeRooms(Tuple2<Long, SearchFreeRoomsQuery> params) {
        Long number = params.getT1();
        SearchFreeRoomsQuery query = params.getT2();
        return api.fetchFreeRooms(query).flatMap(roomsPage -> {
           if(roomsPage.getTotalPages() == 0) {
               return Mono.just(Tuples.of(query, roomsPage));
           } else {
               return api.fetchFreeRooms(query.withPageNumber((int) (number % roomsPage.getTotalPages())))
                       .map((result) -> Tuples.of(query, result));
           }
        });
    }

    private Mono<Void> createRoom(Long number) {
        String city = city(number);
        String name = UUID.randomUUID().toString();
        return api.createRoom(name, city);
    }

    private String city(Long number) {
        return cities.get((int) (number % cities.size()));
    }
}


class Api {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader("Content-type", "application/json")
            .build();

    Mono<Void> createRoom(String name, String city) {
        return webClient.post().uri("/rooms").bodyValue(new CreateRoomCommand(name, city)).retrieve().bodyToMono(Void.class);
    }

    Mono<Page<RoomDto>> fetchFreeRooms(SearchFreeRoomsQuery query) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/rooms")
                .queryParam("city", query.getCity())
                .queryParam("pageNumber", query.getPageNumber())
                .queryParam("fromInclusive", query.getFromInclusive())
                .queryParam("endExclusive", query.getEndExclusive())
                .build()
        )
                .retrieve().bodyToMono(ParameterizedTypeReference.forType(RoomsPage.class));
    }

    Mono<Void> makeReservation(CreateReservationCommand command) {
        return webClient.post()
                .uri("/reservations")
                .bodyValue(command)
                .retrieve()
                .bodyToMono(Void.class);
    }
}