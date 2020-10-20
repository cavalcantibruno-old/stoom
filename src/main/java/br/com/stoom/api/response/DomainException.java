package br.com.stoom.api.response;

public class DomainException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DomainException(String message) {
		super(message);
	}
}
