package co.usco.facultad.ingenieria.pagina.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String PROFESOR = "ROLE_PROFESOR";

    public static final String DECANO = "ROLE_DECANO";

    public static final String JEFE_PROGRAMA = "ROLE_JEFE_PROGRAMA";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
