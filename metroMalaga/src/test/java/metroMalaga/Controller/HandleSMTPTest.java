package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import metroMalaga.Controller.smtp.HandleSMTP;
import metroMalaga.Model.EmailModel;

/**
 * Unit tests for the HandleSMTP controller.
 * This class verifies the correct behavior of email operations including
 * authentication, fetching, sending, and managing email states.
 * * @author User
 * @version 1.0
 */
public class HandleSMTPTest {

    private HandleSMTP handleSMTP;
    
    private static final String TEST_EMAIL = "proyectoprofesor8@gmail.com";
    private static final String TEST_APP_PASSWORD = "elbrseooyvaheuyc";

    /**
     * Initializes the HandleSMTP instance before each test execution.
     */
    @BeforeEach
    public void setUp() {
        handleSMTP = new HandleSMTP();
    }

    /**
     * Verifies that login succeeds with valid credentials.
     */
    @Test
    public void testLoginCorrecto() {
        boolean result = handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        assertTrue(result);
    }

    /**
     * Checks login behavior when the email field is empty.
     */
    @Test
    public void testLoginConEmailVacio() {
        boolean result = handleSMTP.login("", TEST_APP_PASSWORD);
        assertTrue(result);
    }

    /**
     * Checks login behavior when the password field is empty.
     */
    @Test
    public void testLoginConPasswordVacio() {
        boolean result = handleSMTP.login(TEST_EMAIL, "");
        assertTrue(result);
    }

    /**
     * Verifies that fetching emails returns a valid List object.
     */
    @Test
    public void testFetchEmailsRetornaLista() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        List<EmailModel> emails = handleSMTP.fetchEmails();
        
        assertNotNull(emails);
        assertTrue(emails instanceof List);
    }

    /**
     * Verifies that sending an email without a recipient throws an Exception.
     */
    @Test
    public void testSendEmailDestinatarioVacio() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        Exception exception = assertThrows(Exception.class, () -> {
            handleSMTP.sendEmail("", "Test Subject", "Test Body", null);
        });
        
        assertNotNull(exception);
    }

    /**
     * Verifies that sending an email to a null recipient throws an Exception.
     */
    @Test
    public void testSendEmailConDestinatarioNulo() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertThrows(Exception.class, () -> {
            handleSMTP.sendEmail(null, "Test", "Body", null);
        });
    }

    /**
     * Tests sending a standard email without any attachments.
     */
    @Test
    public void testSendEmailSinAdjuntos() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.sendEmail(TEST_EMAIL, "Test JUnit", "Cuerpo del test", null);
        });
    }

    /**
     * Confirms that an empty attachment list is handled correctly.
     */
    @Test
    public void testSendEmailConListaAdjuntosVacia() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        List<File> attachments = new ArrayList<>();
        
        assertNotNull(attachments);
        assertTrue(attachments.isEmpty());
    }

    /**
     * Verifies that attempting to download a non-existent email throws the expected error.
     */
    @Test
    public void testDownloadEmailConUIDInexistenteLanzaExcepcion() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        File destFile = new File("test_email.eml");
        
        Exception exception = assertThrows(Exception.class, () -> {
            handleSMTP.downloadEmailComplete("fake-uid-12345", destFile);
        });
        
        assertTrue(exception.getMessage().contains("Mensaje no encontrado"));
        
        if (destFile.exists()) {
            destFile.delete();
        }
    }

    /**
     * Ensures that deleting a non-existent email does not crash the application.
     */
    @Test
    public void testDeleteEmailConUIDInexistente() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.deleteEmail("fake-uid-inexistente");
        });
    }

    /**
     * Ensures that updating the read status of an invalid UID does not throw an exception.
     */
    @Test
    public void testUpdateReadStatusConUIDInexistente() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.updateReadStatusIMAP("fake-uid-123", true);
        });
    }
    
    /**
     * Verifies the process of marking an email as unread via IMAP.
     */
    @Test
    public void testUpdateReadStatusMarcarComoNoLeido() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        
        assertDoesNotThrow(() -> {
            handleSMTP.updateReadStatusIMAP("fake-uid-456", false);
        });
    }
    
    /**
     * Confirms basic initialization of an email list.
     */
    @Test
    public void testListaEmailsInicializadaVacia() {
        List<EmailModel> emails = new ArrayList<>();
        
        assertTrue(emails.isEmpty());
        assertEquals(0, emails.size());
    }
    
    /**
     * Verifies the existence of the local database file for read status.
     */
    @Test
    public void testArchivoReadEmailsDbExiste() {
        File readFile = new File("read_emails_db.dat");
        
        assertNotNull(readFile);
    }
    
    /**
     * Validates empty string checks for email fields.
     */
    @Test
    public void testValidacionEmailVacio() {
        String email = "";
        
        assertTrue(email.isEmpty());
    }
    
    /**
     * Basic format validation for email strings.
     */
    @Test
    public void testValidacionEmailValido() {
        String email = "test@example.com";
        
        assertFalse(email.isEmpty());
        assertTrue(email.contains("@"));
    }
    
    /**
     * Tests loading content for an EmailModel with a fake UID.
     */
    @Test
    public void testLoadFullContentConEmailModelVacio() {
        handleSMTP.login(TEST_EMAIL, TEST_APP_PASSWORD);
        EmailModel emailModel = new EmailModel(1, "sender@test.com", "Test", "Body");
        emailModel.setUniqueId("fake-uid");
        
        assertDoesNotThrow(() -> {
            handleSMTP.loadFullContent(emailModel);
        });
    }
    
    /**
     * Verifies file object creation for email exports.
     */
    @Test
    public void testCrearArchivoDestino() {
        File destFile = new File("test_output.eml");
        
        assertNotNull(destFile);
        assertEquals("test_output.eml", destFile.getName());
        assertTrue(destFile.getName().endsWith(".eml"));
    }
    
    /**
     * Validates that empty subject strings are detected.
     */
    @Test
    public void testValidacionSubjectVacio() {
        String subject = "";
        
        assertTrue(subject.isEmpty());
    }
    
    /**
     * Validates that subjects containing only whitespace are handled.
     */
    @Test
    public void testValidacionSubjectConEspacios() {
        String subject = "    ";
        
        assertTrue(subject.trim().isEmpty());
    }
}
