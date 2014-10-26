/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ironsg.ironj.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Iron Software Group
 */
@Entity
@Table(name = "params")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Params.findAll", query = "SELECT p FROM Params p"),
    @NamedQuery(name = "Params.findByActivityId", query = "SELECT p FROM Params p WHERE p.paramsPK.activityId = :activityId"),
    @NamedQuery(name = "Params.findByName", query = "SELECT p FROM Params p WHERE p.paramsPK.name = :name"),
    @NamedQuery(name = "Params.findByDescription", query = "SELECT p FROM Params p WHERE p.description = :description"),
    @NamedQuery(name = "Params.findByType", query = "SELECT p FROM Params p WHERE p.type = :type"),
    @NamedQuery(name = "Params.findByValue", query = "SELECT p FROM Params p WHERE p.value = :value")})
public class Params implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParamsPK paramsPK;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 10)
    @Column(name = "type")
    private String type;
    @Size(max = 255)
    @Column(name = "value")
    private String value;

    public Params() {
    }

    public Params(ParamsPK paramsPK) {
        this.paramsPK = paramsPK;
    }

    public Params(int activityId, String name) {
        this.paramsPK = new ParamsPK(activityId, name);
    }

    public ParamsPK getParamsPK() {
        return paramsPK;
    }

    public void setParamsPK(ParamsPK paramsPK) {
        this.paramsPK = paramsPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }
 

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramsPK != null ? paramsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Params)) {
            return false;
        }
        Params other = (Params) object;
        if ((this.paramsPK == null && other.paramsPK != null) || (this.paramsPK != null && !this.paramsPK.equals(other.paramsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ironsg.ironj.entity.Params[ paramsPK=" + paramsPK + " ]";
    }

}
