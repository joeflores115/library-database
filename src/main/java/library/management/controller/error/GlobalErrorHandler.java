package library.management.controller.error;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler 
{
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) 
	{
		log.error("Error {}", ex.getMessage());
		return Map.of("error", ex.getMessage());
	}
	
	
	public static class NoCopiesAvailableException extends RuntimeException {
	    public NoCopiesAvailableException(String message) {
	        super(message);
	    }
	}
	@ExceptionHandler(NoCopiesAvailableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleNoCopiesAvailable(NoCopiesAvailableException ex) 
	{
        log.error("Error {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }
	    
	
}

