package com.github.nikita;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.nikita.deserialize.*;
import com.github.nikita.model.*;
import com.github.nikita.serialize.*;

public class ObjectMapperFactory {

    public static ObjectMapper createObjectMapper( Class<?> nameClass) {
        SimpleModule sm = new SimpleModule();
        ObjectMapper om = new ObjectMapper();
        if (User.class == nameClass) {
            sm.addSerializer(User.class, new UserSerialize());
            sm.addDeserializer(User.class, new UserDeserialize());
        } else if (Product.class == nameClass) {
            sm.addSerializer(Product.class, new ProductSerialize()).addSerializer(new UserSerialize());
            sm.addDeserializer(Product.class, new ProductDeserialize());
        } else if (Categories.class == nameClass) {
            sm.addSerializer(Categories.class, new CategoriesSerialize());
            sm.addDeserializer(Categories.class, new CategoriesDeserialize());
        } else if (CategoriesProduct.class == nameClass) {
            sm.addSerializer(CategoriesProduct.class, new CategoriesProductSerialize()).addSerializer(new ProductSerialize()).addSerializer(new UserSerialize()).addSerializer(new CategoriesSerialize());
            sm.addDeserializer(CategoriesProduct.class, new CategoriesProductDeserialize());
        }  if (Reviews.class == nameClass) {
            sm.addSerializer(Reviews.class, new ReviewsSerialize()).addSerializer(new UserSerialize()).addSerializer(new ProductSerialize());
            sm.addDeserializer(Reviews.class, new ReviewsDeserialize());
        } else if (UserTransaction.class == nameClass) {
            //sm.addSerializer(UserTransaction.class, new ReviewsSerialize()).addSerializer(new UserSerialize()).addSerializer(new ProductSerialize());
            sm.addDeserializer(UserTransaction.class, new UserTransactionDeserialize());
        }
        return om.registerModule(sm);
    }

}
