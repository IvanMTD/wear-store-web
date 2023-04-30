package ru.wear.store.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Client;
import ru.wear.store.web.models.ClientOrder;
import ru.wear.store.web.services.OrderService;
import ru.wear.store.web.utils.ClientDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@SessionAttributes(value = "order")
public class CartController {

    private final OrderService orderService;

    @GetMapping()
    public Mono<Rendering> showCart(@ModelAttribute(name = "order") ClientOrder order){
        return Mono.just(Rendering
                .view("cart/list")
                .modelAttribute("cartCatalog", order.getProducts())
                .modelAttribute("totalCoast", order.getTotalCoast())
                .build()
        );
    }

    @PostMapping("/create")
    public Mono<Rendering> createOrder(@ModelAttribute(name = "order") ClientOrder order, SessionStatus sessionStatus){
        Mono<ClientOrder> orderMono = orderService.save(order);
        orderMono.subscribe(System.out::println);
        sessionStatus.setComplete();

        return Mono.just(Rendering
                .view("redirect:/")
                .build()
        );
    }

    @ModelAttribute(name = "title")
    public String title(){
        return "Cart Page";
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
