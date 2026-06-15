package ec.gob.iess.gestiondocumental.infrastructure.security;

import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.jwt.JwtClaims;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extrae roles SDNGD del access token Keycloak
 * ({@code user.rolesDisponibles}, {@code user.rol}, {@code realm_access}, {@code resource_access}).
 */
public final class SdngdJwtRoleExtractor {

    private SdngdJwtRoleExtractor() {
    }

    public static Set<String> extract(JwtClaims claims) {
        Set<String> roles = new LinkedHashSet<>();
        if (claims == null) {
            return roles;
        }

        Object user = claims.getClaimValue("user");
        if (user instanceof Map<?, ?> userMap) {
            addRoles(roles, userMap.get("rolesDisponibles"));
            addRoles(roles, userMap.get("rol"));
        }

        addRoles(roles, claims.getClaimValue("rol"));
        addRoles(roles, claims.getClaimValue("groups"));

        Object realmAccess = claims.getClaimValue("realm_access");
        if (realmAccess instanceof Map<?, ?> realmMap) {
            addRoles(roles, realmMap.get("roles"));
        }

        Object resourceAccess = claims.getClaimValue("resource_access");
        if (resourceAccess instanceof Map<?, ?> clientsMap) {
            for (Object clientRoles : clientsMap.values()) {
                if (clientRoles instanceof Map<?, ?> clientMap) {
                    addRoles(roles, clientMap.get("roles"));
                }
            }
        }

        return roles;
    }

    public static Set<String> extract(JsonWebToken jwt) {
        Set<String> roles = new LinkedHashSet<>();
        if (jwt == null) {
            return roles;
        }

        if (jwt instanceof OidcJwtCallerPrincipal oidcPrincipal) {
            roles.addAll(extract(oidcPrincipal.getClaims()));
            return roles;
        }

        addRolesFromJsonWebTokenClaim(roles, jwt, "user");
        addRolesFromJsonWebTokenClaim(roles, jwt, "rol");
        addRolesFromJsonWebTokenClaim(roles, jwt, "groups");
        addRolesFromJsonWebTokenClaim(roles, jwt, "realm_access");
        addRolesFromJsonWebTokenClaim(roles, jwt, "resource_access");
        return roles;
    }

    private static void addRolesFromJsonWebTokenClaim(Set<String> roles, JsonWebToken jwt, String claimName) {
        Object claim = jwt.getClaim(claimName);
        if ("user".equals(claimName) && claim instanceof Map<?, ?> userMap) {
            addRoles(roles, userMap.get("rolesDisponibles"));
            addRoles(roles, userMap.get("rol"));
            return;
        }
        if ("realm_access".equals(claimName) && claim instanceof Map<?, ?> realmMap) {
            addRoles(roles, realmMap.get("roles"));
            return;
        }
        if ("resource_access".equals(claimName) && claim instanceof Map<?, ?> clientsMap) {
            for (Object clientRoles : clientsMap.values()) {
                if (clientRoles instanceof Map<?, ?> clientMap) {
                    addRoles(roles, clientMap.get("roles"));
                }
            }
            return;
        }
        addRoles(roles, claim);
    }

    private static void addRoles(Set<String> target, Object claim) {
        if (claim == null) {
            return;
        }
        if (claim instanceof String s) {
            addRoleString(target, s);
            return;
        }
        if (claim instanceof Collection<?> collection) {
            for (Object item : collection) {
                addRoles(target, item);
            }
            return;
        }
        if (claim instanceof JsonArray jsonArray) {
            for (JsonValue value : jsonArray) {
                if (value instanceof JsonString jsonString) {
                    addRoleString(target, jsonString.getString());
                }
            }
            return;
        }
        if (claim instanceof JsonObject jsonObject) {
            addRoles(target, jsonObject.get("rolesDisponibles"));
            addRoles(target, jsonObject.get("rol"));
            addRoles(target, jsonObject.get("roles"));
            return;
        }
        if (claim instanceof JsonValue jsonValue && jsonValue.getValueType() == JsonValue.ValueType.STRING) {
            addRoleString(target, ((JsonString) jsonValue).getString());
        }
    }

    private static void addRoleString(Set<String> target, String value) {
        addRoleString(target, value, true);
    }

    private static void addRoleString(Set<String> target, String value, boolean filterNoise) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        if (filterNoise && isNoiseRole(trimmed)) {
            return;
        }
        target.add(trimmed);
    }

    static boolean isNoiseRole(String role) {
        String s = role.trim().toLowerCase();
        if (s.isEmpty()) {
            return true;
        }
        if (s.startsWith("default-roles-")) {
            return true;
        }
        return switch (s) {
            case "offline_access", "uma_authorization", "manage-account",
                    "manage-account-links", "view-profile", "microprofile-jwt" -> true;
            default -> false;
        };
    }
}
