package com.Instagram_clone.Instagram_clone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Instagram_clone.Instagram_clone.Entity.Post;

@Repository
public interface InstRepository extends JpaRepository<Post,Integer> {

}
