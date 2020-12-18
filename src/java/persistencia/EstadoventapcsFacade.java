/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import dominio.Estadoventapcs;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author msrik
 */
@Stateless
public class EstadoventapcsFacade extends AbstractFacade<Estadoventapcs> implements EstadoventapcsFacadeLocal {

    @PersistenceContext(unitName = "TiendaPC_Practica2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoventapcsFacade() {
        super(Estadoventapcs.class);
    }
    
}
