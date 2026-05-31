package kz.sec.lms.auth.mapper;

import kz.sec.lms.auth.model.User;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.dto.UserDetailsDTO;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDetailsDTO, Long> {
    UserDTO userToUserDTO(User user);

    User userDTOtoUser(UserDTO userDTO);

    List<UserDTO> userToUserDTOList(List<User> users);

    List<User> userDTOtoUserList(List<UserDTO> userDTOList);
}
