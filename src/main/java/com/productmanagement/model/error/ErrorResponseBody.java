package com.productmanagement.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ErrorResponseBody implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("Errors")
  private Errors errors;

  public ErrorResponseBody errors(Errors errors) {
    this.errors = errors;
    return this;
  }



}

