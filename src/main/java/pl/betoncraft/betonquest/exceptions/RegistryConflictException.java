package pl.betoncraft.betonquest.exceptions;

public class RegistryConflictException extends RuntimeException {
    public RegistryConflictException(String idString) {
        super("Tried to register duplicated flag with id \"" + idString + "\"");
    }
}
