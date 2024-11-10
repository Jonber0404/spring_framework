package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao{
    private static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
    private static final String UPDATE_SQL = "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";
    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)";
    private static final String GET_ALL_CUSTOMERS_SQL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER";
    private static final String GET_CUSTOMER_BY_NAME_SQL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE CUSTOMER_NAME = ?";
    private static final String GET_CUSTOMER_BY_ID_SQL = "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE CUSTOMER_ID = ?";

    private static final String INSERT_CALL_SQL = "INSERT INTO CALL_TABLE (CUSTOMER_ID, NOTES, TIME_AND_DATE) VALUES (?,?,?)";
    private static final String GET_CALL_SQL = "SELECT NOTES, TIME_AND_DATE FROM CALL_TABLE WHERE CALL_ID = ?";


    private JdbcTemplate template;

    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    private void createTables() {
        try {
            this.template.update("CREATE TABLE CUSTOMER (CUSTOMER_ID VARCHAR(50) PRIMARY KEY, COMPANY_NAME VARCHAR(100), EMAIL VARCHAR(100), TELEPHONE VARCHAR(20), NOTES VARCHAR(255))");
            this.template.update("CREATE TABLE CALL_TABLE (CALL_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1), CUSTOMER_ID VARCHAR(50), NOTES VARCHAR(255), TIME_AND_DATE DATE, FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID))");
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("Assuming tables exists");
        }
    }

    @Override
    public void create(Customer customer) {
        template.update(INSERT_CUSTOMER_SQL,customer.getCustomerId(),customer.getCompanyName(), customer.getEmail(),customer.getTelephone(), customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return template.queryForObject(GET_CUSTOMER_BY_ID_SQL, new Object[]{customerId}, new CustomerRowMapper());
    }

    @Override
    public List<Customer> getByName(String name) {
        return template.query(GET_CUSTOMER_BY_NAME_SQL, new Object[]{name}, new CustomerRowMapper());
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        template.update(UPDATE_SQL, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(),customerToUpdate.getTelephone(), customerToUpdate.getNotes());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        template.update(DELETE_SQL, oldCustomer.getCompanyName(), oldCustomer.getEmail(),oldCustomer.getTelephone(), oldCustomer.getNotes());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return template.query(GET_ALL_CUSTOMERS_SQL, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = getById(customerId);

        if (customer == null) {
            throw new RecordNotFoundException();
        }

        List<Call> calls = template.query(GET_CALL_SQL, new Object[]{customerId}, new CallRowMapper());

        customer.setCalls(calls);
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = getById(customerId);

        if (customer == null) {
            throw new RecordNotFoundException();
        }

        template.update(INSERT_CALL_SQL, customerId, newCall.getNotes(), newCall.getTimeAndDate());

        customer.addCall(newCall);
    }
}

class CustomerRowMapper implements RowMapper<Customer> {
    public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
        String customerId = rs.getString(1);
        String companyName = rs.getString(2);
        String email = rs.getString(3);
        String telephone = rs.getString(4);
        String notes = rs.getString(5);

        return new Customer("" + customerId, companyName, email, telephone, notes);
    }
}
class CallRowMapper implements RowMapper<Call> {
    public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
        String notes = rs.getString(1);
        Date timeAndDate = rs.getDate(2);

        return new Call(notes, timeAndDate);
    }
}