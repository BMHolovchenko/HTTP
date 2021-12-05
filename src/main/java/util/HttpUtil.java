package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final CloseableHttpClient CLOSEABLE_HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final Gson GSON = new Gson();
    private static final String MAIN_PAGE = "https://petstore.swagger.io/v2";
    private static final String PET = "/pet";
    private static final String IMAGE = "/uploadImage";
    private static final String FIND_BY_STATUS = "/findByStatus?status=";
    private static final String STORE = "https://petstore.swagger.io/v2/store/";
    private static final String INVENTORY = "inventory";
    private static final String ORDER = "order";
    private static final String USER = "https://petstore.swagger.io/v2/user";
    private static final String CREATE = "/createWithList";
    private HttpResponse<String> response;

    public ApiResponse uploadImage(long id, String metaData, File file) throws IOException {
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        StringBody stringBody = new StringBody(metaData, ContentType.MULTIPART_FORM_DATA);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("additionalMetadata", stringBody)
                .addPart("file", fileBody);
        HttpEntity build = builder.build();
        HttpPost post = new HttpPost(String.format("%s%s/%d%s", MAIN_PAGE, PET, id, IMAGE));
        post.setEntity(build);
        return GSON.fromJson(CLOSEABLE_HTTP_CLIENT.execute(post, new BasicResponseHandler()), ApiResponse.class);
    }

    public Pet getPetById(long idPet) throws IOException, InterruptedException {
        String urlUserById = String.format("%s%s/%d", MAIN_PAGE, PET, idPet);
        response = getGETResponse(urlUserById);
        return GSON.fromJson(response.body(), Pet.class);
    }


    public Pet createPet(Pet pet) throws IOException, InterruptedException {
        String url = String.format("%s%s", MAIN_PAGE, PET);
        response = getPOSTResponse(url, pet);
        return GSON.fromJson(response.body(), Pet.class);
    }

    public Pet updatePet(Pet pet) throws IOException, InterruptedException {
        String url = String.format("%s%s", MAIN_PAGE, PET);
        response = getPUTResponse(url, pet);
        return GSON.fromJson(response.body(), Pet.class);
    }

    public Pet updatePetById(Long id, String name, PetStatus status) throws IOException, InterruptedException {
        Pet pet = getPetById(id);
        pet.setName(name);
        pet.setStatus(status);
        return updatePet(pet);
    }

    public List<Pet> findByStatus(PetStatus status) throws IOException, InterruptedException {
        String urlPetStatus = String.format("%s%s%s%s", MAIN_PAGE, PET, FIND_BY_STATUS, status.name());
        response = getGETResponse(urlPetStatus);
        return GSON.fromJson(response.body(), new TypeToken<List<Pet>>() {
        }.getType());
    }

    public ApiResponse deleteById(long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", MAIN_PAGE, PET, id)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public Map<String, Integer> mapOfStatusCodesToQuantities() throws IOException, InterruptedException {
        String url = String.format("%s%s", STORE, INVENTORY);
        response = getGETResponse(url);
        return GSON.fromJson(response.body(), new TypeToken<Map<String, Integer>>() {
        }.getType());
    }

    public Order createOrder(Order order) throws IOException, InterruptedException {
        String url = String.format("%s%s", STORE, ORDER);
        response = getPOSTResponse(url, order);
        return GSON.fromJson(response.body(), Order.class);
    }


    public Order getOrderById(long id) throws IOException, InterruptedException {
        String urlUserById = String.format("%s%s/%d", STORE, ORDER, id);
        response = getGETResponse(urlUserById);
        return GSON.fromJson(response.body(), Order.class);
    }

    public ApiResponse deleteOrderById(long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", STORE, ORDER, id)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse createWithListUsers(List<User> entity) throws IOException, InterruptedException {
        String url = String.format("%s%s", USER, CREATE);
        response = getPOSTResponse(url, entity);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public User getUserByName(String name) throws IOException, InterruptedException {
        String url = String.format("%s/%s", USER, name);
        response = getGETResponse(url);
        return GSON.fromJson(response.body(), User.class);
    }

    public ApiResponse updateUserByName(String name, User user) throws IOException, InterruptedException {
        String url = String.format("%s/%s", USER, name);
        response = getPUTResponse(url, user);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse deleteUserByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s", USER, name)))
                .DELETE()
                .build();
        response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    public ApiResponse createUser(User user) throws IOException, InterruptedException {
        response = getPOSTResponse(USER, user);
        return GSON.fromJson(response.body(), ApiResponse.class);
    }

    private HttpResponse<String> getPUTResponse(String url, Object entity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(GSON.toJson(entity)))
                .header("Content-type", "application/json")
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getPOSTResponse(String url, Object entity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(entity)))
                .header("Content-type", "application/json")
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getGETResponse(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}