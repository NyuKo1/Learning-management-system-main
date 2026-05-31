package kz.sec.lms.auth.mapper;

import kz.sec.lms.auth.model.Role;
import kz.sec.lms.shared.dto.RoleDTO;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDTO, Long> {}
