/*
 * Copyright (C) 2014 Bruno Condemi
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

import com.gn.ironj.entity.Activity;
import com.gn.ironj.entity.Connector;
import com.gn.ironj.entity.Params;
import com.gn.ironj.services.ConnectorFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class JdbcProcessor implements IProcessor {

    @EJB
    ConnectorFacade ejbConnector;

    public JdbcProcessor() {
    }

    public void process(Activity activity, List<Params> params) throws ProcessorExecption{
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "JdbcProcessor called for activity {0}", activity.getName());
        String sql = readSqlFile(activity.getPath());
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Sql file: {0}", activity.getPath());
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Sql string: {0}", sql);
        executeSql(activity.getConnectorId(), sql, params);
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Sql executetd");
    }

    private String readSqlFile(String path) throws ProcessorExecption{
        BufferedReader br = null;
        StringBuilder sql = new StringBuilder(256);
        String line;
         Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Read file: {0}", path);
        try {
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                sql.append(line);
                Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Found line {0}", line);
            }
        } catch (IOException e) {
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.SEVERE, "Error on reading file: {0}", path);
          
            throw new ProcessorExecption();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(JdbcProcessor.class.getName()).log(Level.SEVERE, "Error on closing file: {0}", path);
                throw new ProcessorExecption();
                
            }
        }
        return sql.toString();
    }

    private void executeSql(int cntId, String sql, List<Params> params) {
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Rady for sql execution");
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;

        Connector connector = ejbConnector.findById(cntId);
        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Using connector id: {0}", cntId);
        try {
            
            Class.forName(connector.getDriver());
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Class loaded: {0}", connector.getDriver());
            
            connect = DriverManager.getConnection(connector.getUrl(), connector.getUsername(), connector.getPassword());
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Connection acquired");
            
            preparedStatement = connect.prepareStatement(sql);
            
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Building the parameters list");
            int i = 1;
            for (Iterator<Params> it = params.iterator(); it.hasNext();) {
                Params params1 = it.next();
                String pType = params1.getType();
                Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Parameters "+params1.getParamsPK().getName()+" of type "+params1.getType());
                if (pType.equalsIgnoreCase("STRING")) {
                    preparedStatement.setString(i, params1.getValue());
                } else if (pType.equalsIgnoreCase("NUMBER")) {
                    preparedStatement.setBigDecimal(i, new BigDecimal(params1.getValue()));
                } else if (pType.equalsIgnoreCase("DATE")) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date date;
                    try {
                        date = df.parse(params1.getValue());
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        preparedStatement.setDate(i, sqlDate);
                    } catch (ParseException ex) {
                        Logger.getLogger(JdbcProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.FINE, "Execute the sql statment");
            preparedStatement.execute();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JdbcProcessor.class.getName()).log(Level.SEVERE, null, ex);          
        } finally {
            //Close not null
        }
    }

}
