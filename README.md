# 基础模板后端项目

## TODO：

- [ ] 发现上传文件是个耗时操作，但是文件访问的Url却在上传OSS之前就已经知道了，所以可以采用异步的方式开启新线程执行任务，加快响应的速度

## Notes

### 1.Filter的使用

> 1. 属于Java-web的三大组件（Servlet/Filter/Listener）
>
> 2. 跳脱离于Spring_boot 框架 在Interceptor执行之前执行 所以Springboot的全局异常处理器正常情况下是无法捕获这里抛出的异常

1. 定义Filter：定义一个类，实现Filter接口，并重写其所有方法

2.

配置Filter：Filter（javax.servlet包下或jakarta.servlet|前者低版本java后者高版本java，使用没区别）类上加`@WebFilter(urlPatterns="your patterns")`
注解，配置拦截资源路径。主类上加上`@ServletComponentScan`开启Servlet组件支持。

Spring是默认不支持Java-web的三大组件的（Servlet/Filter/Listener）只有加上`@ServletComponentScan`才支持

3. 重写三大方法,通常只会重写doFilter（）方法，并且调用filterChain.doFilter()方法放行

4. 调用doFilter前执行的逻辑会在资源访问前执行，doFilter后的逻辑会在资源访问结束后执行。

5. 以注解配置的多个Filter执行顺序是字符串比较顺序 🚩

6. 方法中的参数`ServletRequest`，`ServletResponse`本质上就是 `HttpServletRequest` 与 `HttpServletRequest`所以可以直接强转类型
   🚩

#### **Example-Demo：**

以资源访问路径日志记录为例子，记录在每次请求的过程中记录访问日志

```java

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        log.info("DemoFilter init only can be called once");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
//        log.info("DemoFilter destroy called only once");
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 放行前的逻辑
        if (servletRequest instanceof HttpServletRequest httpRequest) {
            String requestUrl = httpRequest.getRequestURI();
            log.info("访问的URL: {}", requestUrl);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

🚩如果在拦截后需要执行响应操作，不能直接返回对象，需要返回JSON字符串，并且使用response中的getWriter().write()写入字符串返回

``` java
@Override
public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    
    // 拦截请求，进行处理
    MyResponseObject responseObject = new MyResponseObject();
    responseObject.setMessage("This is a custom message");
    
    // 设置响应类型为JSON
    httpResponse.setContentType("application/json");
    httpResponse.setCharacterEncoding("UTF-8");
    
    // 将响应对象转换为JSON并写入响应流
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(responseObject);
    httpResponse.getWriter().write(jsonResponse);
}
```

### 2.Interceptor的使用

> Interceptor 拦截器，spring中实现，在进入controller之前执行。**全局异常处理可以捕获这中间的异常**

1. 定义`Interceptor` 实现 `HandlerInterceptor`接口，并且实现其中的方法主要是PreHandle方法
2. Prehandle返回True就放行，false就拦截（但是通常spring的处理方式是抛出异常交给异常处理器）
3. 定义Configration进行拦截器注册配置

#### **Example-Demo**: 输出请求日志

```java

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LogInterceptor preHandle. url: {}", request.getRequestURI());
        return true;
    }
}
```

其中的`Object handler`参数表示的是处理当前请求的控制器（Controller）方法的相关信息。具体来说，它通常是一个`HandlerMethod`
对象，包含了目标处理方法的详细信息，如方法名、参数、注解等。

#### **配置代码**

```java

@Configuration
public class InterceptorConfig extends WebMvcConfig {
    /**
     * 日志拦截器
     */
    @Resource
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}
```

### 3.@Transactional的使用

> spring 用于事务管理的注解，如果设置在方法就是方法整体为一个事务，如果类上，类所有的方法都为一个事务，接口上的话就是接口的所有实现类的所有方法都是事务

### rollbackFor

rollbackFor(default:RuntimeException.class)

默认只有当抛出运行时异常才能回滚事务，但是可以设置rollbackFor属性，指定异常类型。

### propagation

默认值是REQUIRE |NEW

规定事务传播行为：指的就是当一个事务方法被另一个事务方法调用时，这个事务方法该如何进行事务控制

> 如有A，B两个事务方法，A中调用了B，那么B方法是应该加入A中的事务还是应该再开一个新的事务

如果是加入已经存在的事务，不管A还是B方法，只要在执行过程中出现报错，都会回滚事务，如果希望无论A事务是否回滚B都能顺利执行的情况，就需要设置为NEW让B开启新的事务。这个过程SpringBoot事务管理会先将A事务先挂起，待B事务提交后继续执行A事务。

### 4. Aop：切面编程

底层实现是动态代理

#### 记录每个方法的耗时

```java
//必须指定
@Component
//指定切面类
@Aspect
@Slf4j
public class TimeAspect {

    // 通知类型，这里是环绕通知 内部为切入点表达式
    @Around("execution(* com.tzy.basebackend.service.*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // joinPoint 包含了一些原始方法的信息
        // .proceed为执行 并且原始方法可能有返回值，必须返回
        Object res = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("{} 方法耗时 {} ms", joinPoint.getSignature(), endTime - startTime);
        return res;
    }
}
```

#### 通知类型：

@Around ：环绕通知 返回值Object为joinPoint.proceed的结果

@Before ：前置通知

@After ：后置通知，无论是否异常都会执行

@AfterReturning ：返回通知，只有正常返回才能执行

@AfterThrowing ：异常通知，发生异常就会执行

#### 通知的执行顺序

#### 切入点表达式

1. execution 同函数签名 *为通配符 可以通过 || 组合两个execution
2. @annotation(注解全类名) | 自定义注解 只有有这个注解的方法才能被匹配

#### 切入点表达式的复用

如果很多地方都用到同一个切入点表达式

```java

@Around("execution(* com.tzy.basebackend.service.*.*(..))")
public void AroundMethod() {
}
```

可以抽取切入点表达式到一个函数上无参无返回值无函数体

```java
public class TimeAspect {
    @Pointcut("execution(* com.tzy.basebackend.service.*.*(..))")
    public void pointCut() {
    }
}
```

之后就使用`@Around("pointCut()")`就可以使用了，这个使用的原则和方法的使用原则一样，其他的切面类想要使用可以通过包名.方法名调用使用

#### 连接点 JoinPoint

> Spring中用JoinPoint抽象了连接点，可以使用如目标类名，方法名，方法参数

- @Around通知获取连接点信息只能使用 ProceedingJoinPoint
- 其他类型通知只能使用JoinPoint，是ProceedingJoinPoint的父类
