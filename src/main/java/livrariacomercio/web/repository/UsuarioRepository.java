package livrariacomercio.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import livrariacomercio.web.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);

	@Query("from Usuario where login=?1")
	public List<Usuario> buscarUsuarioNome(String nome);

	@Modifying
	@Query(value = "INSERT INTO usuarios_role (usuario_id, role_id) VALUES (:idUsuario, 2)", nativeQuery = true)
	void adicionarPermissaoUsuario(@Param("idUsuario") Long idUsuario);




}
