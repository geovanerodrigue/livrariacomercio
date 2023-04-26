package livrariacomercio.web.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import livrariacomercio.web.model.ItensCompra;

@Repository
@Transactional
public interface ItensCompraRepository extends CrudRepository<ItensCompra, Long> {

	/*
	@Query("from ItensCompra where compra=?1")
	public List<ItensCompra> buscarItensCompraCompra(Compra compra);
	*/
	/*
	@Query("select u from ItensCompra u where u.compra = ?1")
	public List<ItensCompra> buscarItensCompraCompra(Compra compra);
	*/

	 List<ItensCompra> findByCompraId(Long compraId);


}
