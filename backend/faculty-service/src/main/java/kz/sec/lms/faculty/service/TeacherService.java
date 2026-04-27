package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.client.UserFeignClient;
import kz.sec.lms.faculty.dto.TeacherDTO;
import kz.sec.lms.faculty.mapper.TeacherMapper;
import kz.sec.lms.faculty.model.Teacher;
import kz.sec.lms.faculty.repository.TeacherRepository;
import ca.utoronto.lms.shared.dto.RoleDTO;
import ca.utoronto.lms.shared.dto.UserDTO;
import ca.utoronto.lms.shared.dto.UserDetailsDTO;
import ca.utoronto.lms.shared.exception.NotFoundException;
import ca.utoronto.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.utoronto.lms.shared.security.SecurityUtils.ROLE_TEACHER;
import static ca.utoronto.lms.shared.security.SecurityUtils.ROLE_TEACHER_ID;

@Service
public class TeacherService extends ExtendedService<Teacher, TeacherDTO, Long> {
    private final TeacherRepository repository;
    private final TeacherMapper mapper;
    private final UserFeignClient userFeignClient;

    public TeacherService(
            TeacherRepository repository, TeacherMapper mapper, UserFeignClient userFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.userFeignClient = userFeignClient;
    }

    @Override
    @Transactional
    public TeacherDTO save(TeacherDTO teacher) {
        UserDTO userRequest = teacher.getUser();
        UserDTO userResponse =
                userRequest.getId() == null
                        ? userFeignClient.createUser(
                                UserDetailsDTO.builder()
                                        .username(userRequest.getUsername())
                                        .password(userRequest.getPassword())
                                        .authorities(
                                                Set.of(
                                                        RoleDTO.builder()
                                                                .id(ROLE_TEACHER_ID)
                                                                .authority(ROLE_TEACHER)
                                                                .build()))
                                        .build())
                        : userFeignClient.patchUser(userRequest.getId(), userRequest);
        teacher.setUser(userResponse);
        return super.save(teacher);
    }

    @Override
    @Transactional
    public void delete(Set<Long> id) {
        List<Teacher> teachers = (List<Teacher>) repository.findAllById(id);
        Set<Long> userIds = teachers.stream().map(Teacher::getUserId).collect(Collectors.toSet());
        userFeignClient.deleteUser(userIds);
        repository.softDeleteByIds(id);
    }

    @Override
    protected List<TeacherDTO> mapMissingValues(List<TeacherDTO> teachers) {
        map(teachers, TeacherDTO::getUser, TeacherDTO::setUser, userFeignClient::getUser);
        return teachers;
    }

    public TeacherDTO findByUserId(Long userId) {
        Teacher teacher =
                repository
                        .findByUserId(userId)
                        .orElseThrow(() -> new NotFoundException("User id not found"));
        return mapper.toDTO(teacher);
    }
}
