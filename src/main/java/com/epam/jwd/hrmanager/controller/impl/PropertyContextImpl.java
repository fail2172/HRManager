package com.epam.jwd.hrmanager.controller.impl;

import com.epam.jwd.hrmanager.controller.PropertyContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class PropertyContextImpl implements PropertyContext {

    private static final Logger LOGGER = LogManager.getLogger(PropertyContextImpl.class);
    private static final String PAGE_PROPERTIES_PATH = "D:\\JWD_курсы\\HRManager\\src\\main\\resources\\path\\page.properties";
    private static final String CONFIG_PROPERTIES_PATH = "D:\\JWD_курсы\\HRManager\\src\\main\\resources\\config\\db.properties";
    private static final String PAGE_ATTRIBUTE = "page.";
    private static final String DB_ATTRIBUTE = "db.";
    private static final String EMPTY_STRING = "";

    private final Map<String, String> propertyByKeys;

    private PropertyContextImpl() {
        propertyByKeys = new ConcurrentHashMap<>();
    }

    public static PropertyContextImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String get(String name) {
        return propertyByKeys.computeIfAbsent(name, this::receiveProperty);
    }

    private String receiveProperty(String name) {
        try (final FileInputStream stream = new FileInputStream(receiveFilePath(name))) {
            final Properties properties = new Properties();
            properties.load(stream);
            return properties.getProperty(name);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        } catch (IOException e) {
            LOGGER.error("Error while reading file", e);
        }
        return EMPTY_STRING;
    }

    private String receiveFilePath(String name) throws FileNotFoundException {
        if (name.startsWith(PAGE_ATTRIBUTE)) {
            return PAGE_PROPERTIES_PATH;
        } else if (name.startsWith(DB_ATTRIBUTE)) {
            return CONFIG_PROPERTIES_PATH;
        } else {
            throw new FileNotFoundException(format("No such file for: %s", name));
        }
    }

    private static class Holder {
        private static final PropertyContextImpl INSTANCE = new PropertyContextImpl();
    }
}
