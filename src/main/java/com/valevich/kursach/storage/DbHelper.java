package com.valevich.kursach.storage;

import com.valevich.kursach.model.request.product.ProductInOrder;
import com.valevich.kursach.model.response.catalog.CatalogItem;
import com.valevich.kursach.model.response.catalog.ProductItem;
import com.valevich.kursach.model.response.employee.EmployeeItem;
import com.valevich.kursach.model.response.order.OrderItem;
import com.valevich.kursach.model.response.status.StatusItem;
import com.valevich.kursach.model.response.stock.StockItem;
import com.valevich.kursach.model.response.user.ClientInfo;
import com.valevich.kursach.util.ConstantsManager;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DbHelper {

    public DbHelper() {
        initDriver();
    }

    public long insertCategory(String name, String imageUrl, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_CATEGORY,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, imageUrl);
            int rows = statement.executeUpdate();

            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getLong(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean updateCategory(int id, String name, String imageUrl, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.UPDATE_CATEGORY)) {

            statement.setString(1, name);
            statement.setString(2, imageUrl);
            statement.setInt(3, id);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean removeCategory(int id, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.REMOVE_CATEGORY)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public List<CatalogItem> getCatalog() throws SQLException {
        List<CatalogItem> catalog = new ArrayList<>();
        try (Connection connection = getConnection(ConstantsManager.ROOT_USER, ConstantsManager.ROOT_PASS);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CATALOG);
             ResultSet result = statement.executeQuery()) {

            CatalogItem prevCategory = new CatalogItem();
            while (result.next()) {
                int categoryId = result.getInt(ShopContract.CATEGORY_TABLE_NAME + "." + ShopContract.CATEGORY_ID_COLUMN);
                if (categoryId != prevCategory.getId()) {
                    CatalogItem nextCategory = new CatalogItem();
                    nextCategory.setId(categoryId);
                    nextCategory.setName(result.getString(ShopContract.CATEGORY_NAME_COLUMN));
                    nextCategory.setImageUrl(result.getString(ShopContract.CATEGORY_IMAGE_COLUMN));
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

    public List<StockItem> getStocks(String userEmail, String userPass) throws SQLException {
        List<StockItem> stocks = new ArrayList<>();
        try (Connection connection = getConnection(userEmail, userPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_STOCKS);
             ResultSet result = statement.executeQuery()) {

            StockItem prevStock = new StockItem();
            while (result.next()) {
                int stockId = result.getInt(ShopContract.STOCK_TABLE_NAME + "." + ShopContract.STOCK_ID_COLUMN);
                if (stockId != prevStock.getId()) {
                    StockItem nextStock = new StockItem();
                    nextStock.setId(stockId);
                    nextStock.setAddress(result.getString(ShopContract.STOCK_ADDRESS_COLUMN));
                    if (result.getInt(ShopContract.PRODUCT_STOCK_ID_COLUMN) != 0)
                        nextStock.addProduct(createProduct(result));

                    stocks.add(nextStock);
                    prevStock = nextStock;
                } else {
                    prevStock.addProduct(createProduct(result));
                }
            }
        }
        return stocks;
    }

    public int insertStock(String address, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_STOCK)) {

            statement.setString(1, address);
            int rows = statement.executeUpdate();
            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet resultSet = statement.executeQuery(ShopContract.GET_INSERTED_ID)) {
                if (resultSet.next()) return resultSet.getInt(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean updateStock(int id, String address, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.UPDATE_STOCK)) {

            statement.setString(1, address);
            statement.setInt(2, id);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean removeStock(int id, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.REMOVE_STOCK)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public String insertClient(String email, String password) throws SQLException {
        try (Connection connection = getConnection(ConstantsManager.ROOT_USER, ConstantsManager.ROOT_PASS);
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_CLIENT)) {

            String token = UUID.randomUUID().toString();
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, token);

            int rows = statement.executeUpdate();
            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);
            return token;
        }
    }

    public long insertClient(String name, String surname,
                             String phone, String address,
                             String email, String password,
                             String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
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
                                int clientId, String clientToken,
                                String dbUser, String dbUserPass) throws SQLException {
        boolean isAdmin = clientToken == null;
        try (Connection connection = getConnection(dbUser, dbUserPass);
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

    public boolean removeClient(int id, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.DELETE_CLIENT)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public ClientInfo getClientInfo(String email, String password) throws SQLException {
        try (Connection connection = getConnection(ConstantsManager.ROOT_USER, ConstantsManager.ROOT_PASS);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CLIENT_INFO)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                ClientInfo client = null;
                OrderItem prevOrder = new OrderItem();
                while (resultSet.next()) {
                    if (client == null) client = createClient(resultSet);
                    int orderId = resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN);
                    if (orderId != prevOrder.getId()) {
                        OrderItem order = createOrder(resultSet);
                        order.addProduct(createProduct(resultSet));
                        prevOrder = order;
                        if (resultSet.getInt(ShopContract.ORDER_CLIENT_ID_COLUMN) != 0)
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

    public List<ClientInfo> getClients(String dbUser, String dbUserPass) throws SQLException {
        List<ClientInfo> clients = new ArrayList<>();
        try (Connection connection = getConnection(dbUser, dbUserPass);
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
                        if (resultSet.getInt(ShopContract.ORDER_CLIENT_ID_COLUMN) != 0)
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

    public List<StatusItem> getOrders(String dbUser, String dbUserPass) throws SQLException {
        List<StatusItem> statuses = new ArrayList<>();
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_ORDERS);
             ResultSet resultSet = statement.executeQuery()) {

            StatusItem prevStatus = new StatusItem();
            OrderItem prevOrder = new OrderItem();
            while (resultSet.next()) {
                int statusId = resultSet.getInt(ShopContract.STATUS_TABLE_NAME + "." + ShopContract.STATUS_ID_COLUMN);
                int orderId = resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN);

                if (statusId != prevStatus.getId()) {
                    StatusItem nextStatus = new StatusItem();
                    nextStatus.setId(statusId);
                    nextStatus.setDescription(resultSet.getString(ShopContract.STATUS_TABLE_NAME + "." + ShopContract.STATUS_DESCRIPTION_COLUMN));
                    if (resultSet.getInt(ShopContract.ORDER_STATUS_ID_COLUMN) != 0) {
                        OrderItem order = createOrder(resultSet);
                        order.addProduct(createProduct(resultSet));
                        if (resultSet.getInt(ShopContract.ORDER_STATUS_ID_COLUMN) != 0)
                            nextStatus.addOrder(order);
                        prevOrder = order;
                    }
                    statuses.add(nextStatus);
                    prevStatus = nextStatus;
                } else if (orderId != prevOrder.getId()) {
                    OrderItem order = createOrder(resultSet);
                    order.addProduct(createProduct(resultSet));
                    prevOrder = order;
                    prevStatus.addOrder(order);
                } else {
                    prevOrder.addProduct(createProduct(resultSet));
                }
            }
            return statuses;
        }
    }

    public int insertStatus(String description, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_STATUS)) {

            statement.setString(1, description);
            int rows = statement.executeUpdate();
            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet resultSet = statement.executeQuery(ShopContract.GET_INSERTED_ID)) {
                if (resultSet.next()) return resultSet.getInt(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean updateStatus(int id, String description, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.UPDATE_STATUS)) {

            statement.setString(1, description);
            statement.setInt(2, id);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean removeStatus(int id, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.REMOVE_STATUS)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean insertOrder(Date date,
                               String fullName,
                               String phone,
                               String address,
                               double sum,
                               int clientId,
                               List<ProductInOrder> products) throws SQLException {

        try (Connection connection = getConnection(ConstantsManager.ROOT_USER, ConstantsManager.ROOT_PASS)) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_ORDER);
                 PreparedStatement removeFromStockStatement = connection.prepareStatement(ShopContract.REMOVE_FROM_STOCK);
                 PreparedStatement bindProductStatement = connection.prepareStatement(ShopContract.BIND_PRODUCT)) {
                statement.setDate(1, date);
                statement.setString(2, fullName);
                statement.setString(3, phone);
                statement.setString(4, address);
                statement.setInt(5, clientId);
                statement.setDouble(6, sum);
                statement.setInt(7, ConstantsManager.DEFAULT_ORDER_STATUS);

                int rows = statement.executeUpdate();
                if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);
                try (ResultSet resultSet = statement.executeQuery(ShopContract.GET_INSERTED_ID)) {
                    if (resultSet.next()) {
                        int orderId = resultSet.getInt(1);
                        for (ProductInOrder product : products) {
                            bindProductStatement.setInt(1, orderId);
                            bindProductStatement.setInt(2, product.getId());
                            bindProductStatement.setInt(3, product.getAmount());
                            bindProductStatement.addBatch();
                            removeFromStockStatement.setInt(1, product.getAmount());
                            removeFromStockStatement.setInt(2, product.getId());
                            removeFromStockStatement.addBatch();
                        }
                        checkUpdates(bindProductStatement.executeBatch());
                        checkUpdates(removeFromStockStatement.executeBatch());
                    } else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
                }
            } catch (SQLException ex) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw ex;
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        }
    }

    public boolean removeOrder(int id, String dbUser, String dbUserPass) throws SQLException {
        boolean hasDeleted;
        try (Connection connection = getConnection(dbUser, dbUserPass)) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(ShopContract.GET_ORDER_PRODUCTS);
                 PreparedStatement resetStockAmountStatement = connection.prepareStatement(ShopContract.RESET_STOCK_AMOUNT);
                 PreparedStatement removeOrderStatement = connection.prepareStatement(ShopContract.REMOVE_ORDER)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    Map<Integer, Integer> products = new HashMap<>();
                    while (resultSet.next()) {
                        products.put(
                                resultSet.getInt(ShopContract.ORDER_PRODUCT_PRODUCT_ID_COLUMN),
                                resultSet.getInt(ShopContract.ORDER_PRODUCT_AMOUNT_COLUMN));
                    }
                    for (Map.Entry<Integer, Integer> product : products.entrySet()) {
                        resetStockAmountStatement.setInt(1, product.getValue());
                        resetStockAmountStatement.setInt(2, product.getKey());
                        resetStockAmountStatement.addBatch();
                    }
                    checkUpdates(resetStockAmountStatement.executeBatch());
                    removeOrderStatement.setInt(1, id);
                    hasDeleted = removeOrderStatement.executeUpdate() != 0;
                }
            } catch (SQLException ex) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw ex;
            }
            connection.commit();
            connection.setAutoCommit(true);
            return hasDeleted;
        }
    }

    public int insertProduct(String title, String features,
                             int amount, double price,
                             String metrics, String imageUrl,
                             String description, String articul,
                             int categoryId, int stockId,
                             String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.ADD_PRODUCT)) {

            statement.setString(1, title);
            statement.setString(2, features);
            statement.setInt(3, amount);
            statement.setDouble(4, price);
            statement.setString(5, metrics);
            statement.setString(6, imageUrl);
            statement.setString(7, description);
            statement.setString(8, articul);
            statement.setInt(9, categoryId);
            statement.setInt(10, stockId);
            int rows = statement.executeUpdate();
            if (rows == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);

            try (ResultSet resultSet = statement.executeQuery(ShopContract.GET_INSERTED_ID)) {
                if (resultSet.next()) return resultSet.getInt(1);
                else throw new SQLException(ConstantsManager.NO_ID_OBTAINED);
            }
        }
    }

    public boolean removeProduct(int id, String dbUser, String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.REMOVE_PRODUCT)) {

            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public List<EmployeeItem> getEmployees(String dbUser, String dbPass) throws SQLException {
        List<EmployeeItem> employees = new ArrayList<>();
        try (Connection connection = getConnection(dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_EMPLOYEES);
             ResultSet result = statement.executeQuery()) {

            EmployeeItem prevEmployee = new EmployeeItem();
            while (result.next()) {
                int employeeId = result.getInt(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_ID_COLUMN);
                if (employeeId != prevEmployee.getId()) {
                    EmployeeItem nextEmployee = createEmployee(result);
                    if (result.getInt(ShopContract.ORDER_EMPLOYEE_ID_COLUMN) != 0)
                        nextEmployee.addOrder(createOrder(result));

                    employees.add(nextEmployee);
                    prevEmployee = nextEmployee;
                } else {
                    prevEmployee.addOrder(createOrder(result));
                }
            }
        }
        return employees;
    }

    public EmployeeItem getEmployeeInfo(String email, String password) throws SQLException {
        try (Connection connection = getConnection(email, password);
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_EMPLOYEE_INFO)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createEmployee(resultSet);
                } else throw new SQLException(ConstantsManager.WRONG_CREDENTIALS);
            }
        }
    }

    public boolean bindEmployee(int employeeId, int newOrderStatusId,String dbUser,String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.BIND_EMPLOYEE)) {

            statement.setInt(1, employeeId);
            statement.setInt(2, newOrderStatusId);
            return statement.executeUpdate() != 0;
        }
    }

    public boolean setOrderStatus(int newOrderStatusId,String dbUser,String dbUserPass) throws SQLException {
        try (Connection connection = getConnection(dbUser, dbUserPass);
             PreparedStatement statement = connection.prepareStatement(ShopContract.CHANGE_ORDER_STATUS)) {
            statement.setInt(1, newOrderStatusId);
            return statement.executeUpdate() != 0;
        }
    }


    /*
        private Date date;
    private String clientFullName;
    private String clientPhone;
    private String deliveryAddress;
    private int clientId;
    private List<ProductInOrder> products;
     */

