package ru.wear.store.web.validations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.wear.store.web.models.Client;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ClientValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Client.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        if(!client.getPassword().equals(client.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "", "Пароль не совпадает");
        }
        if(client.getIndex() < 100000){
            errors.rejectValue("index","","Индекс не может быть меньше 6 чисел");
        }
        LocalDate clientDate = client.getBirthdate();
        LocalDate currentDate = LocalDate.now();
        try {
            Period period = Period.between(clientDate, currentDate);
            if(period.getYears() < 18){
                errors.rejectValue("birthdate","","Для регистрации вам должно быть больше 18 лет");
            }
        }catch (NullPointerException e){
            errors.rejectValue("birthdate","","Введите правильную дату");
        }
    }
}
