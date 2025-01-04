package microservice.portfolio.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = "application/json")
public interface HackTheBoxAPI {

    @GetExchange("/user/profile/basic/{userId}")
    String getUserProfileBasic(@PathVariable Long userId);
}
