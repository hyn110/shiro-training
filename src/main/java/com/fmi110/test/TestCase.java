package com.fmi110.test;

import com.fmi110.commons.result.Tree;
import com.fmi110.model.User;
import com.fmi110.service.IResourceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestCase {
    public static void main(String[] arg) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        IResourceService service = (IResourceService) ctx.getBean("resourceServiceImpl");

        User user = new User();
        user.setId(2l);
//        List<Tree>       trees   = service.selectAllMenu();
        List<Tree>       trees   = service.selectAllTree();
        for (Tree tree : trees) {
            System.out.println(tree);
        }

    }
}
