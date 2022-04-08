package me.omen.fireagent;

import me.omen.fireagent.dumper.Dumper;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Agent {

    public static String dumpDir;
    // classes with name matching this pattern
    // will be dumped
    public static Pattern classes;

    public static void premain(String agentArgs, Instrumentation inst) {
        agentmain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        new File("classes/").mkdir();

        dumpDir = new File("classes/").getAbsolutePath();

        classes = Pattern.compile(".\\*");
        inst.addTransformer(new Dumper(), true);


        Class[] classes = inst.getAllLoadedClasses();
        List<Class> candidates = new ArrayList<Class>();
        for (Class c : classes) {
            if (c.getName().charAt(0) == '[') {
                continue;
            }


            candidates.add(c);
        }
        try {

            if (!candidates.isEmpty()) {
                inst.retransformClasses(candidates.toArray(new Class[0]));
            } else {
                System.out.println("\n--------------------------------------------------------------------------------------");
                System.out.println("");
                System.out.println("All Classes Dumped");
                System.out.println("");
                System.out.println("--------------------------------------------------------------------------------------");
                System.exit(0);
            }
        } catch (UnmodifiableClassException uce) {
        }


    }

}
