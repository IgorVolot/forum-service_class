package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dto.RolesDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.dto.exceptions.UserAlreadyExistsException;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto) throws UserAlreadyExistsException;

    UserDto getUser(String login);

    UserDto removeUser(String login);

    UserDto updateUser(String login, UserEditDto userEditDto);

    RolesDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword(String login, String newPassword);
}
