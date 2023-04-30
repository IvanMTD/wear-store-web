package ru.wear.store.web.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.ClientOrder;

@Service
public class OrderService {
    private WebClient webClient;

    public OrderService(){
        webClient = WebClient.create();
    }

    public Mono<ClientOrder> save(ClientOrder clientOrder){
        return webClient
                .post()
                .uri("http://localhost:8090/orders/add")
                .bodyValue(clientOrder)
                .retrieve()
                .bodyToMono(ClientOrder.class);
    }
}
