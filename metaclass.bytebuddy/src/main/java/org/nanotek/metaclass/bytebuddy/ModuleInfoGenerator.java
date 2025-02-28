package org.nanotek.metaclass.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class ModuleInfoGenerator {
    public static void main(String[] args) {
        try {
            // Create a dynamic type for the module-info class
//            DynamicType.Unloaded<?> moduleInfoType = new ByteBuddy()
//                    .makeModule("com.example.module")
//                    .defineModule()
//                    .name("com.example.module")
//                    .requires("java.base")
//                    .exports("com.example.module")
//                    .make();
//
//            // Load the generated module-info class into the JVM
//            Class<?> moduleInfoClass = moduleInfoType
//                    .load(ModuleInfoGenerator.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
//                    .getLoaded();
//
//            // Verify the generated module-info class
//            System.out.println("Generated module-info class: " + moduleInfoClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
