package bd.com.dipti.testspringsecurity.restcontroller;

import bd.com.dipti.testspringsecurity.model.RegistrationRequest;
import bd.com.dipti.testspringsecurity.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/registration")
@AllArgsConstructor
public class RegistrationRestController {
    private RegistrationService service;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return service.register(request);
    }
}