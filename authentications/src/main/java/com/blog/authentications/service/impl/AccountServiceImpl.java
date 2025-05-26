package com.blog.authentications.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blog.authentications.clients.NotificationServiceClient;
import com.blog.authentications.dtos.ResponseDTO;
import com.blog.authentications.dtos.UserDto;
import com.blog.authentications.entities.ActivationHash;
import com.blog.authentications.entities.AppRole;
import com.blog.authentications.entities.Token;
import com.blog.authentications.entities.Users;
import com.blog.authentications.exceptions.GeneralException;
import com.blog.authentications.exceptions.RessourceNotFoundException;
import com.blog.authentications.model.ActivationHashGenerator;
import com.blog.authentications.repo.ActivationHashRepository;
import com.blog.authentications.repo.AppRoleRepository;
import com.blog.authentications.repo.AppUserRepository;
import com.blog.authentications.repo.TokenRespository;
import com.blog.authentications.request.MailRequest;
import com.blog.authentications.request.SignUpRequest;
import com.blog.authentications.response.SignUpResponse;
import com.blog.authentications.service.AccountService;
import com.blog.authentications.utils.Constants;
import com.blog.authentications.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final ActivationHashRepository activationHashRepository;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationServiceClient notificationServiceClient;
    private final TokenRespository tokenRespository;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
                              PasswordEncoder passwordEncode, ActivationHashRepository activationHashRepository,
                              NotificationServiceClient notificationServiceClient, TokenRespository tokenRespository) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncode;
        this.tokenRespository = tokenRespository;
        this.activationHashRepository = activationHashRepository;
        this.notificationServiceClient = notificationServiceClient;
    }

    @Override
    public Users addNewUser(Users user) {
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(user);
    }

    @Override
    public UserDto getUserById(Long id) throws RessourceNotFoundException {
        Users user = appUserRepository.findById(id).orElseThrow(()->{
            throw new RessourceNotFoundException("User not found");
        });
        return new UserDto(user);
    }

    public Users getUserByIdFeign(Long id) throws RessourceNotFoundException {
        Users user = appUserRepository.findById(id).orElseThrow(()->{
            throw new RessourceNotFoundException("User not found");
        });
        return user;
    }

    @Override
    public AppRole addNewRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Users user = appUserRepository.findByUsername(username).orElseThrow(()->{
            throw new RuntimeException("User doesn't not exist");
        });
        AppRole role = appRoleRepository.findByRoleName(roleName).orElseThrow(()-> {
            throw new RuntimeException("Role not found");
        });
        user.getAppRoles().add(role);
    }

    @Override
    public Users loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username).get();
    }

    @Override
    public List<Users> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (signUpRequest==null) {
            throw new IllegalArgumentException("signUpRequest must not be null");
        }
        Users user = new Users();
        AppRole role = appRoleRepository.findByRoleName("USER").orElseThrow(()->{
            throw new RuntimeException("Role not found");
        });
        Collection<AppRole> appRoles = new ArrayList<>();
        appRoles.add(role);
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setStatus(Constants.STATE_DEACTIVATED);
        user.setActive(false);
        user.setAppRoles(appRoles);
        user = appUserRepository.save(user);
        return generateActivationHash(user);
    }

    public SignUpResponse generateActivationHash(Users user){

        String activationNewHash = ActivationHashGenerator.generateActivationHash();
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30);
        ActivationHash activationHash = new ActivationHash();
        activationHash.setExpirationDate(expirationDate);
        activationHash.setHash(activationNewHash);
        activationHash.setUser(user);
        activationHashRepository.save(activationHash);

        this.notificationServiceClient.sendActivationEmail(new MailRequest(user.getId(), activationHash.getHash()));

        return new SignUpResponse("200", "Nouveau hash genéreé avec succès pour une durrée de 30 minutes !", user);
    }

    @Override
    public ResponseDTO generateNewActivationHash(String holdHash) throws GeneralException {
        ActivationHash activationHash = activationHashRepository.findByHash(holdHash);
        if (activationHash != null) {
            // Génération du hash d'activation
            String activationNewHash = ActivationHashGenerator.generateActivationHash();

            // Calcul de la date d'expiration (par exemple, 24 heures après la création du
            // hash)
            LocalDateTime expirationDate = LocalDateTime.now().plusHours(24);

            // Envoi de l'email contenant le lien d'activation à l'utilisateur
//            mailService.sendActivationEmail(activationHash.getUser(), activationNewHash);

            // Mise à jour de l'entrée ActivationHash liée à l'utilisateur
            activationHash.setHash(activationNewHash);
            activationHash.setExpirationDate(expirationDate);
            activationHashRepository.save(activationHash);
            return new ResponseDTO("200", "Nouveau hash genéreé avec succès !");
        } else {
            throw new GeneralException(Constants.ACCOUNT_ACTIVATION_LINK_NOT_FOUND_CODE,
                    Constants.ACCOUNT_ACTIVATION_LINK_NOT_FOUND);
        }
    }

    @Override
    public ResponseDTO activateAccount(String activationHash) throws GeneralException {
        ActivationHash activationHashEntity = activationHashRepository.findByHash(activationHash);

        if (activationHashEntity == null) {
            throw new GeneralException(Constants.ACCOUNT_ACTIVATION_LINK_NOT_FOUND_CODE,
                    Constants.ACCOUNT_ACTIVATION_LINK_NOT_FOUND);
        }
        if (activationHashEntity.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new GeneralException(Constants.ACCOUNT_ACTIVATION_LINK_EXPIRED_CODE,
                    Constants.ACCOUNT_ACTIVATION_LINK_EXPIRED);
        }
        Users user = activationHashEntity.getUser();
        user.setLastUpdateOn(new Date());
        user.setStatus(Constants.STATE_ACTIVATED);
        user.setActive(true);
        var savedUser = appUserRepository.save(user);
        activationHashRepository.delete(activationHashEntity);

        return new ResponseDTO("200", "Compte activé avec succès !");
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authToken = request.getHeader(JWTUtil.AUTH_HEADER);
        if (authToken != null && authToken.startsWith(JWTUtil.TOKEN_PREFIX)) {
            try{
                String refreshToken = authToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Users loadedUser = this.loadUserByUsername(username);
                String jwtAccessToken = JWT.create()
                        .withSubject(loadedUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())//nom de l'aplication qui a generer le token;
                        .withClaim("roles", loadedUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token", jwtAccessToken);
                idToken.put("refresh-token", refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
            } catch (Exception e) {
                throw e;
            }
        }else {
            throw new RuntimeException("Refresh token ");
        }
    }

    @Override
    public Token findByTk(String tk) {
        return tokenRespository.findByTk(tk).orElse(null);
    }


}
