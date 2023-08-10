/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.mvc01.controller;

import pos.mvc01.model.CustomerModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import pos.mvc01.db.DBConnection;


/**
 *
 * @author wmara
 */
public class CustomerController {
    public String saveCustomer(CustomerModel customer)throws SQLException{
      Connection connection =  DBConnection.getInstance().getConnection();
         
      String query = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?,?)";
      PreparedStatement preparedStatment = connection.prepareStatement(query);
      preparedStatment.setString(1, customer.getCustID() );
      preparedStatment.setString(2, customer.getTitle() );
      preparedStatment.setString(3, customer.getName());
      preparedStatment.setString(4, customer.getDob() );
      preparedStatment.setDouble(5, customer.getSalary());
      preparedStatment.setString(6, customer.getAddress() );
      preparedStatment.setString(7, customer.getCity());
      preparedStatment.setString(8, customer.getProvince() );
      preparedStatment.setString(9, customer.getZip());
      
      if(preparedStatment.executeUpdate()>0){
          return "Success";
      } else{
          return "Fail";
      } 
      
    }   
    public ArrayList<CustomerModel> getAllCustomes() throws SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        
        String query = "SELECT * FROM Customer";
        
        PreparedStatement statement = connection.prepareStatement(query);
        
        ResultSet rst = statement.executeQuery();
        
        ArrayList <CustomerModel> customerModels = new ArrayList<>();
        
        while(rst.next()){
            CustomerModel cm = new CustomerModel(rst.getString(1), 
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9));
            customerModels.add(cm);
        }
        return customerModels;  
    }
}

