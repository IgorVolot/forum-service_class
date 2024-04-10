package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.UserRepository;
import ait.cohort34.accounting.dto.RolesDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.accounting.service.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    final UserRepository userRepository;
    final ModelMapper modelMapper;

    private UserAccount findUserAccountOrThrow(String login) {
        return userRepository.findById(login).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) throws UserAlreadyExistsException {
        if (!userRepository.existsById(userRegisterDto.getLogin())){
            UserDto userDto = modelMapper.map(userRegisterDto, UserDto.class);
             UserAccount userAccount = modelMapper.map(userDto, UserAccount.class);
             userRepository.save(userAccount);
             return userDto;
        }
        throw new UserAlreadyExistsException(userRegisterDto.getLogin());
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = findUserAccountOrThrow(login);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount = findUserAccountOrThrow(login);
        userRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = findUserAccountOrThrow(login);
        if(userEditDto.getFirstName()!=null) {
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if(userEditDto.getLastName()!=null) {
            userAccount.setLastName(userEditDto.getLastName());
        }
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = findUserAccountOrThrow(login);
        if (isAddRole) {
            userAccount.addRole(role);
        } else {
            userAccount.removeRole(role);
        }
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = findUserAccountOrThrow(login);
        userAccount.setPassword(newPassword);
        userRepository.save(userAccount);
    }
}
