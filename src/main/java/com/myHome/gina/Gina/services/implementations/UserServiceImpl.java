package com.myHome.gina.Gina.services.implementations;

import com.myHome.gina.Gina.models.Service;
import com.myHome.gina.Gina.models.ServiceUserMapping;
import com.myHome.gina.Gina.models.User;
import com.myHome.gina.Gina.repositories.ServiceUserMappingRepository;
import com.myHome.gina.Gina.repositories.UserRepository;
import com.myHome.gina.Gina.services.UserService;
import com.myHome.gina.Gina.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ServiceUserMappingRepository serviceUserMappingRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ServiceUserMappingRepository serviceUserMappingRepository) {
        this.userRepository = userRepository;
        this.serviceUserMappingRepository = serviceUserMappingRepository;
    }

    @Override
    public boolean create(User user) {
        try {
            user.setPassword(AuthUtils.hashPassword(user.getPassword()));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        try {
            if(populate(user)) {
                AuthUtils.deleteFile(user.getPic());
                userRepository.delete(user);
                serviceUserMappingRepository.deleteAllByUserId(user.getUserId());
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean populate(User user) {
        try {
            User actual = null;
            if (Objects.nonNull(user.getUserId()))
                actual = userRepository.findById(user.getUserId()).orElse(null);
            else if (Objects.nonNull(user.getEmail()))
                actual = userRepository.findByEmail(user.getEmail()).orElse(null);
            if(Objects.nonNull(actual)){
                user.setPhone(actual.getPhone());
                user.setPic(actual.getPic());
                user.setUserId(actual.getUserId());
                user.setName(actual.getName());
                user.setPassword(actual.getPassword());
                user.setAge(actual.getAge());
                user.setGender(actual.getGender());
                user.setDetails(actual.getDetails());
                user.setCreation(actual.getCreation());
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            var actualUser = userRepository.findByEmail(user.getEmail()).orElse(null);
            if(Objects.isNull(actualUser)) return false;
            if(AuthUtils.checkPassword(user.getPassword(),actualUser.getPassword())){
                if(Objects.nonNull(user.getPic())) actualUser.setPic(user.getPic());
                if(Objects.nonNull(user.getName())) actualUser.setName(user.getName());
                if(Objects.nonNull(user.getAge())) actualUser.setAge(user.getAge());
                if(Objects.nonNull(user.getDetails())) actualUser.setDetails(user.getDetails());
                if(Objects.nonNull(user.getPhone())) actualUser.setPhone(user.getPhone());
                userRepository.save(actualUser);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean givePermission(User user, Service service) {
        try {
            ServiceUserMapping serviceUserMapping = new ServiceUserMapping();
            serviceUserMapping.setServiceId(service.getServiceId());
            serviceUserMapping.setUserId(user.getUserId());
            serviceUserMappingRepository.save(serviceUserMapping);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePermission(User user, Service service) {
        try {
            ServiceUserMapping serviceUserMapping = new ServiceUserMapping();
            serviceUserMapping.setServiceId(service.getServiceId());
            serviceUserMapping.setUserId(user.getUserId());
            serviceUserMappingRepository.delete(serviceUserMapping);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
