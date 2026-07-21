package serenityrest.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * DatabaseConfig
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para la configuración de base de datos del proyecto.
 * Lee las variables {@code db.*} del bloque del ambiente activo en serenity.conf.
 *
 * <p>El ambiente se selecciona con la propiedad de sistema {@code -Denvironment=<env>}
 * o {@code -Dserenity.env=<env>} (ej.: {@code dev}, {@code staging}, {@code prod}).
 * Si ninguna está definida, usa {@code dev} por defecto.</p>
 *
 * <p>Cambiar el apuntamiento de BD para TODAS las TX se hace exclusivamente en:
 * {@code src/test/resources/serenity.conf} → bloque {@code environments.<env>}</p>
 *
 * Variable principal de apuntamiento de BD (cambia el destino para TODAS las TX):
 * <pre>
 *   db.url       ← PRINCIPAL — JDBC URL completa (jdbc:postgresql://host:port/name)
 * </pre>
 *
 * Variables auxiliares (definidas por ambiente en serenity.conf):
 * <pre>
 *   db.host      → hostname o IP del servidor de base de datos
 *   db.port      → puerto del servidor de base de datos
 *   db.name      → nombre de la base de datos (schema catalog)
 *   db.user      → usuario de conexión
 *   db.password  → contraseña de conexión (placeholder — no hardcodear credenciales reales)
 *   db.schema    → schema por defecto
 * </pre>
 *
 * PROYECTO : Everest — Automatización API Grupo Aval
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class DatabaseConfig {

    private DatabaseConfig() {}

    // ── Resolución del ambiente activo ────────────────────────────────────────

    private static final String ACTIVE_ENV = resolveActiveEnvironment();
    private static final Config DB_CONFIG  = loadDbConfig();

    private static String resolveActiveEnvironment() {
        String env = System.getProperty("environment");
        if (env == null || env.trim().isEmpty()) {
            env = System.getProperty("serenity.env", "dev");
        }
        return env.trim();
    }

    private static Config loadDbConfig() {
        return ConfigFactory.load("serenity.conf")
                .getConfig("environments." + ACTIVE_ENV);
    }

    // ── Getters públicos ──────────────────────────────────────────────────────

    /**
     * Hostname o IP del servidor de base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.host}
     */
    public static String getHost() {
        return DB_CONFIG.getString("db.host");
    }

    /**
     * Puerto del servidor de base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.port}
     */
    public static String getPort() {
        return DB_CONFIG.getString("db.port");
    }

    /**
     * Nombre de la base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.name}
     */
    public static String getName() {
        return DB_CONFIG.getString("db.name");
    }

    /**
     * Usuario de conexión a la base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.user}
     */
    public static String getUser() {
        return DB_CONFIG.getString("db.user");
    }

    /**
     * Contraseña de conexión a la base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.password}
     */
    public static String getPassword() {
        return DB_CONFIG.getString("db.password");
    }

    /**
     * Schema por defecto de la base de datos.
     * Configurado en {@code serenity.conf} → {@code environments.<env>.db.schema}
     */
    public static String getSchema() {
        return DB_CONFIG.getString("db.schema");
    }

    /**
     * JDBC URL completa de conexión — <b>variable principal de apuntamiento de BD</b>.
     * Modificar este valor en {@code serenity.conf} → {@code environments.<env>.db.url}
     * cambia el destino de BD para TODAS las TX del proyecto.
     * Ejemplo: {@code jdbc:postgresql://localhost:5432/everest_dev}
     */
    public static String getUrl() {
        return DB_CONFIG.getString("db.url");
    }
}
