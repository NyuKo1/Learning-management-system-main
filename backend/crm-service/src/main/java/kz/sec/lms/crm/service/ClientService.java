package kz.sec.lms.crm.service;

import kz.sec.lms.shared.exception.NotFoundException;
import kz.sec.lms.shared.service.ExtendedService;
import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.mapper.ClientMapper;
import kz.sec.lms.crm.model.Client;
import kz.sec.lms.crm.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService extends ExtendedService<Client, ClientDTO, Long> {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientService(ClientRepository repository, ClientMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected List<ClientDTO> mapMissingValues(List<ClientDTO> clients) {
        return clients;
    }

    public List<ClientDTO> findAll() {
        return mapper.toDTO(repository.findByDeletedFalseOrderByCreatedAtDesc());
    }

    @Transactional
    public ClientDTO linkLmsAccount(Long clientId, Long lmsUserId) {
        Client client = repository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + clientId));
        client.setHasLmsAccount(true);
        if (lmsUserId != null) {
            client.setLmsUserId(lmsUserId);
        }
        return mapper.toDTO(repository.save(client));
    }
}
