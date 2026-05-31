package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.AdministratorDTO;
import kz.sec.lms.faculty.model.Administrator;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministratorMapper extends BaseMapper<Administrator, AdministratorDTO, Long> {
    @Mapping(source = "userId", target = "user")
    AdministratorDTO toDTO(Administrator administrator);

    @Mapping(source = "user.id", target = "userId")
    Administrator toModel(AdministratorDTO administratorDTO);

    UserDTO userDTOFromId(Long id);
}
