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

package com.ironsg.ironj.services;

import com.ironsg.ironj.entity.Connector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Iron Software Group
 */
@Stateless
public class ConnectorFacade extends AbstractFacade<Connector> {
    @PersistenceContext(unitName = "com.ironsg_IronJ_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConnectorFacade() {
        super(Connector.class);
    }
    public Connector findById(int id){
        Query q = em.createNamedQuery("Connector.findById");
        q.setParameter("id", id);
        return (Connector)q.getSingleResult();
    }
    

}
