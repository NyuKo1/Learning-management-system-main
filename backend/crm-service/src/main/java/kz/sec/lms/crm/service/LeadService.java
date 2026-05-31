package kz.sec.lms.crm.service;

import kz.sec.lms.crm.dto.LeadDTO;
import kz.sec.lms.crm.mapper.LeadMapper;
import kz.sec.lms.crm.model.Lead;
import kz.sec.lms.crm.repository.LeadRepository;
import kz.sec.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadService extends ExtendedService<Lead, LeadDTO, Long> {

    private final LeadRepository repository;
    private final LeadMapper mapper;

    public LeadService(LeadRepository repository, LeadMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected List<LeadDTO> mapMissingValues(List<LeadDTO> leads) {
        return leads;
    }

    public List<LeadDTO> findAll() {
        return mapper.toDTO(repository.findByDeletedFalseOrderByCreatedAtDesc());
    }

    public long countByStatus(String status) {
        return repository.countByStatusAndDeletedFalse(status);
    }

    public LeadDTO updateStatus(Long id, String status) {
        return repository.findById(id).map(lead -> {
            lead.setStatus(status);
            return mapper.toDTO(repository.save(lead));
        }).orElse(null);
    }
}
