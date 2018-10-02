package com.dimlo;

import com.dimlo.model.Role;
import com.dimlo.model.User;
import com.dimlo.repository.UserRepository;
import com.dimlo.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping(path="/db")
public class MainController {
    private UserRepository userRepository;

    private JavaMailSender javaMailSender;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path="/add")
    public String addNewUserPost (@RequestParam String email) {
        String emailLowerCase = email.toLowerCase();
        try {
            if (userRepository.findByEmail(emailLowerCase) == null ) {
                User newUser = new User();
                newUser.setEmail(emailLowerCase);
                String password = PasswordGenerator.simpleGenerate();
                newUser.setPassword(passwordEncoder.encode(password));
                newUser.setActive(true);
                newUser.setRoles(Collections.singleton(Role.USER));

                userRepository.save(newUser);

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(emailLowerCase);
                message.setSubject("CCSSS Registration");
                message.setText(password);
                javaMailSender.send(message);
                return "redirect:/login?success";

            } else {
                return "redirect:/login?dub";
            }
        } catch (TransactionSystemException e) {
            return "redirect:/login?invalid";
        }


    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
