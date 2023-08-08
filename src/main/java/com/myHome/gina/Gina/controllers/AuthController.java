package com.myHome.gina.Gina.controllers;

import com.myHome.gina.Gina.constants.enums.Gender;
import com.myHome.gina.Gina.models.Service;
import com.myHome.gina.Gina.models.User;
import com.myHome.gina.Gina.services.OrgService;
import com.myHome.gina.Gina.services.UserService;
import com.myHome.gina.Gina.utils.AuthUtils;
import com.myHome.gina.Gina.utils.ErrorResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/auth")
@CrossOrigin(value = "*")
@RestController
public class AuthController {

    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public Map signUp(@RequestParam Map<String,String> body){
        String type = body.get("type");
        if(type.equals("user")) {
            String name = body.get("name");
            String pic = body.get("pic");
            String password = body.get("password");
            String email = body.get("email");
            String details = body.get("details");
            String age = body.get("age");
            String phone = body.get("phone");
            String gender = body.get("gender");
            if (AuthUtils.anyNull(
                    name, pic, password, email,
                    age, phone, gender
            )) return ErrorResponseUtils.badRequest(null);
            User user = User.builder()
                    .userId(AuthUtils.getRandomId(8))
                    .details(Objects.isNull(details) ? "" : details)
                    .age(Integer.parseInt(age))
                    .phone(phone)
                    .email(email)
                    .name(name)
                    .pic(pic)
                    .password(password)
                    .creation(Timestamp.from(Instant.now()))
                    .gender(gender.toLowerCase().equals("male") ? Gender.MALE : Gender.FEMALE)
                    .build();
            if (userService.create(user))
                return Map.of(
                        "success", true,
                        "body", AuthUtils.jwtTokenGenerate(user)
                );
            return ErrorResponseUtils.internalServerError(null);
        } else if (type.equals("service")) {
            String name = body.get("name");
            String pic = body.get("pic");
            String password = body.get("password");
            String email = body.get("email");
            String details = body.get("details");
            if (AuthUtils.anyNull(
                    name, pic, password, email
            )) return ErrorResponseUtils.badRequest(null);
            Service service = Service.builder()
                    .serviceId(AuthUtils.getRandomId(8))
                    .creation(Timestamp.from(Instant.now()))
                    .details(Objects.isNull(details)?"":details)
                    .email(email)
                    .name(name)
                    .password(password)
                    .pic(pic)
                    .build();
            if(orgService.create(service)){
                return Map.of(
                        "success", true,
                        "body", AuthUtils.jwtTokenGenerate(service)
                );
            }
            return ErrorResponseUtils.internalServerError(null);
        } else return ErrorResponseUtils.badRequest(null);
    }

    @PostMapping("/signIn")
    public Map signIn(@RequestParam Map<String,String> body){
        String type = body.get("type");
        if(type.equals("user")) {
            String email = body.get("email");
            String password= body.get("password");
            if(AuthUtils.anyNull(email,password))
                return ErrorResponseUtils.badRequest(null);
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .build();
            if(userService.populate(user) && AuthUtils.checkPassword(password,user.getPassword())){
                return Map.of(
                        "success", true,
                        "body", Map.of(
                                "userId",user.getUserId(),
                                "name",user.getName(),
                                "email",user.getEmail(),
                                "details",user.getDetails(),
                                "pic",user.getPic(),
                                "phone",user.getPhone(),
                                "age",user.getAge(),
                                "gender",user.getGender().name(),
                                "jwt",AuthUtils.jwtTokenGenerate(user)
                        )
                );
            } else return ErrorResponseUtils.unauthorized(null);
        } else if (type.equals("service")) {
            String email = body.get("email");
            String password= body.get("password");
            if(AuthUtils.anyNull(email,password))
                return ErrorResponseUtils.badRequest(null);
            Service service = Service.builder()
                    .email(email)
                    .password(password)
                    .build();
            if(orgService.populate(service) && AuthUtils.checkPassword(password,service.getPassword())){
                return Map.of(
                        "success", true,
                        "body", Map.of(
                                "serviceId",service.getServiceId(),
                                "name",service.getName(),
                                "pic",service.getPic(),
                                "email",service.getEmail(),
                                "details",service.getDetails(),
                                "jwt",AuthUtils.jwtTokenGenerate(service)
                        )
                );
            } else return ErrorResponseUtils.unauthorized(null);
        } else return ErrorResponseUtils.badRequest(null);
    }

