package konta.projectmd4.service;

import konta.projectmd4.dto.req.FormLogin;
import konta.projectmd4.dto.req.FormRegister;
import konta.projectmd4.dto.resp.JwtResponse;
import konta.projectmd4.exception.CustomException;

public interface IAuthService {
    void register(FormRegister formRegister) throws CustomException;

    JwtResponse login(FormLogin formLogin) throws CustomException;
}
