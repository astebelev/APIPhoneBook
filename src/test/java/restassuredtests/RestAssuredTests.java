package restassuredtests;

import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ContactDto;
import dto.GetAllContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.given;

public class RestAssuredTests {

    @BeforeMethod
    public void precondition() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com";
        RestAssured.basePath = "api";
    }

    @Test
    public void login(){
        AuthRequestDto body = AuthRequestDto.builder()
                .email("john9090@mail.com")
                .password("Aa12345~")
                .build();


        AuthResponseDto responseDto = given().contentType("application/json")
                .body(body)
                .when()
                .post("/login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }


    @Test
    public void getAllContacts(){
        GetAllContactDto responseDto = given()
                .header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImpvaG45MDkwQG1haWwuY29tIn0.uFp-ZueoxtelSBWIrrsl7SmMAKUNBmjQNj33Zh2ik6w")
                .get("/contact")
                .then()
                .assertThat().statusCode(200)
                .extract().body().as(GetAllContactDto.class);

        for(ContactDto contact: responseDto.getContacts()){
            System.out.println(contact.getId() + "***"+ contact.getName() + "***");
            System.out.println("==============================");
        }

    }

    @Test
    public void deleteContact(){

        String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImpvaG45MDkwQG1haWwuY29tIn0.uFp-ZueoxtelSBWIrrsl7SmMAKUNBmjQNj33Zh2ik6w";

        String status = given()
                .header("Authorization",token)
                .when()
                .delete("/contact/7775")
                .then()
                .assertThat().statusCode(200)
                .extract().path("status");
        System.out.println(status);
    }

    @Test
    public void addNewContact(){

        String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImpvaG45MDkwQG1haWwuY29tIn0.uFp-ZueoxtelSBWIrrsl7SmMAKUNBmjQNj33Zh2ik6w";

        ContactDto contactDto= ContactDto.builder()
                .address("Haifa")
                .email("hoolo@mail.com")
                .name("Holly")
                .lastName("Dow")
                .phone("997765321")
                .description("friend").build();

        int id =given().header("Authorization",token)
                .contentType("application/json")
                .body(contactDto)
                .when()
                .post("/contact")
                .then()
                .assertThat().statusCode(200)
                .extract().path("id");
        System.out.println(id);
    }
}
