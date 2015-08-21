package com.android.daka.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.android.daka.Config;

import android.util.Log;

public class ReflectionUtils {
    static final String TAG = Config.TAG_APP+"ReflectionUtils";
    public static void test(){
     // 利用newInstance()方法，获取构造方法的实例
     // Class的newInstance方法，仅提供默认无参的实例化方法，类似于无参的构造方法
     // Constructor的newInstance方法，提供了带参数的实例化方法，类似于含参的构造方法
        try{
            Log.d(TAG, "test() 00");
//            String className = "android.app.ActivityManagerNative";
//            String methodName = "getDefault";
            String className = "com.android.internal.policy.impl.PhoneWindowManager";
            String methodName = "launchHomeFromHotKey";
            Class cls = Class.forName(className);

 //           Constructor ct = cls.getConstructor(null);
   //         Object obj1 = ct.newInstance(null); //构造创建实例
            Object obj2 = cls.newInstance();

            Method mMethod1 = cls.getDeclaredMethod(methodName);
            mMethod1.invoke(obj2);
    /*        // 根据方法名获取指定方法的参数类型列表
            Class paramTypes[] = getParamTypes(cls, methodName);
            // 获取带参方法
            Method mMethod2 = cls.getDeclaredMethod(methodName, paramTypes);
            mMethod2.setAccessible(true);
            
            mMethod2.invoke(obj1,"mMethod2.invoke~~"); //*/
        }catch(Exception e){
            Log.e(TAG, "test error:"+e.toString());
        }
    }
    /**
     * 获取方法mName的参数类型，返回值保存在Class[]中
     */
    public static Class[] getParamTypes(Class cls, String mName) {
        Class[] cs = null;
        
        /*
         * Note: 由于我们一般通过反射机制调用的方法，是非public方法
         * 所以在此处使用了getDeclaredMethods()方法
         */
        Method[] mtd = cls.getDeclaredMethods();    
        for (int i = 0; i < mtd.length; i++) {
            if (!mtd[i].getName().equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
                continue;
            }
            
            cs = mtd[i].getParameterTypes();
        }
        return cs;
    }
    
