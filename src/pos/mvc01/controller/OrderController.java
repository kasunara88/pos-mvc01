/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pos.mvc01.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import pos.mvc01.db.DBConnection;
import pos.mvc01.model.OrderDetailModel;
import pos.mvc01.model.OrderModel;

/**
 *
 * @author wmara
 */
public class OrderController {

    public String placeOrder(OrderModel orderModel, ArrayList<OrderDetailModel> orderDetailModels) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO Orders VALUES(?,?,?)";

            PreparedStatement statmentForOrder = connection.prepareStatement(query);
            statmentForOrder.setString(1, orderModel.getOrderID());
            statmentForOrder.setString(2, orderModel.getOrderDate());
            statmentForOrder.setString(3, orderModel.getCustID());

            if (statmentForOrder.executeUpdate() > 0) {

                boolean isOrderDetailSaved = true;
                String orderDetailQuery = "INSERT INTO orderdetail VALUES (?, ?, ?, ?)";

                for (OrderDetailModel orderDetailModel : orderDetailModels) {
                    PreparedStatement statmentForOrderDetail = connection.prepareStatement(orderDetailQuery);
                    statmentForOrderDetail.setString(1, orderModel.getOrderID());
                    statmentForOrderDetail.setString(3, orderDetailModel.getItemCode());
                    statmentForOrderDetail.setInt(3, orderDetailModel.getOrderQty());
                    statmentForOrderDetail.setDouble(4, orderDetailModel.getDiscount());

                    if (!(statmentForOrder.executeUpdate() > 0)) {
                        isOrderDetailSaved = false;
                    }
                }

                if (isOrderDetailSaved) {
                    boolean isItemUpdated = true;
                    String itemQuery = "UPDATE Item SET  QtyOnHand = QtyOnHand - ? WHERE ItemCode =? ";
                    for (OrderDetailModel orderDetailModel : orderDetailModels) {
                        PreparedStatement statementForItem = connection.prepareStatement(itemQuery);
                        statementForItem.setInt(1, orderDetailModel.getOrderQty());
                        statementForItem.setString(2, orderDetailModel.getItemCode());
                        if (statementForItem.executeUpdate() > 0) {

                        }
                    }

                    if (isItemUpdated) {
                        connection.commit();
                        return "Success";
                    } else {
                        connection.rollback();
                        return "Item Update Error";
                    }
                } else {
                    connection.rollback();
                    return "Order Detail Save Error";
                }

            } else {
                connection.rollback();
                return "Order Save Error";
            }

        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            return e.getMessage();
        } finally {
            connection.setAutoCommit(true);
        }

    }
}
