package ru.rsatu.service;

import ru.rsatu.pojo.Clients;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@ApplicationScoped
public class ClientService {
    @Inject
    EntityManager em;

    @Transactional
    public Clients createClient(String name, String contacts, String address, Date date) {
        Clients cl = new Clients();
        cl.setName(name);
        cl.setContacts(contacts);
        cl.setAddress(address);
        cl.setDate(date);
        return cl;
    }

    //вставка данных
    @Transactional
    public Clients insertClient(Clients cl) {
        em.merge(cl);
        em.flush();
        return cl;
    }

    //обновление данных
    @Transactional
    public Clients updateClient(Clients cl) {
        em.merge(cl);
        em.flush();
        return cl;
    }

    //удаление данных
    @Transactional
    public void deleteClient(Long clientID) {
        Clients c = this.getClientById(clientID);
        // Clients c = getClientById(clients.getclientID());
        em.remove(c);
        em.flush();
    }

    public int getCountClients() {
        Number number = (Number) em.createQuery(" select count(name) from Clients ").getResultList().get(0);
        return number.intValue();
    }

    public List<Clients> getClients() {
        Query query = em.createQuery(" select c from Clients c ");
        List<Clients> listClients = query.getResultList();
        return listClients;
    }

    public List<Clients> getClientsPage(int page) {
        Query query = em.createQuery(" select c from Clients c ");
        query.setFirstResult((page-1)*4);
        query.setMaxResults(4);

        List<Clients> listClients = query.getResultList();
        return listClients;
    }

    public Clients getClientById(Long clientID) {
        Clients cl = em.find(Clients.class, clientID);
        return cl;
    }
}
