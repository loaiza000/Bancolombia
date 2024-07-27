package examenBackend2.examenBackend2.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import examenBackend2.examenBackend2.models.Account;
import examenBackend2.examenBackend2.models.Transfer;
import examenBackend2.examenBackend2.models.User;

@RestController
@CrossOrigin(origins = "*")
public class BancolombiaControllers {

    List<Account> accounts = new ArrayList<>();
    List<Transfer> transfers = new ArrayList<>();
    List<User> users = new ArrayList<>();

    @PostMapping("/users")
    public ResponseEntity<?> postUsers(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (user.getCc().equals(null) || user.getCc().isEmpty() || user.getEmail().equals(null)
                || user.getEmail().isEmpty() || user.getLastname().equals(null) || user.getLastname().isEmpty()
                || user.getName().equals(null) || user.getName().isEmpty()) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Todos los campos son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User ccDuplicada = users.stream().filter(item -> item.getCc().equals(user.getCc())).findAny().orElse(null);

        if (ccDuplicada != null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "La cc ya se encuentra en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User emailDuplicado = users.stream().filter(item -> item.getEmail().equals(user.getEmail())).findAny()
                .orElse(null);

        if (emailDuplicado != null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "El email ya existe en otro registro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        users.add(user);
        response.put("ok", true);
        response.put("data", users);
        response.put("message", "Usuario guardado");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserIsActivated(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        List<User> usersActivados = users.stream().filter(item -> item.getIsActivated().equals(true))
                .collect(Collectors.toList());

        response.put("ok", true);
        response.put("data", usersActivados);
        response.put("message", "Lista de usuarios activados");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> userId(@RequestBody User user, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (userEncontrado == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro el usuario");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("ok", true);
        response.put("data", userEncontrado);
        response.put("message", "Se encontro el usuario");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/allusers")
    public ResponseEntity<?> listarUsuarios() {
        Map<String, Object> response = new HashMap<>();

        response.put("ok", true);
        response.put("data", users);
        response.put("message", "Esta es la lista de usuarios");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> putUser(@RequestBody User user, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (userEncontrado == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro el usuario");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (user.getCc().equals(null) || user.getCc().isEmpty() || user.getEmail().equals(null)
                || user.getEmail().isEmpty() || user.getLastname().equals(null) || user.getLastname().isEmpty()
                || user.getName().equals(null) || user.getName().isEmpty()) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Todos los campos son obligatorios para actualizar");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!userEncontrado.getCc().equals(user.getCc())) {
            User ccDuplicado = users.stream()
                    .filter(p -> p.getCc().equals(user.getCc())).findAny().orElse(null);

            if (ccDuplicado != null) {
                response.put("ok", false);
                response.put("data", "");
                response.put("message", "La cc ya existe en otro registro");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!userEncontrado.getEmail().equals(user.getEmail())) {
            User emailDuplicado = users.stream()
                    .filter(p -> p.getCc().equals(user.getEmail())).findAny().orElse(null);

            if (emailDuplicado != null) {
                response.put("ok", false);
                response.put("data", "");
                response.put("message", "El email ya existe en otro registro");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        userEncontrado.setCc(user.getCc());
        userEncontrado.setEmail(user.getEmail());
        userEncontrado.setLastname(user.getLastname());
        userEncontrado.setName(user.getName());

        response.put("ok", true);
        response.put("data", userEncontrado);
        response.put("message", "Usuario actualizado");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        User userEncontrado = users.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (userEncontrado == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro el usuario");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        userEncontrado.setIsActivated(false);
        response.put("ok", true);
        response.put("data", userEncontrado);
        response.put("message", "Usuario desactivado");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /// ACCOUNT ///

    @PostMapping("/accounts")
    public ResponseEntity<?> postAccount(@RequestBody Account cuenta, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (cuenta.getBalance() == 0 || cuenta.getNumber().equals(null) || cuenta.getNumber().isEmpty()
                || cuenta.getType().equals(null) || cuenta.getType().isEmpty()) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Todos los datos son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Account numeroRepetido = accounts.stream().filter(item -> item.getNumber().equals(cuenta.getNumber())).findAny()
                .orElse(null);

        if (numeroRepetido != null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "El numero de cuenta ya se encuentra registrado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!cuenta.getType().equals("ahorro") || !cuenta.getType().equals("corriente")) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Solo se puede ahorro o corriente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Account userRelacionado = accounts.stream().filter(item -> item.getIdUser().equals(user.getId())).findAny()
                .orElse(null);

        if (userRelacionado == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No hay ningun usuario relacionado a la cuenta bancaria");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        accounts.add(cuenta);
        response.put("ok", true);
        response.put("data", accounts);
        response.put("message", "Cuenta bancaria creada");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> listarCuentas(@RequestBody Account cuenta) {
        Map<String, Object> response = new HashMap<>();

        Account cuentasActivadas = accounts.stream().filter(item -> item.getIsActivated().equals(true)).findAny()
                .orElse(null);

        if (cuentasActivadas == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontraron cuentas activadas");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("ok", true);
        response.put("data", cuentasActivadas);
        response.put("message", "Lista de cuentas activadas");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> cuentaId(@RequestBody Account cuenta, @PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEncontrada = accounts.stream()
                .filter(item -> item.getId().equals(id) && item.getIsActivated().equals(true)).findAny().orElse(null);

        if (cuentaEncontrada == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro la cuenta");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("ok", true);
        response.put("data", cuentaEncontrada);
        response.put("message", "Cuenta encontrada");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> listarCunetas() {
        Map<String, Object> response = new HashMap<>();

        response.put("ok", false);
        response.put("data", accounts);
        response.put("message", "Lista de cuentas bancarias");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/accounts{id}")
    public ResponseEntity<?> putCuenta(@PathVariable UUID id, @RequestBody Account cuenta) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEcnontrada = accounts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cuentaEcnontrada == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro la cuenta");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (cuenta.getBalance() == 0 || cuenta.getNumber().equals(null) || cuenta.getNumber().isEmpty()
                || cuenta.getType().equals(null) || cuenta.getType().isEmpty()) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Todos los datos son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!cuentaEcnontrada.getNumber().equals(cuenta.getNumber())) {
            Account cambioNumber = accounts.stream()
                    .filter(item -> item.getNumber().equals(cuenta.getNumber())).findAny().orElse(null);

            if (cambioNumber != null) {
                response.put("ok", false);
                response.put("data", "");
                response.put("message", "No se puede cambiar el numero de cuenta");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!cuentaEcnontrada.getIdUser().equals(cuenta.getIdUser())) {
            Account cambioIdUser = accounts.stream()
                    .filter(item -> item.getIdUser().equals(cuenta.getIdUser())).findAny().orElse(null);

            if (cambioIdUser != null) {
                response.put("ok", false);
                response.put("data", "");
                response.put("message", "El id de usario es diferente, tiene que ser igual");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        if (!cuenta.getType().equals("ahorro") || !cuenta.getType().equals("corriente")) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Solo se puede ahorro o corriente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        cuentaEcnontrada.setBalance(cuenta.getBalance());
        cuentaEcnontrada.setNumber(cuenta.getNumber());
        cuentaEcnontrada.setType(cuenta.getType());

        response.put("ok", true);
        response.put("data", cuentaEcnontrada);
        response.put("message", "Cuenta actualizada");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> desactivarCuenta(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Account cuentaEncontrada = accounts.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);

        if (cuentaEncontrada == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro la cuenta bancaria");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        cuentaEncontrada.setIsActivated(false);
        response.put("ok", true);
        response.put("data", cuentaEncontrada);
        response.put("message", "Cuenta desactivada");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /// TRANSFERENCIAS ///

    @GetMapping("/transfers")
    public ResponseEntity<?> listarTransferencias() {
        Map<String, Object> response = new HashMap<>();

        response.put("ok", true);
        response.put("data", transfers);
        response.put("message", "Esta es la lista de transferencias");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<?> transferrenciaId(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        Transfer transferenciaEncontrada = transfers.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElse(null);

        if (transferenciaEncontrada == null) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No se encontro la transferencia");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }

        response.put("ok", true);
        response.put("data", transferenciaEncontrada);
        response.put("message", "Esta es la transferencia");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /// TRANSFERENCIAS ///

    @PostMapping("/transfers")
    public ResponseEntity<?> transferMoney(@RequestBody Transfer transfer) {
        Map<String, Object> response = new HashMap<>();

        Account originAccount = accounts.stream().filter(item -> item.getId().equals(transfer.getOriginAccount()))
                .findAny().orElse(null);

        if (originAccount == null || originAccount.getIsActivated() == false) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Cuenta bancaria de origen no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Account destinationAccount = accounts.stream()
                .filter(item -> item.getId().equals(transfer.getDestinationAccount())).findAny().orElse(null);

        if (destinationAccount == null || destinationAccount.getIsActivated() == false) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "Cuenta bancaria de destino no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (originAccount.getBalance() < transfer.getAmount()) {
            response.put("ok", false);
            response.put("data", "");
            response.put("message", "No tiene saldo sufienciente para realizar la transferencia");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        originAccount.setBalance(originAccount.getBalance() - transfer.getAmount());

        destinationAccount.setBalance(destinationAccount.getBalance() + transfer.getAmount());

        transfers.add(transfer);
        response.put("ok", true);
        response.put("data", transfer);
        response.put("message", "Transferencia bancaria realizada con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
