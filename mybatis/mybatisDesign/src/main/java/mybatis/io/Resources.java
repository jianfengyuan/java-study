package mybatis.io;

import java.io.InputStream;

/**
 * @program: mybatisDesign
 * @description: 使用类加载去读取文件
 * @author: Kim_yuan
 * @create: 2020-09-20 17:03
 **/

public class Resources {

    /*
    * 根据输入的参数, 获得字节输入流
    * */
    public static InputStream getResourceAsStream(String filePath) {
        // Class.getClassLoader.getResourceAsStream(String path) ：
        // 默认则是从ClassPath根下获取，path不能以’/'开头，最终是由ClassLoader获取资源。

        // Resources.class拿到本类的字节码信息
        // .getClassLoader() 获得本类的类加载器
        // .getResourceAsStream(filePath) 根据类加载器的位置得到配置文件的字节流
        return Resources.class.getClassLoader().getResourceAsStream(filePath);

    }

}
