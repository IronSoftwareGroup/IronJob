/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gn.ironj.services;

import com.gn.ironj.entity.Config;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bruno Condemi
 */
@Stateless
public class ConfigFacade extends AbstractFacade<Config> {
    @PersistenceContext(unitName = "com.ironsg_IronJ_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigFacade() {
        super(Config.class);
    }
    public String getPropertyValue(String key){
        Query q = em.createNamedQuery("Config.findByPropertyKey");
        q.setParameter("propertyKey", key);
        String r = ((Config)q.getSingleResult()).getPropertyValue();
        return r;
    }

}
