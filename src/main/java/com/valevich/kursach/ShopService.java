package com.valevich.kursach;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.valevich.kursach.model.request.category.DeleteCategoryPayload;
import com.valevich.kursach.model.request.category.NewCategoryPayload;
import com.valevich.kursach.model.request.category.UpdateCategoryPayload;
import com.valevich.kursach.model.request.client.AddClientPayload;
import com.valevich.kursach.model.request.client.ClientPayload;
import com.valevich.kursach.model.request.client.DeleteClientPayload;
import com.valevich.kursach.model.request.client.UpdateClientPayload;
import com.valevich.kursach.model.response.DefaultResponse;
import com.valevich.kursach.model.response.DefaultInsertResponse;
import com.valevich.kursach.model.response.user.ClientRegistration;
import com.valevich.kursach.storage.DbHelper;
import com.valevich.kursach.storage.ShopContract;
import com.valevich.kursach.util.ConstantsManager;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static spark.Spark.*;


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
                if (!dbHelper.isAccessAllowed(newCategory.getToken(), ShopContract.RolesContract.EDIT_CATALOG)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                long id = dbHelper.insertCategory(newCategory.getName());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
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
                if (!dbHelper.isAccessAllowed(categoryToRemove.getToken(), ShopContract.RolesContract.EDIT_CATALOG)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                boolean hasDeleted = dbHelper.removeCategory(categoryToRemove.getId());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                e.printStackTrace();
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
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
                if (!dbHelper.isAccessAllowed(categoryToUpdate.getToken(), ShopContract.RolesContract.EDIT_CATALOG)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                boolean hasUpdated = dbHelper.updateCategory(categoryToUpdate.getId(),categoryToUpdate.getName());
                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                e.printStackTrace();
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
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
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
            }
        });

        post("/client/register", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ClientPayload newClient = mapper.readValue(req.body(), ClientPayload.class);
                if (!newClient.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST_FRIENDLY));
                }
                String token = dbHelper.insertClient(newClient.getEmail(),newClient.getPassword());
                res.status(200);
                res.type("application/json");
                return dataToJson(new ClientRegistration(token));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                String message = e.getMessage() != null && e.getMessage().contains("Duplicate")
                        ? ConstantsManager.DUPLICATE_EMAIL
                        : ConstantsManager.INVALID_REQUEST_FRIENDLY;
                return dataToJson(new DefaultResponse(message));
            }
        });

        post("/client/login", (req, res) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ClientPayload clientPayload = mapper.readValue(req.body(), ClientPayload.class);
                if (!clientPayload.isValid()) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST_FRIENDLY));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getClient(clientPayload.getEmail(),clientPayload.getPassword()));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                String message = e.getMessage() != null && e.getMessage().equals(ConstantsManager.WRONG_CREDENTIALS)
                        ? ConstantsManager.WRONG_CREDENTIALS
                        : ConstantsManager.INVALID_REQUEST;
                return dataToJson(new DefaultResponse(message));
            }
        });

        get("/clients/get", (req, res) -> {
            try {
                String token = req.headers("token");
                if (token == null) {
                    res.status(HTTP_BAD_REQUEST);
                    return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
                }
                if (!dbHelper.isAccessAllowed(token, ShopContract.RolesContract.READ_CLIENTS)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                res.status(200);
                res.type("application/json");
                return dataToJson(dbHelper.getClients());
            } catch (SQLException e) {
                e.printStackTrace();
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
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
                if (!dbHelper.isAccessAllowed(newClient.getEmployeeToken(), ShopContract.RolesContract.EDIT_CLIENTS)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                long id = dbHelper.insertClient(newClient.getName(),
                        newClient.getSurname(),
                        newClient.getPhoneNumber(),
                        newClient.getAddress(),
                        newClient.getEmail(),
                        newClient.getPassword());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultInsertResponse(id));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                String message = e.getMessage() != null && e.getMessage().contains("Duplicate")
                        ? ConstantsManager.DUPLICATE_EMAIL
                        : ConstantsManager.INVALID_REQUEST_FRIENDLY;
                return dataToJson(new DefaultResponse(message));
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
                if (!dbHelper.isAccessAllowed(clientToUpdate.getEmployeeToken(), ShopContract.RolesContract.EDIT_CLIENTS)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }
                boolean hasUpdated = dbHelper.updateClient(clientToUpdate.getName(),
                        clientToUpdate.getSurname(),
                        clientToUpdate.getPhoneNumber(),
                        clientToUpdate.getAddress(),
                        clientToUpdate.getEmail(),
                        clientToUpdate.getPassword(),
                        clientToUpdate.getId());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasUpdated ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                String message = e.getMessage() != null && e.getMessage().contains("Duplicate")
                        ? ConstantsManager.DUPLICATE_EMAIL
                        : ConstantsManager.INVALID_REQUEST_FRIENDLY;
                return dataToJson(new DefaultResponse(message));
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
                if (!dbHelper.isAccessAllowed(clientToRemove.getToken(), ShopContract.RolesContract.EDIT_CLIENTS)) {
                    res.status(HTTP_FORBIDDEN);
                    return dataToJson(new DefaultResponse(ConstantsManager.ACCESS_NOT_ALLOWED));
                }

                boolean hasDeleted = dbHelper.removeClient(clientToRemove.getId());

                res.status(200);
                res.type("application/json");
                return dataToJson(new DefaultResponse(hasDeleted ? ConstantsManager.OPERATION_SUCCESSFULL : ConstantsManager.INVALID_REQUEST));
            } catch (JsonParseException | JsonMappingException | SQLException e) {
                res.status(HTTP_BAD_REQUEST);
                return dataToJson(new DefaultResponse(ConstantsManager.INVALID_REQUEST));
            }
        });
    }


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
