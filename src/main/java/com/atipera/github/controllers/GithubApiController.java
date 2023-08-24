package com.atipera.github.controllers;

import com.atipera.github.model.UserDto;
import com.atipera.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
class GithubApiController {

    private final UserService userService;

    @Autowired
    GithubApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<?> getUser(@RequestHeader("Accept") String acceptHeader, @PathVariable("username") String username) {
        if (acceptHeader.equalsIgnoreCase("application/xml")) { //obsługa w przypadku żądania XML
            ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE.value(), "Not acceptable header");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)  // Ustawia format odpowiedzi na JSON
                    .body(errorMessage);
        } else {
            UserDto userDto = userService.getUser(username);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
            return ResponseEntity.ok(userDto);
        }
    }
}

class ErrorMessage {
    private int status;
    private String message;

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }
}




