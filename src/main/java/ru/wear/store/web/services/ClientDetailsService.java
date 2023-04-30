package ru.wear.store.web.services;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Client;
import ru.wear.store.web.utils.ClientDetails;

@Service
public class ClientDetailsService implements ReactiveUserDetailsService {

    private WebClient webClient;

    public ClientDetailsService() {
        this.webClient = WebClient.create();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if(username.equals("")){
            username = "null";
        }
        return webClient
                .get()
                .uri("http://localhost:8090/auth/username/" + username)
                .retrieve()
                .bodyToMono(Client.class)
                .map(client -> new ClientDetails(client).toUserDetails())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(username)));
    }
}
