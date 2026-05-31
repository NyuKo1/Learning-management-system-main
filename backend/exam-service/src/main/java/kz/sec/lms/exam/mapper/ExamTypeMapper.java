package kz.sec.lms.exam.mapper;

import kz.sec.lms.exam.dto.ExamTypeDTO;
import kz.sec.lms.exam.model.ExamType;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamTypeMapper extends BaseMapper<ExamType, ExamTypeDTO, Long> {}