    @PostMapping("/update")
    public Map update(@RequestParam Map<String,String> body){
        String type = body.get("type");
        if(type.equals("user")) {
            String name = body.get("name");
            String pic = body.get("pic");
            String password = body.get("password");
            String email = body.get("email");
            String details = body.get("details");
            String age = body.get("age");
            String phone = body.get("phone");
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .pic(pic)
                    .details(details)
                    .age(Objects.isNull(age)?-1:Integer.parseInt(age))
                    .phone(phone)
                    .build();
            if(userService.update(user))
                return Map.of("success",true);
            return ErrorResponseUtils.unauthorized(null);
        } else if (type.equals("service")) {
            String name = body.get("name");
            String pic = body.get("pic");
            String password = body.get("password");
            String email = body.get("email");
            String details = body.get("details");
            Service service = Service.builder()
                    .name(name)
                    .password(password)
                    .pic(pic)
                    .email(email)
                    .details(details)
                    .build();
            if(orgService.update(service))
                return Map.of("success",true);
            return ErrorResponseUtils.unauthorized(null);
        } else return ErrorResponseUtils.badRequest(null);
    }

    @PostMapping("/permission")
    public Map givePermission(@RequestParam Map<String,String> body){
        String userId = body.get("userId");
        String serviceId = body.get("serviceId");
        if(AuthUtils.anyNull(userId,serviceId))
            return ErrorResponseUtils.badRequest(null);
        if(userService.givePermission(
                User.builder().userId(userId)
                        .build(), Service.builder().serviceId(serviceId)
                        .build()
        )) return Map.of("success",true);
        return ErrorResponseUtils.internalServerError(null);
    }

    @DeleteMapping("/permission")
    public Map deletePermission(@RequestParam Map<String,String> body){
        String userId = body.get("userId");
        String serviceId = body.get("serviceId");
        if(AuthUtils.anyNull(userId,serviceId))
            return ErrorResponseUtils.badRequest(null);
        if(userService.deletePermission(
                User.builder().userId(userId)
                        .build(), Service.builder().serviceId(serviceId)
                        .build()
        )) return Map.of("success",true);
        return ErrorResponseUtils.internalServerError(null);
    }

    @PostMapping("/delete")
    public Map delete(@RequestParam Map<String,String> body){
        String type = body.get("type");
        if(type.equals("user")) {
            String email = body.get("email");
            String password= body.get("password");
            if(AuthUtils.anyNull(email,password))
                return ErrorResponseUtils.badRequest(null);
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .build();
            if(userService.delete(user))
                return Map.of("success",true);
            return ErrorResponseUtils.internalServerError(null);
        }  else if (type.equals("service")) {
            String email = body.get("email");
            String password= body.get("password");
            if(AuthUtils.anyNull(email,password))
                return ErrorResponseUtils.badRequest(null);
            Service service = Service.builder()
                    .email(email)
                    .password(password)
                    .build();
            if(orgService.delete(service))
                return Map.of("success",true);
            return ErrorResponseUtils.internalServerError(null);
        } else return ErrorResponseUtils.badRequest(null);
    }

    @PostMapping("/details")
    public Map getDetails(@RequestParam Map<String,String> body){
        String userId = body.get("userId");
        String serviceId = body.get("serviceId");
        if(AuthUtils.anyNull(userId,serviceId))
            return ErrorResponseUtils.badRequest(null);
        User user = orgService.getDetails(serviceId,userId);
        if(Objects.nonNull(user)){
            return Map.of(
                    "success",true,
                    "body",Map.of(
                            "name",user.getName(),
                            "age",user.getAge(),
                            "pic",user.getPic(),
                            "email",user.getEmail(),
                            "details",user.getDetails(),
                            "phone",user.getPhone(),
                            "gender",user.getGender().name(),
                            "userId",user.getUserId()
                    )
            );
        }
        return ErrorResponseUtils.forbidden(null);
    }

    @PostMapping("/checkJwt")
    public Map checkJwt(@RequestParam Map<String,String> body){
        String jwt = body.get("jwt");
        if(Objects.isNull(jwt)) return ErrorResponseUtils.badRequest(null);
        return Map.of("success",true,"isValid",AuthUtils.checkJwtToken(jwt));
    }
}
