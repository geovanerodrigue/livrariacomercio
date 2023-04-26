package livrariacomercio.web.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import livrariacomercio.web.model.Compra;

@Repository
@Transactional
public interface CompraRepository extends CrudRepository<Compra, Long> {
	/*
	@Query("select u from Compra u where u.compra = ?1")
	public List<Compra> buscarItensCompra(Compra compra);
	*/

	 List<Compra> findByUsuarioId(Long usuarioId);

}
