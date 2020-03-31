package com.github.nikita;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.nikita.deserialize.CategoriesDeserialize;
import com.github.nikita.deserialize.CategoriesProductDeserialize;
import com.github.nikita.deserialize.ProductDeserialize;
import com.github.nikita.deserialize.UserDeserialize;
import com.github.nikita.model.Categories;
import com.github.nikita.model.CategoriesProduct;
import com.github.nikita.model.Product;
import com.github.nikita.model.User;
import com.github.nikita.serialize.CategoriesProductSerialize;
import com.github.nikita.serialize.CategoriesSerialize;
import com.github.nikita.serialize.ProductSerialize;
import com.github.nikita.serialize.UserSerialize;

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
        }
        return om.registerModule(sm);
    }

}
