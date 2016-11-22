package com.valevich.kursach.storage;

import com.valevich.kursach.model.response.catalog.CatalogItem;
import com.valevich.kursach.model.response.catalog.ProductItem;
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
                ProductItem product = createProduct(result);
                if (categoryId != prevCategory.getId()) {
                    CatalogItem nextCategory = new CatalogItem();
                    nextCategory.setId(categoryId);
                    nextCategory.setName(result.getString(ShopContract.CATEGORY_NAME_COLUMN));
                    if (result.getInt(ShopContract.PRODUCT_CATEGORY_ID_COLUMN) != 0)
                        nextCategory.addProduct(product);

                    catalog.add(nextCategory);
                    prevCategory = nextCategory;
                } else {
                    prevCategory.addProduct(product);
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
            statement.setString(2,surname);
            statement.setString(3, phone);
            statement.setString(4,address);
            statement.setString(5, email);
            statement.setString(6,password);
            statement.setString(7,UUID.randomUUID().toString());

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
                                     String email, String password,int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.UPDATE_CLIENT_ADMIN)) {

            statement.setString(1, name);
            statement.setString(2,surname);
            statement.setString(3, phone);
            statement.setString(4,address);
            statement.setString(5, email);
            statement.setString(6,password);
            statement.setInt(7,id);

            return statement.executeUpdate() != 0;

        }
    }

    public boolean removeClient(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.DELETE_CLIENT)) {

            statement.setInt(1,id);
            return statement.executeUpdate() != 0;
        }
    }

    public ClientInfo getClient(String email, String password) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_CLIENT)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createClient(resultSet);
                } else {
                    throw new SQLException(ConstantsManager.WRONG_CREDENTIALS);
                }
            }
        }
    }

    public List<ClientInfo> getClients() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ShopContract.GET_ALL_CLIENTS);
             ResultSet resultSet = statement.executeQuery()) {

            List<ClientInfo> clients = new ArrayList<>();
            while (resultSet.next()) {
                clients.add(createClient(resultSet));
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
                result.getInt(1),
                result.getInt(ShopContract.PRODUCT_STOCK_ID_COLUMN),
                result.getInt(ShopContract.PRODUCT_PROVIDER_ID_COLUMN),
                result.getString(ShopContract.PRODUCT_TITLE_COLUMN),
                result.getString(ShopContract.PRODUCT_FEATURES_COLUMN),
                result.getInt(ShopContract.PRODUCT_SUPPLY_PERIOD_COLUMN),
                result.getInt(ShopContract.PRODUCT_STOCK_AMOUNT_COLUMN),
                result.getDouble(ShopContract.PRODUCT_PRICE_COLUMN),
                result.getString(ShopContract.PRODUCT_METRICS_COLUMN),
                result.getString(ShopContract.PRODUCT_IMAGE_URL),
                result.getDate(ShopContract.PRODUCT_FIRST_SUPPLY_DATE_COLUMN),
                result.getString(ShopContract.PRODUCT_DESCRIPTION_COLUMN),
                result.getString(ShopContract.PRODUCT_ARTICUL_COLUMN));
    }

    private ClientInfo createClient(ResultSet resultSet) throws SQLException {
        return new ClientInfo(
                resultSet.getInt(ShopContract.CLIENT_ID_COLUMN),
                resultSet.getString(ShopContract.CLIENT_NAME_COLUMN),
                resultSet.getString(ShopContract.CLIENT_SURNAME_COLUMN),
                resultSet.getString(ShopContract.CLIENT_PHONE_COLUMN),
                resultSet.getDouble(ShopContract.CLIENT_CARD_NUMBER),
                resultSet.getInt(ShopContract.CLIENT_CARD_CVV),
                resultSet.getString(ShopContract.CLIENT_CARD_OWNER),
                resultSet.getDate(ShopContract.CLIENT_CARD_EXP_DATE),
                resultSet.getString(ShopContract.CLIENT_ADDRESS_COLUMN),
                resultSet.getString(ShopContract.CLIENT_EMAIL_COLUMN),
                resultSet.getString(ShopContract.CLIENT_TOKEN_COLUMN)
        );
    }
}
