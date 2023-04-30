package ru.wear.store.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Client;
import ru.wear.store.web.models.ClientOrder;
import ru.wear.store.web.utils.ClientDetails;

@Controller
@RequestMapping("/")
@SessionAttributes(value = "order")
public class MainPageController {
    @GetMapping()
    public Mono<Rendering> homePage(){
        return Mono.just(Rendering
                .view("home")
                .build()
        );
    }

    @ModelAttribute(name = "title")
    public String title(){
        return "Main Page";
    }

    @ModelAttribute(name = "order")
    public ClientOrder order(){
        return new ClientOrder();
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
