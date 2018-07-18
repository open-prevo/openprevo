package ch.prevo.pakt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.prevo.pakt.entities.TozsPtverm;
import ch.prevo.pakt.entities.TozsPtvermPK;

@Repository
public interface PartnerVermittlungRepository extends JpaRepository<TozsPtverm, TozsPtvermPK> {
	
	public List<TozsPtverm> findByIdCdmandantAndCdmeld(short idCdmandant, short cdmeld);
	
}
