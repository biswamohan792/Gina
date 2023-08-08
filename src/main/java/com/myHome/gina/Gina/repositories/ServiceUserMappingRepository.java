package com.myHome.gina.Gina.repositories;

import com.myHome.gina.Gina.models.ServiceUserMapping;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.repository.CrudRepository;

@Data
class ServiceUserMappingCompositeId {
    String userId;
    String serviceId;
}

public interface ServiceUserMappingRepository extends CrudRepository<ServiceUserMapping,ServiceUserMappingCompositeId> {
    boolean existsByUserIdAndServiceId(String userId, String serviceId);
    void deleteAllByUserId(String userId);
    void deleteAllByServiceId(String serviceId);
}
