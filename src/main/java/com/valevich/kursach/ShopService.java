package com.valevich.kursach;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.valevich.kursach.model.request.AccessPayload;
import com.valevich.kursach.model.request.category.DeleteCategoryPayload;
import com.valevich.kursach.model.request.category.NewCategoryPayload;
import com.valevich.kursach.model.request.category.UpdateCategoryPayload;
import com.valevich.kursach.model.request.client.*;
import com.valevich.kursach.model.request.order.BindEmployeePayload;
import com.valevich.kursach.model.request.order.DeleteOrderPayload;
import com.valevich.kursach.model.request.order.NewOrderPayload;
import com.valevich.kursach.model.request.order.SetOrderStatusPayload;
import com.valevich.kursach.model.request.product.DeleteProductPayload;
import com.valevich.kursach.model.request.product.NewProductPayload;
import com.valevich.kursach.model.request.status.DeleteStatusPayload;
import com.valevich.kursach.model.request.status.NewStatusPayload;
import com.valevich.kursach.model.request.status.UpdateStatusPayload;
import com.valevich.kursach.model.request.stock.DeleteStockPayload;
import com.valevich.kursach.model.request.stock.NewStockPayload;
import com.valevich.kursach.model.request.stock.UpdateStockPayload;
import com.valevich.kursach.model.response.DefaultInsertResponse;
import com.valevich.kursach.model.response.DefaultResponse;
import com.valevich.kursach.model.response.user.ClientRegistration;
import com.valevich.kursach.storage.DbHelper;
import com.valevich.kursach.util.ConstantsManager;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static spark.Spark.get;
import static spark.Spark.post;


public class ShopService {

