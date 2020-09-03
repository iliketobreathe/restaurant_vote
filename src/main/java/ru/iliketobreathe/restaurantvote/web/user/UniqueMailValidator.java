package ru.iliketobreathe.restaurantvote.web.user;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.iliketobreathe.restaurantvote.HasIdAndEmail;
import ru.iliketobreathe.restaurantvote.model.User;
import ru.iliketobreathe.restaurantvote.repository.user.DataJpaUserRepository;
import ru.iliketobreathe.restaurantvote.web.ExceptionInfoHandler;


@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final DataJpaUserRepository repository;

    public UniqueMailValidator(DataJpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
        if (dbUser != null && !dbUser.getId().equals(user.getId())) {
            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}
