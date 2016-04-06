package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 01.04.16.
 */
public class InputArgValidatorTest {

    @Mock
    private UserRepository userRepo;

    @Test
    public void checkNotNullValidator() {
        try {
            InputArgValidator.checkNotNull("notnull", "notNullTest");
            InputArgValidator.checkNotNull(1L, "notNullTest");
            InputArgValidator.checkNotNull(new Card(), "notNullTest");
            InputArgValidator.checkNotNull(Character.CHEYENNE, "notNullTest");
        } catch (InvalidInputException iie) {
            Assert.fail("NotNullValidation are ok and should not throw exception.");
        }

        try {
            InputArgValidator.checkNotNull(null, "notNullTest");
            Assert.fail("NotNullValidation is nok and should have thrown an exception.");
        } catch (InvalidInputException iie) {
            Assert.assertTrue("NotNullValidation is nok and has trown an exception", true);
        }
    }

    @Test
    public void checkNotEmptyValidator() {
        try {
            InputArgValidator.checkNotEmpty("notnull", "notEmptyTest");
        } catch (InvalidInputException iie) {
            Assert.fail("NotEmptyValidation is ok and should not throw an exception.");
        }

        try {
            InputArgValidator.checkNotNull(null, "notEmptyTest");
            InputArgValidator.checkNotNull("", "notEmptyTest");
            Assert.fail("NotEmptyValidation is nok and should have thrown an exception.");
        } catch (InvalidInputException iie) {
            Assert.assertTrue("NotEmptyValidation is nok and has trown an exception", true);
        }
    }

    @Test
    public void checkIfPositiveNumberForLongValidator() {
        try {
            InputArgValidator.checkIfPositiveNumber(0L, "posNumber");
            InputArgValidator.checkIfPositiveNumber(1L, "posNumber");
            InputArgValidator.checkIfPositiveNumber(Long.MAX_VALUE, "posNumber");
        } catch (InvalidInputException iie) {
            Assert.fail("PosNumberValidation is ok and should not throw an exception.");
        }

        try {
            InputArgValidator.checkIfPositiveNumber(Long.MIN_VALUE, "posNumber");
            InputArgValidator.checkIfPositiveNumber(-1L, "posNumber");
            InputArgValidator.checkIfPositiveNumber(0L, "posNumber");
            Assert.fail("PosNumberValidation is nok and should have thrown an exception.");
        } catch (InvalidInputException iie) {
            Assert.assertTrue("PosNumberValidation is nok and has trown an exception", true);
        }
    }

    @Test
    public void checkUserNameNotUsedValidator() {
        MockitoAnnotations.initMocks(this);
        when(userRepo.findByUsername("ExistingUsername")).thenReturn(new User("existing", "username"));

        try {
            InputArgValidator.checkUserNameNotUsed("NotExistingUsername", userRepo, "username");
        } catch (InvalidInputException iie) {
            Assert.fail("UserNameValidation is ok and should not throw an exception.");
        }

        try {
            InputArgValidator.checkUserNameNotUsed("ExistingUsername", userRepo, "username");
            Assert.fail("UserNameValidation is nok and should have thrown an exception.");
        } catch (InvalidInputException iie) {
            Assert.assertTrue("UserNameValidation is nok and has trown an exception", true);
        }
    }
}
