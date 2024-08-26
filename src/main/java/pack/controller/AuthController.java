package pack.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.UserDto;
import pack.entity.User;
import pack.model.AuthModel;

@RestController
@CrossOrigin("*")
public class AuthController {
    
    @Autowired
    private AuthModel model;
    
    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestBody UserDto userDto) {

        String id = userDto.getId();
        String pwd = userDto.getPwd();

        System.out.println("Received ID: " + id);
        System.out.println("Received Password: " + pwd);

        UserDto user = model.login(id, pwd);
        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            response.put("success", true);
            response.put("user", user);
        } else {
            response.put("success", false);
            response.put("message", "Invalid credentials");
        }

        return response;
    }

}