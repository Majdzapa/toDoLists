package com.todolists.exceptions;

import java.time.LocalDate;

public class ErrorObject {
	String message;
	Integer statusCode;
	LocalDate timestemp;
	public ErrorObject(String message, Integer statusCode, LocalDate timestemp) {
		super();
		this.message = message;
		this.statusCode = statusCode;
		this.timestemp = timestemp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorObject() {

	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public LocalDate getTimestemp() {
		return timestemp;
	}
	public void setTimestemp(LocalDate timestemp) {
		this.timestemp = timestemp;
	}

}
