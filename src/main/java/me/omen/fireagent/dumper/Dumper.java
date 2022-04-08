package me.omen.fireagent.dumper;

import me.omen.fireagent.Agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Dumper implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class redefinedClass, ProtectionDomain protDomain,
                            byte[] classBytes) {
            dumpClass(className, classBytes);

        return null;
    }



    private static void dumpClass(String className, byte[] classBuf) {
        try {

            className = className.replace("/", File.separator);

            if (className.contains("java") || className.contains("jdk") || className.contains("sun") || className.contains("jetbrains") || className.contains("me/omen") || className.contains("me\\omen") || className.contains("intellij") || className.contains(" net\\dv8tion\\jda") || className.contains(" net/dv8tion/jda") || className.contains(" net\\minecraft") || className.contains(" net/minecraft") || className.contains(" org\\bukkit") || className.contains(" org/bukkit") || className.contains(" org\\spigotmc") || className.contains(" org/spigotmc")) {
                return;
            }

            if(!className.contains("$")) {
                System.out.println("\n--------------------------------------------------------------------------------------");
                System.out.println("");
                System.out.println("Dumping: " + className + ".class");
                System.out.println("");
            }



            StringBuilder buf = new StringBuilder();

            buf.append(Agent.dumpDir);
            buf.append(File.separatorChar);

            int index = className.lastIndexOf(File.separatorChar);

            if (index != -1) {
                buf.append(className.substring(0, index));
            }
            String dir = buf.toString();
            new File(dir).mkdirs();


            String fileName = Agent.dumpDir +
                    File.separator + className + ".class";

            FileOutputStream fos = new FileOutputStream(fileName);

            fos.write(classBuf);

            fos.close();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("SUCCESS");
            System.out.println("--------------------------------------------------------------------------------------");
        } catch (Exception exp) {
            exp.printStackTrace();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("FAILED");
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }
}