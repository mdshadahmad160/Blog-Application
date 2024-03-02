package com.myblog.blogapp.exception;
import com.myblog.blogapp.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    //Specefic Exception
    // i.e Wrong id ,Wrong PanCardNo,Wrong AdharCardNo
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest){
        ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }
    //Global Exception It will handle any exception:
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllException(
            Exception exception,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails=new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return  new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
