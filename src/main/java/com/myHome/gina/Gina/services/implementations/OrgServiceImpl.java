package com.myHome.gina.Gina.services.implementations;

import com.myHome.gina.Gina.models.User;
import com.myHome.gina.Gina.repositories.ServiceRepository;
import com.myHome.gina.Gina.repositories.ServiceUserMappingRepository;
import com.myHome.gina.Gina.repositories.UserRepository;
import com.myHome.gina.Gina.services.OrgService;
import com.myHome.gina.Gina.utils.AuthUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrgServiceImpl implements OrgService {

    private final ServiceRepository serviceRepository;

    private final UserRepository userRepository;

    private final ServiceUserMappingRepository serviceUserMappingRepository;

    @Autowired
    public OrgServiceImpl(ServiceRepository serviceRepository, UserRepository userRepository, ServiceUserMappingRepository serviceUserMappingRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.serviceUserMappingRepository = serviceUserMappingRepository;
    }

    @Override
    public boolean create(com.myHome.gina.Gina.models.Service service) {
        try{
            service.setPassword(AuthUtils.hashPassword(service.getPassword()));
            serviceRepository.save(service);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(com.myHome.gina.Gina.models.Service service) {
        try{
            if(populate(service)) {
                AuthUtils.deleteFile(service.getPic());
                serviceUserMappingRepository.deleteAllByServiceId(service.getServiceId());
                serviceRepository.delete(service);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean populate(@NonNull com.myHome.gina.Gina.models.Service service) {
        try {
            com.myHome.gina.Gina.models.Service actual = null;
            if (Objects.nonNull(service.getServiceId()))
                actual = serviceRepository.findById(service.getServiceId()).orElse(null);
            else if (Objects.nonNull(service.getEmail()))
                actual = serviceRepository.findByEmail(service.getEmail()).orElse(null);
            if (Objects.nonNull(actual)) {
                service.setName(actual.getName());
                service.setDetails(actual.getDetails());
                service.setServiceId(actual.getServiceId());
                service.setPic(actual.getPic());
                service.setPassword(actual.getPassword());
                service.setEmail(actual.getEmail());
                service.setCreation(actual.getCreation());
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(com.myHome.gina.Gina.models.Service service) {
        try {
            var actualService = serviceRepository.findByEmail(service.getEmail()).orElse(null);
            if (Objects.isNull(actualService)) return false;
            if (AuthUtils.checkPassword(service.getPassword(), actualService.getPassword())) {
                if (Objects.nonNull(service.getDetails())) actualService.setDetails(service.getDetails());
                if (Objects.nonNull(service.getPic())) actualService.setPic(service.getPic());
                if (Objects.nonNull(service.getName())) actualService.setName(service.getName());
                serviceRepository.save(actualService);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getDetails(String serviceId, String userId) {
        try {
            if (serviceUserMappingRepository.existsByUserIdAndServiceId(userId, serviceId))
                return userRepository.findById(userId).orElse(null);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
