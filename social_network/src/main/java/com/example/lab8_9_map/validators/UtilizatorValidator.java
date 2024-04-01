package com.example.lab8_9_map.validators;

import com.example.lab8_9_map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String exceptions = new String();
        if(entity.getLastName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getLastName().charAt(0))))
            exceptions = exceptions + "Lastname incorrect!\n";
        if(entity.getFirstName().matches(".*[0-9].*") || !(Character.isUpperCase(entity.getFirstName().charAt(0))))
            exceptions += "Firstname incorrect!\n";
        if(exceptions.length() != 0)
            throw new ValidationException(exceptions);
    }
}

