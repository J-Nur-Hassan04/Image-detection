package com.indegene.vem.common;

import org.springframework.http.HttpStatus;

public class CommonApiResponse {

	private String message;
	private Integer status;
	private Object resBody;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Object getResBody() {
		return resBody;
	}

	public void setResBody(Object resBody) {
		this.resBody = resBody;
	}

	public void setSuccess() {
		message = "SUCCESS";
		status = HttpStatus.OK.value();
	}
}