    /**
     * 根据string获取参数类型，返回值保存在Class[]中
     * 如"int"或"Integer" 为：Integer.TYPE
     */
    public Class[] getMethodTypesClass(String[] types) {
        Class[] cs = new Class[types.length];
        
        for (int i = 0; i < cs.length; i++) {
            if (types[i] != null || !types[i].trim().equals("")) {
                if (types[i].equals("int") || types[i].equals("Integer")) {
                    cs[i] = Integer.TYPE;
                } 
                else if (types[i].equals("float") || types[i].equals("Float")) {
                    cs[i] = Float.TYPE;
                }
                else if (types[i].equals("double") || types[i].equals("Double")) {
                    cs[i] = Double.TYPE;
                }
                else if (types[i].equals("boolean") || types[i].equals("Boolean")) {
                    cs[i] = Boolean.TYPE;
                }
                else {
                    cs[i] = String.class;
                }
            }
        }
        return cs;
    }
    /**
     * 根据类型string和值string 获取参数Object[]
     */
    public Object[] getMethodParamObject(String[] types, String[] params) {
        
        Object[] retObjects = new Object[params.length];
    
        for (int i = 0; i < retObjects.length; i++) {
            if(!params[i].trim().equals("")||params[i]!=null){  
                if(types[i].equals("int")||types[i].equals("Integer")){  
                    retObjects[i]= new Integer(params[i]);  
                }
                else if(types[i].equals("float")||types[i].equals("Float")){  
                    retObjects[i]= new Float(params[i]);  
                }
                else if(types[i].equals("double")||types[i].equals("Double")){  
                    retObjects[i]= new Double(params[i]);  
                }
                else if(types[i].equals("boolean")||types[i].equals("Boolean")){  
                    retObjects[i]=new Boolean(params[i]);  
                }
                else{  
                    retObjects[i] = params[i];  
                }  
            } 
        }
        
        return retObjects;
    }
    /**
     * 获取反射类中的构造方法
     * 输出打印格式："Modifier修饰域   构造方法名(参数类型列表)"
     */
    public static void get_Reflection_Constructors(Class r) {
        
        Class temp = r.getClass();
        String className = temp.getName();        // 获取指定类的类名
        
        try {
            Constructor[] theConstructors = temp.getDeclaredConstructors();           // 获取指定类的公有构造方法
            
            for (int i = 0; i < theConstructors.length; i++) {
                int mod = theConstructors[i].getModifiers();    // 输出修饰域和方法名称
                System.out.print(Modifier.toString(mod) + " " + className + "(");

                Class[] parameterTypes = theConstructors[i].getParameterTypes();       // 获取指定构造方法的参数的集合
                for (int j = 0; j < parameterTypes.length; j++) {    // 输出打印参数列表
                    System.out.print(parameterTypes[j].getName());
                    if (parameterTypes.length > j+1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取反射类的方法
     * 打印输出格式："RetType FuncName(paramTypeList)"
     */
    public static void get_Reflection_Method(ActivityUtils r) {
        
        Class temp = r.getClass();
        String className = temp.getName();
        
        /*
         * Note: 方法getDeclaredMethods()只能获取到由当前类定义的所有方法，不能获取从父类继承的方法
         * 方法getMethods() 不仅能获取到当前类定义的public方法，也能得到从父类继承和已经实现接口的public方法
         * 请查阅开发文档对这两个方法的详细描述。
         */
        Method[] methods = temp.getDeclaredMethods();
        //Method[] methods = temp.getMethods();
        StringBuilder sb = null;
        Log.i(TAG, "get_Reflection_Method() methods.length:"+methods.length
                +" className:"+className);
        for (int i = 0; i < methods.length; i++) {
            // 打印输出方法的修饰域
            int mod = methods[i].getModifiers();
            System.out.print(Modifier.toString(mod) + " ");
            
            // 输出方法的返回类型
            System.out.print(methods[i].getReturnType().getName());    
            
            // 获取输出的方法名
            System.out.print(" " + methods[i].getName() + "(");
            
            sb = new StringBuilder();
            sb.append(Modifier.toString(mod) + " ");
            sb.append(methods[i].getReturnType().getName());
            sb.append(" " + methods[i].getName() + "(");
            
            // 打印输出方法的参数列表
            Class[] parameterTypes = methods[i].getParameterTypes();
            for (int j = 0; j < parameterTypes.length; j++) {
                System.out.print(parameterTypes[j].getName());
                if (parameterTypes.length > j+1) {
                    System.out.print(", ");
                    sb.append(", ");
                }
            }
            System.out.println(")");
            sb.append(")");
            Log.i(TAG, "method>>"+sb.toString());
        }
    }
    
    /**
     * 获取反射类中的属性和属性值
     * 输出打印格式："Modifier Type : Name = Value"
     * Note: 对于未初始化的指针类型的属性，将不输出结果
     */
    public static void get_Reflection_Field_Value(Class r) {
        
        Class temp = r.getClass();    // 获取Class类的对象的方法之一
        
        try {
            System.out.println("public 属性");
            Field[] fb = temp.getFields();
            for (int i = 0; i < fb.length; i++) {
                
                Class cl = fb[i].getType();    // 属性的类型
                
                int md = fb[i].getModifiers();    // 属性的修饰域
                
                Field f = temp.getField(fb[i].getName());    // 属性的值
                f.setAccessible(true);
                Object value = (Object)f.get(r);
                
                // 判断属性是否被初始化
                if (value == null) {
                    System.out.println(Modifier.toString(md) + " " + cl + " : "      + fb[i].getName());
                }
                else {
                    System.out.println(Modifier.toString(md) + " " + cl + " : "      + fb[i].getName() + " = " + value.toString());
                }
            }
            
            System.out.println("public & 非public 属性");
            Field[] fa = temp.getDeclaredFields();
            for (int i = 0; i < fa.length; i++) {
                
                Class cl = fa[i].getType();    // 属性的类型
                
                int md = fa[i].getModifiers();    // 属性的修饰域
                
                Field f = temp.getDeclaredField(fa[i].getName());    // 属性的值
                f.setAccessible(true);    // Very Important
                Object value = (Object) f.get(r);
                
                if (value == null) {
                    System.out.println(Modifier.toString(md) + " " + cl + " : "      + fa[i].getName());
                }
                else {
                    System.out.println(Modifier.toString(md) + " " + cl + " : "  + fa[i].getName() + " = " + value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
