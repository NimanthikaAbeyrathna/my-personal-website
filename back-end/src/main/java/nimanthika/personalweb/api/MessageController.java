package nimanthika.personalweb.api;

import nimanthika.personalweb.dto.MessageDTO;
import nimanthika.personalweb.dto.ResponseErrorDTO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {

    @Autowired
    private BasicDataSource pool;

    @PostMapping
    public ResponseEntity<?> saveMessage (@RequestBody MessageDTO message){

        try (Connection connection = pool.getConnection()) {

            PreparedStatement stm = connection.prepareStatement
                    ("INSERT INTO message ( date,name, email,message) VALUES (?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);


            stm.setString(1, message.getDateTime());
            stm.setString(2, message.getName());
            stm.setString(3, message.getEmail());
            stm.setString(4, message.getTextMessage());

            stm.executeUpdate();


            return new ResponseEntity<>( HttpStatus.CREATED);


        }catch (SQLException e){
            e.printStackTrace();
            return  new ResponseEntity<>(new ResponseErrorDTO(500,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
