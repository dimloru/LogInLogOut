package com.dimlo;

import com.dimlo.model.User;
import com.dimlo.repository.UserRepository;
import com.dimlo.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/db") //get rid of it
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public String addNewUserPost (@RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        if (email != null && !email.isEmpty() &&
                userRepository.findByEmail(email) == null ) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(PasswordGenerator.generate());

            userRepository.save(newUser); //wrong format exception
        } else {
            System.out.println("NOT NEW NOT NEW NOT NEW NOT NEW NOT NEW");
            return "redirect:/login"; // redirectAttributes
            // do something
        }


        return "redirect:login"; // different view like "an email with a pass has been sent..."
                        // + link to /login
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
