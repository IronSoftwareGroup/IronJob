/*
 * Copyright (C) 2014 bruno
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ironsg.ironj.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bruno Condemi
 */
@Entity
@Table(name = "connector")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Connector.findAll", query = "SELECT c FROM Connector c"),
    @NamedQuery(name = "Connector.findById", query = "SELECT c FROM Connector c WHERE c.id = :id"),
    @NamedQuery(name = "Connector.findByName", query = "SELECT c FROM Connector c WHERE c.name = :name"),
    @NamedQuery(name = "Connector.findByDescription", query = "SELECT c FROM Connector c WHERE c.description = :description"),
    @NamedQuery(name = "Connector.findByUsername", query = "SELECT c FROM Connector c WHERE c.username = :username"),
    @NamedQuery(name = "Connector.findByPassword", query = "SELECT c FROM Connector c WHERE c.password = :password"),
    @NamedQuery(name = "Connector.findByDriver", query = "SELECT c FROM Connector c WHERE c.driver = :driver"),
    @NamedQuery(name = "Connector.findByUrl", query = "SELECT c FROM Connector c WHERE c.url = :url")})
public class Connector implements Serializable {
    @Size(max = 10)
    @Column(name = "type")
    private String type;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 256)
    @Column(name = "description")
    private String description;
    @Size(max = 45)
    @Column(name = "username")
    private String username;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @Size(max = 256)
    @Column(name = "driver")
    private String driver;
    @Size(max = 512)
    @Column(name = "url")
    private String url;

    public Connector() {
    }

    public Connector(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Connector)) {
            return false;
        }
        Connector other = (Connector) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gn.ironj.entity.Connector[ id=" + id + " ]";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
