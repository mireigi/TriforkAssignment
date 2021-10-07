package trifork.messagesender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagesender.model.ValueObject;

@SpringBootTest
class ValueObjectTests {

	@ParameterizedTest
    @MethodSource("provideInputsForDifferentValues")
	void equals_WhenValuesAreDifferent_ReturnsFalse(String nameA, int valueA, String nameB, int valueB) {
		TestClass sutA = new TestClass(nameA, valueA);
		TestClass sutB = new TestClass(nameB, valueB);

		assertFalse(sutA.equals(sutB));
	}

	@Test
	void equals_WhenValuesAreEqual_ReturnsTrue() {
		String name = "Hello World!";
        int value = 10;
		TestClass sutA = new TestClass(name, value);
		TestClass sutB = new TestClass(name, value);

		assertTrue(sutA.equals(sutB));
	}

    @ParameterizedTest
    @MethodSource("provideInputsForDifferentValues")
    void hashCode_WhenValuesAreDifferent_ReturnsDifferentHashCodes(String nameA, int valueA, String nameB, int valueB) {
		TestClass sutA = new TestClass(nameA, valueA);
		TestClass sutB = new TestClass(nameB, valueB);

		assertNotEquals(sutA.hashCode(), sutB.hashCode());
    }

	@Test
	void hashCode_WhenValuesAreEqual_ReturnsEqualHashCodes() {
		String name = "Hello World!";
        int value = 10;
		TestClass sutA = new TestClass(name, value);
		TestClass sutB = new TestClass(name, value);

        assertEquals(sutA.hashCode(), sutB.hashCode());
	}

    private static Stream<Arguments> provideInputsForDifferentValues() {
        return Stream.of(
          Arguments.of("Hello World!", 10, "Lorem Ipsum", 10),
          Arguments.of("Hello World!", 10, "Hello World!", 20)
        );
    }

    class TestClass extends ValueObject
    {
        public String name;
        public int value;

        public TestClass(String name, int value) {
            super();

            this.name = name;
            this.value = value;
        }

        @Override
        protected Object[] getEqualityComponents() {
            return new Object[]{
                name,
                value
            };
        }
        
    }
}
