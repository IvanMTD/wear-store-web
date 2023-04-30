package ru.wear.store.web.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Product;

import java.io.FileNotFoundException;

@Service
public class ProductService {
    private WebClient webClient;

    public ProductService(){
        webClient = WebClient.create();
    }

    public Flux<Product> findAll(){
        return webClient
                .get()
                .uri("http://localhost:8090/catalog/get/all")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Product> findById(long id){
        return webClient
                .get()
                .uri("http://localhost:8090/catalog/get/" + id)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Product> save(Product product){
        return webClient
                .post()
                .uri("http://localhost:8090/catalog/add")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
