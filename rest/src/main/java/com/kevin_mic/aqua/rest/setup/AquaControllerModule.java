package com.kevin_mic.aqua.rest.setup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.kevin_mic.aqua.rest.mockgpio.GpioControllerMock;
import com.kevin_mic.aqua.rest.mockgpio.PCF8574ProviderFactoryMock;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderFactory;
import com.kevin_mic.aqua.service.gpio.PCF8574ProviderFactoryImpl;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;
import java.io.IOException;

public class AquaControllerModule extends AbstractModule {
    private static DBI jdbi = null;
    private static Scheduler scheduler = null;
    private AquaConnectionProvider provider;

    protected void configure() {
        bind(DatabaseConfiguration.class).to(AquaControllerConfig.class);

        // Bind each dao to a provider
        try {
            ClassPath from = ClassPath.from(Thread.currentThread().getContextClassLoader());
            ImmutableSet<ClassPath.ClassInfo> topLevelClasses = from.getTopLevelClasses("com.kevin_mic.aqua.dao");
            for (ClassPath.ClassInfo topLevelClass : topLevelClasses) {
                if (topLevelClass.getName().endsWith("Dbi")) {
                    Class aClass = Thread.currentThread().getContextClassLoader().loadClass(topLevelClass.getName());
                    bind(aClass).toProvider(generateDaoProvider(aClass));
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while looking up dao classes", e);
        }
    }

    @Provides
    @Singleton
    public GpioController provideController(GpioControllerMock mockGpio) {
        String mockgpio = System.getProperty("mockgpio");
        if (Boolean.TRUE.toString().equalsIgnoreCase(mockgpio)) {
            return mockGpio;
        }
        return GpioFactory.getInstance();
    }

    @Provides
    @Singleton
    public PCF8574ProviderFactory providePCF8574Factory(GpioControllerMock mockGpio) {
        String mockgpio = System.getProperty("mockgpio");
        if (Boolean.TRUE.toString().equalsIgnoreCase(mockgpio)) {
            return new PCF8574ProviderFactoryMock();
        }
        return new PCF8574ProviderFactoryImpl();
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                ;

        return mapper;
    }

    @Provides
    public Scheduler get(Environment environment, Injector injector, AquaControllerConfig config) {
        if (scheduler == null) {
            try {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.setJobFactory(injector.getInstance(GuiceJobFactory.class));
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
        return scheduler;
    }

    @Provides
    public DBI get(Environment environment, AquaControllerConfig config) {
        if (jdbi == null) {
            synchronized (this) {
                if (jdbi == null) {
                    final DBIFactory factory = new DBIFactory();
                    jdbi = factory.build(environment, config.getDataSourceFactory(null), "postgres");
                }
            }
        }
        return jdbi;
    }

    static <T> com.google.inject.Provider<T> generateDaoProvider(Class<T> clazz) {
        return new com.google.inject.Provider<T>() {
            @Inject
            javax.inject.Provider<DBI> dbi;

            public T get() {
                return dbi.get().onDemand(clazz);
            }
        };
    }
}
