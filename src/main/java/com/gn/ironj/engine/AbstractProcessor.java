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

package com.gn.ironj.engine;

import com.gn.ironj.entity.Connector;
import com.gn.ironj.services.ConnectorFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Bruno Condemi
 */
@Stateless
public class AbstractProcessor {

   @EJB
   ConnectorFacade ejbConnector;

    public AbstractProcessor() {
    }
   
   
    
    public Connector retriveConnectorProperties(int connectorId){
       Connector connector = ejbConnector.findById(connectorId);
    return  connector;
    }

}
