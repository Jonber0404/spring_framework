package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ActionDaoJpaImpl implements ActionDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
        return em.createQuery("select action from Action as action where action.OWNING_USER=:userId",
                Action.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        try {
            actionToUpdate = em.find(Action.class, actionToUpdate.getActionId());

            actionToUpdate.setDetails(actionToUpdate.getDetails());
            actionToUpdate.setComplete(actionToUpdate.isComplete());
            actionToUpdate.setOwningUser(actionToUpdate.getOwningUser());
            actionToUpdate.setRequiredBy(actionToUpdate.getRequiredBy());

            em.merge(actionToUpdate);
        }
        catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        try {
            Action action = em.find(Action.class, oldAction.getActionId());
            em.remove(action);
        }
        catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }
}
