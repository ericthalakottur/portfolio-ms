package microservice.portfolio.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(accept = "application/json")
public interface AtCoderAPI {

    @GetExchange("/user/submissions?user={username}&from_second={from_second}")
    List<String> getAtCoderCodeSubmissions(@PathVariable String username, @PathVariable Long from_second);
}
