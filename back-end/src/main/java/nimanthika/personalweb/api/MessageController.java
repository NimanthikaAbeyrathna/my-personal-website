package nimanthika.personalweb.api;

import nimanthika.personalweb.dto.MessageDTO;
import nimanthika.personalweb.dto.ResponseErrorDTO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {

    @Autowired
    private BasicDataSource pool;
    @Autowired
    private ServletContext servletContext;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveFiles(@RequestPart("files") List<Part> files,
                                  UriComponentsBuilder urlBuilder){

        List<String> fileUrlList = new ArrayList<>();

        if (files!= null) {
            String fileDirPath =  servletContext.getRealPath("/files");

            for (Part file : files) {

                String filePath =  new File( fileDirPath,file.getSubmittedFileName()).getAbsolutePath();
                File fileDir = new File(fileDirPath);

                if (!fileDir.exists()) {
                    if (fileDir.mkdirs()) {
                        System.out.println("Directory created successfully: " + fileDir.getAbsolutePath());
                    } else {
                        System.out.println("Failed to create the directory: " + fileDir.getAbsolutePath());

                    }
                }

                try{
                    file.write(filePath);
                    UriComponentsBuilder cloneBuilder = urlBuilder.cloneBuilder();


                    String fileUrl = cloneBuilder.pathSegment("files",file.getSubmittedFileName()).toUriString();
                    fileUrlList.add(fileUrl);

                }catch (IOException e){
                    e.printStackTrace();
                    return  new ResponseEntity<>(new ResponseErrorDTO(500,e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        }

        return new ResponseEntity<>( HttpStatus.CREATED);

    }

}
