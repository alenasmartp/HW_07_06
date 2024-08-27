package org.example.lesson25.hw;

import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import org.example.lesson25.currency.CurrencyService;
import reactor.core.publisher.Flux;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.stream.Collectors;

public class HomeWork {
    public static void main(String[] args) {
//       Task 1
        Flux<String> names = Flux.just("Max", "Masha", "Alexander", "Olga");
        names
                .map(s -> s.length())
                .collectList()
                .subscribe(
                        list -> System.out.println(list)
                );
//        Task 2
        names
                .flatMap(s -> Flux.just(s.toLowerCase(), s.toUpperCase()))
                .collect(
                        Collectors.toSet()
                )
                .subscribe(
                        set -> System.out.println(set)
                );

//       Task 3
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.frankfurter.app")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(ReactorCallAdapterFactory.create())
                .build();

        CurrencyService service = retrofit.create(CurrencyService.class);

        Flux<Double> euros = Flux.just(10.0, 20.0, 50.0, 100.0, 500.0);

        euros
                .flatMap(e -> service.convert(e, "EUR", "GBP"))
                .map(c -> c.getRates().get("GBP"))
                .flatMap(p -> service.convert(p, "GBP", "USD"))
                .map(c -> c.getRates().get("USD"))
                .collectList()
                .subscribe(
                        result -> System.out.println(result)
                );
    }
}
