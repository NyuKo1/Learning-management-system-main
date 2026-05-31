package kz.sec.lms.faculty.repository;

import kz.sec.lms.faculty.model.Address;
import kz.sec.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {
    @Override
    @Query(
            "select x from #{#entityName} x "
                    + "where x.deleted = false "
                    + "and (cast(x.id as string) like :search "
                    + "or x.street like :search or cast(x.number as string) like :search)")
    Page<Address> findContaining(Pageable pageable, String search);
}
