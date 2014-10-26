/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ironsg.ironj.services;

import com.ironsg.ironj.entity.Params;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Iron Software Group
 */
@Stateless
public class ParamsFacade extends AbstractFacade<Params> {
    @PersistenceContext(unitName = "com.ironsg_IronJ_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParamsFacade() {
        super(Params.class);
    }
    
    public List<Params> findByActivity(int activityId){
        Query q = em.createNamedQuery("Params.findByActivityId");
        q.setParameter("activityId", activityId);
        return q.getResultList();
    }

}
