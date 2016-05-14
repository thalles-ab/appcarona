package br.uvv.carona.model;

import java.io.Serializable;
import java.util.List;

public class BaseObject implements Serializable {
    public long id;
    public Long version;

    public List<Error> erros;
}