    public static void main(String[] args) {
        DbHelper dbHelper = new DbHelper();

        post("/category/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewCategoryPayload newCategory = mapper.readValue(req.body(), NewCategoryPayload.class);
                if (!newCategory.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                long id = dbHelper.insertCategory(newCategory.getName(),
                        newCategory.getImageUrl(),
                        newCategory.getUserEmail(),
                        newCategory.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/category/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteCategoryPayload categoryToRemove = mapper.readValue(req.body(), DeleteCategoryPayload.class);
                if (!categoryToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasDeleted = dbHelper.removeCategory(categoryToRemove.getId(),
                        categoryToRemove.getUserEmail(),
                        categoryToRemove.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/category/update", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UpdateCategoryPayload categoryToUpdate = mapper.readValue(req.body(), UpdateCategoryPayload.class);
                if (!categoryToUpdate.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasUpdated = dbHelper.updateCategory(categoryToUpdate.getId(),
                        categoryToUpdate.getName(),
                        categoryToUpdate.getImageUrl(),
                        categoryToUpdate.getUserEmail(),
                        categoryToUpdate.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        get("/catalog", (request, response) -> {
            try {
                String token = request.headers("token");
                if (token == null) {
                    response.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                if (!dbHelper.isUserExist(token)) {
                    response.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                response.status(200);
                response.type("application/json");
                return dataToJson(dbHelper.getCatalog());
            } catch (SQLException e) {
                response.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        get("/stocks/get", (req, res) -> {
            try {
                String email = req.headers("email");
                String pass = req.headers("password");
                if (email == null || pass == null) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getStocks(email, pass));
            } catch (SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/stock/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewStockPayload newStock = mapper.readValue(req.body(), NewStockPayload.class);
                if (!newStock.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                long id = dbHelper.insertStock(newStock.getAddress(),
                        newStock.getUserEmail(),
                        newStock.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/stock/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteStockPayload stockToRemove = mapper.readValue(req.body(), DeleteStockPayload.class);
                if (!stockToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasDeleted = dbHelper.removeStock(stockToRemove.getId(),
                        stockToRemove.getUserEmail(),
                        stockToRemove.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/stock/update", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UpdateStockPayload stockToUpdate = mapper.readValue(req.body(), UpdateStockPayload.class);
                if (!stockToUpdate.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasUpdated = dbHelper.updateStock(stockToUpdate.getId(),
                        stockToUpdate.getAddress(),
                        stockToUpdate.getUserEmail(),
                        stockToUpdate.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/register", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ClientAuthPayload newClient = mapper.readValue(req.body(), ClientAuthPayload.class);
                if (!newClient.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                String token = dbHelper.insertClient(newClient.getEmail(), newClient.getPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new ClientRegistration(token));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/login", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ClientAuthPayload clientPayload = mapper.readValue(req.body(), ClientAuthPayload.class);
                if (!clientPayload.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getClientInfo(clientPayload.getEmail(), clientPayload.getPassword()));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        get("/clients/get", (req, res) -> {
            try {
                String email = req.headers("email");
                String pass = req.headers("password");
                if (email == null || pass == null) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getClients(email, pass));
            } catch (SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                AddClientPayload newClient = mapper.readValue(req.body(), AddClientPayload.class);
                if (!newClient.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }

                long id = dbHelper.insertClient(newClient.getName(),
                        newClient.getSurname(),
                        newClient.getPhoneNumber(),
                        newClient.getAddress(),
                        newClient.getEmail(),
                        newClient.getPassword(),
                        newClient.getUserEmail(),
                        newClient.getUserPassword());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/update", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UpdateClientPayload clientToUpdate = mapper.readValue(req.body(), UpdateClientPayload.class);
                if (!clientToUpdate.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }

                boolean hasUpdated = dbHelper.updateClient(clientToUpdate.getName(),
                        clientToUpdate.getSurname(),
                        clientToUpdate.getPhoneNumber(),
                        clientToUpdate.getAddress(),
                        clientToUpdate.getEmail(),
                        clientToUpdate.getPassword(),
                        clientToUpdate.getId(),
                        null,
                        clientToUpdate.getUserEmail(),
                        clientToUpdate.getUserPassword());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/update_personal", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UpdatePersonalInfoPayload clientInfoToUpdate = mapper.readValue(req.body(), UpdatePersonalInfoPayload.class);
                if (!clientInfoToUpdate.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                if (!dbHelper.isUserExist(clientInfoToUpdate.getToken())) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                boolean hasUpdated = dbHelper.updateClient(clientInfoToUpdate.getName(),
                        clientInfoToUpdate.getSurname(),
                        clientInfoToUpdate.getPhoneNumber(),
                        clientInfoToUpdate.getAddress(),
                        clientInfoToUpdate.getEmail(),
                        clientInfoToUpdate.getPassword(),
                        0,
                        clientInfoToUpdate.getToken(),
                        ConstantsManager.ROOT_USER,
                        ConstantsManager.ROOT_PASS);

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/client/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteClientPayload clientToRemove = mapper.readValue(req.body(), DeleteClientPayload.class);
                if (!clientToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }

                boolean hasDeleted = dbHelper.removeClient(clientToRemove.getId(),
                        clientToRemove.getUserEmail(),
                        clientToRemove.getUserPassword());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        get("/orders/get", (req, res) -> {
            try {
                String email = req.headers("email");
                String pass = req.headers("password");
                if (email == null || pass == null) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getOrders(email, pass));
            } catch (SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(getMessage(e));
            }
        });

        post("/status/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewStatusPayload newStatus = mapper.readValue(req.body(), NewStatusPayload.class);
                if (!newStatus.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                long id = dbHelper.insertStatus(newStatus.getDescription(),
                        newStatus.getUserEmail(),
                        newStatus.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/status/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteStatusPayload statusToRemove = mapper.readValue(req.body(), DeleteStatusPayload.class);
                if (!statusToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasDeleted = dbHelper.removeStatus(statusToRemove.getId(),
                        statusToRemove.getUserEmail(),
                        statusToRemove.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
        post("/status/update", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UpdateStatusPayload statusToUpdate = mapper.readValue(req.body(), UpdateStatusPayload.class);
                if (!statusToUpdate.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasUpdated = dbHelper.updateStatus(statusToUpdate.getId(),
                        statusToUpdate.getDescription(),
                        statusToUpdate.getUserEmail(),
                        statusToUpdate.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/order/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewOrderPayload newOrder = mapper.readValue(req.body(), NewOrderPayload.class);
                if (!newOrder.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                if (!dbHelper.isUserExist(newOrder.getToken())) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                boolean isSuccessful = dbHelper.insertOrder(newOrder.getDate(),
                        newOrder.getClientFullName(),
                        newOrder.getClientPhone(),
                        newOrder.getDeliveryAddress(),
                        newOrder.getSum(),
                        newOrder.getClientId(),
                        newOrder.getProducts());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(isSuccessful ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/order/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteOrderPayload orderToRemove = mapper.readValue(req.body(), DeleteOrderPayload.class);
                if (!orderToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasDeleted = dbHelper.removeOrder(orderToRemove.getId(),
                        orderToRemove.getUserEmail(),
                        orderToRemove.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/product/add", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewProductPayload newProduct = mapper.readValue(req.body(), NewProductPayload.class);
                if (!newProduct.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }

                long id = dbHelper.insertProduct(newProduct.getTitle(),
                        newProduct.getFeatures(),
                        newProduct.getAmount(),
                        newProduct.getPrice(),
                        newProduct.getMetrics(),
                        newProduct.getImageUrl(),
                        newProduct.getDescription(),
                        newProduct.getArticul(),
                        newProduct.getCategoryId(),
                        newProduct.getStockId(),
                        newProduct.getUserEmail(),
                        newProduct.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/product/remove", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DeleteProductPayload productToRemove = mapper.readValue(req.body(), DeleteProductPayload.class);
                if (!productToRemove.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasDeleted = dbHelper.removeProduct(productToRemove.getId(),
                        productToRemove.getUserEmail(),
                        productToRemove.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        get("/employees/get", (req, res) -> {
            try {
                String email = req.headers("email");
                String pass = req.headers("password");
                if (email == null || pass == null) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getEmployees(email, pass));
            } catch (SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/employee/login", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                AccessPayload authPayload = mapper.readValue(req.body(), AccessPayload.class);
                if (!authPayload.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getEmployeeInfo(authPayload.getUserEmail(), authPayload.getUserPassword()));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/order/set/employee", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                BindEmployeePayload payload = mapper.readValue(req.body(), BindEmployeePayload.class);
                if (!payload.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasUpdated = dbHelper.bindEmployee(payload.getEmployeeId(),
                        payload.getOrderStatusId(),
                        payload.getUserEmail(),
                        payload.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });

        post("/order/set/status", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                SetOrderStatusPayload payload = mapper.readValue(req.body(), SetOrderStatusPayload.class);
                if (!payload.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                boolean hasUpdated = dbHelper.setOrderStatus(
                        payload.getOrderStatusId(),
                        payload.getUserEmail(),
                        payload.getUserPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(getMessage(e)));
            }
        });
    }

    private static String getMessage(Exception e) {
        String message = e.getMessage();
        if (message == null) return ConstantsManager.INVALID_REQUEST;
        if (message.contains("Duplicate")) return ConstantsManager.DUPLICATE_EMAIL;
        if (message.contains("denied")) return ConstantsManager.ACCESS_NOT_ALLOWED;
        if (message.contains("Out of range")) return ConstantsManager.NO_PRODUCTS_LEFT;
        if (message.equals(ConstantsManager.WRONG_CREDENTIALS)) return message;
        return ConstantsManager.INVALID_REQUEST;
    }

    /*
    duplicate entry
    Неправильный userEmail или пароль
    access not allowed
     */
    private static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}
