package com.myHome.gina.Gina.services;

import com.myHome.gina.Gina.models.Service;
import com.myHome.gina.Gina.models.User;

public interface OrgService {
    boolean create(Service service);
    boolean delete(Service service);
    boolean populate(Service service);
    boolean update(Service service);
    User getDetails(String serviceId, String userId);
}
