package com.Instagram_clone.Instagram_clone.JwtLogines.Repositroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.OTP;

@Repository
public interface Otprepository extends JpaRepository<OTP,Integer>{
	Optional<OTP> findByOtp(String otp);
}
