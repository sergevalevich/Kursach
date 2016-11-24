package com.valevich.kursach.storage;

import com.valevich.kursach.model.response.catalog.CatalogItem;
import com.valevich.kursach.model.response.catalog.ProductItem;
import com.valevich.kursach.model.response.order.OrderItem;
import com.valevich.kursach.model.response.user.ClientInfo;
import com.valevich.kursach.util.ConstantsManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.valevich.kursach.storage.ShopContract.*;

public class DbHelper {

    public DbHelper() {
        initDriver();
    }

    public long insertCategory(String name) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_CATEGORY,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            int rows = statement.executeUpdate();

            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getLong(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean updateCategory(int id, String name) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.UPDATE_CATEGORY)) {

            statement.setString(1, name);
            statement.setInt(2, id);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean removeCategory(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.REMOVE_CATEGORY)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public List<CatalogItem> getCatalog() throws SQLException {
        List<CatalogItem> catalog = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CATALOG);
             ResultSet result = statement.executeQuery()) {

            CatalogItem prevCategory = new CatalogItem();
            while (result.next()) {
                int categoryId = result.getInt(ShopContract.CATEGORY_TABLE_NAME + "." + ShopContract.CATEGORY_ID_COLUMN);
                if (categoryId != prevCategory.getId()) {
                    CatalogItem nextCategory = new CatalogItem();
                    nextCategory.setId(categoryId);
                    nextCategory.setName(result.getString(ShopContract.CATEGORY_NAME_COLUMN));
                    if (result.getInt(ShopContract.PRODUCT_CATEGORY_ID_COLUMN) != 0)
                        nextCategory.addProduct(createProduct(result));

                    catalog.add(nextCategory);
                    prevCategory = nextCategory;
                } else {
                    prevCategory.addProduct(createProduct(result));
                }
            }
        }
        return catalog;
    }

    public String insertClient(String email, String password) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_CLIENT)) {

            String token = UUID.randomUUID().toString();
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, token);

            statement.executeUpdate();
            return token;
        }
    }

    public long insertClient(String name, String surname,
                             String phone, String address,
                             String email, String password) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_CLIENT_ADMIN,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, phone);
            statement.setString(4, address);
            statement.setString(5, email);
            statement.setString(6, password);
            statement.setString(7, UUID.randomUUID().toString());

            int rows = statement.executeUpdate();

            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getLong(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean updateClient(String name, String surname,
                                String phone, String address,
                                String email, String password,
                                int clientId, String clientToken) throws SQLException {
        boolean isAdmin = clientToken == null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(isAdmin
                     ? ShopContract.UPDATE_CLIENT_ADMIN
                     : ShopContract.UPDATE_CLIENT_CLIENT)) {

            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, phone);
            statement.setString(4, address);
            statement.setString(5, email);
            statement.setString(6, password);
            if (isAdmin) statement.setInt(7, clientId);
            else statement.setString(7, clientToken);

            return statement.executeUpdate() != 0;

        }
    }

    public boolean removeClient(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.DELETE_CLIENT)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public ClientInfo getClientInfo(String email, String password) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CLIENT_INFO)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                ClientInfo client = null;
                OrderItem prevOrder = new OrderItem();
                while (resultSet.next()) {
                    if(client == null) client = createClient(resultSet);
                    int orderId = resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN);
                    if (orderId != prevOrder.getId()) {
                        OrderItem order = createOrder(resultSet);
                        order.addProduct(createProduct(resultSet));
                        prevOrder = order;
                        client.addOrder(order);
                    } else {
                        prevOrder.addProduct(createProduct(resultSet));
                    }
                }
                if (client == null) throw new SQLException(ConstantsManager.WRONG_CREDENTIALS);
                return client;
            }
        }
    }

    public List<ClientInfo> getClients() throws SQLException {
        List<ClientInfo> clients = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CLIENTS_WITH_ORDERS);
             ResultSet resultSet = statement.executeQuery()) {

            ClientInfo prevClient = new ClientInfo();
            OrderItem prevOrder = new OrderItem();
            while (resultSet.next()) {
                int clientId = resultSet.getInt(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_ID_COLUMN);
                int orderId = resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN);

                if (clientId != prevClient.getId()) {
                    ClientInfo nextClient = createClient(resultSet);
                    if (resultSet.getInt(ShopContract.ORDER_CLIENT_ID_COLUMN) != 0) {
                        OrderItem order = createOrder(resultSet);
                        order.addProduct(createProduct(resultSet));
                        nextClient.addOrder(order);
                        prevOrder = order;
                    }
                    clients.add(nextClient);
                    prevClient = nextClient;
                } else if (orderId != prevOrder.getId()) {
                    OrderItem order = createOrder(resultSet);
                    order.addProduct(createProduct(resultSet));
                    prevOrder = order;
                    prevClient.addOrder(order);
                } else {
                    prevOrder.addProduct(createProduct(resultSet));
                }
            }
            return clients;
        }
    }

    public boolean isAccessAllowed(String token, int action) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.IS_ACCESS_ALLOWED)) {

            statement.setInt(1, action);
            statement.setString(2, token);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getInt(1) > 0;
            }
        }
    }

    public boolean isUserExist(String token) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_ALL_TOKENS)) {

            statement.setString(1, token);
            statement.setString(2, token);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void initDriver() {
        try {
            Class.forName(ShopContract.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private ProductItem createProduct(ResultSet result) throws SQLException {
        return new ProductItem(
                result.getInt(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_ID_COLUMN),
                result.getString(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.STOCK_ADDRESS_COLUMN),
                result.getString(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_TITLE_COLUMN),
                result.getString(ShopContract.PRODUCT_FEATURES_COLUMN),
                result.getInt(ShopContract.PRODUCT_SUPPLY_PERIOD_COLUMN),
                result.getInt(ShopContract.PRODUCT_STOCK_AMOUNT_COLUMN),
                result.getDouble(ShopContract.PRODUCT_PRICE_COLUMN),
                result.getString(ShopContract.PRODUCT_METRICS_COLUMN),
                result.getString(ShopContract.PRODUCT_IMAGE_URL),
                result.getDate(ShopContract.PRODUCT_FIRST_SUPPLY_DATE_COLUMN),
                result.getString(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_DESCRIPTION_COLUMN),
                result.getString(ShopContract.PRODUCT_ARTICUL_COLUMN));
    }

    private ClientInfo createClient(ResultSet resultSet) throws SQLException {
        ClientInfo nextClient = new ClientInfo();
        nextClient.setId(resultSet.getInt(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_ID_COLUMN));
        nextClient.setName(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_NAME_COLUMN));
        nextClient.setSurname(ShopContract.CLIENTS_TABLE_NAME + "." + resultSet.getString(ShopContract.CLIENT_SURNAME_COLUMN));
        nextClient.setPhoneNumber(ShopContract.CLIENTS_TABLE_NAME + "." + resultSet.getString(ShopContract.CLIENT_PHONE_COLUMN));
        nextClient.setAddress(ShopContract.CLIENTS_TABLE_NAME + "." + resultSet.getString(ShopContract.CLIENT_ADDRESS_COLUMN));
        nextClient.setEmail(ShopContract.CLIENTS_TABLE_NAME + "." + resultSet.getString(ShopContract.CLIENT_EMAIL_COLUMN));
        nextClient.setToken(ShopContract.CLIENTS_TABLE_NAME + "." + resultSet.getString(ShopContract.CLIENT_TOKEN_COLUMN));
        return nextClient;
    }

    private OrderItem createOrder(ResultSet resultSet) throws SQLException {
        OrderItem order = new OrderItem();
        order.setId(resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN));
        order.setDate(resultSet.getDate(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_DATE_COLUMN));
        order.setClientFullName(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_CLIENT_NAME_COLUMNN));
        order.setClientPhone(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_PHONE_NUMBER_COLUMN));
        order.setStatus(resultSet.getString(ShopContract.STATUS_TABLE_NAME + "." + ShopContract.STATUS_DESCRIPTION_COLUMN));
        order.setEmployeeName(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_NAME_COLUMN));
        order.setEmployeeSurname(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_SURNAME_COLUMN));
        order.setEmployeePhone(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_PHONE_COLUMN));
        order.setDeliveryAddress(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ADDRESS_COLUMN));
        order.setSum(resultSet.getDouble(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_SUM_COLUMN));
        return order;
    }

}
