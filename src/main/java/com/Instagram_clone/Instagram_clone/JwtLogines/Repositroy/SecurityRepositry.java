package com.Instagram_clone.Instagram_clone.JwtLogines.Repositroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.LoginEntity;

@Repository
public interface SecurityRepositry extends JpaRepository<LoginEntity,Integer>{
	
	Optional<LoginEntity> findByUsername(String username);
	
	@Query(value="SELECT *FROM login_entity WHERE username= :userna",nativeQuery=true)
	LoginEntity getByusername(@Param("userna") String username);

	
}