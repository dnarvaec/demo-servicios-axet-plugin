/**
 * karate-config.js
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para configuración de entorno.
 * NUNCA hardcodear URLs, credenciales ni API keys en los .feature.
 *
 * Uso:
 *   mvn test                         → entorno dev (default)
 *   mvn test -Dkarate.env=staging    → entorno staging
 *   mvn test -Dkarate.env=prod       → entorno prod
 * ─────────────────────────────────────────────────────────────────────────────
 */
function fn() {

  var env = karate.env || 'dev';
  karate.log('[karate-config] Entorno activo:', env);

  // ── Configuración por entorno ─────────────────────────────────────────────
  var envConfig = {
    dev: {
      jsonplaceholderUrl : 'https://jsonplaceholder.typicode.com',
      reqresUrl          : 'https://reqres.in/api',
      petstoredUrl       : 'https://petstore.swagger.io/v2',
      connectTimeout     : 10000,
      readTimeout        : 15000
    },
    staging: {
      jsonplaceholderUrl : 'https://jsonplaceholder.typicode.com',
      reqresUrl          : 'https://reqres.in/api',
      petstoredUrl       : 'https://petstore.swagger.io/v2',
      connectTimeout     : 15000,
      readTimeout        : 20000
    },
    prod: {
      jsonplaceholderUrl : 'https://jsonplaceholder.typicode.com',
      reqresUrl          : 'https://reqres.in/api',
      petstoredUrl       : 'https://petstore.swagger.io/v2',
      connectTimeout     : 20000,
      readTimeout        : 30000
    }
  };

  var config = envConfig[env] || envConfig['dev'];

  // ── Credenciales desde variables de entorno — NUNCA en código ────────────
  config.reqresApiKey  = karate.properties['REQRES_API_KEY']  || java.lang.System.getenv('REQRES_API_KEY')  || 'reqres-free-v1';
  config.reqresEmail   = karate.properties['REQRES_EMAIL']    || java.lang.System.getenv('REQRES_EMAIL')    || 'eve.holt@reqres.in';
  config.reqresPassword= karate.properties['REQRES_PASSWORD'] || java.lang.System.getenv('REQRES_PASSWORD') || 'cityslicka';

  // ── Headers comunes reutilizables ─────────────────────────────────────────
  config.jsonHeaders = {
    'Content-Type' : 'application/json',
    'Accept'       : 'application/json'
  };

  config.reqresHeaders = {
    'Content-Type' : 'application/json',
    'Accept'       : 'application/json',
    'x-api-key'   : config.reqresApiKey
  };

  // ── Timeouts globales ─────────────────────────────────────────────────────
  karate.configure('connectTimeout', config.connectTimeout);
  karate.configure('readTimeout',    config.readTimeout);

  // ── SSL: deshabilitar validación de certificados (entornos corporativos) ──
  karate.configure('ssl', true);

  // ── Función de pausa para rate-limit (usar con moderación) ───────────────
  config.pause = function(ms) { java.lang.Thread.sleep(ms); };

  return config;
}
