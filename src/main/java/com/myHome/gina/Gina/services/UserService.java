package com.myHome.gina.Gina.services;

import com.myHome.gina.Gina.models.Service;
import com.myHome.gina.Gina.models.User;

public interface UserService {
    boolean create(User user);
    boolean delete(User user);
    boolean populate(User user);
    boolean update(User user);
    boolean givePermission(User user, Service service);
    boolean deletePermission(User user, Service service);
}
