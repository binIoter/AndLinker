package com.codezjx.andlinker;

import com.codezjx.andlinker.annotation.ClassName;
import com.codezjx.andlinker.annotation.In;
import com.codezjx.andlinker.annotation.Inout;
import com.codezjx.andlinker.annotation.Out;

import java.lang.annotation.Annotation;

/**
 * Created by codezjx on 2017/9/17.<br/>
 */
public interface ParameterHandler<T> {

    void apply(RequestBuilder builder, T value, int index);
    
    final class CallbackHandler<T> implements ParameterHandler<T> {

        Class<?> mParamType;

        public CallbackHandler(Class<?> paramType) {
            mParamType = paramType;
        }

        @Override
        public void apply(RequestBuilder builder, T value, int index) {
            Logger.d("CallbackHandler", "ParameterHandler mParamType:" + mParamType + " value:" + value);
            ClassName annotation = mParamType.getAnnotation(ClassName.class);
            String className = (annotation != null) ? annotation.value() : "";
            if (Utils.isStringBlank(className)) {
                throw new IllegalArgumentException("Callback type must provide @ClassName");
            }
            CallbackTypeWrapper wrapper = new CallbackTypeWrapper(className);
            builder.applyWrapper(index, wrapper);
        }

    }
    
    final class ParamDirectionHandler<T> implements ParameterHandler<T> {

        Annotation mAnnotation;
        Class<?> mParamType;

        public ParamDirectionHandler(Annotation annotation, Class<?> paramType) {
            mAnnotation = annotation;
            mParamType = paramType;
        }

        @Override
        public void apply(RequestBuilder builder, T value, int index) {
            Logger.d("ParamDirectionHandler", " mParamType:" + mParamType + " value:" + value + " index:" + index);
            if (Utils.canOnlyBeInType(mParamType) && !(mAnnotation instanceof In)) {
                throw new IllegalArgumentException("Primitives are in by default, and cannot be otherwise.");
            }
            BaseTypeWrapper wrapper = null;
            if (mAnnotation instanceof In) {
                wrapper = new InTypeWrapper(value, mParamType);
            } else if (mAnnotation instanceof Out) {
                wrapper = new OutTypeWrapper(value, mParamType);
            } else if (mAnnotation instanceof Inout) {
                wrapper = new InOutTypeWrapper(value, mParamType);
            }
            builder.applyWrapper(index, wrapper);
        }
    }
    
    final class DefaultParameterHandler<T> implements ParameterHandler<T> {

        Class<?> mParamType;

        public DefaultParameterHandler(Class<?> paramType) {
            mParamType = paramType;
        }

        @Override
        public void apply(RequestBuilder builder, T value, int index) {
            if (Utils.canOnlyBeInType(mParamType)) {
                InTypeWrapper wrapper = new InTypeWrapper(value, mParamType);
                builder.applyWrapper(index, wrapper);
            } else {
                throw new IllegalArgumentException("Parameter type '" + mParamType.getSimpleName() + "' can be an out type, so you must declare it as @In, @Out or @Inout.");
            }
        }
    }
}
