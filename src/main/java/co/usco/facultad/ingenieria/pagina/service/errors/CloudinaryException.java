package co.usco.facultad.ingenieria.pagina.service.errors;

public class CloudinaryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CloudinaryException() {
        super("Error en Cloudinary");
    }

    public CloudinaryException(String message) {
        super(message);
    }
}
