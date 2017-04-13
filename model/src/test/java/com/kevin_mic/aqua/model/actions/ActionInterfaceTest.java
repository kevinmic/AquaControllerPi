package com.kevin_mic.aqua.model.actions;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.kevin_mic.aqua.model.actions.metadata.Schedule;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class ActionInterfaceTest {

    @Test
    public void test_max_schedules() throws IOException {
        ClassPath classPath = ClassPath.from(ActionInterface.class.getClassLoader());
        ImmutableSet<ClassPath.ClassInfo> topLevelClassesRecursive = classPath.getTopLevelClassesRecursive(ActionInterface.class.getPackage().getName());
        List<Class> classes = new ArrayList<>();
        for (ClassPath.ClassInfo classInfo : topLevelClassesRecursive) {
            classes.add(classInfo.load());
        }

        classes.forEach(this::checkClass);
    }

    public void checkClass(Class clazz) {
        List<Field> scheduleFields = FieldUtils.getFieldsListWithAnnotation(clazz, Schedule.class);
        if (scheduleFields.size() > 1) {
            fail("Class has more than one schedule - " + clazz.getName());
        }
    }

}