//    public boolean isAccessAllowed(String token, int action) throws SQLException {
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(ShopContract.IS_ACCESS_ALLOWED)) {
//
//            statement.setInt(1, action);
//            statement.setString(2, token);
//
//            try (ResultSet result = statement.executeQuery()) {
//                return result.next() && result.getInt(1) > 0;
//            }
//        }
//    }

    public boolean isUserExist(String token) throws SQLException {
        try (Connection connection = getConnection(ConstantsManager.ROOT_USER, ConstantsManager.ROOT_PASS);
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
            Class.forName(ConstantsManager.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Connection getConnection(String user, String pass) throws SQLException {
        return DriverManager.getConnection(ConstantsManager.DB_URL, user, pass);
    }

    private ProductItem createProduct(ResultSet result) throws SQLException {
        return new ProductItem(
                result.getInt(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_ID_COLUMN),
                result.getString(ShopContract.STOCK_TABLE_NAME + "." + ShopContract.STOCK_ADDRESS_COLUMN),
                result.getString(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_TITLE_COLUMN),
                result.getString(ShopContract.PRODUCT_FEATURES_COLUMN),
                result.getInt(ShopContract.PRODUCT_STOCK_AMOUNT_COLUMN),
                result.getDouble(ShopContract.PRODUCT_PRICE_COLUMN),
                result.getString(ShopContract.PRODUCT_METRICS_COLUMN),
                result.getString(ShopContract.PRODUCT_IMAGE_URL),
                result.getString(ShopContract.PRODUCT_TABLE_NAME + "." + ShopContract.PRODUCT_DESCRIPTION_COLUMN),
                result.getString(ShopContract.PRODUCT_ARTICUL_COLUMN));
    }

    private ClientInfo createClient(ResultSet resultSet) throws SQLException {
        ClientInfo nextClient = new ClientInfo();
        nextClient.setId(resultSet.getInt(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_ID_COLUMN));
        nextClient.setName(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_NAME_COLUMN));
        nextClient.setSurname(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_SURNAME_COLUMN));
        nextClient.setPhoneNumber(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_PHONE_COLUMN));
        nextClient.setAddress(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_ADDRESS_COLUMN));
        nextClient.setEmail(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_EMAIL_COLUMN));
        nextClient.setToken(resultSet.getString(ShopContract.CLIENTS_TABLE_NAME + "." + ShopContract.CLIENT_TOKEN_COLUMN));
        return nextClient;
    }

    private OrderItem createOrder(ResultSet resultSet) throws SQLException {
        OrderItem order = new OrderItem();
        order.setId(resultSet.getInt(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ID_COLUMN));
        order.setDate(resultSet.getDate(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_DATE_COLUMN));
        order.setClientFullName(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_CLIENT_NAME_COLUMN));
        order.setClientPhone(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_PHONE_NUMBER_COLUMN));
        order.setStatus(resultSet.getString(ShopContract.STATUS_TABLE_NAME + "." + ShopContract.STATUS_DESCRIPTION_COLUMN));
        order.setEmployeeName(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_NAME_COLUMN));
        order.setEmployeeSurname(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_SURNAME_COLUMN));
        order.setEmployeePhone(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_PHONE_COLUMN));
        order.setDeliveryAddress(resultSet.getString(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_ADDRESS_COLUMN));
        order.setSum(resultSet.getDouble(ShopContract.ORDER_TABLE_NAME + "." + ShopContract.ORDER_SUM_COLUMN));
        return order;
    }

    private EmployeeItem createEmployee(ResultSet resultSet) throws SQLException {
        EmployeeItem employee = new EmployeeItem();
        employee.setId(resultSet.getInt(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_ID_COLUMN));
        employee.setAge(resultSet.getInt(ShopContract.EMPLOYEE_AGE_COLUMN));
        employee.setName(resultSet.getString(ShopContract.EMPLOYEES_TABLE_NAME + "." + ShopContract.EMPLOYEE_NAME_COLUMN));
        employee.setPhoneNumber(resultSet.getString(ShopContract.EMPLOYEE_PHONE_COLUMN));
        employee.setPosition(resultSet.getString(ShopContract.EMPLOYEE_POSITION_COLUMN));
        employee.setSurname(resultSet.getString(ShopContract.EMPLOYEE_SURNAME_COLUMN));
        employee.setToken(resultSet.getString(ShopContract.EMPLOYEE_TOKEN_COLUMN));
        employee.setEmail(resultSet.getString(ShopContract.EMPLOYEE_EMAIL_COLUMN));
        return employee;
    }

    private void checkUpdates(int[] updates) throws SQLException {
        for (int records : updates) {
            if (records == 0) throw new SQLException(ConstantsManager.NO_ROWS_AFFECTED);
        }
    }

}
