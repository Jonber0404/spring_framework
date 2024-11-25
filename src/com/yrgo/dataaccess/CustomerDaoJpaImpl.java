package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerDaoJpaImpl implements CustomerDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try{
            return(Customer)em.createQuery("select customer from Customer as customer where customer.customerId=:customerId").setParameter("customerId", customerId).getSingleResult();
        }catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return em.createQuery("select customer from Customer as customer where customer.companyName=:name", Customer.class).setParameter("name", name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        try {
            customerToUpdate = em.find(Customer.class, customerToUpdate.getCustomerId());

            customerToUpdate.setCompanyName(customerToUpdate.getCompanyName());
            customerToUpdate.setEmail(customerToUpdate.getEmail());
            customerToUpdate.setTelephone(customerToUpdate.getTelephone());
            customerToUpdate.setNotes(customerToUpdate.getNotes());

            em.merge(customerToUpdate);
        }
        catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        try {
            Customer customer = em.find(Customer.class, oldCustomer.getCustomerId());
            em.remove(customer);
        }
        catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("select customer from Customer as customer" , Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            return em.createQuery(
                            "select customer from Customer as customer left join fetch customer.calls where customer.customerId = :customerId",
                            Customer.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        try {
        Customer customer = em.find(Customer.class, customerId);

        customer.addCall(newCall);

        em.persist(newCall);
        }
        catch (javax.persistence.NoResultException e) {
            throw new RecordNotFoundException();
        }
    }
}
