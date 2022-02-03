package com.epam.esm.web.model.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTwoWayConverter<S, T> implements GenericConverter {

    private Class<S> classOfS;
    private Class<T> classOfT;

    public AbstractTwoWayConverter() {
        Type typeA = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Type typeB = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.classOfS = (Class) typeA;
        this.classOfT = (Class) typeB;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> convertibleTypes = new HashSet<>();
        convertibleTypes.add(new ConvertiblePair(classOfS, classOfT));
        convertibleTypes.add(new ConvertiblePair(classOfT, classOfS));

        return convertibleTypes;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        if (classOfS.equals(sourceType.getType())) {
            return this.convertTo((S) source);
        } else {
            return this.convertBack((T) source);
        }
    }

    protected abstract T convertTo(S source);

    protected abstract S convertBack(T source);
}
