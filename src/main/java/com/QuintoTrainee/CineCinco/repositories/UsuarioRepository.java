package com.QuintoTrainee.CineCinco.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuintoTrainee.CineCinco.entities.Compra;
import com.QuintoTrainee.CineCinco.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	@Query("SELECT c FROM Usuario c WHERE c.email =:mail")
    public Usuario buscarPorMail(@Param("mail") String mail);

	@Query("SELECT c FROM Usuario u, IN(u.compras) c WHERE c.fechaAprobacionPago IS NULL AND u.id LIKE :idUser ORDER BY c.alta DESC")
    public List<Compra> getCompraPendiente(@Param("idUser") String idUser);
	
	@Query("SELECT u FROM Usuario u, IN(u.compras) c WHERE c.fechaAprobacionPago IS NULL AND c.id LIKE :idCompra")
	public Usuario getUsuarioByCompra(@Param("idCompra") String idCompra);
	
}
