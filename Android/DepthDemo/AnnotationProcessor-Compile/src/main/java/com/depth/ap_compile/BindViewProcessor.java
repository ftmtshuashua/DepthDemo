package com.depth.ap_compile;


import com.depth.ap_annoation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)                                   //自动注册
@SupportedAnnotationTypes("com.depth.ap_annoation.BindView")  //指名要处理的注解
@SupportedSourceVersion(SourceVersion.RELEASE_7)              //知名支持的java版本
public class BindViewProcessor extends AbstractProcessor {

    private Messager mMessager;      //日志相关的辅助类
    private Filer mFiler;            //文件相关的辅助类
    private Elements mElementUtils;  //元素相关的辅助类


    /**
     * 每一个注解处理器类都必须有一个空的构造函数。
     * 然而，这里有一个特殊的init()方法，它会被注解处理工具调用，
     * 并输入ProcessingEnviroment参数。
     * <p>
     * ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer。
     *
     * @param pe
     */
    @Override
    public synchronized void init(ProcessingEnvironment pe) {
        super.init(pe);
        mFiler = pe.getFiler();
        mMessager = pe.getMessager();
        mElementUtils = pe.getElementUtils();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "=========init============");
    }


    /**
     * 这相当于每个处理器的主函数main()。在这里写扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让查询出包含特定注解的被注解元素。
     *
     * @param sets
     * @param env
     * @return
     */
    public boolean process(Set<? extends TypeElement> sets, RoundEnvironment env) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, MessageFormat.format("process({0},{1})", sets, env));

        Map<String, List<Element>> mClassMap = new HashMap<>();
        for (TypeElement typeElement : sets) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, MessageFormat.format("注解处理 -> ({0},{1})", typeElement, env));

            for (Element textview : env.getElementsAnnotatedWith(typeElement)) {
                String mainactivity = textview.getEnclosingElement().asType().toString();
                if (mClassMap.get(mainactivity) == null) {
                    mClassMap.put(mainactivity, new ArrayList<>());
                }
                mClassMap.get(mainactivity).add(textview);
            }
        }

        Set<Map.Entry<String, List<Element>>> entries = mClassMap.entrySet();
        for (Map.Entry<String, List<Element>> entry : entries) {
            generateViewBinding(entry.getKey(), entry.getValue());
        }

        return true;
    }

    //生成文件
    private void generateViewBinding(String activity, List<Element> bindview) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, MessageFormat.format("{0} -> {1}", activity, bindview));

        TypeElement classTypeElement = mElementUtils.getTypeElement(activity);
        mMessager.printMessage(Diagnostic.Kind.NOTE, MessageFormat.format("ClassName.get(classTypeElement) -> {0}", ClassName.get(classTypeElement)));

        //构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(ClassName.get(classTypeElement), "target", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(ClassName.get("android.view", "View"), "source").build())
                .addAnnotation(ClassName.bestGuess("android.support.annotation.UiThread"))
                .addModifiers(Modifier.PUBLIC);
        constructorMethodBuilder.addStatement("this.target = target");
        for (Element element : bindview) {
            int id =  element.getAnnotation(BindView.class).value();
            constructorMethodBuilder.addStatement("target.$L = source.findViewById($L)", element.getSimpleName().toString(), id);
        }

        //取消注册方法
//        MethodSpec.Builder unbindMethodSpec = MethodSpec.methodBuilder("unbind")  .addModifiers(Modifier.PUBLIC);
//        unbindMethodSpec.addStatement("$T target = this.target", ClassName.get(classTypeElement));
//        unbindMethodSpec.addStatement("this.target = null");
//        for (Element element : bindview) {
//            Name simpleName = element.getSimpleName();
//            unbindMethodSpec.addStatement("target.$L = null", simpleName.toString());
//        }

        TypeSpec typeSpec = TypeSpec.classBuilder(classTypeElement.getSimpleName() + "_ViewBinding")
                .addField(ClassName.get(classTypeElement), "target", Modifier.PRIVATE)
                .addMethod(constructorMethodBuilder.build())
//                .addMethod(unbindMethodSpec.build())
                .addSuperinterface(ClassName.bestGuess("com.depth.ap_core.Unbinder"))
                .addModifiers(Modifier.PUBLIC)
                .build();

        String packageName = mElementUtils.getPackageOf(classTypeElement).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec) .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}