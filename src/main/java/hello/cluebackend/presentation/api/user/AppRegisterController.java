package hello.cluebackend.presentation.api.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppRegisterController {

    @GetMapping("/app/register")
    public String appRegister(){
        return "appRegister";
    }
}
