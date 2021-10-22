package myagent.transformers;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyApplicationTransformer implements ClassFileTransformer {

    private final String targetClassName;
    private final ClassLoader targetClassLoader;

    public MyApplicationTransformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");
        if (!className.equals(finalTargetClassName))
            return byteCode;

        if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {

            System.out.println("[Agent] Transforming class MyAtm");
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod("add");

                /*m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = System.currentTimeMillis();");

                StringBuilder endBlock = new StringBuilder();

                m.addLocalVariable("endTime", CtClass.longType);
                m.addLocalVariable("opTime", CtClass.longType);
                endBlock.append("endTime = System.currentTimeMillis();");
                endBlock.append("opTime = (endTime-startTime)/1000;");*/

                //m.insertAfter("System.out.println(\"les arguments sont: \"+a+\" et \"+b);");

                //CtMethod m2 = new CtMethod(m.getReturnType(), m.getName(), m.getParameterTypes(), cc);
                //m.addLocalVariable("c", CtClass.doubleType);
                m.setBody("{" +
                        "     System.out.println(\"args: \"+$1+\" et \"+$2);" +
                        "     int c = 5;" +
                        "     System.out.println(\"c=\"+c);" +
                        "     return $1 + $2;" +
                        "  }");

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return byteCode;
    }

}
