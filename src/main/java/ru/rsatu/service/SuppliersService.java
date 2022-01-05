package ru.rsatu.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import ru.rsatu.pojo.Items;
import ru.rsatu.pojo.Suppliers;

@ApplicationScoped
public class SuppliersService {
    @Inject
    EntityManager em;

    @Transactional
    public void createSupplier() {
        Suppliers sup = new Suppliers();
        sup.setName("Name");
        sup.setContacts("Discription");
        em.persist(sup);
    }

    //вставка данных
    @Transactional
    public Suppliers insertSupplier(Suppliers s) {
        em.merge(s);
        em.flush();
        em.clear();
        return s;
    }

    //обновление данных
    @Transactional
    public Suppliers updateSupplier(Suppliers s) {
        em.merge(s);
        em.flush();
        em.clear();
        return s;
    }

    //удаление данных
    @Transactional
    public void deleteSupplier(Suppliers sup) {
        Suppliers s = getSupplierById(sup.getId());
        em.remove(s);
        em.flush();
    }

    public Suppliers getSupplierById(Long id) {
        Suppliers s = em.find(Suppliers.class, id);
        return s;
    }

    public List<Suppliers> getSuppliers(int page) {
        Query query = em.createQuery(" select s from Suppliers s ");
        query.setFirstResult((page-1)*10);
        query.setMaxResults(10);
        List<Suppliers> listSuppliers = query.getResultList();
        return listSuppliers;
    }
    public List<Suppliers> getAllSuppliers() {
        return em.createQuery(" select c from Suppliers c ", Suppliers.class).getResultList();
    }
    public int countSuppliers() {
        Number ordersQTY = (Number) em.createQuery(" select count(id) from Suppliers ").getResultList().get(0);
        return ordersQTY.intValue() ;
    }
}
