package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.dto.AddressDTO;
import kz.sec.lms.faculty.mapper.AddressMapper;
import kz.sec.lms.faculty.model.Address;
import kz.sec.lms.faculty.repository.AddressRepository;
import kz.sec.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends BaseService<Address, AddressDTO, Long> {
    private final AddressRepository repository;
    private final AddressMapper mapper;

    public AddressService(AddressRepository repository, AddressMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
