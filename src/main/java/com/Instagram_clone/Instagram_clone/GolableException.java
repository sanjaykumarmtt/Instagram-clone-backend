package com.Instagram_clone.Instagram_clone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Instagram_clone.Instagram_clone.ExceptionHan.HostelException;


@ControllerAdvice
public class GolableException {
	
	@ExceptionHandler(HostelException.class)
	public ResponseEntity<String> banknotfound(HostelException bankexc){
		return new ResponseEntity<>(bankexc.getMessage(),HttpStatus.NOT_FOUND);	
		 
	}

}
