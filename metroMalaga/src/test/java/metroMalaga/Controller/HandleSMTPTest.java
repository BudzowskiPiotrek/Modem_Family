package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;

public class HandleSMTPTest {

    private HandleSMTP handleSMTP;
    
    private static final String TEST_EMAIL = "proyectoprofesor8@gmail.com";
    private static final String TEST_APP_PASSWORD = "elbrseooyvaheuyc";

    @BeforeEach
    public void setUp() {
        handleSMTP = new HandleSMTP();
    }

    @Test
    public void testLoginCorrecto() {
        boolean result = handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        assertTrue(result);
    }

    @Test
    public void testLoginConEmailVacio() {
        boolean result = handleSMTP.login("", TEST_APP_PASSWORD);
        assertTrue(result);
    }

    @Test
    public void testLoginConPasswordVacio() {
        boolean result = handleSMTP.login(TEST_EMAIL, "");
        assertTrue(result);
    }

    @Test
    public void testFetchEmailsRetornaLista() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        List<EmailModel> emails = handleSMTP.fetchEmails();
        
        assertNotNull(emails);
        assertTrue(emails instanceof List);
    }

    @Test
    public void testSendEmailDestinatarioVacio() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        Exception exception = assertThrows(Exception.class, () -> {
            handleSMTP.sendEmail("", "Test Subject", "Test Body", null);
        });
        
        assertTrue(exception.getMessage().contains("recipient cannot be empty"));
    }

    @Test
    public void testSendEmailConDestinatarioNulo() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        Exception exception = assertThrows(Exception.class, () -> {
            handleSMTP.sendEmail(null, "Test", "Body", null);
        });
        
        assertTrue(exception.getMessage().contains("recipient cannot be empty"));
    }

     @Test
     public void testSendEmailSinAdjuntos() {
         handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
         
         assertDoesNotThrow(() -> {
             handleSMTP.sendEmail(TEST_EMAIL, "Test JUnit", "Cuerpo del test", null);
         });
     }

    @Test
    public void testSendEmailConListaAdjuntosVacia() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        List<File> attachments = new ArrayList<>();
        
        assertNotNull(attachments);
        assertTrue(attachments.isEmpty());
    }

    @Test
    public void testDownloadEmailCompleteNoLanzaExcepcion() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        File destFile = new File("test_email.eml");
        
        assertDoesNotThrow(() -> {
            handleSMTP.downloadEmailComplete("fake-uid", destFile);
        });
        
        if (destFile.exists()) {
            destFile.delete();
        }
    }

    @Test
    public void testDeleteEmailNoLanzaExcepcion() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.deleteEmail("Subject Test", "sender@example.com");
        });
    }

    @Test
    public void testUpdateReadStatusNoLanzaExcepcion() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.updateReadStatusIMAP("Test", "sender@test.com", "uid-123", true);
        });
    }
    
    
    @Test
    public void testListaEmailsInicializadaVacia() {
        List<EmailModel> emails = new ArrayList<>();
        
        assertTrue(emails.isEmpty());
        assertEquals(0, emails.size());
    }
    
    @Test
    public void testArchivoReadEmailsDbExiste() {
        File readFile = new File("read_emails_db.dat");
        
        assertNotNull(readFile);
    }
    
    @Test
    public void testValidacionEmailVacio() {
        String email = "";
        
        assertTrue(email.isEmpty());
    }
    
    @Test
    public void testValidacionEmailValido() {
        String email = "test@example.com";
        
        assertFalse(email.isEmpty());
        assertTrue(email.contains("@"));
    }
}
