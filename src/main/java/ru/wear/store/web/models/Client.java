package ru.wear.store.web.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Client {

    private long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String username;
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String password;
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String confirmPassword;

    private String role;

    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "[А-ЯA-Z][а-яa-z]+", message = "Укажите Фамилию в формате: Иванов")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String surname;
    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "[А-ЯA-Z][а-яa-z]+", message = "Укажите Имя в формате: Иван")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String name;
    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "[А-ЯA-Z][а-яa-z]+", message = "Укажите Отчество в формате: Иванович")
    @Size(min = 4, max=16, message = "Размер 4-16 знаков")
    private String middleName;
    private Gender gender;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Past(message = "Введите правильную дату")
    @NotNull(message = "Не может быть Null")
    private LocalDate birthdate;

    @NotBlank(message = "Поле не может быть пустым")
    @Email(message = "Не валидный E-Mail")
    private String eMail;
    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "\\+\\d+", message = "Укажите номер телефона в формате +79998887766")
    @Size(min = 12, max=12, message = "Размер 12 знаков")
    private String phoneNumber;

    @Digits(message="Индекс должен содержать 6 цифр", fraction = 0, integer = 6)
    private int index;
    private String country;
    private String state;
    private String city;
    private String street;
    private int houseNumber;
    private int apartmentNumber;

    public enum Gender {
        MALE("Male"), FEMALE("Female");

        private final String description;

        Gender(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
