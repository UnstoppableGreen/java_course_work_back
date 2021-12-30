package ru.rsatu.service;

import ru.rsatu.pojo.Status;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class StatusService {

    @Inject
    EntityManager em;

    @Transactional
    public void createStatus() {
        Status status = new Status();
        status.setName("Name");
        status.setDescription("Discription");
        em.persist(status);
    }

    //вставка данных
    @Transactional
    public Status insertStatus(Status status) {
        em.persist(status);
        em.flush();
        return status;
    }

    //обновление данных
    @Transactional
    public Status updateStatus(Status status) {
        em.merge(status);
        em.flush();
        return status;
    }

    //удаление данных
    @Transactional
    public void deleteStatus(Status status) {
        Status s = getStatusById(status.getId().intValue());
        em.remove(s);
        em.flush();
    }

    public List<Status> getStatuses() {
        return em.createQuery(" select s from Status s ", Status.class).getResultList();
    }

    public Status getStatusById(Integer integer) {
        Status status = em.find(Status.class, integer);
        return status;
    }

}
