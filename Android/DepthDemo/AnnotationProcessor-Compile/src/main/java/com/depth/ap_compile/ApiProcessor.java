package com.depth.ap_compile;

import com.depth.ap_annoation.Api;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 11:54
 * </pre>
 */
@SupportedAnnotationTypes("com.depth.ap_annoation.Api")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApiProcessor extends AbstractProcessor {

    public static final String API_SERVER = "package $package;\n\n" +
            "\n" +
            "public class $className {\n" +
            "    private $className() {}\n" +
            "\n" +
            "public static final void $method (){}\n" +
            "}\n";

    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Api.class)) {
            String class_name = element.getSimpleName().toString();
            PackageElement class_package = mElements.getPackageOf(element);
            String class_package_name = class_package.getQualifiedName().toString();

            mMessager.printMessage(Diagnostic.Kind.NOTE, "---------------------------- objectType:" + class_name);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "---------------------------- Package:" + class_package_name);

            String clsname = class_name + "Generate"; //生成类的名字
            String body = API_SERVER;
            String classBody = body
                    .replace("$package", class_package_name)
                    .replace("$className", clsname)
                    .replace("$method", element.getAnnotation(Api.class).method());
            try {
                JavaFileObject source = mFiler.createSourceFile(class_package_name + "." + clsname);
                Writer writer = source.openWriter();
                writer.write(classBody);
                writer.flush();
                writer.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
