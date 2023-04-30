package ru.wear.store.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.wear.store.web.models.Client;
import ru.wear.store.web.models.Product;
import ru.wear.store.web.models.ClientOrder;
import ru.wear.store.web.services.ProductService;
import ru.wear.store.web.utils.ClientDetails;
import ru.wear.store.web.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/catalog")
@SessionAttributes(value = "order")
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final Path basePath = Paths.get("./src/main/resources/upload");

    @GetMapping()
    public Mono<Rendering> showCatalog(){
        return Mono.just(Rendering
                .view("catalog/list")
                .modelAttribute("catalog",productService.findAll())
                .modelAttribute("imageUtil",new ImageUtil())
                .build()
        );
    }

    @GetMapping("/{id}")
    public Mono<Rendering> showItem(@PathVariable(name = "id") long id){
        return Mono.just(Rendering
                .view("catalog/item")
                .modelAttribute("item", productService.findById(id))
                .modelAttribute("imageUtil",new ImageUtil())
                .build()
        );
    }

    @PostMapping("/{id}")
    public Mono<Rendering> addInCart(@PathVariable(value = "id") long id, @ModelAttribute(name = "order") ClientOrder clientOrder){

        productService.findById(id)
                .doOnNext(p -> {
                    System.out.println(p.getId());
                    clientOrder.addProduct(p);
                })
                .subscribe();

        return Mono.just(Rendering
                .view("redirect:/catalog/" + id)
                .build()
        );
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('Admin')")
    public Mono<Rendering> newProductForm(){
        return Mono.just(Rendering
                .view("catalog/new")
                .modelAttribute("product",new Product())
                .modelAttribute("postUrl","/catalog/add")
                .build()
        );
    }

    @PostMapping(path = "/add")
    @PreAuthorize("hasRole('Admin')")
    public Mono<Rendering> addNewProduct(@ModelAttribute(name = "product") Product product, @RequestPart("file")Mono<FilePart> filePartMono) throws IOException {

        filePartMono
                .doOnNext(f -> System.out.println(f.filename()))
                .flatMap(f -> f.transferTo(basePath.resolve("default.png")))
                .subscribe();

        File file = new File("./src/main/resources/upload/default.png");
        if(file.exists()){
            product.setImage(Files.readAllBytes(file.toPath()));
        }

        productService.save(product).subscribe();

        return Mono.just(Rendering
                .view("redirect:/catalog")
                .build()
        );
    }

    @ModelAttribute(name = "title")
    public String title(){
        return "Catalog Page";
    }

    @ModelAttribute(name = "auth")
    public boolean auth(Authentication authentication){
        return authentication != null && authentication.isAuthenticated();
    }

    @ModelAttribute(name = "order")
    public ClientOrder order(){
        return new ClientOrder();
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

    @ModelAttribute(name = "admin")
    public Mono<Boolean> isAdmin(Authentication authentication){
        if(authentication != null && authentication.isAuthenticated()){
            Client client = ((ClientDetails) authentication.getPrincipal()).getClient();
            if(client.getRole().equals("ROLE_Admin")){
                return Mono.just(true);
            }else{
                return Mono.just(false);
            }
        }else{
            return Mono.just(false);
        }
    }
}
