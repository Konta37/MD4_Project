package konta.projectmd4.service.user;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.entity.Users;

public interface IUserService {
    public Users changeStatus(Long userId) throws CustomException;
}
