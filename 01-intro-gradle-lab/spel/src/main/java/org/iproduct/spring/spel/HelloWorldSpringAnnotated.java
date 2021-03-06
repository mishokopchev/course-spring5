package org.iproduct.spring.spel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HelloWorldSpringAnnotated {
    public static void main(String... args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("org.iproduct.spring.spel");

        //@Value test
        ArticlePresenter mr = ctx.getBean("presenter", ArticlePresenter.class);
        mr.present();

        //SpEL test
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, HelloWorldSpringAnnotated.class.getClassLoader());
        ExpressionParser parser = new SpelExpressionParser(config);

        double value = parser.parseExpression("((42 div 5) % 3 + 1) * 10").getValue(Double.class);
        System.out.println(value);

        boolean trueValue =parser.parseExpression("2 == 1 + 1").getValue(Boolean.class);
        System.out.println(trueValue);

        boolean trueValue2 = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        System.out.println(trueValue2);

        boolean falseValue = parser.parseExpression(
                "'xyz' instanceof T(Integer)").getValue(Boolean.class);

        boolean falseValue2 = parser.parseExpression(
                "not ('5.00' matches '^-?\\d+(\\.\\d{2})?$')").getValue(Boolean.class);
        System.out.println(falseValue2);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(ctx.getBeanFactory()));
        String beanPropWithDefault = parser.parseExpression(
                "@beanA.message != null ? @beanA.message : 'default message'").getValue(context, String.class);
        System.out.println(beanPropWithDefault);

        String beanPropWithDefaultElvis = parser.parseExpression(
                "@beanA.message ?:'default message'").getValue(context, String.class);
        System.out.println(beanPropWithDefault);

        String thirdTitle = parser.parseExpression(
                "@presenter.articleProvider.articles[2]?.title ?:'no title'").getValue(context, String.class);
        System.out.println(thirdTitle);

        List<Article> springTitles = parser.parseExpression(
                "@provider.articles.?[title matches '.*Spring.*'].![title]")
                .getValue(context, List.class);
        System.out.println(springTitles);

        Expression expression = parser.parseExpression("'Any string !'.replace(\" \", \"\").length()");
        int result = expression.getValue(Integer.class);

        CarPark park = new CarPark();
        park.getCars().add(new Car("Opel", 5,
                new Engine(4, 2500, 16, "engine model1"),2500, 2013 ));
        park.getCars().add(new Car("Opel1", 5,
                new Engine(4, 2500, 16, "engine model2"),2500, 2013 ));
        park.getCars().add(new Car("Opel2", 5,
                new Engine(4, 2500, 16, "engine model3"),2500, 2013 ));
        park.getCars().add(new Car("Opel3", 5,
                new Engine(4, 2500, 16, "engine model4"),2500, 2013 ));

        Expression expression2 = parser.parseExpression("cars.![make]");

        EvaluationContext context2 = new StandardEvaluationContext(park);
//        expression2.setValue(context,"new engine model");
        List result2 = (List) expression2.getValue(context2);
        System.out.println("SpEL:" +result2);


        CarPark park2 = parser.parseExpression("T(org.iproduct.spring.spel.CarPark).create({" +
                "new org.iproduct.spring.spel.Car(\"Opel\", 3, 2550, 2013)," +
                "new org.iproduct.spring.spel.Car(\"VW\", 2, 1350, 2010)," +
                "new org.iproduct.spring.spel.Car(\"Audi\", 5, 2200, 2017)" +
            "})").getValue(CarPark.class);
        EvaluationContext context3 = new StandardEvaluationContext(park2);
        Expression expression3 = parser.parseExpression("cars.![make]");
        List result3 = (List) expression3.getValue(context3);
        System.out.println("SpEL:" +result3);


        EvaluationContext context4 = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Map mapOfMaps = (Map) parser
                .parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}")
                .getValue(context4);
        System.out.println("SpEL:" + mapOfMaps.get("name"));

        // create an array of integers
        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

// create parser and set variable 'primes' as the array of integers
        ExpressionParser parser2 = new SpelExpressionParser();
        EvaluationContext context5 = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context5.setVariable("primes", primes);

// all prime numbers > 10 from the list (using selection ?{...})
// evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
                "#primes.?[#this>10]").getValue(context5);

        System.out.println("SpEL:" + primesGreaterThanTen);
    }
}