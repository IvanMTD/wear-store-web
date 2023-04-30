package ru.wear.store.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Client;
import ru.wear.store.web.utils.ClientDetails;
import ru.wear.store.web.validations.ClientValidator;


@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final ClientValidator clientValidator;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Mono<Rendering> registrationPage(){
        return Mono.just(Rendering
                .view("registration/form")
                .modelAttribute("client", new Client())
                .modelAttribute("genders", Client.Gender.values())
                .build()
        );
    }

    @PostMapping("/new")
    public Mono<Rendering> registerClient(@ModelAttribute(name = "client") @Valid Client client, Errors errors){
        clientValidator.validate(client,errors);
        if(errors.hasErrors()){
            return Mono.just(Rendering
                    .view("registration/form")
                    .modelAttribute("client", client)
                    .modelAttribute("genders", Client.Gender.values())
                    .build()
            );
        }

        client.setRole("ROLE_User");
        client.setPassword(passwordEncoder.encode(client.getPassword()));

        WebClient.create()
                .post()
                .uri("http://localhost:8090/auth")
                .bodyValue(client)
                .retrieve()
                .bodyToMono(Client.class)
                .subscribe(System.out::println);

        return Mono.just(Rendering
                .view("redirect:/")
                .build()
        );
    }

    @GetMapping("/login")
    public Mono<Rendering> signInPage(){
        return Mono.just(Rendering
                .view("registration/login")
                .build()
        );
    }

    @ModelAttribute(name = "title")
    public String title(){
        return "Auth Page";
    }

    @ModelAttribute(name = "auth")
    public boolean auth(Authentication authentication){
        return authentication != null && authentication.isAuthenticated();
    }

    @ModelAttribute(name = "fullName")
    public String client(Authentication authentication){
        if(authentication != null && authentication.isAuthenticated()){
            Client client = ((ClientDetails) authentication.getPrincipal()).getClient();
            return client.getSurname() + " " + client.getName() + " " + client.getMiddleName();
        }else{
            return null;
        }
    }
}
