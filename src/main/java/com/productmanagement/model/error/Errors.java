package com.productmanagement.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Errors implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Error> error = new ArrayList<>();

  public Errors error(List<Error> error) {
    this.error = error;
    return this;
  }

  public Errors addErrorItem(Error errorItem) {
    this.error.add(errorItem);
    return this;
  }

}

