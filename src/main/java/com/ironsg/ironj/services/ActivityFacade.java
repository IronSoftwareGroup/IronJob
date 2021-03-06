/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ironsg.ironj.services;

import com.ironsg.ironj.entity.Activity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Iron Software Group
 */
@Stateless
public class ActivityFacade extends AbstractFacade<Activity> {
    @PersistenceContext(unitName = "com.ironsg_IronJ_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityFacade() {
        super(Activity.class);
    }

}
