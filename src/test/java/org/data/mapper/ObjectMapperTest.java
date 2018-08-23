package org.data.mapper;

import java.util.Objects;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author lili.he
 */
public class ObjectMapperTest {

    public static class Value {

        String textValue;
        Integer integerValue;

        public String getTextValue() {
            return textValue;
        }

        public void setTextValue(String textValue) {
            this.textValue = textValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.textValue);
            hash = 83 * hash + Objects.hashCode(this.integerValue);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Value other = (Value) obj;
            if (!Objects.equals(this.textValue, other.textValue)) {
                return false;
            }
            if (!Objects.equals(this.integerValue, other.integerValue)) {
                return false;
            }
            return true;
        }

    }

    private DataMapper<Value, Value> mapper;

    @Before
    public void setUp() {
        mapper = new DataMapper<>(Value.class, Value.class);
    }

    @Test
    public void testMapping() {
        Value from = new Value();
        from.integerValue = 1;
        from.textValue = "test value";
        assertThat(mapper.apply(from)).isEqualTo(from);
    }

    @Test
    public void testComplexMapping() {
        Value from = new Value();
        from.integerValue = 1;
        from.textValue = "test value";

        mapper.addChildRuleByName("integerValue",
                new AssemblerBuilder<Integer, Integer, Value, Value>()
                        .childMapper(v -> v)
                        .emptyIfError(0)
                        .fromValueGetter(value -> value.integerValue)
                        .toValueSetter((value, integer) -> {
                            value.setIntegerValue(integer * 2);
                        })
                        .build()
        );

        assertThat(mapper.apply(from)).isNotEqualTo(from);
        assertThat(mapper.apply(from).getIntegerValue()).isEqualTo(from.getIntegerValue() * 2);
    }

}
