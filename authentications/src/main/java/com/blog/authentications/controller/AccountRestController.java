package com.blog.authentications.controller;

import com.blog.authentications.dtos.ResponseDTO;
import com.blog.authentications.dtos.UserDto;
import com.blog.authentications.entities.AppRole;
import com.blog.authentications.entities.Token;
import com.blog.authentications.entities.Users;
import com.blog.authentications.exceptions.GeneralException;
import com.blog.authentications.request.ActivationRequest;
import com.blog.authentications.request.SignUpRequest;
import com.blog.authentications.response.SignUpResponse;
import com.blog.authentications.service.AccountService;
import com.blog.authentications.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//@CrossOrigin("*")
@RequestMapping("/api/v1/")
@RestController
public class AccountRestController {

    private AccountService accountService;

    private LogoutService logoutService;

    public AccountRestController(AccountService accountService, LogoutService logoutService) {
        this.accountService = accountService;
        this.logoutService = logoutService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(accountService.listUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return ResponseEntity.ok(accountService.addNewUser(user));
    }

    @PostMapping("/roles")
    public ResponseEntity<AppRole> addRole(@RequestBody AppRole appRole) {
        return ResponseEntity.ok(accountService.addNewRole(appRole));
    }

    @PostMapping("/addRoleToUser")
    public ResponseEntity addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUserName(), roleUserForm.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        accountService.refreshToken(request, response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Users> getProfile(Principal principal) {
        return ResponseEntity.ok(accountService.loadUserByUsername(principal.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        this.logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @PostMapping("signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(accountService.signUp(signUpRequest));
    }

    @PostMapping(value = "activate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> activateAccount(
            @RequestBody @Valid ActivationRequest activationRequest) throws GeneralException {
        return ResponseEntity.ok(accountService.activateAccount(activationRequest.getActivationHash()));
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) throws GeneralException {
        return ResponseEntity.ok(accountService.getUserById(id));
    }


    @GetMapping("users/token")
    ResponseEntity<Token> findByTk(@RequestParam String tk) {
        return ResponseEntity.ok(accountService.findByTk(tk));
    }

    @GetMapping("users/name")
    ResponseEntity<Users> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(accountService.loadUserByUsername(username));
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RoleUserForm {
    private String role;
    private String userName;
}
